package net.splusk.autoSyncEnablerOnWifi;


import android.app.Activity;
import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.CheckBox;

public class SyncOnWifiActivity extends Activity {

    private static final int PREF_DISABLED = -1;
    private static final int PREF_ENABLED = 1;
	private static final String TAG = "ACCSYNCONWIFI";
	private CheckBox onOff;
	private SyncOnWifiStateReceiver receiver;
	private IntentFilter intentFilter = new IntentFilter();
	private int accSync = PREF_DISABLED;
	private SharedPreferences mPrefs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Try pack manager instead    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        onOff = (CheckBox) findViewById(R.id.on_off_option);
        
        mPrefs = getSharedPreferences(TAG, Context.MODE_PRIVATE);
        accSync = mPrefs.getInt("accsync_setting", 0);
        
        if (accSync == PREF_DISABLED) {
        	onOff.setChecked(false);
        } else {
        	onOff.setChecked(true);
        }
        
        //FIXME: Only do if checked!!!!
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		receiver = new SyncOnWifiStateReceiver();
		registerReceiver(receiver, intentFilter);

    }
    
    /** register the BroadcastReceiver with the intent values to be matched */
    /**@Override
    public void onResume() {
        super.onResume();
        //FIXME: only do this if settings are set!!!
		intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		receiver = new SyncOnWifiStateReceiver();
		registerReceiver(receiver, intentFilter);
    }*/
    
    @Override
    public void onPause() {
        //Set preferences!!!!
    	final SharedPreferences mPrefs = getSharedPreferences(TAG, Context.MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = mPrefs.edit();
        int state = onOff.isChecked() ? PREF_ENABLED : PREF_DISABLED;
        prefsEditor.putInt("accsync_setting", state);
        prefsEditor.commit();

        super.onPause();
    }
    
    @Override
    public void onDestroy() {
    	unregisterReceiver(receiver);
    	super.onDestroy();
    }
    
}
