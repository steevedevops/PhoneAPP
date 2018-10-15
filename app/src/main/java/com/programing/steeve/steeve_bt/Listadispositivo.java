package com.programing.steeve.steeve_bt;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Steeve on 15/11/2017.
 */

public class Listadispositivo extends ListActivity {
    BluetoothAdapter meuBluetoothconectar = null;
    static String ENDEREZO_MAC = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, R.layout.nombre_dispositivos);
        meuBluetoothconectar = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> dispositivosvinculdos = meuBluetoothconectar.getBondedDevices();

        if (dispositivosvinculdos.size()> 0){
            for (BluetoothDevice dispositivo : dispositivosvinculdos){
                String nomebt  = dispositivo.getName();
                String macbt = dispositivo.getAddress();
                ArrayBluetooth.add(nomebt + "\n" +macbt);
            }
        }
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String informaciogeral = ((TextView) v).getText().toString();
        //Toast.makeText(getApplicationContext(), "Info: "+ informaciogeral, Toast.LENGTH_LONG).show();
        String enderezoMac = informaciogeral.substring(informaciogeral.length()- 17);
        //Toast.makeText(getApplicationContext(), "Info: "+ enderezoMac, Toast.LENGTH_LONG).show();
        Intent retornamac = new Intent();
        retornamac.putExtra(ENDEREZO_MAC, enderezoMac);
        setResult(RESULT_OK, retornamac);
        finish();
    }
}

