package com.example.recyclerview.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.recyclerview.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class image_view_page_2 extends Fragment {
    Integer country_code;

    String imageUrl_2 = null;
    FrameLayout frameLayout;
    ImageView background;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private Boolean Night = true;
    private FloatingActionButton fab, fab1, fab2;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        //bundle 로 전달받은 country code 객체를 사용한다.
        Bundle bundle = getArguments();
        if(bundle != null){
            country_code = bundle.getInt("Country_code");
        }

        Log.d("country_code : ", String.valueOf(country_code));

        //retrofit 사용
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl(TodoInterface.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        TodoInterface todoInterface2 = retrofit2.create(TodoInterface.class);
        todoInterface2.getData(String.valueOf(country_code+1)).enqueue(new Callback<Todos>() {
            @Override
            public void onResponse(Call<Todos> call, Response<Todos> response) {
                if (response.isSuccessful()) {
                    Todos todos2 = response.body();
                    Log.d("12301283-00", todos2.toString());
                    if (todos2 != null) {
                        imageUrl_2 = todos2.getContent2();
                        Glide.with(frameLayout).load(imageUrl_2).into(background);
                        Log.d("data_photo() _2", imageUrl_2);
                        Log.d("data.content()", todos2.getContent2() + "");
                        Log.d("data.todoid()",String.valueOf( todos2.getId()));
                        Log.e("getData end", "======================================");
                    }
                }
                else Log.d("why...not", "hi///");

            }

            @Override
            public void onFailure(Call<Todos> call, Throwable t) {
                Log.d("why...not...not", "hi///");
                Log.d("hello", t.toString());
            }
        });


        frameLayout = (FrameLayout) inflater.inflate(R.layout.content_image_inlarge,container, false);
        background = (ImageView)frameLayout.findViewById(R.id.image_large);

        frameLayout = (FrameLayout) inflater.inflate(R.layout.content_image_inlarge,container, false);
        background = (ImageView)frameLayout.findViewById(R.id.image_large);

        fab_open = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_close);

        fab = frameLayout.findViewById(R.id.fab);
        fab1 = frameLayout.findViewById(R.id.fab1);
        fab2 = frameLayout.findViewById(R.id.fab2);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                anim();
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hi_1", Toast.LENGTH_SHORT).show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hi_2", Toast.LENGTH_SHORT).show();
            }
        });

        return frameLayout;
    }


    public void anim() {
        if (isFabOpen) {
            fab1.startAnimation(fab_close);
            fab2.startAnimation(fab_close);
            fab1.setClickable(false);
            fab2.setClickable(false);
            isFabOpen = false;
        } else {
            fab1.startAnimation(fab_open);
            fab2.startAnimation(fab_open);
            fab1.setClickable(true);
            fab2.setClickable(true);
            isFabOpen = true;
        }
    }

}
