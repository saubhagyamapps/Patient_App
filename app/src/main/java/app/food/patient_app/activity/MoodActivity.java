package app.food.patient_app.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import app.food.patient_app.R;

public class MoodActivity extends AppCompatActivity  {

    ImageView closeImage, nextImage,image_Happy, image_inLOve, image_Smiling, image_smilingHappy,image_VeryHappy,
    image_Confused,image_Sad,image_UnHappy,image_Mad, image_Angry;
    String mDate, mTime, mMood, mActivity, mNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        initialize();
        getClick();
    }


    private void initialize() {
        closeImage = (ImageView) findViewById(R.id.close_dialog);
        nextImage = (ImageView) findViewById(R.id.next_dialog);
        image_Happy = findViewById(R.id.happy);
        image_inLOve = findViewById(R.id.inlove);
        image_Smiling = findViewById(R.id.smiling);
        image_smilingHappy = findViewById(R.id.smilinghappy);
        image_VeryHappy = findViewById(R.id.veryhappy);
        image_Confused = findViewById(R.id.confused);
        image_Sad = findViewById(R.id.sad);
        image_UnHappy = findViewById(R.id.unhappy);
        image_Mad = findViewById(R.id.mad);
        image_Angry = findViewById(R.id.angry);




    }

    private void getClick() {

        nextImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        closeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MoodActivity.this, NavigationActivity.class));
            }
        });
        image_Happy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Happy";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_inLOve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="In Love";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_Smiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Smiling";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_Angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Angry";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_Confused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Confused";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_Mad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Mad";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_Sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Sad";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_smilingHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Smiling Happy";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_UnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Unhappy";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
        image_VeryHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Very Happy";
                Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
                intent.putExtra("moodkey",mMood);
                startActivity(intent);
            }
        });
    }


}