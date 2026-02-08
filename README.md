# Android Bluetooth Serial & Helper 📱

A simplified Android library that handles the entire Bluetooth connection workflow. It includes a **Helper class** to automatically scan/select paired devices via a dialog, and a **Serial class** to handle the background socket connection and data transmission.

[![Documentation](https://img.shields.io/badge/Documentation-View_Site-blue?style=for-the-badge&logo=google-chrome)](https://www.malekalhafi.com/projects/bt-serial.html)

**View full documentation:** [https://www.malekalhafi.com/projects/bt-serial.html](https://www.malekalhafi.com/projects/bt-serial.html)

## Features
- **UI Helper:** Built-in `AlertDialog` to show paired devices and select one.
- **Auto-Connect:** Handles the `UUID` and socket creation automatically.
- **Simple IO:** Easy methods like `PrintLine` and `Write` for sending data to Arduino/Microcontrollers.

## Installation
1. Copy `BluetoothSerial.java` and `BluetoothHelper.java` to your project.
2. Add permissions to `AndroidManifest.xml`:
   ```xml
   <uses-permission android:name="android.permission.BLUETOOTH" />
   <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />

# Usage
## Select a Device (Activity A)
​Use BluetoothHelper to show a list of paired devices. When the user picks one, it automatically starts your next Activity with the MAC address.
```java
BluetoothHelper btHelper = new BluetoothHelper();

// Check if BT is on, and show dialog
// Pass 'this' (current activity) and the Intent for the NEXT activity
Intent intent = new Intent(this, ControlActivity.class);
btHelper.Default(this, intent); 
```
## Connect and Send Data
​In your second activity (e.g., ControlActivity), retrieve the MAC address and start the connection.
```java
public class ControlActivity extends Activity {
    
    BluetoothSerial btSerial = new BluetoothSerial();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // 1. Get MAC Address passed from the Helper
        String macAddress = getIntent().getStringExtra("MAC");
        
        // 2. Begin Connection (Shows "Connecting..." dialog automatically)
        btSerial.Begin(macAddress, this);
    }

    // Example: Send data when a button is clicked
    public void onLightOn() {
        if(btSerial.isConnected) {
            btSerial.PrintLine("LIGHT_ON"); // Sends string + newline
        }
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        btSerial.Close(); // Clean up connection
    }
}

```
