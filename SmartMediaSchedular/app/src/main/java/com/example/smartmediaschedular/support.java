package com.example.smartmediaschedular;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class support extends AppCompatActivity {

    TextInputLayout support;
    String support1;
    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        getSupportActionBar().setTitle("Support");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Toast.makeText(getApplicationContext(),"Support",Toast.LENGTH_SHORT).show();
        support=(TextInputLayout) findViewById(R.id.support);
        submit=(Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                support1=support.getEditText().getText().toString().trim();

                if(validateSupport())
                {
                    Toast.makeText(getApplicationContext(),support1,Toast.LENGTH_SHORT).show();
                    Call<List<status>> call=ApiClient.getInstance().getApi().insertSupport("insertSupport",support1);
                    call.enqueue(new Callback<List<status>>() {
                        @Override
                        public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                            List<status> list=response.body();
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

    public boolean validateSupport()
    {
        if(support1.isEmpty())
        {
            support.setError("Field does not Empty");
            return false;
        }
        else
        {
            support.setError(null);
            return true;
        }
    }
}
