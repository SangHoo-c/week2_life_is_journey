package com.example.recyclerview.gallery;


import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.recyclerview.R;

import static java.lang.Integer.parseInt;


public class Image_inlarge extends AppCompatActivity {
    int MAX_PAGE = 3;
    Fragment cur_fragment = new Fragment();
    int country_code;
    int country_code_from_main;
    int country_code_from_tab3 = -1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_image_slide);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager_image);
        viewPager.setAdapter(new adapter(getSupportFragmentManager()));

        //무슨 나라인지 country_code 를 받을 수 있다.
        //0 --> santorin
        //1 --> rome..
        country_code_from_main = getIntent().getIntExtra("arr_position",-1);
        Log.d("get position value : ", String.valueOf(country_code_from_main));

        String tmp= getIntent().getStringExtra("country_code2");;
        if(tmp !=null){
            country_code_from_tab3 =  parseInt(tmp);
        }
//        country_code_from_tab3 = getIntent().getIntExtra("country_code2", -1);
        Log.d("get position value : ", String.valueOf(country_code_from_tab3));

        if(country_code_from_tab3 <0) country_code = country_code_from_main;
        else country_code = country_code_from_tab3;

    }

    //view pager adapter 설정
    public class adapter extends FragmentPagerAdapter{
        public adapter(FragmentManager fm){
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if(position<0 || MAX_PAGE<=position)
                return null;
            switch (position){
                case 0:
                    //country code 를 각 이미지 page 에 전달하는 과정
                    cur_fragment=new image_view_page_1();
                    Bundle bundle1 = new Bundle(1);
                    bundle1.putInt("Country_code", country_code);
                    cur_fragment.setArguments(bundle1);
                    break;
                case 1:
                    //country code 를 각 이미지 page 에 전달하는 과정
                    cur_fragment=new image_view_page_2();
                    Bundle bundle2 = new Bundle(1);
                    bundle2.putInt("Country_code", country_code);
                    cur_fragment.setArguments(bundle2);
                    break;
                case 2:
                    //country code 를 각 이미지 page 에 전달하는 과정
                    cur_fragment=new image_view_page_3();
                    Bundle bundle3 = new Bundle(1);
                    bundle3.putInt("Country_code", country_code);
                    cur_fragment.setArguments(bundle3);
                    break;
            }
            return cur_fragment;
        }

        @Override
        public int getCount() {
            return MAX_PAGE;
        }
    }
}
