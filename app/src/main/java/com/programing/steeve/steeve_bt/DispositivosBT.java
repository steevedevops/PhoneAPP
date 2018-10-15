package com.programing.steeve.steeve_bt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.lang.reflect.Array;
import java.util.Set;

public class DispositivosBT extends AppCompatActivity {
    BluetoothAdapter meuBluetoothconectar = null;
    public static final String TAG = "DispositivosBT";
    static String ENDEREZO_MAC = null;
    ListView Idlista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dispositivos_bt);

        ArrayAdapter <String> ArrayBluetooth = new ArrayAdapter<String>(this, R.layout.nombre_dispositivos);

        Idlista = (ListView) findViewById(R.id.Idlista);//por el momento no es necesario

        meuBluetoothconectar = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosvinculdos = meuBluetoothconectar.getBondedDevices();

        if (dispositivosvinculdos.size()> 0){
            for (BluetoothDevice dispositivo : dispositivosvinculdos){
                String nomebt  = dispositivo.getName();
                String macbt = dispositivo.getAddress();
                ArrayBluetooth.add(nomebt + "\n" +macbt);
            }
        }
        Idlista.setAdapter(ArrayBluetooth);
    }
}
