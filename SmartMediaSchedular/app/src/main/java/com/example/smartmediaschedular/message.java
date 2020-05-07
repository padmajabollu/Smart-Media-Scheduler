package com.example.smartmediaschedular;


import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Contacts;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.speech.RecognizerIntent;
import android.text.format.DateFormat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class message extends Fragment {

    private static final int PICK_CONTACTS=1;
    private static final int CONTACT=1;
    String not_id;
    private static final int SPEECH_RECORD=2;
    String Phone="";
    String sender1,msg1,phno1,date1,time1;
    TextInputLayout sender,phno,msg,ddate,ttime;
    TextInputEditText date,time,phone,mmsg;
    Button send,contact,mic;
    Button schedule_date,schedule_time;
    AlertDialog.Builder builder;

    public message() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_message, container, false);
        sender=(TextInputLayout) view.findViewById(R.id.sender_name);
        phno=(TextInputLayout) view.findViewById(R.id.phno);
        msg=(TextInputLayout) view.findViewById(R.id.message);
        ddate=(TextInputLayout)view.findViewById(R.id.date1);
        ttime=(TextInputLayout)view.findViewById(R.id.time1);
        phone=(TextInputEditText) view.findViewById(R.id.phone);
        builder=new AlertDialog.Builder(getContext());
        date=(TextInputEditText) view.findViewById(R.id.date);
        time=(TextInputEditText) view.findViewById(R.id.time);
        mmsg=(TextInputEditText) view.findViewById(R.id.message1);
        contact=(Button) view.findViewById(R.id.contact);
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission(Manifest.permission.READ_CONTACTS,CONTACT);
                Intent intent=new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent,PICK_CONTACTS);
            }
        });

        mic=(Button) view.findViewById(R.id.mic);
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
                    Toast.makeText(getContext(),"Sorry, Your Device Don't Support Speech Input",Toast.LENGTH_SHORT).show();


                }
            }
        });
        send=(Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sender1=sender.getEditText().getText().toString().trim();
                phno1=phno.getEditText().getText().toString().trim();
                phno1=phno1.replaceAll("\\n","");
                msg1=msg.getEditText().getText().toString().trim();
                date1=date.getText().toString().trim();
                time1=time.getText().toString().trim();

                if(validateSender() && validatephno() && validatemsg() && validatedate() && validatetime()) {
                    String toast = sender1 + "\n" + phno1 + "\n" + msg1 + "\n" + date1 + "\n" + time1;

                    Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();

                    builder.setMessage("Do you want to Schedule ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        finalize();
                                        Call<List<status>> call=ApiClient.getInstance().getApi().insert_message(
                                          "insert_message",sender1,phno1,msg1,date1,time1,0
                                        );

                                        call.enqueue(new Callback<List<status>>() {
                                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                            @Override
                                            public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                                                List<status> list=new ArrayList<>();
                                                list=response.body();
                                                Toast.makeText(getContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();

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

                                                Intent intent=new Intent(getContext(),MainActivity.class);
                                                startActivity(intent);
                                                getActivity().finish();


                                            }

                                            @Override
                                            public void onFailure(Call<List<status>> call, Throwable t) {
                                                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                            }                                        });

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

                    AlertDialog alertDialog = builder.create();
                    alertDialog.setTitle("Confirmation of Scheduling");
                    alertDialog.show();
                }

            }
        });

        schedule_date=(Button) view.findViewById(R.id.schedule_date);
        schedule_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();
                int year=calendar.get(Calendar.YEAR);
                int month=calendar.get(Calendar.MONTH);
                final int dayofmon=calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog=new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
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

        schedule_time=(Button) view.findViewById(R.id.schedule_time);
        schedule_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar=Calendar.getInstance();

                int hour=calendar.get(Calendar.HOUR_OF_DAY);
                int min=calendar.get(Calendar.MINUTE);
                int sec=calendar.get(Calendar.SECOND);
                TimePickerDialog timePickerDialog=new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
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

     return  view;
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

                msg.setError("Message is Invalid");
                return false;
            }
        }
        else
        {
            msg.setError("Field can't be Empty");
            return false;
        }
    }

    private boolean validatephno() {
        if(!phno1.isEmpty())
        {
            String[] calls=phno1.split(",");
            int count=0;

            for (int j=0;j<calls.length;j++)
            {
                int c=0;
                String s=calls[j];

                for (int i=0;i<s.length();i++)
                {
                    //Toast.makeText(getContext(),Integer.toString((int)s.charAt(i)),Toast.LENGTH_SHORT).show();


                    if((int)s.charAt(i)>=48 && (int)s.charAt(i)<=57)
                    {
                        c+=1;
                    }
                    else if((int)s.charAt(i)==43)
                    {
                        c+=1;
                    }

                }

                if (c==calls[j].length() && (c==13 || c==10))
                {
                    count+=1;
                }

            }

            if(count==(calls.length))
            {
                phno.setError(null);
                return true;
            }
            else
            {
                phno.setError("Phone Number is Invalid");
                return false;
            }

        }
        else
        {
            phno.setError("Field can't be Empty");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PICK_CONTACTS && resultCode==getActivity().RESULT_OK)
        {
            ContentResolver resolver=getContext().getContentResolver();
            Uri uri=data.getData();

            Cursor cursor=resolver.query(uri,null,null,null,null);

           while (cursor.moveToNext())
           {
               String contactid=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
               String contactphone=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
               String contactname=cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

               if ("1".equals(contactphone))
               {
                   Cursor phones=resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID+"="+contactid,null,null);
                   String PhoneNumber = "";
                   while (phones.moveToNext())
                   {
                       PhoneNumber=phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                   }

                   Phone+=PhoneNumber+",";
                   Phone=Phone.replaceAll("\\s","");

                   phone.setText(Phone.substring(0,Phone.length()-1));

                   //Toast.makeText(getContext(),("\n"+phone.getText().toString()),Toast.LENGTH_SHORT).show();
                   phones.close();
               }
           }
           cursor.close();
           }
        else if(requestCode==SPEECH_RECORD && resultCode==getActivity().RESULT_OK && data!=null)
        {
            ArrayList<String> result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mmsg.setText(result.get(0));
        }
    }


    public void checkPermission(String permission,int requestCode)
    {
        if(ContextCompat.checkSelfPermission(getContext(),permission)== PackageManager.PERMISSION_DENIED)
        {

            ActivityCompat.requestPermissions(getActivity(),new String[]{permission},requestCode);
        }
        else
        {
            //Toast.makeText(getContext(),"Permission Already Granted",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==CONTACT)
        {
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(),"Storage Permission Granted",Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(),"Camera Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void startAlarm(Calendar c) {
        AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getContext(), Remainder.class);

        intent.putExtra(Intent.EXTRA_TITLE,sender1+"-"+phno1+"-"+msg1+"-"+not_id);
        Toast.makeText(getContext(),sender1+"-"+phno1+"-"+msg1+"-"+not_id,Toast.LENGTH_SHORT).show();

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),Integer.parseInt(not_id), intent, 0);
        if (c.before(Calendar.getInstance())) {
            c.add(Calendar.DATE, 1);
        }

        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }


}
