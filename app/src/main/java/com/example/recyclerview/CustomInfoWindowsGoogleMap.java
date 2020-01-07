package com.example.recyclerview;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class CustomInfoWindowsGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;

    public CustomInfoWindowsGoogleMap(Context ctx) {
        this.context = ctx;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View view = ((Activity) context).getLayoutInflater().inflate(R.layout.custom_marker_info, null);
        TextView name_tv = view.findViewById(R.id.marker_title);
        TextView snippet_tv = view.findViewById(R.id.marker_snippet);
        name_tv.setText(marker.getTitle());
        snippet_tv.setText(marker.getSnippet());
        return view;
    }
}