package cleverhouse.listtodo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private NetworkMonitor mNetworkMonitor;

    WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNetworkMonitor = new NetworkMonitor();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mNetworkMonitor,intentFilter);
    }
    public class NetworkMonitor extends BroadcastReceiver{

        private String LOG_TAG = "myNetworkMonitor";
        @Override
        public void onReceive(Context context, Intent intent) {


            String action = intent.getAction();
            Log.d(LOG_TAG, action);

            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&   activeNetwork.isConnectedOrConnecting();
            Log.d(LOG_TAG,"isConnected: "+isConnected);

            if (!isConnected)
                return;
            boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
            Log.d(LOG_TAG,"isWiFi: "+isWiFi);

            if (!isWiFi)
                return;
            WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo connectionInfo = wifiManager.getConnectionInfo();
            Log.d(LOG_TAG,connectionInfo.getSSID());

        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mNetworkMonitor);
        super.onDestroy();
    }

}
