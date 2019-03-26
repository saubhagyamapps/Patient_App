package app.food.patient_app.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.TimeZone;

import app.food.patient_app.R;
import app.food.patient_app.fragment.MoodCalendarFragment;
import app.food.patient_app.model.MoodInsertModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoodNextActivity extends AppCompatActivity {
    ImageView closeImage, image_Work, image_Meeting, image_Friends, image_Exercise, image_Music;
    FloatingActionButton fab;
    TextView txt_Date, txt_Time;
    String localTime;
    Date currentTime;
    DateFormat dateFormat, timeFormat;
    DatePickerDialog datePickerDialog;
    Calendar dateSelected;
    Date startDate;
    int todaydate, thisMonth;
    Calendar mCalendar;
    EditText edt_Notes;
    String mDate, mMood, mTime, mActivity, mNotes;
    String getCurrentDate;
    SessionManager sessionManager;
    HashMap<String, String> user;
    String fdate;
    Bundle bundle = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_next);
        mMood = getIntent().getExtras().getString("moodkey");
        initialize();
        setDate_Time();
        getDate_Time();
        getClick();
    }

    private void initialize() {
        sessionManager = new SessionManager(MoodNextActivity.this);
        user = sessionManager.getUserDetails();
        mCalendar = Calendar.getInstance();
        closeImage = (ImageView) findViewById(R.id.close_next_activity);
        fab = (FloatingActionButton) findViewById(R.id.fab_button);
        txt_Date = (TextView) findViewById(R.id.txt_today_date);
        txt_Time = findViewById(R.id.txt_today_time);
        edt_Notes = findViewById(R.id.edt_notes);
        image_Work = findViewById(R.id.work);
        image_Meeting = findViewById(R.id.meeting);
        image_Friends = findViewById(R.id.friends);
        image_Exercise = findViewById(R.id.exercise);
        image_Music = findViewById(R.id.music);

    }

    private void getValue() {
        startDate = mCalendar.getTime();

        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        mDate = sd.format(startDate);
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        localTime = timeFormat.format(currentTime);
        mTime = localTime;
        mNotes = edt_Notes.getText().toString();

        InsertMoodData();

    }



    private void getClick() {

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValue();
            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });
        txt_Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        txt_Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCalendar = Calendar.getInstance();
                int hour = mCalendar.get(Calendar.HOUR);
                int minute = mCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(MoodNextActivity.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        // txt_Time.setText( selectedHour + ":" + selectedMinute);
                        txt_Time.setText(String.format("%02d:%02d %s", selectedHour, selectedMinute,
                                selectedHour < 12 ? "AM" : "PM"));
                    }
                }, hour, minute, false);//Yes 24 hour time

                mTimePicker.show();
            }
        });
        image_Work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = "Work";
                Toast.makeText(MoodNextActivity.this, "Work selected as Activity", Toast.LENGTH_SHORT).show();
            }
        });
        image_Meeting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = "Meeting";
                Toast.makeText(MoodNextActivity.this, "Meeting Selected as Activity", Toast.LENGTH_SHORT).show();
            }
        });
        image_Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = "Friends";
                Toast.makeText(MoodNextActivity.this, "Friends Selected as Activity", Toast.LENGTH_SHORT).show();
            }
        });
        image_Exercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = "Exercise";
                Toast.makeText(MoodNextActivity.this, "Exercise Selected as Activity", Toast.LENGTH_SHORT).show(); mActivity = "Meeting";

            }
        });
        image_Music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity = "Music";
                Toast.makeText(MoodNextActivity.this, "Music Selected as Activity", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void InsertMoodData() {
        int mMinute = new Time(System.currentTimeMillis()).getMinutes();
        int mHour = new Time(System.currentTimeMillis()).getHours();
        int hour = Calendar.getInstance().get(Calendar.HOUR);
        int second = Calendar.getInstance().get(Calendar.SECOND);
        String mCurrentTime=String.valueOf(hour)+":"+String.valueOf(mMinute)+":"+String.valueOf(second);
        Constant.progressDialog(MoodNextActivity.this);
        Call<MoodInsertModel> moodInsertModelCall =
                Constant.apiService.SetMoodDetails(user.get(sessionManager.KEY_ID), mDate, mCurrentTime, mMood, mActivity, mNotes);

        moodInsertModelCall.enqueue(new Callback<MoodInsertModel>() {
            @Override
            public void onResponse(Call<MoodInsertModel> call, Response<MoodInsertModel> response) {
                Constant.progressBar.dismiss();
                String status = response.body().getStatus();
                if (response.body().getStatus().equals("0")) {
                    Toast.makeText(MoodNextActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MoodNextActivity.this, NavigationActivity.class));

                } else {
                    Toast.makeText(MoodNextActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MoodInsertModel> call, Throwable t) {
                Constant.progressBar.dismiss();
            }
        });
    }

    private void setDate_Time() {

        String getCurrentDate = DateFormat.getDateInstance().format(new Date());
        currentTime = mCalendar.getTime();
        timeFormat = new SimpleDateFormat("HH:mm:a");
        timeFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        localTime = timeFormat.format(currentTime);
        txt_Date.setText(getCurrentDate);
        txt_Time.setText(localTime);
    }

    private void getDate_Time() {
        datePickerDialog = new DatePickerDialog(MoodNextActivity.this, R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String getCurrentDate = DateFormat.getDateInstance().format(new Date());
                mCalendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                TimeZone tz = dateSelected.getTimeZone();
                startDate = mCalendar.getTime();
                todaydate = mCalendar.get(Calendar.DAY_OF_MONTH);
                fdate = sd.format(startDate);
               // txt_Date.setText(fdate);
            }
        }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH), mCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

}
