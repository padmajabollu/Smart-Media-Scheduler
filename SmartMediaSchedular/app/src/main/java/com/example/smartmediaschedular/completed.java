package com.example.smartmediaschedular;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class completed extends Fragment {

    public ArrayList<String[]> complete=new ArrayList<>();
    public ArrayList<String> list=new ArrayList<>();
    ListView listView;

    public completed() {

    }
//Toast.makeText(getContext(),"No Schedules Completed",Toast.LENGTH_SHORT).show();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_completed, container, false);
        getActivity().setTitle("Smart Media Scheduler");

        listView=view.findViewById(R.id.complete);

        Call<List<Pending_completed>> call=ApiClient.getInstance().getApi().getCompleted("getCompleted");
        call.enqueue(new Callback<List<Pending_completed>>() {
            @Override
            public void onResponse(Call<List<Pending_completed>> call, Response<List<Pending_completed>> response) {
                List<Pending_completed> completed=response.body();
                if(completed.size()>0)
                {
                    for (int i=0;i<completed.size();i++)
                    {
                        String[] com1=new String[6];
                        com1[0]=completed.get(i).getId();
                        com1[1]=completed.get(i).getSender();
                        com1[2]=completed.get(i).getReceiver();
                        com1[3]=completed.get(i).getDate();
                        com1[4]=completed.get(i).getTime();
                        com1[5]=completed.get(i).getChoice();
                        String s=com1[1]+"\t:\t"+com1[2]+"\n"+com1[3]+"\t|\t"+com1[4];
                        list.add(s);
                        complete.add(com1);

                    }
                    ArrayAdapter<String> arrayAdapter=new ArrayAdapter<String>(
                            getContext(),
                            android.R.layout.simple_list_item_1,
                            list
                    );
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (complete.get(position)[5].equalsIgnoreCase("message"))
                            {
                                Intent intent=new Intent(getContext(),message_delete.class);
                                intent.putExtra("id",complete.get(position)[0]);
                                startActivity(intent);
                                getActivity().finish();
                            }
                            else
                            {
                                Intent intent=new Intent(getContext(),email_delete.class);
                                intent.putExtra("id",complete.get(position)[0]);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }
                    });


                }
                else
                {
                    //Toast.makeText(getContext(),"No Schedules Completed",Toast.LENGTH_SHORT).show();

                }
            }

            @Override


            public void onFailure(Call<List<Pending_completed>> call, Throwable t) {
                Toast.makeText(getContext(),t.toString(),Toast.LENGTH_SHORT).show();

            }
        });


        return view;
    }

}
