package app.food.patient_app.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import app.food.patient_app.R;

public class DashBoardFragment extends Fragment {
    View mView;
    TextView txt_Sleep_Percentage, txt_Sleep_Hour;
    TextView txt_Social_Percentage, txt_Social_Hour;
    TextView txt_Work_Percentage, txt_Work_Hour;
    TextView txt_Exercise_Percentage, txt_Exercise_Hour;
    LinearLayout Sleep_Layout, Social_Layout, Work_Layout, Exercise_Layout;
    RelativeLayout Mood_Layout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        getActivity().setTitle("DashBoard");
        Initialize();
        Clicked();
        return mView;
    }
    private void  Initialize()
    {
        Sleep_Layout = mView.findViewById(R.id.sleep_layout);
        Social_Layout = mView.findViewById(R.id.social_layout);
        Work_Layout = mView.findViewById(R.id.work_layout);
        Exercise_Layout = mView.findViewById(R.id.exercise_layout);
        Mood_Layout =mView.findViewById(R.id.mood_layout_);
        txt_Sleep_Percentage = mView.findViewById(R.id.txt_sleeppercentage);
        txt_Sleep_Hour = mView.findViewById(R.id.txt_sleep_hour);
        txt_Social_Percentage = mView.findViewById(R.id.txt_social_percentage);
        txt_Social_Hour = mView.findViewById(R.id.txt_social_hour);
        txt_Work_Percentage = mView.findViewById(R.id.txt_work_percentage);
        txt_Work_Hour = mView.findViewById(R.id.txt_work_hour);
        txt_Exercise_Percentage = mView.findViewById(R.id.txt_exercise_percentage);
        txt_Exercise_Hour = mView.findViewById(R.id.txt_exercise_hour);
    }


    private void Clicked()
    {
        Mood_Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager =  getActivity().getSupportFragmentManager();
                FragmentTransaction ft =  fragmentManager.beginTransaction();
                ft.replace(R.id.content_frame, new MoodCalendarFragment());
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }
}
