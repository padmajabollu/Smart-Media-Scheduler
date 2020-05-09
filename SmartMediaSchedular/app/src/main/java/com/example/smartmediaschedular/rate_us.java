package com.example.smartmediaschedular;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class rate_us extends AppCompatActivity {

    RatingBar ratingBar;
    Button feedback;
    TextInputLayout experience;
    String rating,expe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        getSupportActionBar().setTitle("Rate Us");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toast.makeText(getApplicationContext(),"Rate Us",Toast.LENGTH_SHORT).show();

        experience=(TextInputLayout) findViewById(R.id.support);
        ratingBar=(RatingBar) findViewById(R.id.rating);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //Toast.makeText(getApplicationContext(),Integer.toString((int)rating),Toast.LENGTH_SHORT).show();
            }
        });
        LayerDrawable stars=(LayerDrawable) ratingBar.getProgressDrawable() ;
        stars.getDrawable(2).setColorFilter(Color.parseColor("#4065DD"), PorterDuff.Mode.SRC_ATOP);
        feedback=(Button) findViewById(R.id.feedback);

        feedback.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                rating = Integer.toString((int) ratingBar.getRating());
                expe = experience.getEditText().getText().toString().trim();
                String toast = rating + "\n" + expe;
                if (validateRating()) {
                   // Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
                    Call<List<status>> call=ApiClient.getInstance().getApi().insertRateus("insertRateus",rating,expe);
                    call.enqueue(new Callback<List<status>>() {
                        @Override
                        public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                            List<status> list=new ArrayList<>();
                            list=response.body();
                            Toast.makeText(getApplicationContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();

                        }

                        @Override
                        public void onFailure(Call<List<status>> call, Throwable t) {
                            Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });

                }
            }
        });
    }

    public boolean validateRating()
    {
        if(ratingBar.getRating()<=0)
        {
            Toast.makeText(getApplicationContext(),"Please, give the Rating",Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Remainder.class);
        intent.putExtra(Intent.EXTRA_TITLE,"Remainder of Scheduling !-Padmaja Bollu-pjbollu@mitaoe.ac.in-Birthday Wishes.");

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }

    private void cancelAlarm() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Remainder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        alarmManager.cancel(pendingIntent);
        Toast.makeText(getApplicationContext(),"Alarm Canceled",Toast.LENGTH_SHORT).show();
    }



}
