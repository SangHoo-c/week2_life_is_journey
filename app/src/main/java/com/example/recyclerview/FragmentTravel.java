package com.example.recyclerview;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class FragmentTravel extends Fragment {
    View v;
    ImageView nextButton;


    CheckBox btn_low_money, btn_high_money;

    RelativeLayout relativeLayout_1;

    final String[] travel_continent = new String[]{"asia", "europe", "america", "oceania", "middle_east"};
    final String[] travel_type = new String[]{"food", "scenery", "activity", "rest", "extreme"};
    final String[] travel_money = new String[]{"low_money", "high_money"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_fragment_travel, container, false);
        btn_low_money = v.findViewById(R.id.low_money);
        btn_high_money = v.findViewById(R.id.high_money);
        btn_low_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_low_money.isChecked()) btn_high_money.setChecked(false);
            }
        });
        btn_high_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btn_high_money.isChecked()) btn_low_money.setChecked(false);
            }
        });
        nextButton = (ImageView) v.findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox;
                int resID;
                ArrayList<String> travel_continent_selected = new ArrayList<>();
                ArrayList<String> travel_type_selected = new ArrayList<>();
                ArrayList<String> travel_money_selected = new ArrayList<>();
                for (String continent : travel_continent) {
                    resID = getResources().getIdentifier(continent, "id", getActivity().getPackageName());
                    Log.d("12394071094", Integer.toString(resID));
                    checkBox = (CheckBox) getActivity().findViewById(resID);
                    Log.d("12381019", checkBox.toString());
                    if (checkBox.isChecked()) travel_continent_selected.add(continent);
                }
                for (String type : travel_type) {
                    resID = getResources().getIdentifier(type, "id", getActivity().getPackageName());
                    checkBox = (CheckBox) getActivity().findViewById(resID);
                    if (checkBox.isChecked()) travel_type_selected.add(Integer.toString(find(travel_type, type) + 1));
                }
                for (String money : travel_money) {
                    resID = getResources().getIdentifier(money, "id", getActivity().getPackageName());
                    checkBox = (CheckBox) getActivity().findViewById(resID);
                    if (checkBox.isChecked()) {
                        if (money.equals("low_money")) travel_money_selected.add("0");
                        else travel_money_selected.add("1");
                    }
                }
                if (travel_continent_selected.size() == 0 || travel_type_selected.size() == 0 || travel_money_selected.size() == 0) {
                    Toast.makeText(getActivity(), "항목별로 하나 이상을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle args = new Bundle();
                args.putStringArrayList("travel_continent_selected", travel_continent_selected);
                args.putStringArrayList("travel_type_selected", travel_type_selected);
                args.putStringArrayList("travel_money_selected", travel_money_selected);

                FragmentTravel2 fragmentTravel2 = new FragmentTravel2();
                fragmentTravel2.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.fragment_travel, fragmentTravel2, null).addToBackStack(null).commit();
            }
        });
        return v;
    }

    public static int find(String[] a, String target) {
        for (int i = 0; i < a.length; i++) {
            if (target.equals(a[i])) return i;
        } return -1;
    }
}
