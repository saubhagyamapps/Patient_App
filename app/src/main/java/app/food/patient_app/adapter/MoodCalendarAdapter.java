package app.food.patient_app.adapter;

import android.content.Context;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import app.food.patient_app.R;
import app.food.patient_app.model.GetMoodNotesModel;

public class MoodCalendarAdapter extends RecyclerView.Adapter<MoodCalendarAdapter.MyViewHolder> {

    Context mContext;
    private List<GetMoodNotesModel.ResultBean> resultBeanList;

    public MoodCalendarAdapter(Context mContext, List<GetMoodNotesModel.ResultBean> resultBeanList) {
        this.mContext = mContext;
        this.resultBeanList = resultBeanList;
    }


    @NonNull
    @Override
    public MoodCalendarAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.moodlist,viewGroup,false);
        return new MoodCalendarAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoodCalendarAdapter.MyViewHolder myViewHolder, int position) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //String date = simpleDateFormat.format(resultBeanList.get(position).getDate());
        myViewHolder.txt_Activity.setText(resultBeanList.get(position).getActivities());
        myViewHolder.txt_Notes.setText(resultBeanList.get(position).getNotes());
    /*    String[] items1 = resultBeanList.get(position).getDate().split("-");
        String d1=items1[0];
        String m1=items1[1];
        String y1=items1[2];
        *//*int d = Integer.parseInt(d1);
        int m = Integer.parseInt(m1);
        int y = Integer.parseInt(y1);*//*
        String F_date = */
        myViewHolder.txt_Date.setText(resultBeanList.get(position).getDate());
        myViewHolder.txt_Time.setText(resultBeanList.get(position).getTime());
        if (resultBeanList.get(position).getMode().equals("Happy")) {
            myViewHolder.image_Mood.setImageResource(R.drawable.happy);
        } else if (resultBeanList.get(position).getMode().equals("In Love")){
            myViewHolder.image_Mood.setImageResource(R.drawable.in_love);
        } else if (resultBeanList.get(position).getMode().equals("Smiling")){
            myViewHolder.image_Mood.setImageResource(R.drawable.smiling);
        } else if (resultBeanList.get(position).getMode().equals("Angry")){
            myViewHolder.image_Mood.setImageResource(R.drawable.angry);
        }else if (resultBeanList.get(position).getMode().equals("Confused")){
            myViewHolder.image_Mood.setImageResource(R.drawable.confused);
        }else if (resultBeanList.get(position).getMode().equals("Mad")){
            myViewHolder.image_Mood.setImageResource(R.drawable.mad);
        }else if (resultBeanList.get(position).getMode().equals("Sad")){
            myViewHolder.image_Mood.setImageResource(R.drawable.sad);
        }else if (resultBeanList.get(position).getMode().equals("Smiling Happy")){
            myViewHolder.image_Mood.setImageResource(R.drawable.smilinghappy);
        }else if (resultBeanList.get(position).getMode().equals("Unhappy")){
            myViewHolder.image_Mood.setImageResource(R.drawable.unhappy);
        }else if (resultBeanList.get(position).getMode().equals("Very Happy")){
            myViewHolder.image_Mood.setImageResource(R.drawable.veryhappy);
        }
    }

    @Override
    public int getItemCount() {
        return resultBeanList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView txt_Date, txt_Time, txt_Activity,txt_Notes;
        ImageView image_Mood;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_Date = itemView.findViewById(R.id.txt_mood_date);
            txt_Activity = itemView.findViewById(R.id.txt_activity);
            txt_Notes =itemView.findViewById(R.id.txt_notes);
            image_Mood = itemView.findViewById(R.id.mood_image);
            txt_Time = itemView.findViewById(R.id.txt_time);
        }
    }
}
