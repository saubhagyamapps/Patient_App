package app.food.patient_app.activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import java.util.HashMap;
import java.util.StringTokenizer;

import app.food.patient_app.R;
import app.food.patient_app.fragment.CallLogsFragment;
import app.food.patient_app.fragment.DashBoardFragment;
import app.food.patient_app.fragment.GetCurrentLocationFragment;
import app.food.patient_app.fragment.GoogleFitDataFragment;
import app.food.patient_app.fragment.MoodCalendarFragment;
import app.food.patient_app.fragment.ResetPasswordFragment;
import app.food.patient_app.fragment.WorkLocationFragment;
import app.food.patient_app.lockscreen.LockScreenCountActivity;
import app.food.patient_app.model.CalllogsListModel;
import app.food.patient_app.model.RemainingCallModel;
import app.food.patient_app.receiver.AlarmReceiver;
import app.food.patient_app.service.ConnectionCheckService;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.ui.MainActivity;
import app.food.patient_app.util.Constant;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static app.food.patient_app.util.Constant.session;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SessionManager sessionManager;
    Fragment fragment;
    private static final String TAG = "NavigationActivity";
    CalllogsListModel calllogsListModel;
    ArrayList<CalllogsListModel> calllogsListModels = new ArrayList<>();
    JSONObject singObj;
    String mName;
    private String url;
    ArrayList<HashMap<String, String>> contactList;
    String mMonthId;
    NavigationView navigationView;
    TextView txtName, txtEmail;
    CircleImageView imgUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        Constant.setSession(NavigationActivity.this);


        Intent intent = new Intent(NavigationActivity.this, ConnectionCheckService.class);
        intent.setAction(ConnectionCheckService.ACTION_START_FOREGROUND_SERVICE);
        startService(intent);
        contactList = new ArrayList<>();
        Constant.setSession(getApplicationContext());
        RemainningCallAPI();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.content_frame, new DashBoardFragment());
        ft.commit();
        setNotifacation();
        sessionManager = new SessionManager(NavigationActivity.this);
        getFilePaths();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

         navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        setnavigationHeader();
    }


    public void getFilePaths() {
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
        final String orderBy = MediaStore.Images.Media._ID;
        //Stores all the images from the gallery in Cursor
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null,
                null, orderBy);
        //Total number of images
        int count = cursor.getCount();

        //Create an array to store path to all the images
        String[] arrPath = new String[count];
        Log.e(TAG, "getFilePaths----->: " + count);
        for (int i = 0; i < count; i++) {
            cursor.moveToPosition(i);
            int dataColumnIndex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            //Store the path of the image
            arrPath[i] = cursor.getString(dataColumnIndex);
            Log.i("PATH", arrPath[i]);
        }
