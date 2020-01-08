package com.example.recyclerview;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class TravelDialogFragment extends DialogFragment {
    View v;
    ListView listView;
    ArrayAdapter<String> adapter;
    ArrayList<String> users = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.travel_dialog, null);
        getDialog().setTitle("이 여행지를 방문한 사용자들");

        listView = v.findViewById(R.id.listview);
//        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_lis)

        return v;
    }
}