package com.example.recyclerview;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.recyclerview.gallery.Image_inlarge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FragmentTravel2<Butto> extends Fragment {
    View v;

    ArrayList<TravelRegion> travelRegions = new ArrayList<>();
    final String[] engNames = new String[]{"Seoul", "Osaka", "Sapporo", "Beijing", "Bangkok", "Danang", "Taipei", "Guam", "Sydney", "Alaska"
            , "Barcelona", "Santorini", "Paris", "Rome", "Napoli", "England", "Nederland", "Iceland", "Peru", "Brazil", "Cuba", "Canada"
            , "LosAngeles", "LasVagas", "Congo", "Madagascar", "Egypt", "Israel", "Dubai", "Iraq"};
    final String[] korNames = new String[]{"서울", "오사카", "삿포로", "베이징", "방콕", "다낭", "타이베이", "괌", "시드니", "알래스카"
            , "바르셀로나", "산토리니", "파리", "로마", "나폴리", "영국", "네덜란드", "아이슬란드", "페루", "브라질", "쿠바", "캐나다"
            , "로스앤젤레스", "라스베가스", "콩고 민주 공화국", "마다가스카르", "이집트", "이스라엘", "두바이", "이라크"};
    final String[] continents = new String[]{"Asia", "Asia", "Asia", "Asia", "Asia", "Asia", "Asia", "Asia", "오세아니아", "오세아니아"
            , "유럽", "유럽", "유럽", "유럽", "유럽", "유럽", "유럽", "유럽", "아메리카", "아메리카", "아메리카", "아메리카"
            , "아메리카", "아메리카", "중동", "중동", "중동", "중동", "중동", "중동"};
    final String[] types = new String[]{"2,3", "2,3", "1,2", "2,3", "1,4", "1,2", "3,4", "1,2", "2,4", "3"
            , "1,3", "1,3", "1,3", "1,3", "1,2", "3,4", "1", "1,3", "1,2,3,4", "1,2,3", "1,3", "1,3,4"
            , "1,3", "1,3", "1,3,4", "1,2,3", "1,2,3", "1,3,4", "2,3,4", "2,3", "2,3,4"};
    final String[] money = new String[]{"0", "0", "0", "0", "0", "0", "0", "0", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1", "1"
        , "1", "1", "1", "1", "1", "1", "1", "1", "1"};

    final String[] latitudes = new String[]{"126.9", "135.5", "141.35", "116.4", "100.53", "108.2", "121.56", "144.76", "151.2", "-149.58", "23.27"
            , "45.9", "29.96", "34.859", "55.22", "42.64", "2.16", "25.45", "2.35", "12.5", "14.25", "-2.48", "4.86", "-18.49", "-76.19", "-46.79", "-78.78", "-101.35", "-118.3", "-115.2"};

    final String[] longitudes = new String[]{"37.55", "34.68", "43.06", "39.93", "13.76", "16.05", "25.03", "13.45", "-33.87", "61.63", "-2.62", "-20.44", "27.65", "31.686", "25.131", "33.8635", "41.397", "36.39", "48.85", "41.9", "40.85", "54.6", "55.29", "64.95", "-11.07", "-15.4", "21.85", "57.31", "34.05", "36.16"};


    //GOTO image gallery button
    Button button_gallery;

    //선택받은 나라 코드
    Integer country_code2 = 1;

    public FragmentTravel2() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fragment_travel2, container, false);
//        ArrayList<String> travel_continent_selected = getArguments().getStringArrayList("travel_continent_selected");
//        ArrayList<String> travel_type_selected = getArguments().getStringArrayList("travel_type_selected");
//        ArrayList<String> travel_money_selected = getArguments().getStringArrayList("travel_money_selected");

//        TravelRegion travelRegion;
//        ArrayList<TravelRegion> travelRegionSelected = new ArrayList<>();
//        for (int i = 0; i < engNames.length; i++) {
//            travelRegion = new TravelRegion(engNames[i], korNames[i], continents[i], new ArrayList<String>(Arrays.asList(types[i].split(",")))
//                    , money[i], Long.parseLong(latitudes[i]), Long.parseLong(longitudes[i]));
//            travelRegions.add(travelRegion);
//        }
//        for (int i = 0; i < travelRegions.size(); i++) {
//            if (travel_continent_selected.contains(travelRegions.get(i).getContinent())) {
//                if (intersection(travel_type_selected, travelRegions.get(i).getType()).size() > 0) {
//                    if (Integer.parseInt(travel_money_selected.get(0)) >= Integer.parseInt(travelRegions.get(i).getMoney())) {
//                        Log.d("selected!", travelRegions.get(i).getKorName());
//                        travelRegionSelected.add(travelRegions.get(i));
//                    }
//                }
//            }
//        }

        button_gallery = (Button) v.findViewById(R.id.goto_gallery);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_gallery = new Intent(getActivity(), Image_inlarge.class);
                intent_gallery.putExtra("country_code2", String.valueOf(country_code2));
//                intent_gallery.putExtra("country_code2", country_code2);
                startActivity(intent_gallery);
            }
        });


        return v;
    }

    public ArrayList<String> intersection(ArrayList<String> list1, ArrayList<String> list2) {
        ArrayList<String> list = new ArrayList<>();
        for (String t: list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        } return list;
    }
}