package com.example.recyclerview;


import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.recyclerview.gallery.Image_inlarge;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
import retrofit2.Retrofit;

public class FragmentTravel2 extends Fragment implements OnMapReadyCallback, PlacesListener {
    View v;
    TextView title;
    ImageView galleryButton;
    ImageView refreshButton;
    ImageView contactsButton;
    private GoogleMap mMap;
    private MapView mapView = null;
    LatLng placeLocation;

    int index;
    ArrayList<TravelRegion> travelRegions = new ArrayList<>();
    ArrayList<TravelRegion> travelRegionSelected = new ArrayList<>();
    final String[] engNames = new String[]{"Seoul", "Osaka", "Sapporo", "Beijing", "Bangkok", "Danang", "Taipei", "Guam", "Sydney", "Alaska"
            , "Coongo", "Madagascar", "Egypt", "Israel", "Dubai" , "Iraq", "Barcelona", "Santorini", "Paris", "Rome", "Napoli", "England",
            "Nederland", "Iceland", "Peru", "Brazil", "Cuba", "Canada", "LosAngeles", "LasVagas"};
    final String[] korNames = new String[]{"서울", "오사카", "삿포로", "베이징", "방콕", "다낭", "타이베이", "괌", "시드니", "알래스카"
            , "콩고 민주 공화국", "마다가스카르", "이집트", "이스라엘", "두바이", "이라크", "바르셀로나", "산토리니", "파리", "로마", "나폴리", "영국"
            , "네덜란드", "아이슬란드", "페루", "브라질", "쿠바", "캐나다", "로스앤젤레스", "라스베가스"};
    final String[] continents = new String[]{"asia", "asia", "asia", "asia", "asia", "asia", "asia", "asia", "oceania", "oceania"
            , "middle_east", "middle_east", "middle_east", "middle_east", "middle_east", "middle_east", "europe", "europe", "europe", "europe", "europe", "europe"
            , "europe", "europe", "america", "america", "america", "america", "america", "america"};
    final String[] types = new String[]{"2,3", "2,3", "1,2", "2,3", "1,4", "1,2", "3,4", "1,2", "2,4", "3,5"
            , "1,3,5", "1,3,5", "1,3,5", "1,3,5", "1,2,5", "3,4,5", "1,5", "1,3", "1,2,3,4", "1,2,3", "1,3", "1,3,4"
            , "1,3", "1,3", "1,3,4", "1,2,3", "1,2,3", "1,3,4", "2,3,4", "2,3", "2,3,4"};
    final String[] money = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"
            , "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    final String[] latitudes = new String[]{"37.55", "34.68", "43.06", "39.93", "13.76", "16.05", "25.03", "13.45", "-33.87", "61.63", "-2.62", "-20.44", "27.65", "31.686", "25.131", "33.8635", "41.397", "36.39", "48.85", "41.9", "40.85", "54.6", "55.29", "64.95", "-11.07", "-15.4", "21.85", "57.31", "34.05", "36.16"};
    final String[] longitudes = new String[]{"126.9", "135.5", "141.35", "116.4", "100.53", "108.2", "121.56", "144.76", "151.2", "-149.58", "23.27"
            , "45.9", "29.96", "34.859", "55.22", "42.64", "2.16", "25.45", "2.35", "12.5", "14.25", "-2.48", "4.86", "-18.49", "-76.19", "-46.79", "-78.78", "-101.35", "-118.3", "-115.2"};

    private static MediaPlayer mediaPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fragment_travel2, container, false);

        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.travel);
        mediaPlayer.setLooping(false);
        mediaPlayer.start();

        ArrayList<String> travel_continent_selected = getArguments().getStringArrayList("travel_continent_selected");
        ArrayList<String> travel_type_selected = getArguments().getStringArrayList("travel_type_selected");
        ArrayList<String> travel_money_selected = getArguments().getStringArrayList("travel_money_selected");

        TravelRegion travelRegion;

        Log.d("travel_continent_selected", String.join(",", travel_continent_selected));
        Log.d("travel_type_selected", String.join(",", travel_type_selected));
        Log.d("travel_money_selected", String.join(",", travel_money_selected));

        for (int i = 0; i < engNames.length; i++) {
            travelRegion = new TravelRegion(engNames[i], korNames[i], continents[i], new ArrayList<String>(Arrays.asList(types[i].split(",")))
                    , money[i], Double.parseDouble(latitudes[i]), Double.parseDouble(longitudes[i]));
            travelRegions.add(travelRegion);
            Log.d("----2103-109", travelRegion.getKorName());
            Log.d("----2103-109", travelRegion.getLatitude().toString());
            Log.d("----2103-109", travelRegion.getLongitude().toString());
        }
        for (int i = 0; i < travelRegions.size(); i++) {
            if (travel_continent_selected.contains(travelRegions.get(i).getContinent())) {
                if (intersection(travel_type_selected, travelRegions.get(i).getType()).size() > 0) {
                    if (Integer.parseInt(travel_money_selected.get(0)) >= Integer.parseInt(travelRegions.get(i).getMoney())) {
//                        Log.d("selected!", travelRegions.get(i).getKorName());
                        travelRegionSelected.add(travelRegions.get(i));
                    }
                }
            }
        }
