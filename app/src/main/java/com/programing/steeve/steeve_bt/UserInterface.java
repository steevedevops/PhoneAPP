package com.programing.steeve.steeve_bt;

import android.app.Activity;
import android.app.Instrumentation;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class UserInterface extends AppCompatActivity {

    //os controles da tela
    Button Idled1,Idled2,Idled3,Idconectar;

    private static final int SOLICITA_ATIVA_BT = 1;
    private static final int SOLICITA_CONECTA_BT = 2;

    ConnectedThread connectedThread;

    private static String MAC = null;
    UUID meu_uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSoket = null;
    boolean conection = false;
    BluetoothAdapter meuBluetoothAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_interface);
        /*Relcacionado los componentes para sus eventos */
        Idled1 = (Button)findViewById(R.id.Idled1);
        Idled2 = (Button) findViewById(R.id.Idled2);
        Idled3 = (Button) findViewById(R.id.Idled3);
        Idconectar = (Button) findViewById(R.id.idConectar);


        /*Testanto la conecio del blu*/
        meuBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (meuBluetoothAdapter == null){
            //Bloco de mensagen
            Toast.makeText(getApplicationContext(),"Su dispositivo no contiene bluetooth", Toast.LENGTH_SHORT).show();
        }
        else if(!meuBluetoothAdapter.isEnabled()){
            Intent ativaBT = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);//Para abilitar el bluetooh automaticamnte
            startActivityForResult(ativaBT,SOLICITA_ATIVA_BT);
        }

        Idconectar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conection){
                   try {
                       meuSoket.close();
                       conection = false;
                       Idconectar.setText("Conectar");
                       Toast.makeText(getApplicationContext(),"Desconectado",Toast.LENGTH_LONG).show();
                   }catch (IOException error){
                       Toast.makeText(getApplicationContext(),"Falla"+error,Toast.LENGTH_LONG).show();
                   }

                }else{
                   Intent abrelista = new Intent(UserInterface.this, Listadispositivo.class);
                    startActivityForResult(abrelista, SOLICITA_CONECTA_BT);
                }
            }
        });


        Idled1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conection){
                    connectedThread.enviar("led1");
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth no esta conectado para o envio de led 1",Toast.LENGTH_LONG).show();
                }
            }
        });

        Idled2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conection){
                    connectedThread.enviar("led2");
                }else {
                    Toast.makeText(getApplication(),"Bluetooth no esta conectado para o envio de led 2", Toast.LENGTH_LONG).show();
                }
            }
        });

        Idled3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conection){
                    connectedThread.enviar("led3");
                }else {
                    Toast.makeText(getApplication(),"Bluetooth no esta conectado para o envio de led 3", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       // super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case SOLICITA_ATIVA_BT:
                if(resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(),"Bluetooth Ativado",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),"Bluetooth Desativado",Toast.LENGTH_LONG).show();
                    //finish();
                }break;
            case SOLICITA_CONECTA_BT:
                if (resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(Listadispositivo.ENDEREZO_MAC);
                    Toast.makeText(getApplicationContext(),"MAC de Conection: "+MAC,Toast.LENGTH_LONG).show();
                    meuDevice = meuBluetoothAdapter.getRemoteDevice(MAC);
                    try{
                        meuSoket = meuDevice.createRfcommSocketToServiceRecord(meu_uuid);

                        meuSoket.connect();
                        conection = true;

                        connectedThread = new ConnectedThread(meuSoket);
                        connectedThread.start();

                        Idconectar.setText("Desconectar");
                        Toast.makeText(getApplicationContext(),"Estas conectado con" + MAC,Toast.LENGTH_LONG).show();

                    }catch (IOException error){
                        conection = true;
                        Toast.makeText(getApplicationContext(),"Hay un error en conectar"+error,Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(),"Falla en la busqueda del MAC",Toast.LENGTH_LONG).show();
                }

        }//fin del switch
    }
    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
          /*  while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                  //  mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                    //        .sendToTarget();
                } catch (IOException e) {
                    break;
                }
            }*/
        }

        /* Call this from the main activity to send data to the remote device */
        public void enviar(String dadosenviar) {
            byte[] msgbuffer = dadosenviar.getBytes();
            try {
                mmOutStream.write(msgbuffer);
            } catch (IOException e) {}
        }


    }
}