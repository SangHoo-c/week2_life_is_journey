package com.example.recyclerview;

import android.content.Intent;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.recyclerview.gallery.Fragment_Gallery;
import com.google.android.material.tabs.TabLayout;



public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    public FragmentContacts fragmentContacts;
    public Fragment_Gallery fragmentGallery;
    public FragmentTravel fragmentTravel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String accountName = intent.getStringExtra("Name");

        fragmentContacts = new FragmentContacts(accountName);
        fragmentGallery = new Fragment_Gallery();
        fragmentTravel = new FragmentTravel();

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        // Add Fragment and set tab name
        adapter.AddFragment(fragmentContacts, "");
        adapter.AddFragment(fragmentGallery, "");
        adapter.AddFragment(fragmentTravel, "");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set tab image
        tabLayout.getTabAt(0).setIcon(R.drawable.address_book);
        tabLayout.getTabAt(1).setIcon(R.drawable.gallery);
        tabLayout.getTabAt(2).setIcon(R.drawable.travel_icon);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        // Remove Shadow From the action bar
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setElevation(0);
    }

    private void changeView(int index) {
        switch (index) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                break;
        }    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

