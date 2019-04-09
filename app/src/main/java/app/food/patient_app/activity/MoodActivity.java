package app.food.patient_app.activity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import app.food.patient_app.R;

public class MoodActivity extends AppCompatActivity  {

    ImageView closeImage, nextImage,image_Happy, image_inLOve, image_Smiling, image_smilingHappy,image_VeryHappy,
    image_Confused,image_Sad,image_UnHappy,image_Mad, image_Angry;
    String mDate, mTime, mMood, mActivity, mNotes;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
        overridePendingTransition(R.anim.slide_left_enter, R.anim.slide_left_exit);
        initialize();

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
        getClick();


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
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood,image_Happy);
            }
        });
        image_inLOve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="In Love";
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_inLOve);
            }
        });
        image_Smiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Smiling";
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_Smiling);
            }
        });
        image_Angry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Angry";
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_Angry);
            }
        });
        image_Confused.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Confused";
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);

                IntenMethod(mMood, image_Confused);
            }
        });
        image_Mad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Mad";
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_Mad);
            }
        });
        image_Sad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Sad";
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_Sad);
            }
        });
        image_smilingHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Smiling Happy";
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_smilingHappy);
            }
        });
        image_UnHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Unhappy";
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_UnHappy);
            }
        });
        image_VeryHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMood ="Very Happy";
                image_VeryHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
                image_Happy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_inLOve.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Smiling.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Angry.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Confused.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Mad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_Sad.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_UnHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                image_smilingHappy.setColorFilter(getResources().getColor(R.color.tinColorIcon_us), PorterDuff.Mode.SRC_IN);
                IntenMethod(mMood, image_VeryHappy);
            }
        });
    }

    public void IntenMethod(String moodKey, ImageView image){
        image.setColorFilter(getResources().getColor(R.color.tinColorIcon_s), PorterDuff.Mode.SRC_IN);
        Toast.makeText(MoodActivity.this, "Mood is "+mMood , Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(MoodActivity.this,MoodNextActivity.class);
        intent.putExtra("moodkey",moodKey);
        startActivity(intent);
    }

}