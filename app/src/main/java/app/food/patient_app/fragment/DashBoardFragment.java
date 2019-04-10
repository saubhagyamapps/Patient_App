package app.food.patient_app.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.budiyev.android.circularprogressbar.CircularProgressBar;

import app.food.patient_app.R;

public class DashBoardFragment extends Fragment {
    View mView;
    TextView txt_Sleep_Percentage,txt_Social_Percentage,txt_Work_Percentage,txt_Exercise_Percentage;
    CircularProgressBar Sleep_Progressbar, Social_Progressbar, Work_Progressbar, Exercise_Progressbar;
    LinearLayout Sleep_Layout, Social_Layout, Work_Layout, Exercise_Layout;
    RelativeLayout Mood_Layout;
    Drawable drawable;
    Resources res;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        getActivity().setTitle("DashBoard");
        Initialize();
        SetProgressBars();
        Clicked();
        return mView;
    }
    private void  Initialize()
    {

        Sleep_Progressbar = mView.findViewById(R.id.sleep_progress_bar);
        Social_Progressbar = mView.findViewById(R.id.social_progress_bar);
        Work_Progressbar = mView.findViewById(R.id.work_progress_bar);
        Exercise_Progressbar = mView.findViewById(R.id.work_progress_bar);
       /* Sleep_Layout = mView.findViewById(R.id.sleep_layout);
        Social_Layout = mView.findViewById(R.id.social_layout);
        Work_Layout = mView.findViewById(R.id.work_layout);
        Exercise_Layout = mView.findViewById(R.id.exercise_layout);*/
        Mood_Layout =mView.findViewById(R.id.mood_layout_);
        txt_Sleep_Percentage = mView.findViewById(R.id.txt_sleep_percentage);
        txt_Social_Percentage = mView.findViewById(R.id.txt_social_percentage);
        txt_Work_Percentage = mView.findViewById(R.id.txt_work_percentage);
        txt_Exercise_Percentage = mView.findViewById(R.id.txt_exercise_percentage);

    }
    public void SetProgressBars()
    {
        Sleep_Progressbar.setMaximum(100);
        Sleep_Progressbar.setProgress(45f);
        Sleep_Progressbar.setAnimateProgress(true);
        txt_Sleep_Percentage.setText(45+"%");

        Social_Progressbar.setMaximum(100);
        Social_Progressbar.setProgress(40f);
        txt_Social_Percentage.setText(40+"%");

        Work_Progressbar.setMaximum(100);
        Work_Progressbar.setProgress(30f);
        txt_Work_Percentage.setText(30+"%");

        Exercise_Progressbar.setMaximum(100);
        Exercise_Progressbar.setProgress(20f);
        txt_Exercise_Percentage.setText(20+"%");

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
