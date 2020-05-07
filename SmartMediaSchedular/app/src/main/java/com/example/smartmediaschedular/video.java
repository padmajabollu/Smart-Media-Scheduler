package com.example.smartmediaschedular;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class video extends Fragment {

    private static final int CAMERA_PERMISSION=100;
    Button video;
    TextInputLayout attachment;
    TextInputEditText attachment1;
    Uri uri;
    String media_title,media1;
    Long media_size;
    VideoView videoView;
    MediaController mediaController;
    String sender1,receiver1,subject1,date1,time1;
    TextInputLayout sender,receiver,subject,ddate,ttime;
    TextInputEditText date,time;
    Button send;
    Button schedule_date,schedule_time;
    AlertDialog.Builder builder;

    public video() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_video, container, false);

        attachment=view.findViewById(R.id.attach);
        attachment1=view.findViewById(R.id.attach1);

        sender=(TextInputLayout) view.findViewById(R.id.sender_name);
        receiver=(TextInputLayout) view.findViewById(R.id.receiver_email);
        subject=(TextInputLayout) view.findViewById(R.id.emailsub);
        ddate=(TextInputLayout)view.findViewById(R.id.date1);
        ttime=(TextInputLayout)view.findViewById(R.id.time1);
        date=(TextInputEditText) view.findViewById(R.id.date);
        time=(TextInputEditText) view.findViewById(R.id.time);
        builder=new AlertDialog.Builder(getContext());

        send=(Button) view.findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sender1=sender.getEditText().getText().toString().trim();
                receiver1=receiver.getEditText().getText().toString().trim();
                receiver1=receiver1.replaceAll("\\n","");
                subject1=subject.getEditText().getText().toString().trim();
                subject1=subject1.replaceAll("\\n","");
                date1=date.getText().toString().trim();
                time1=time.getText().toString().trim();


                if(validateSender()  && validateReceiver() && validateAttach() && validateSubject() && validatedate() && validatetime()) {
                    String toast = sender1 + "\n" + receiver1 + "\n" + subject1 + "\n" + date1 + "\n" + time1;

                    Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();


                    builder.setMessage("Do you want to Schedule ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    try {
                                        finalize();

                                        Call<List<status>> call=ApiClient.getInstance().getApi().insert_email(
                                                "insert_email",sender1,receiver1,media_title,subject1,date1,time1,0,media1
                                        );
                                        call.enqueue(new Callback<List<status>>() {
                                            @Override
                                            public void onResponse(Call<List<status>> call, Response<List<status>> response) {
                                                List<status> list=new ArrayList<>();
                                                list=response.body();
                                                Toast.makeText(getContext(),list.get(0).getStatus(),Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onFailure(Call<List<status>> call, Throwable t) {
                                                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();

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

                    AlertDialog alertDialog = builder.create();
                    alertDialog.setTitle("Confirmation of Scheduling");
                    alertDialog.show();

                }

            }
        });



        videoView=(VideoView) view.findViewById(R.id.show);
        video=(Button) view.findViewById(R.id.video);
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkPermission(Manifest.permission.CAMERA,CAMERA_PERMISSION);
                Intent intent=new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent,1);
            }
        });

        mediaController=new MediaController(getContext());

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
        return view;
    }


    private boolean validateAttach() {
        if (!attachment.getEditText().getText().toString().isEmpty())
        {
            if((media_size/1000000)<=5)
            {

                attachment.setError(null);
                return true;
            }
            else
            {
                attachment.setError("Size must be less or equal to 5MB");
                return false;
            }

        }
        else
        {
            attachment.setError("Select the Attachment");
            /*attachment.setFocusableInTouchMode(false);
            attachment.setFocusable(false);
            */
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
                int c=0;
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


    private boolean validateSubject() {
        if (!subject1.isEmpty())
        {
            int c=0;
            String s=subject1.toLowerCase();
            for (int i=0;i<subject1.length();i++)
            {
                if((int)s.charAt(i)>=97 && (int)s.charAt(i)<=122)
                {
                    c+=1;
                }
                else if ((int) s.charAt(i)==10)
                {
                    c+=1;
                }
                else if ((int) s.charAt(i)==32)
                {
                    c+=1;
                }


            }
            if (c==subject1.length() && subject1.length()>4) {
                subject.setError(null);
                return true;
            }
            else {
                subject.setError("Title or Message Invalid");
                return false;
            }
        }
        else
        {
            subject.setError("Field can't be Empty");
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
        if(requestCode==1 && resultCode==RESULT_OK)
        {
            uri=data.getData();
            attachment1.setText(data.toString());
            videoView.setVideoURI(uri);
            videoView.setMediaController(mediaController);
            mediaController.setAnchorView(videoView);
            videoView.start();

            Cursor cursor=getContext().getContentResolver().query(uri,null,null,null,null);
            int nameIndex=cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
            int sizeIndex=cursor.getColumnIndex(OpenableColumns.SIZE);
            cursor.moveToFirst();
            media_title=cursor.getString(nameIndex);
            String type=getContext().getContentResolver().getType(data.getData());
            String[] split_media=type.split("/");
            media_size=cursor.getLong(sizeIndex);
               // Toast.makeText(getContext(),split_media[0]+"\n"+uri.toString(),Toast.LENGTH_SHORT).show();
                try {

                    InputStream fileInputStream= getContext().getContentResolver().openInputStream(data.getData());
                    byte[] bytes=new byte[1024];
                    ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                    int len;
                    while ((len=fileInputStream.read(bytes))>-1)
                    {
                        byteArrayOutputStream.write(bytes,0,len);
                    }

                    byte[] bytes1=byteArrayOutputStream.toByteArray();

                    media1=Base64.encodeToString(bytes1, Base64.DEFAULT);
                    //Toast.makeText(getApplicationContext(),media1,Toast.LENGTH_SHORT).show();

                }
                catch (Exception e)
                {
                    //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

                }


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
            Toast.makeText(getContext(),"Permission Already Granted",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode==CAMERA_PERMISSION)
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
}
