package com.example.smartmediaschedular;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class home extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;

    public home() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        tabLayout=(TabLayout) view.findViewById(R.id.tablayout);

        viewPager=(ViewPager) view.findViewById(R.id.viewpager);

        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter viewPagerAdapter=new ViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.addFragment(new pending(),"Pending");
        viewPagerAdapter.addFragment(new completed(),"Completed");
        viewPager.setAdapter(viewPagerAdapter);
    }


}


