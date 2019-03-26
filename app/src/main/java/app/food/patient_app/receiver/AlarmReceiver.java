package app.food.patient_app.receiver;

import android.app.AlarmManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.data.AppItem;
import app.food.patient_app.data.DataManager;
import app.food.patient_app.model.ImageCountModel;
import app.food.patient_app.model.SocialusageListModel;
import app.food.patient_app.util.AppUtil;
import app.food.patient_app.util.Constant;
import app.food.patient_app.util.MyContentObserver;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by ptyagi on 4/17/17.
 */

/**
 * AlarmReceiver handles the broadcast message and generates Notification
 */
public class AlarmReceiver extends BroadcastReceiver {
    private long mTotal;
    String mMessages;
    String mTital;

    SocialusageListModel socialusageListModel;
    private List<SocialusageListModel> array_list;
    JSONObject singObj;

    private static final String TAG = "AlarmReceiver";

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        getFilePaths(context);
        storeSocialTime(context);
        context.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, new MyContentObserver(new Handler(), context));
        lockORunlock(context);
    }

    private void lockORunlock(Context context) {
        KeyguardManager myKM = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        if( myKM.inKeyguardRestrictedInputMode()) {
            //it is locked
            Toast.makeText(context, "locked", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "locked:------ >" );
        } else {
            //it is not locked
            Toast.makeText(context, "Unlocked", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Unlocked:------ >" );
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void storeSocialTime(Context context) {
        array_list = new ArrayList<>();

        DataManager manager = DataManager.getInstance();
        List<AppItem> items = manager.getApps(context, 0, 0);
        for (AppItem item : items) {
            if (item.mUsageTime <= 0) continue;
            mTotal += item.mUsageTime;
        }
        Log.e(TAG, "Total Time:==> " + AppUtil.formatMilliSeconds(mTotal));
        for (AppItem item : items) {
            Log.e(TAG, "App name: " + item.mName);
            mTital = item.mName;
            mMessages = AppUtil.formatMilliSeconds(item.mUsageTime);
            Log.e(TAG, "usages Time " + mMessages);

            socialusageListModel = new SocialusageListModel(item.mName, item.mUsageTime);
            array_list.add(socialusageListModel);
        }

        JSONArray contaArray = new JSONArray();
        for (int i = 0; i < array_list.size(); i++) {
            try {
                singObj = new JSONObject();
                singObj.put("name", array_list.get(i).getName());
                singObj.put("usage", array_list.get(i).getUsage());
                contaArray.put(singObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(contaArray.toString());
        Log.e(TAG, "Array_LIst " + array.toString());
        SocialUsageCall(array, context);

        Intent myIntent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //Registering contact observer

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();

        int mMinute = new Time(System.currentTimeMillis()).getMinutes() + 1;
     //   int mMinute = new Time(System.currentTimeMillis()).getMinutes();
        int mHour = new Time(System.currentTimeMillis()).getHours();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int mSecond = Calendar.getInstance().get(Calendar.SECOND);
        Log.e(TAG, "Time---> : " + hour);
        firingCal.set(Calendar.HOUR, hour); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, mMinute); // Particular minute
        firingCal.set(Calendar.SECOND, mSecond); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent1);
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent1);
        }

        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        CharSequence name = "Product";// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

            Notification notification = new Notification.Builder(context)
                    .setContentTitle(mTital)
                    .setContentText(mMessages)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID)
                    .build();
            NotificationManager mNotificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNotificationManager.createNotificationChannel(mChannel);

            mNotificationManager.notify(notifyID, notification);
        }
    }



    private void SocialUsageCall(JsonArray array, Context context) {
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(context);

        Call<String> stringCall = Constant.apiService.InsertSocialData(Constant.mUserId, mCurrentDate, array.toString());
        stringCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("response", "Getting response from server : " + response.message());
                Log.e(TAG, "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("response", "Getting response from server : " + t);
            }
        });

    }

    public void getFilePaths(Context context) {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        int count = cursor.getCount();

      /*  String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths:-----> " + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }*/
        APICALLIMAGESCOUNT(context, count);

        cursor.close();


    }

    private void APICALLIMAGESCOUNT(final Context context, int count) {
        Date todayDate;
        String mCurrentDate;
        todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mCurrentDate = df.format(todayDate);
        Constant.setSession(context);
        Call<ImageCountModel> countModelCall = Constant.apiService.getImageCount(Constant.mUserId, mCurrentDate, String.valueOf(count));
        countModelCall.enqueue(new Callback<ImageCountModel>() {
            @Override
            public void onResponse(Call<ImageCountModel> call, Response<ImageCountModel> response) {
                if (response.body().getStatus().equals("0")) {
                    Toast.makeText(context, response.body().getResult().get(0).getCount(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ImageCountModel> call, Throwable t) {

            }
        });
    }


    /*  private void getImages(final Context context) {
        context.getContentResolver().registerContentObserver(android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI, true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        Toast.makeText(context, "Internal Media has been changed", Toast.LENGTH_SHORT).show();
                        Log.d("your_tag", "Internal Media has been changed");
                        super.onChange(selfChange);
                        Long timestamp = readLastDateFromMediaStore(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                        Log.e(TAG, "onChange: " + timestamp);

                    }
                }
        );
        context.getContentResolver().registerContentObserver(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
                new ContentObserver(new Handler()) {
                    @Override
                    public void onChange(boolean selfChange) {
                        Toast.makeText(context, "External Media has been changed", Toast.LENGTH_SHORT).show();
                        Log.d("your_tag", "External Media has been changed");
                        super.onChange(selfChange);

                        Long timestamp = readLastDateFromMediaStore(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        Log.e(TAG, "onChange: " + timestamp);
                    }
                }
        );
    }
      private Long readLastDateFromMediaStore(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, "date_added DESC");
        long dateAdded = -1;
        if (cursor.moveToNext()) {
            dateAdded = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED));
            Log.e(TAG, "Add new Images" + dateAdded);
            Toast.makeText(context, "Add new Images in Phone------>", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        return dateAdded;
    }
    */
}
