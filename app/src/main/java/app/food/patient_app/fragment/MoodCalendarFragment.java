package app.food.patient_app.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import app.food.patient_app.R;
import app.food.patient_app.activity.MoodActivity;
import app.food.patient_app.adapter.MoodCalendarAdapter;
import app.food.patient_app.model.GetMoodNotesModel;
import app.food.patient_app.sessionmanager.SessionManager;
import app.food.patient_app.util.Constant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoodCalendarFragment extends Fragment {
    View mView;
    DatePicker datePicker;
    SpaceNavigationView spaceNavigationView;
    Calendar dateSelected;
    private DatePickerDialog datePickerDialog;
    Calendar newCalendar;
    Date startDate;
    int todaydate, thisMonth;
    Calendar newDate;
    String localTime;
    Date currentTime;
    DateFormat dateFormat;
    Dialog moodDialog, entriesDilog;
    String mDate, mTime,mMood, mActivity, mNotes;
    RecyclerView recyclerView_mood;
    int[] num = new int[1];
    private List<GetMoodNotesModel.ResultBean> resultBeanList;
    MoodCalendarAdapter moodCalendarAdapter;
    SessionManager sessionManager;
    HashMap<String, String> user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_mood_calendar, container, false);
        int year = Calendar.getInstance().get(Calendar.YEAR);
        getActivity().setTitle("Your Mood");
        dateSelected = Calendar.getInstance();
        initialization();
        getClick();
        setDateTimeField();
        EntriesDialog();

        return mView;

    }


    private void initialization() {
        recyclerView_mood = mView.findViewById(R.id.mood_recycle);
        moodCalendarAdapter = new MoodCalendarAdapter(getActivity(),resultBeanList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView_mood.setLayoutManager(layoutManager);
        spaceNavigationView = (SpaceNavigationView) mView.findViewById(R.id.space);
        //spaceNavigationView.initWithSaveInstanceState();
        spaceNavigationView.setCentreButtonIcon(R.drawable.add_entries);
        spaceNavigationView.addSpaceItem(new SpaceItem("Entries", R.drawable.ic_menu_manage));
        spaceNavigationView.addSpaceItem(new SpaceItem("Calendar", R.drawable.calendar));
        sessionManager = new SessionManager(getActivity());
        user = sessionManager.getUserDetails();
        getMoodData();
    }

    private void getClick() {
        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                moodDialog = new Dialog(getActivity(), R.style.EntridialogTheme);
                moodDialog.setContentView(R.layout.add_mood_layout);
               startActivity(new Intent(getActivity(), MoodActivity.class));
            }

            @Override

            public void onItemClick(int itemIndex, String itemName) {

                switch (itemName) {
                    case "Entries":
                        break;
                    case "Calendar":
                        datePickerDialog.show();
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemName) {
                    case "Entries":
                        break;
                    case "Calendar":
                        datePickerDialog.show();
                        break;
                }
            }
        });
    }

    private void getMoodData()
    {
        Constant.progressDialog(getActivity());
        final Call<GetMoodNotesModel> moodNotesModelCall = Constant.apiService.getMoodDetails(user.get(sessionManager.KEY_ID));
        moodNotesModelCall.enqueue(new Callback<GetMoodNotesModel>() {
            @Override
            public void onResponse(Call<GetMoodNotesModel> call, Response<GetMoodNotesModel> response) {
                List<GetMoodNotesModel.ResultBean> resultBean = response.body().getResult();
                Constant.progressBar.dismiss();
                if (response.body().getStatus().equals("0")){
                    moodCalendarAdapter = new MoodCalendarAdapter(getActivity(),resultBean);
                    recyclerView_mood.setHasFixedSize(true);
                    moodCalendarAdapter.notifyDataSetChanged();
                    recyclerView_mood.setAdapter(moodCalendarAdapter);
                }
                else {
                    Toast.makeText(getActivity(), "Error...", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GetMoodNotesModel> call, Throwable t) {
                Constant.progressBar.dismiss();
            }
        });
    }

    private void setDateTimeField() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
                TimeZone tz = dateSelected.getTimeZone();
                startDate = newDate.getTime();
                todaydate = newDate.get(Calendar.DAY_OF_MONTH);
                String fdate = sd.format(startDate);
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private void EntriesDialog() {
        entriesDilog = new Dialog(getActivity(), R.style.EntridialogTheme);
        entriesDilog.setContentView(R.layout.add_entries_custom);
        ImageView closeImage = (ImageView) entriesDilog.findViewById(R.id.close_dialog);
        FloatingActionButton fab = (FloatingActionButton) entriesDilog.findViewById(R.id.fab_button);
        TextView txt_Date = (TextView) entriesDilog.findViewById(R.id.txt_today_date);
        final TextView txt_Time = (TextView) entriesDilog.findViewById(R.id.txt_today_time);
        String getCurrentDate = DateFormat.getDateInstance().format(new Date());
        Calendar mCalendar = Calendar.getInstance();
        currentTime = mCalendar.getTime();
        dateFormat = new SimpleDateFormat("HH:mm:a");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+5:30"));
        localTime = dateFormat.format(currentTime);
    }
}
