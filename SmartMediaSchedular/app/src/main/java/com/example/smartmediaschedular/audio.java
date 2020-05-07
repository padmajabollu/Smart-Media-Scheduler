package com.example.smartmediaschedular;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class audio extends Fragment {


    TextInputLayout attachment;
    TextInputEditText attachment1;
    String media_title,media1;
    Long media_size;
    Button buttonStart, buttonStop, buttonPlayLastRecordAudio, buttonStopPlayingRecording ;
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int RequestPermissionCode = 1;
    MediaPlayer mediaPlayer ;
    String sender1,receiver1,subject1,date1,time1,attach;
    TextInputLayout sender,receiver,subject,ddate,ttime;
    TextInputEditText date,time;
    Button send;
    Button schedule_date,schedule_time;
    AlertDialog.Builder builder;

    public audio() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_audio, container, false);


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
                attachment1.setText(AudioSavePathInDevice);


                if(validateSender() && validateReceiver() && validateAttach() && validateSubject() && validatedate() && validatetime()) {

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

        buttonStart = (Button) view.findViewById(R.id.button);
        buttonStop = (Button) view.findViewById(R.id.button2);
        buttonPlayLastRecordAudio = (Button) view.findViewById(R.id.button3);
        buttonStopPlayingRecording = (Button) view.findViewById(R.id.button4);

        buttonStop.setEnabled(false);
        buttonStop.setBackgroundColor(Color.parseColor("#ffffff"));
        buttonStop.setTextColor(Color.parseColor("#000000"));

        buttonPlayLastRecordAudio.setEnabled(false);
        buttonPlayLastRecordAudio.setBackgroundColor(Color.parseColor("#ffffff"));
        buttonPlayLastRecordAudio.setTextColor(Color.parseColor("#000000"));

        buttonStopPlayingRecording.setEnabled(false);
        buttonStopPlayingRecording.setBackgroundColor(Color.parseColor("#ffffff"));
        buttonStopPlayingRecording.setTextColor(Color.parseColor("#000000"));

        random = new Random();

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(checkPermission()) {

                    AudioSavePathInDevice =
                            Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                                    CreateRandomAudioFileName(5) + "AudioRecording.3gp";

                    MediaRecorderReady();

                    try {
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }


                    buttonStart.setEnabled(false);
                    buttonStart.setBackgroundColor(Color.parseColor("#ffffff"));
                    buttonStart.setTextColor(Color.parseColor("#000000"));

                    buttonStop.setEnabled(true);
                    buttonStop.setBackgroundColor(Color.parseColor("#4065DD"));
                    buttonStop.setTextColor(Color.parseColor("#ffffff"));

                    Toast.makeText(getContext(), "Recording started",
                            Toast.LENGTH_LONG).show();
                } else {
                    requestPermission();
                }

            }
        });

        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                buttonStop.setEnabled(false);
                buttonStop.setBackgroundColor(Color.parseColor("#ffffff"));
                buttonStop.setTextColor(Color.parseColor("#000000"));

                buttonPlayLastRecordAudio.setEnabled(true);
                buttonPlayLastRecordAudio.setBackgroundColor(Color.parseColor("#4065DD"));
                buttonPlayLastRecordAudio.setTextColor(Color.parseColor("#ffffff"));

                buttonStart.setEnabled(true);
                buttonStart.setBackgroundColor(Color.parseColor("#4065DD"));
                buttonStart.setTextColor(Color.parseColor("#ffffff"));

                buttonStopPlayingRecording.setEnabled(false);
                buttonStopPlayingRecording.setBackgroundColor(Color.parseColor("#ffffff"));
                buttonStopPlayingRecording.setTextColor(Color.parseColor("#000000"));


                Toast.makeText(getContext(), "Recording Completed",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonPlayLastRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) throws IllegalArgumentException,
                    SecurityException, IllegalStateException {

                buttonStop.setEnabled(false);
                buttonStop.setBackgroundColor(Color.parseColor("#ffffff"));
                buttonStop.setTextColor(Color.parseColor("#000000"));

                buttonStart.setEnabled(false);
                buttonStart.setBackgroundColor(Color.parseColor("#ffffff"));
                buttonStart.setTextColor(Color.parseColor("#000000"));
                buttonStopPlayingRecording.setEnabled(true);
                buttonStopPlayingRecording.setBackgroundColor(Color.parseColor("#4065DD"));
                buttonStopPlayingRecording.setTextColor(Color.parseColor("#ffffff"));
                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(AudioSavePathInDevice);
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(getContext(),AudioSavePathInDevice,Toast.LENGTH_SHORT).show();

                Toast.makeText(getContext(), "Recording Playing",
                        Toast.LENGTH_LONG).show();
            }
        });

        buttonStopPlayingRecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buttonStop.setEnabled(false);
                buttonStop.setBackgroundColor(Color.parseColor("#ffffff"));
                buttonStop.setTextColor(Color.parseColor("#000000"));
                buttonStart.setEnabled(true);
                buttonStart.setBackgroundColor(Color.parseColor("#4065DD"));
                buttonStart.setTextColor(Color.parseColor("#ffffff"));

                buttonStopPlayingRecording.setEnabled(false);
                buttonStopPlayingRecording.setBackgroundColor(Color.parseColor("#ffffff"));
                buttonStopPlayingRecording.setTextColor(Color.parseColor("#000000"));
                buttonPlayLastRecordAudio.setEnabled(true);
                buttonPlayLastRecordAudio.setBackgroundColor(Color.parseColor("#4065DD"));
                buttonPlayLastRecordAudio.setTextColor(Color.parseColor("#ffffff"));

                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    MediaRecorderReady();
                }
            }
        });

        return view;
    }


    public void MediaRecorderReady(){
        mediaRecorder=new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    public String CreateRandomAudioFileName(int string){
        StringBuilder stringBuilder = new StringBuilder( string );
        int i = 0 ;
        while(i < string ) {
            stringBuilder.append(RandomAudioFileName.
                    charAt(random.nextInt(RandomAudioFileName.length())));

            i++ ;
        }
        return stringBuilder.toString();
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(getActivity(), new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, RequestPermissionCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length> 0) {
                    boolean StoragePermission = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;
                    boolean RecordPermission = grantResults[1] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (StoragePermission && RecordPermission) {
                        Toast.makeText(getContext(), "Permission Granted",
                                Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }


    private boolean validateAttach() {
        if (!attachment.getEditText().getText().toString().isEmpty())
        {
            File file=new File(AudioSavePathInDevice);
            media_title=file.getName();
            media_size=file.length();
            Uri uri=Uri.fromFile(file);
            try {

                InputStream fileInputStream= new FileInputStream(file);
                byte[] bytes=new byte[1024];
                ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
                int len;
                while ((len=fileInputStream.read(bytes))>-1)
                {
                    byteArrayOutputStream.write(bytes,0,len);
                }

                byte[] bytes1=byteArrayOutputStream.toByteArray();

                media1= Base64.encodeToString(bytes1, Base64.DEFAULT);
            //    Toast.makeText(getContext(),media1,Toast.LENGTH_SHORT).show();

            }
            catch (Exception e)
            {
                //Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();

            }
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


    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(),
                RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED;
    }
}