//        Log.d("19-2341-3401358-1305", Integer.toString(travelRegionSelected.size()));
        if (travelRegionSelected.size() == 0) {
            Toast.makeText(getContext(), "Please select again", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(FragmentTravel2.this).commit();
            fragmentManager.popBackStack();
        }
//        Log.d("19-2341-3401358-1305", Integer.toString(travelRegionSelected.size()));
        index = new Random().nextInt(travelRegionSelected.size());

        title = (TextView) v.findViewById(R.id.selected_region);
        title.setText(travelRegionSelected.get(index).getKorName());

        Log.d("----2103-109", travelRegionSelected.get(index).getKorName());
        Log.d("----2103-109", travelRegionSelected.get(index).getLatitude().toString());
        Log.d("----2103-109", travelRegionSelected.get(index).getLongitude().toString());

        mapView = (MapView) v.findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        galleryButton = (ImageView) v.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(getActivity(), Image_inlarge.class);
                intent_gallery.putExtra("country_code2", travelRegionSelected.get(index).getEngName());
//                intent_gallery.putExtra("country_code2", country_code2);
                startActivity(intent_gallery);
            }
        });
        refreshButton = (ImageView) v.findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle args = new Bundle();
                args.putStringArrayList("travel_continent_selected", travel_continent_selected);
                args.putStringArrayList("travel_type_selected", travel_type_selected);
                args.putStringArrayList("travel_money_selected", travel_money_selected);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentTravel2 newFragmentTravel2 = new FragmentTravel2();
                newFragmentTravel2.setArguments(args);
                fragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(), newFragmentTravel2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        contactsButton = (ImageView) v.findViewById(R.id.contactsButton);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CompositeDisposable compositeDisposable = new CompositeDisposable();
                IRetrofit iRetrofit;
                Retrofit accountClient = AccountClient.getInstance();
                iRetrofit = accountClient.create(IRetrofit.class);

                compositeDisposable.add(iRetrofit.getUserInfo(travelRegionSelected.get(index).getEngName())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<String>() {
                            @Override
                            public void accept(String response) throws Exception {
                                Log.d("!!!!!!!!!!!!", response);
                                JsonParser jsonParser = new JsonParser();
//                                String str = "[{'_id':'a','photo_uri':'a','name':'박후민','phone_number':'010-3264-6509','email':'a','password':'abc','regions_visited':'a'.'salt':'a'}]";
                                JsonArray jsonArray = (JsonArray) jsonParser.parse(response);
                                int index = -1;
                                if(jsonArray.size() >= 0) index = new Random().nextInt(jsonArray.size());
                                for (int i = 0; i < jsonArray.size(); i++) {
                                    JsonObject object = (JsonObject) jsonArray.get(i);
                                    String name = object.get("name").getAsString();
                                    String photo_uri = object.get("photo_uri").getAsString();
                                    String phone_number = object.get("phone_number").getAsString();
                                    String email = object.get("email").getAsString();
                                    if (index == i) {
                                        String phone_number_with_tel = "tel:" + phone_number;
                                        Uri number = Uri.parse(phone_number_with_tel);
                                        Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                                        v.getContext().startActivity(callIntent);
                                    }
                                }
                            }
                        }));
             }
        });
        return v;
    }

    public ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> list = new ArrayList<>();
        for (String t : list1) {
            if (list2.contains(t)) {
                list.add(t);
            }
        }
        return list;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.clear();
        Double latitude = travelRegionSelected.get(index).getLatitude();
        Double longitude = travelRegionSelected.get(index).getLongitude();
        Log.d("123=-1", Double.toString(latitude));
        Log.d("1294012709", Double.toString(longitude));
        placeLocation = new LatLng(latitude, longitude);
        MapsInitializer.initialize(getContext());
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(placeLocation);
        mMap.addMarker(markerOptions);

        CameraUpdate location = CameraUpdateFactory.newLatLngZoom(placeLocation, 5);
        mMap.animateCamera(location);

    }

    @Override
    public void onPlacesFailure(PlacesException e) {

    }

    @Override
    public void onPlacesStart() {

    }

    @Override
    public void onPlacesSuccess(List<Place> places) {

    }

    @Override
    public void onPlacesFinished() {

    }
}