Package com.malekalhafi.bluetooth;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.provider.Telephony;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

/**
 * Created by Malek Alhafi on 7/5/2016.
 */
public class BluetoothSerial {
    private Activity activity;
    private BluetoothAdapter mBluetoothAdapter;
    private AlertDialog alertDialog;
    private  ConnectThread connectThread;
    private  BluetoothSocket mmSocket;
    public boolean isConnected = false;

    public void Begin(String MAC ,Activity input) {
        activity = input;
        mBluetoothAdapter = BluetoothHelper.mBluetoothAdapter;
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(MAC);


        alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setTitle("Connecting");
        alertDialog.setMessage("Loading ... ");
        alertDialog.show();
        connectThread = new ConnectThread(device);
        connectThread.start();
    }

    public void Close(){
    connectThread.cancel();
    }

    private class ConnectThread extends Thread implements Runnable {

        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            // Use a temporary object that is later assigned to mmSocket,
            // because mmSocket is final
            BluetoothSocket tmp = null;
            mmDevice = device;
            UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            // Get a BluetoothSocket to connect with the given BluetoothDevice
            try {
                // MY_UUID is the app's UUID string, also used by the server code
                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
            } catch (IOException e) { }
            mmSocket = tmp;
        }

        public void run() {
            // Cancel discovery because it will slow down the connection
            mBluetoothAdapter.cancelDiscovery();

            try {
                // Connect the device through the socket. This will block
                // until it succeeds or throws an exception
                mmSocket.connect();
            } catch (IOException connectException) {
                // Unable to connect; close the socket and get out
                try {
                    mmSocket.close();
                } catch (IOException closeException) { }
                return;
            }
            isConnected = true;
            alertDialog.dismiss();
            // Do work to manage the connection (in a separate thread)
           // manageConnectedSocket(mmSocket);
        }

        /** Will cancel an in-progress connection, and close the socket */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    public void PrintLine(String string){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        string+="\n";
        connectedThread.write(string.getBytes());
    }
    public void WriteAllText(String string){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.write(string.getBytes());
    }
    public void WriteAllBytes(byte[] bytes){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        connectedThread.write(bytes);
    }
    public void Write(char c){
    ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        String string = "";
        string += c;
        connectedThread.write(string.getBytes());
    }

    public String ReadAllText(){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        return connectedThread.readln();
    }
    public byte[] ReadAllBytes(){
        ConnectedThread connectedThread = new ConnectedThread(mmSocket);
        return connectedThread.readln().getBytes();
    }
    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
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

        public String readln() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            String response = "";
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    response += String.valueOf(bytes);
                } catch (IOException e) {
                    break;
                }
            }
            return response;
        }


        /* Call this from the main activity to send data to the remote device */
        public void write(byte[] bytes) {
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) { }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

}