// The cursor should be freed up after use with close()
        cursor.close();


    }

    private void setnavigationHeader() {
        View header = navigationView.getHeaderView(0);
        txtName = (TextView) header.findViewById(R.id.txtName);
        txtEmail = (TextView) header.findViewById(R.id.txtEmail);
    //    imgUser = header.findViewById(R.id.imageView);
        txtName.setText(Constant.mUserName.toUpperCase());
        txtEmail.setText(Constant.mUserEmail);
      /*  Glide.with(getApplicationContext()).load(user.get(session.KEY_IMAGE))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgUser);*/
    }


    private void setNotifacation() {
        Intent myIntent = new Intent(NavigationActivity.this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(NavigationActivity.this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Calendar firingCal = Calendar.getInstance();
        Calendar currentCal = Calendar.getInstance();
        //    int mMinute = new Time(System.currentTimeMillis()).getMinutes() ;
        int mMinute = new Time(System.currentTimeMillis()).getMinutes() + 1;
        int mHour = new Time(System.currentTimeMillis()).getHours();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int SECOND = Calendar.getInstance().get(Calendar.SECOND);
        firingCal.set(Calendar.HOUR, hour); // At the hour you wanna fire
        firingCal.set(Calendar.MINUTE, mMinute); // Particular minute
        firingCal.set(Calendar.SECOND, SECOND); // particular second

        long intendedTime = firingCal.getTimeInMillis();
        long currentTime = currentCal.getTimeInMillis();

        if (intendedTime >= currentTime) {
            // you can add buffer time too here to ignore some small differences in milliseconds
            // set from today
            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        } else {
            // set from next day
            // you might consider using calendar.add() for adding one day to the current day
            firingCal.add(Calendar.DAY_OF_MONTH, 1);
            intendedTime = firingCal.getTimeInMillis();

            alarmManager.setRepeating(AlarmManager.RTC, intendedTime, AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        fragment = null;

        if (id == R.id.nav_manage) {
            fragment = new DashBoardFragment();
        } else if (id == R.id.nav_calendar) {
            fragment = new MoodCalendarFragment();
        } else if (id == R.id.nav_calllogs) {
            fragment = new CallLogsFragment();
        } else if (id == R.id.nav_socialapp) {
            fragment = new MainActivity();

        } else if (id == R.id.nav_googlefit) {
            fragment = new GoogleFitDataFragment();

        } else if (id == R.id.nav_resetpassword) {

            fragment = new ResetPasswordFragment();

        } else if (id == R.id.nav_editprofile) {

            //fragment = new EditProfilFragment();
            startActivity(new Intent(getApplicationContext(), FitnessActivity.class));
        } else if (id == R.id.nav_logout) {

            sessionManager.logoutUser();
            finish();
        } else if (id == R.id.nav_getCurrentLocation) {

            fragment = new GetCurrentLocationFragment();
            //  startActivity(new Intent(getApplicationContext(), SearchLocationActivity.class));
        } else if (id == R.id.nav_Voice) {

            //fragment = new VoiceAnalizerFragment();
            startActivity(new Intent(getApplicationContext(), LockScreenCountActivity.class));

        }
        else if (id == R.id.nav_work_location) {

            fragment = new WorkLocationFragment();
           // startActivity(new Intent(getApplicationContext(), LockScreenCountActivity.class));

        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


    private void RemainningCallAPI() {
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c);

        Call<RemainingCallModel> callModelCall = Constant.apiService.remainingCall(Constant.mUserId, formattedDate);
        callModelCall.enqueue(new Callback<RemainingCallModel>() {
            @Override
            public void onResponse(Call<RemainingCallModel> call, Response<RemainingCallModel> response) {
                try {
                    for (int i = 0; i < response.body().getResult().size(); i++) {
                        getCallDetails(response.body().getResult().get(i));

                    }
                }catch (Exception e){

                }

            }

            @Override
            public void onFailure(Call<RemainingCallModel> call, Throwable t) {

            }
        });
    }

    private void getCallDetails(String s) {
        Log.e(TAG, "Display Api remaining date" + s);
        StringBuffer sb = new StringBuffer();
        Cursor managedCursor = managedQuery(CallLog.Calls.CONTENT_URI, null, null,
                null, null);
        int name = managedCursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        sb.append("Call Log :");
        while (managedCursor.moveToNext()) {

            String Name = managedCursor.getString(name);
            String phNumber = managedCursor.getString(number);
            String callType = managedCursor.getString(type);
            String callDate = managedCursor.getString(date);
            Date callDayTime = new Date(Long.valueOf(callDate));
            String callDuration = managedCursor.getString(duration);
            String dir = null;
            StringTokenizer tk = new StringTokenizer(String.valueOf(callDayTime));
            String day = tk.nextToken();
            String Month = tk.nextToken();
            String Date = tk.nextToken();
            String Time = tk.nextToken();
            String TIMEZONE = tk.nextToken();
            String Year = tk.nextToken();

            if (Month.equals("Jan")) {
                mMonthId = "01";
            } else if (Month.equals("Feb")) {
                mMonthId = "02";
            } else if (Month.equals("Mar")) {
                mMonthId = "03";
            } else if (Month.equals("Apr")) {
                mMonthId = "04";
            } else if (Month.equals("May")) {
                mMonthId = "05";
            } else if (Month.equals("Jun")) {
                mMonthId = "06";
            } else if (Month.equals("Jul")) {
                mMonthId = "07";
            } else if (Month.equals("Aug")) {
                mMonthId = "08";
            } else if (Month.equals("Sep")) {
                mMonthId = "09";
            } else if (Month.equals("Oct")) {
                mMonthId = "10";
            } else if (Month.equals("Nov")) {
                mMonthId = "11";
            } else if (Month.equals("Dec")) {
                mMonthId = "12";
            }

            String mNewDate = Year + "-" + mMonthId + "-" + Date;
            String mDate = Year + "-" + mMonthId + "-" + Date;

            int dircode = Integer.parseInt(callType);
           /* switch (dircode) {
                case CallLog.Calls.INCOMING_TYPE:
                    dir = "INCOMING";
                    break;
                case CallLog.Calls.OUTGOING_TYPE:
                    dir = "OUTGOING";
                    break;
                case CallLog.Calls.MISSED_TYPE:
                    dir = "MISSED";
                    break;

            }*/

            if (Name != null && !Name.isEmpty() && !Name.equals("null")) {
                mName = Name;
            } else {
                mName = "Unknown";
            }
            if (mNewDate.equals(s)) {
                calllogsListModel = new CalllogsListModel(mName, phNumber, callType, mDate, Time, callDuration);
                calllogsListModels.add(calllogsListModel);
            }

        }


        JSONArray contaArray = new JSONArray();
        for (int i = 0; i < calllogsListModels.size(); i++) {
            try {
                singObj = new JSONObject();
                singObj.put("name", calllogsListModels.get(i).getName());
                singObj.put("contactno", calllogsListModels.get(i).getPhone_number());
                singObj.put("type", calllogsListModels.get(i).getCall_type());
                singObj.put("duration", calllogsListModels.get(i).getCall_duration());
                singObj.put("time", calllogsListModels.get(i).getCall_time());
                singObj.put("date", calllogsListModels.get(i).getCall_date());
                contaArray.put(singObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        JsonParser parser = new JsonParser();
        JsonArray array = (JsonArray) parser.parse(contaArray.toString());
        org.json.JSONObject object = singObj;
        JsonParser jsonParser = new JsonParser();
//        JsonObject gsonObject = (JsonObject) jsonParser.parse(object.toString());
        Constant.setSession(getApplicationContext());
        Log.e(TAG, "getCallDetails: " + array.toString());


        apicall(array.toString(), s);

    }

    private void apicall(String toString, String s) {
        Constant.setSession(getApplicationContext());
        Log.e(TAG, "mUserId: " + Constant.mUserId);

        Call<String> call = Constant.apiService.sendLocation(toString, Constant.mUserId, s);
        call.enqueue(new Callback<String>() {
                         @Override
                         public void onResponse(Call<String> call, Response<String> response) {
                             Log.d("response", "Getting response from server : " + response.message());
                             Log.e(TAG, "onResponse: " + response.message());
                         }

                         @Override
                         public void onFailure(Call<String> call, Throwable t) {
                             Log.d("response", "Getting response from server : " + t.getMessage());
                         }
                     }
        );
    }


}
