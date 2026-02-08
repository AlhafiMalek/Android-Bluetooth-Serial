package com.malekalhafi.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by Malek Alhafi on 7/4/2016.
 */
public class BluetoothHelper {
    private static final int REQUEST_ENABLE_BT = 1;
    public static BluetoothAdapter mBluetoothAdapter = null ;
    private Activity activity = null;

    public boolean is_supported(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
        return false;
         }else{
        return true;
        }
    }

    public void enable(){
    if (!mBluetoothAdapter.isEnabled()) {
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
    }
}

    public boolean Start(Activity input){
    activity = input;
    if(is_supported()){
    enable();
        return true;
    }else{
        return false;
    }
    }

    public ArrayAdapter<String> get_paired_devices_list (){

    ArrayAdapter<String> mArrayAdapter = new ArrayAdapter<String>(activity , android.R.layout.simple_list_item_1);
    Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

    // If there are paired devices
    if (pairedDevices.size() > 0) {
        // Loop through paired devices
        for (BluetoothDevice device : pairedDevices) {
            // Add the name and address to an array adapter to show in a ListView
            mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
        }
    }
    return mArrayAdapter;
}

    public void Default(final Activity input1 , final Intent intent){
        if(Start(input1)) {

            final ArrayAdapter<String> mArrayAdapter = get_paired_devices_list();

            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setTitle("Make your selection");
            builder.setAdapter(mArrayAdapter, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    String mac = (String) mArrayAdapter.getItem(item);
                    mac = mac.replaceAll(".*\n", "");
                    intent.putExtra("MAC", mac);
                    dialog.dismiss();
                    activity.startActivity(intent);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();



        }

    }



}
