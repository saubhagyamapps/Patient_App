package app.food.patient_app.lockcount;


import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import app.food.patient_app.R;
import app.food.patient_app.util.Constant;

public final class IndicatorService extends Service implements SharedPreferences.OnSharedPreferenceChangeListener {
    SharedPrefererenceCounter counter;
    SharedPreferences sharedPreferences;
    private CounterReceiver lockReceiver = new CounterReceiver();
    final private Handler mHandler = new Handler();
    private static final String TAG = "IndicatorService";
    String countere = "1";
    int Count;
    private final Runnable mHandlerRunnable = new Runnable() {

        @Override
        public void run() {

            Constant.mLockCounter = countere;
            Log.e(TAG, "run:----> " + countere);
            Toast.makeText(IndicatorService.this, "LOCK" + Count, Toast.LENGTH_SHORT).show();
            IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            registerReceiver(lockReceiver, filter);
            sharedPreferences.registerOnSharedPreferenceChangeListener(IndicatorService.this);

            mHandler.postDelayed(this, 1000);
        }
    };


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void onDestroy() {
        unregisterReceiver(lockReceiver);
        super.onDestroy();

    }

    public void onCreate() {
        super.onCreate();
        counter = SharedPrefererenceCounter.getInstance(this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandler.removeCallbacks(mHandlerRunnable);
        mHandler.post(mHandlerRunnable);
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("value")) {
        } else if (s.equals("value_of")) {
        } else if (s.equals("value_on")) {
            countere = counter.getOffCounter();
            Count++;
        }
    }
}
