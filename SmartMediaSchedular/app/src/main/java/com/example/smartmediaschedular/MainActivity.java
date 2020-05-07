package com.example.smartmediaschedular;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private AppBarConfiguration mAppBarConfiguration;
    DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.email, R.id.message, R.id.video,
                R.id.audio)
                .setDrawerLayout(drawer)
                .build();
        navigationView.setNavigationItemSelectedListener(this);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    public boolean onCreateOptionMenu(Menu menu)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id=item.getItemId();

        if(id==R.id.rateus)
        {
            Intent intent=new Intent(getApplicationContext(),rate_us.class);
            startActivity(intent);
        }
        else if(id==R.id.exit)
        {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
        else if(id==R.id.support)
        {
            Intent intent=new Intent(getApplicationContext(),support.class);
            startActivity(intent);
        }
        else if(id==R.id.share)
        {
            Toast.makeText(getApplicationContext(),"Share",Toast.LENGTH_SHORT).show();
            Intent share=new Intent(Intent.ACTION_SEND);
            String text = "https://play.google.com/store/apps/details?id=com.smartmediaschedular&hl=en";
            share.putExtra(Intent.EXTRA_TEXT, text);
            share.setType("text/plain");

            startActivity(Intent.createChooser(share,"Share with"));

        }
        return super.onOptionsItemSelected(item);

    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id=menuItem.getItemId();

        if (id==R.id.email)
        {
            Toast.makeText(getApplicationContext(),"Email",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.share)
        {
            Toast.makeText(getApplicationContext(),"Share",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.message)
        {
            Toast.makeText(getApplicationContext(),"Message",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.audio)
        {
            Toast.makeText(getApplicationContext(),"Audio",Toast.LENGTH_SHORT).show();
        }
        else if (id==R.id.video)
        {
            Toast.makeText(getApplicationContext(),"Video",Toast.LENGTH_SHORT).show();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
