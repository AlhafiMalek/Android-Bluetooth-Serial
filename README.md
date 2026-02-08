# Android Bluetooth Serial 🔵

A robust helper class for handling Bluetooth Serial Port Profile (SPP) connections on Android. It simplifies device discovery, connection management, and data transmission (Strings/Bytes) without blocking the UI thread.

[![Documentation](https://img.shields.io/badge/Documentation-View_Site-blue?style=for-the-badge&logo=google-chrome)](https://www.malekalhafi.com/projects/bt-serial.html)

**View full documentation:** [https://www.malekalhafi.com/projects/bt-serial.html](https://www.malekalhafi.com/projects/bt-serial.html)

## Features
- **Easy Connection:** Connect to paired devices by name or MAC address.
- **Async Communication:** Handles reading/writing in background threads.
- **Event Listeners:** simple callbacks for `OnConnected`, `OnDataReceived`, and `OnError`.
- **Auto-formatting:** Option to append newlines (`\n` or `\r\n`) automatically.

## Installation
1. Copy `BluetoothSerial.java` to your project.
2. Add permissions to `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.BLUETOOTH" />
   <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
   <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
   <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
# Usage
## Initialize 
```java
BluetoothSerial bt = new BluetoothSerial(context, new BluetoothSerial.BluetoothDelegate() {
    @Override
    public void onBluetoothConnected() {
        Toast.makeText(context, "Connected!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothDataReceived(String message) {
        Log.d("BT", "Received: " + message);
    }

    @Override
    public void onBluetoothError(String error) {
        Log.e("BT", "Error: " + error);
    }
});

```
## Connect
```java
// Connect to specific device MAC address (HC-05/HC-06)
bt.connect("98:D3:31:FD:15:C1");
```
## Send Data
``` java
bt.send("Hello Arduino!"); 
// Or send bytes
bt.send(new byte[]{0x01, 0x02});
```
