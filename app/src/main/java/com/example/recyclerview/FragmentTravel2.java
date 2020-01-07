package com.example.recyclerview;


import android.content.Intent;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class FragmentTravel2 extends Fragment {
    Fragment pm;

    View v;
    TextView title;
    ImageView galleryButton;
    ImageView refreshButton;
    ImageView contactsButton;

    ArrayList<TravelRegion> travelRegions = new ArrayList<>();
    final String[] engNames = new String[]{"Seoul", "Osaka", "Sapporo", "Beijing", "Bangkok", "Danang", "Taipei", "Guam", "Sydney", "Alaska"
            , "Barcelona", "Santorini", "Paris", "Rome", "Napoli", "England", "Nederland", "Iceland", "Peru", "Brazil", "Cuba", "Canada"
            , "LosAngeles", "LasVagas", "Congo", "Madagascar", "Egypt", "Israel", "Dubai", "Iraq"};
    final String[] korNames = new String[]{"서울", "오사카", "삿포로", "베이징", "방콕", "다낭", "타이베이", "괌", "시드니", "알래스카"
            , "바르셀로나", "산토리니", "파리", "로마", "나폴리", "영국", "네덜란드", "아이슬란드", "페루", "브라질", "쿠바", "캐나다"
            , "로스앤젤레스", "라스베가스", "콩고 민주 공화국", "마다가스카르", "이집트", "이스라엘", "두바이", "이라크"};
    final String[] continents = new String[]{"asia", "asia", "asia", "asia", "asia", "asia", "asia", "asia", "oceania", "oceania"
            , "europe", "europe", "europe", "europe", "europe", "europe", "europe", "europe", "america", "america", "america", "america"
            , "america", "america", "middle_east", "middle_east", "middle_east", "middle_east", "middle_east", "middle_east"};
    final String[] types = new String[]{"2,3", "2,3", "1,2", "2,3", "1,4", "1,2", "3,4", "1,2", "2,4", "3,5"
            , "1,3,5", "1,3,5", "1,3,5", "1,3,5", "1,2,5", "3,4,5", "1,5", "1,3", "1,2,3,4", "1,2,3", "1,3", "1,3,4"
            , "1,3", "1,3", "1,3,4", "1,2,3", "1,2,3", "1,3,4", "2,3,4", "2,3", "2,3,4"};
    final String[] money = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"
            , "1", "1", "1", "1", "1", "1", "1", "1", "1"};
    final String[] latitudes = new String[]{"126.9", "135.5", "141.35", "116.4", "100.53", "108.2", "121.56", "144.76", "151.2", "-149.58", "23.27"
            , "45.9", "29.96", "34.859", "55.22", "42.64", "2.16", "25.45", "2.35", "12.5", "14.25", "-2.48", "4.86", "-18.49", "-76.19", "-46.79", "-78.78", "-101.35", "-118.3", "-115.2"};
    final String[] longitudes = new String[]{"37.55", "34.68", "43.06", "39.93", "13.76", "16.05", "25.03", "13.45", "-33.87", "61.63", "-2.62", "-20.44", "27.65", "31.686", "25.131", "33.8635", "41.397", "36.39", "48.85", "41.9", "40.85", "54.6", "55.29", "64.95", "-11.07", "-15.4", "21.85", "57.31", "34.05", "36.16"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fragment_travel2, container, false);

        ArrayList<String> travel_continent_selected = getArguments().getStringArrayList("travel_continent_selected");
        ArrayList<String> travel_type_selected = getArguments().getStringArrayList("travel_type_selected");
        ArrayList<String> travel_money_selected = getArguments().getStringArrayList("travel_money_selected");

        TravelRegion travelRegion;
        ArrayList<TravelRegion> travelRegionSelected = new ArrayList<>();

        Log.d("travel_continent_selected", String.join(",", travel_continent_selected));
        Log.d("travel_type_selected", String.join(",", travel_type_selected));
        Log.d("travel_money_selected", String.join(",", travel_money_selected));

        for (int i = 0; i < engNames.length; i++) {
            travelRegion = new TravelRegion(engNames[i], korNames[i], continents[i], new ArrayList<String>(Arrays.asList(types[i].split(",")))
                    , money[i], Double.parseDouble(latitudes[i]), Double.parseDouble(longitudes[i]));
            travelRegions.add(travelRegion);
//            Log.d("----2103-109", travelRegion.getKorName());
//            Log.d("----2103-109", travelRegion.getEngName());
//            Log.d("----2103-109", travelRegion.getContinent());
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
        Log.d("19-2341-3401358-1305", Integer.toString(travelRegionSelected.size()));
        if (travelRegionSelected.size() == 0) {
            Toast.makeText(getContext(), "Please select again", Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(FragmentTravel2.this).commit();
            fragmentManager.popBackStack();
        }
        Log.d("19-2341-3401358-1305", Integer.toString(travelRegionSelected.size()));
        int index = new Random().nextInt(travelRegionSelected.size());

        title = (TextView) v.findViewById(R.id.selected_region);
        title.setText(travelRegionSelected.get(index).getKorName());
        galleryButton = (ImageView) v.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(getActivity(), Image_inlarge.class);
                intent_gallery.putExtra("country_code2", String.valueOf(1));
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
                fragmentTransaction.commitAllowingStateLoss();
            }
        });
        Log.d("으악악", "으악악");

        contactsButton = (ImageView) v.findViewById(R.id.contactsButton);
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
}