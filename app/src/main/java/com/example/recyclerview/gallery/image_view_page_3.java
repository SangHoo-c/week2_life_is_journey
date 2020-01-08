package com.example.recyclerview.gallery;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.recyclerview.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class image_view_page_3 extends Fragment {
    Integer country_code;
    String imageUrl_1 = null;
    String activity, food, sight, money, rest;
    FrameLayout frameLayout;
    ImageView background;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton fab, fab1, fab2;

    ImageView like_activity, like_food,like_sight,like_money,like_rest;
    TextView country, like_count;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    LottieAnimationView animationView;

    Todos todos;
    Fragment p2m = this;

    boolean isGpsEnabled, isNetWorkEnabled;

    int flag=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        frameLayout = (FrameLayout) inflater.inflate(R.layout.content_image_inlarge,container, false);

        if(flag == 0){
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(p2m).attach(p2m).commit();
            flag=1;
        }

        builder = new AlertDialog.Builder(getActivity());
        final View layout = inflater.inflate(R.layout.activity_custome_dialogue_2, (ViewGroup)frameLayout.findViewById(R.id.layout_root));
        like_activity = layout.findViewById(R.id.like_activity);
        like_food = layout.findViewById(R.id.like_food);
        like_sight = layout.findViewById(R.id.like_sight);
        like_money = layout.findViewById(R.id.like_money);
        like_rest = layout.findViewById(R.id.like_rest);
        country = layout.findViewById(R.id.text_country_name);
        like_count = frameLayout.findViewById(R.id.text_like_count);

        LocationManager locationManager = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
        isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        isNetWorkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0,0, locationListener);
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,0,locationListener);
//
//        String locationProvider = LocationManager.GPS_PROVIDER;
//        Location lastKnowLocation = locationManager.getLastKnownLocation(locationProvider);
//        if(lastKnowLocation != null){
//            double lng = lastKnowLocation.getLongitude();
//            double lat = lastKnowLocation.getLatitude();
//            Log.d("hi","lt" + lng + ", lng =" + "lat");
//        }


        //bundle 로 전달받은 country code 객체를 사용한다.
        Bundle bundle = getArguments();
        if(bundle != null){
            country_code = bundle.getInt("Country_code");
        }

        Log.d("country_code : ", String.valueOf(country_code));

        //retrofit 사용
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(TodoInterface.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final TodoInterface todoInterface = retrofit.create(TodoInterface.class);
        todoInterface.getData(String.valueOf(country_code+1)).enqueue(new Callback<Todos>() {
            @Override
            public void onResponse(Call<Todos> call, Response<Todos> response) {
                if (response.isSuccessful()) {
                    todos = response.body();
                    if (todos != null) {
                        Log.d("data.photo_()", todos.getContent2());
                        imageUrl_1 = todos.getContent2();
                        activity = todos.getActivity();
                        food = todos.getFood();
                        sight = todos.getSight();
                        money = todos.getMoney();
                        rest = todos.getRest();
                        country.setText(todos.getCountry());
                        like_count.setText(todos.getLike());

                        Glide.with(frameLayout).load(imageUrl_1).into(background);

                        //activity
                        if(activity.equals("0")){
                            like_activity.setBackgroundResource(R.drawable.icon_heart_grey48);
                        }else{
                            like_activity.setBackgroundResource(R.drawable.icons_heart_red_48);
                        }

                        //food
                        if(food.equals("0")){
                            like_food.setBackgroundResource(R.drawable.icon_heart_grey48);
                        }else{
                            like_food.setBackgroundResource(R.drawable.icons_heart_red_48);
                        }

                        //sight
                        if(sight.equals("0")){
                            like_sight.setBackgroundResource(R.drawable.icon_heart_grey48);
                        }else{
                            like_sight.setBackgroundResource(R.drawable.icons_heart_red_48);
                        }

                        //money
                        if(money.equals("1")){
                            like_money.setBackgroundResource(R.drawable.icon_heart_grey48);
                        }else{
                            like_money.setBackgroundResource(R.drawable.icons_heart_red_48);
                        }

                        //rest
                        if(rest.equals("0")){
                            like_rest.setBackgroundResource(R.drawable.icon_heart_grey48);
                        }else{
                            like_rest.setBackgroundResource(R.drawable.icons_heart_red_48);
                        }
                        Log.d("data_activity : ", activity);
                        Log.d("data_activity : ", food);
                        Log.d("data_activity : ", sight);
                        Log.d("data_activity : ", money);
                        Log.d("data_activity : ", rest);

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

        animationView = (LottieAnimationView) frameLayout.findViewById(R.id.like_anim_view);
        animationView.setAnimation("like.json");
        animationView.setVisibility(View.VISIBLE);
        animationView.setEnabled(true);
        animationView.loop(false);
        animationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animationView.setEnabled(false);
                todoInterface.getUpdate(String.valueOf(country_code+1)).enqueue(new Callback<Todos>() {
                    @Override
                    public void onResponse(Call<Todos> call, Response<Todos> response) {
                        if(response.isSuccessful()){
                            Log.d("like it!!!", String.valueOf(todos.getLike()));
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    ft.detach(p2m).attach(p2m).commit();
                                }
                            },500);
                            flag=0;
                        }
                        else{
                            Log.d("like.. it..fail..", "fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<Todos> call, Throwable t) {
                        Log.d("like.. it..fail2..", "fail2");
                    }
                });
                animationView.playAnimation();
            }
        });


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
                alertDialog.show();
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "hi_2", Toast.LENGTH_SHORT).show();
            }
        });


        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

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
