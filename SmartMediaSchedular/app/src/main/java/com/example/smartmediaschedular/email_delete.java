package com.example.smartmediaschedular;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class email_delete extends AppCompatActivity {

    private static final int PICK_FILE=1;
    private static final int READ_EXTERNAL_STORAGE=100;
    private static final int SPEECH_RECORD=2;
    Long media_size;
    String not_id;
    String media_title,media1;
    AlertDialog.Builder builder,builder1;
    String sender1,receiver1,subject1,msg1,date1,time1,attach;
    TextInputLayout sender,receiver,msg,ddate,ttime,attach1;
    TextInputEditText attachment,sender2,receiver2;
    TextInputEditText date,time,mmsg;
    Button send,mic,browse,delete;
    Button schedule_date,schedule_time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_delete);
        final String id=getIntent().getStringExtra("id");
       // Toast.makeText(this,id,Toast.LENGTH_SHORT).show();

        getSupportActionBar().setTitle("Email");

        sender=(TextInputLayout) findViewById(R.id.sender_name);
        receiver=(TextInputLayout) findViewById(R.id.receiver_email);
        attachment=(TextInputEditText) findViewById(R.id.attachment);
        msg=(TextInputLayout) findViewById(R.id.message);
        attach1=(TextInputLayout) findViewById(R.id.attachment1);
        ddate=(TextInputLayout)findViewById(R.id.date1);
        ttime=(TextInputLayout)findViewById(R.id.time1);
        date=(TextInputEditText) findViewById(R.id.date);
        time=(TextInputEditText) findViewById(R.id.time);
        mmsg=(TextInputEditText) findViewById(R.id.message1);
        sender2=(TextInputEditText) findViewById(R.id.sender_name1);
        receiver2=(TextInputEditText) findViewById(R.id.receiver_email1);

        Call<List<emailData>> call=ApiClient.getInstance().getApi().getEmail("getEmail",id);
        call.enqueue(new Callback<List<emailData>>() {
            @Override
            public void onResponse(Call<List<emailData>> call, Response<List<emailData>> response) {

                List<emailData> list=response.body();
                attachment.setText(list.get(0).getAttachment_name());
                sender2.setText(list.get(0).getSender_name());
                receiver2.setText(list.get(0).getReceiver_email());
                mmsg.setText(list.get(0).getSubject());
                media_title=list.get(0).getAttachment_name();
                media1=list.get(0).getMedia1();
                if (list.get(0).getStatus().equals("0"))
                {
                    date.setText(list.get(0).getSchedule_date());
                    time.setText(list.get(0).getSchedule_time());

                }

            }

            @Override
            public void onFailure(Call<List<emailData>> call, Throwable t) {
                Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();

            }
        });
        browse=(Button) findViewById(R.id.attach) ;
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                try {
                    startActivityForResult(intent, PICK_FILE);

                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(),"There is no File Explorer Clients Installed.",Toast.LENGTH_SHORT).show();
                }

            }

        });
        mic=(Button) findViewById(R.id.mic);
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Hello,Say Now ....");
                try {
                    startActivityForResult(intent,SPEECH_RECORD);
                }
                catch (ActivityNotFoundException e)
                {
                    Toast.makeText(getApplicationContext(),"Sorry, Your Device Don't Support Speech Input",Toast.LENGTH_SHORT).show();


                }
            }
        });

        builder1=new AlertDialog.Builder(this);
        delete=findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                builder1.setMessage("Do you want Delete ?")
                        .setIcon(R.drawable.logo)
                        .setTitle("Confirmation of Delete")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Call<List<status>> call1=ApiClient.getInstance().getApi().deleteEmail("deleteEmail",id);
                                call1.enqueue(new Callback<List<status>>() {
                                    @Override
                                    public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                                        List<status> list=response.body();

                                        Toast.makeText(getApplicationContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();

                                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                        //intent.putExtra("status","Successfully Deleted");
                                        startActivity(intent);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<List<status>> call, Throwable t) {
                                        Toast.makeText(getApplicationContext(),t.toString(),Toast.LENGTH_SHORT).show();

                                    }
                                });


                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog=builder1.create();
                dialog.show();


            }
        });

        builder=new AlertDialog.Builder(this);
        send=(Button) findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender1=sender.getEditText().getText().toString().trim();
                receiver1=receiver.getEditText().getText().toString().trim();
                receiver1=receiver1.replaceAll("\\n","");
                attach=attachment.getText().toString().trim();
                msg1=msg.getEditText().getText().toString().trim();
                msg1=msg1.replaceAll("\\n","");
                date1=date.getText().toString().trim();
                time1=time.getText().toString().trim();

                if(validateSender() && validateReceiver() && validateAttach() && validatemsg() && validatedate() && validatetime())
                {
                    String toast=sender1+"\n"+receiver1+"\n"+attach+"\n"+msg1+"\n"+date1+"\n"+time1;
                    Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();

                    builder.setMessage("Do you want to Schedule ?")
                            .setIcon(R.drawable.logo)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        Call<List<status>> call=ApiClient.getInstance().getApi().insert_email(
                                                "insert_email",sender1,receiver1,media_title,msg1,date1,time1,0,media1
                                        );
                                        call.enqueue(new Callback<List<status>>() {
                                            @Override
                                            public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                                                List<status> list=new ArrayList<>();
                                                list=response.body();
                                                Toast.makeText(getApplicationContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();


                                                Calendar calendar = Calendar.getInstance();

                                                not_id=list.get(0).getId();
                                                String[] date=date1.split("/");
                                                String[] time=time1.split(":");
                                                int day=Integer.parseInt(date[0]);
                                                int mon=Integer.parseInt(date[1])-1;
                                                int year=Integer.parseInt(date[2]);
                                                int hour=Integer.parseInt(time[0]);
                                                int min=Integer.parseInt(time[1]);
                                                int sec=0;

                                                calendar.set(Calendar.YEAR,year);
                                                calendar.set(Calendar.MONTH,mon);
                                                calendar.set(Calendar.DAY_OF_MONTH,day);
                                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                                calendar.set(Calendar.MINUTE, min+1);
                                                calendar.set(Calendar.SECOND, sec);

                                                int hour2=calendar.get(Calendar.HOUR_OF_DAY);
                                                //Toast.makeText(getApplicationContext(),Integer.toString(hour2),Toast.LENGTH_SHORT).show();

                                                //Toast.makeText(getApplication(),Integer.toString(year)+"/"+Integer.toString(mon)+"/"+Integer.toString(day),Toast.LENGTH_SHORT).show();
                                                //Toast.makeText(getApplication(),Integer.toString(hour)+"/"+Integer.toString(min+1)+"/"+Integer.toString(sec),Toast.LENGTH_SHORT).show();

                                                startAlarm(calendar);

                                                Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                                                startActivity(intent);
                                                finish();

                                            }

                                            @Override
                                            public void onFailure(Call<List<status>> call, Throwable t) {

                                                Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    } catch (Throwable throwable) {
                                        throwable.printStackTrace();
                                    }
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("Confirmation of Scheduling");
                    alertDialog.show();


                }


            }
        });


        schedule_date=(Button) findViewById(R.id.schedule_date);
        schedule_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                final int dayofmon=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(email_delete.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        String mon,dmon;
                        month=month+1;
                        if(month<10)
                        {

                            mon="0"+Integer.toString(month);
                        }
                        else
                        {
                            mon=Integer.toString(month);
                        }
                        if(dayofmon<10)
                        {
                            dmon="0"+Integer.toString(dayofmon);
                        }
                        else
                        {
                            dmon=Integer.toString(dayofmon);
                        }
                        date.setText(dmon+"/"+mon+"/"+year);
                    }
                },year,month,dayofmon);

                Date d1=new Date();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        schedule_time=(Button) findViewById(R.id.schedule_time);
        schedule_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);
                int sec=calendar.get(Calendar.SECOND);
                TimePickerDialog timePickerDialog=new TimePickerDialog(email_delete.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String h,min;
                        if(hourOfDay<10)
                        {

                            h="0"+Integer.toString(hourOfDay);
                        }
                        else
                        {
                            h=Integer.toString(hourOfDay);
                        }
                        if(minute<10)
                        {
                            min="0"+Integer.toString(minute);
                        }
                        else
                        {
                            min=Integer.toString(minute);
                        }
                        time.setText(h+":"+min);
                    }
                },hour,min,true);
                timePickerDialog.show();
            }
        });

    }

    private boolean validatemsg() {

        if(!msg1.isEmpty())
        {  int c=0;

            for (int i=0;i<msg1.length();i++)
            {
                if((int)msg1.charAt(i)>=32 && (int)msg1.charAt(i)<=176)
                {
                    c+=1;
                }
                else if ((int) msg1.charAt(i)==10)
                {
                    c+=1;
                }


            }

            if (msg1.length()>=4 && msg1.length()==c ) {
                msg.setError(null);
                return true;
            }
            else {

                msg.setError("Title or Message is Invalid");
                return false;
            }
        }
        else
        {
            msg.setError("Field can't be Empty");
            return false;
        }
    }

    private boolean validateReceiver() {
        if(!receiver1.isEmpty())
        {
            String[] emails=receiver1.split(",");
            int count=0;

            for (int j=0;j<emails.length;j++)
            {

                String s=emails[j];

                if(Patterns.EMAIL_ADDRESS.matcher(s).matches())
                {

                    count+=1;
                }

            }

            if(count==(emails.length))
            {
                receiver.setError(null);
                return true;
            }
            else
            {
                receiver.setError("Receiver Email Id Invalid");
                return false;
            }

        }
        else
        {
            receiver.setError("Field can't be Empty");
            return false;
        }
    }

    private boolean validateSender() {

        if(!sender1.isEmpty())
        {
            int c=0;
            String l_sender=sender1.toLowerCase();
            for (int i=0;i<sender1.length();i++)
            {
                if((int)l_sender.charAt(i)>=97 && (int)l_sender.charAt(i)<=122)
                {

                    c+=1;
                }
                else if((int)l_sender.charAt(i)==32)
                {

                    c+=1;
                }
            }

            if (sender1.length()>=4 && sender1.length()==c ) {
                sender.setError(null);
                return true;
            }
            else {
                sender.setError("Sender Name is Invalid");
                return false;
            }

        }

        else
        {
            sender.setError("Field can't be Empty");
            return false;
        }
    }

    private boolean validatedate() {
        if (!date.getText().toString().toString().isEmpty())
        {
            ddate.setError(null);
            return true;
        }
        else
        {
            ddate.setError("Select the Date");
            return false;
        }
    }


    private boolean validatetime() {
        if (!time.getText().toString().toString().isEmpty())
        {
            ttime.setError(null);
            return true;
        }
        else
        {
            ttime.setError("Select the Time");
            return false;
        }
    }


    private boolean validateAttach() {
        if (!attach.isEmpty())
        {
            attach1.setError(null);
            return true;
            /*if((media_size/1000000)<=5)
            {

                attach1.setError(null);
                return true;
            }
            else
            {
                attach1.setError("Size must be less or equal to 5MB");
                return false;
            }
*/
        }
        else
        {
            attach1.setError("Select the Attachment");
            /*attachment.setFocusableInTouchMode(false);
            attachment.setFocusable(false);
            */
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==SPEECH_RECORD && resultCode==this.RESULT_OK && data!=null)
        {
            ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mmsg.setText(result.get(0));
        }
        else if (requestCode==PICK_FILE && resultCode== Activity.RESULT_OK)
        {
            Uri uri=data.getData();
            //  Toast.makeText(getContext(),uri.toString(),Toast.LENGTH_SHORT).show();

            String path=data.getData().getPath();
            attachment.setText(path);
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
            int nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            media_title=cursor.getString(nameIndex);
            String type=getContentResolver().getType(data.getData());
            String[] split_media=type.split("/");
            media_size=cursor.getLong(sizeIndex);
            if(split_media[0].equals("image"))
            {

                try {

                    Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),uri);
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,byteArrayOutputStream);
                    byte[] bytes=byteArrayOutputStream.toByteArray();
                    media1= Base64.encodeToString(bytes,Base64.DEFAULT);
                    //Toast.makeText(getContext(),media1,Toast.LENGTH_SHORT).show();



                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                // Toast.makeText(getContext(),split_media[0]+"\n"+path,Toast.LENGTH_SHORT).show();
                try {

                    InputStream fileInputStream= getContentResolver().openInputStream(data.getData());
                    byte[] bytes=new byte[1024];
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    int len;
                    while ((len=fileInputStream.read(bytes))>-1)
                    {
                        byteArrayOutputStream.write(bytes,0,len);
                    }

                    byte[] bytes1=byteArrayOutputStream.toByteArray();
                    Toast.makeText(getApplicationContext(),Long.toString(media_size),Toast.LENGTH_SHORT).show();

                    media1=Base64.encodeToString(bytes1, Base64.DEFAULT);

                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

                }


            }

        }


    }


    public void checkPermission(String permission,int requestCode)
    {
        if(ContextCompat.checkSelfPermission(this,permission)== PackageManager.PERMISSION_DENIED)
        {

            ActivityCompat.requestPermissions(this,new String[]{permission},requestCode);
        }
        else
        {
            //Toast.makeText(getApplicationContext(),"Permission Already Granted",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==READ_EXTERNAL_STORAGE)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getApplicationContext(),"Storage Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(),"Camera Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();

        super.onBackPressed();
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, Remainder.class);

        intent.putExtra(Intent.EXTRA_TITLE,sender1+"-"+receiver1+"-"+msg1+"-"+not_id);
        Toast.makeText(getApplicationContext(),sender1+"-"+receiver1+"-"+msg1+"-"+not_id,Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,Integer.parseInt(not_id), intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }


}
