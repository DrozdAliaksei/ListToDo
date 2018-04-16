package cleverhouse.toastwifistate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    WifiManager wifi;
    String wifis[];
    WifiScanReceiver wifiReciever;

    String networkSSID = "SoftAP";
    String networkPass = "12345678";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv=(ListView)findViewById(R.id.listView);
        wifi=(WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        //Проверяем включен ли WiFi, если нет то включаем
        enableWifi();

        wifiReciever = new WifiScanReceiver();
        wifi.startScan();



        myConnect();
    }


    protected void onPause() {
        unregisterReceiver(wifiReciever);
        super.onPause();
    }

    protected void onResume() {
        registerReceiver(wifiReciever, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }



    private class WifiScanReceiver extends BroadcastReceiver {

        public void onReceive(Context c, Intent intent) {

            List<ScanResult> wifiScanList = wifi.getScanResults();
            wifis = new String[wifiScanList.size()];

            for(int i = 0; i < wifiScanList.size(); i++){
                wifis[i] = ((wifiScanList.get(i)).toString());
            }
            lv.setAdapter(new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,wifis));
        }
    }



    public void enableWifi() {

        if (!wifi.isWifiEnabled()) {
            wifi.setWifiEnabled(true);

            Toast toast = Toast.makeText(getApplicationContext(), "Wifi Turned On", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    void myConnect(){



        WifiConfiguration wifiConfig = new WifiConfiguration();
        wifiConfig.SSID = String.format("\"%s\"", networkSSID);
        wifiConfig.preSharedKey = String.format("\"%s\"", networkPass);

        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
//remember id
        int netId = wifiManager.addNetwork(wifiConfig);
        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }
}
