package com.example.recyclerview.gallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.recyclerview.R;

public class Fragment_Gallery extends Fragment {

    private Context mContext;

    //여기선 각 나라당 하나씩만 가져오면 됨
    //grid view 에 각 나라의 사진을 한개씩 보여주려함
    //0 --> snatorini
    //1 --> rome
    int image_travel_arr[] = {
            R.drawable.icon_seoul,
            R.drawable.icon_osaka,
            R.drawable.icon_sapporo,
            R.drawable.icon_beijing,
            R.drawable.icon_bangkok,
            R.drawable.icon_danag,

            R.drawable.icon_taipei,
            R.drawable.icon_gaum,
            R.drawable.icon_sydney,
            R.drawable.icon_alaska,
            R.drawable.icon_congo,
            R.drawable.icon_madagascar,

            R.drawable.icon_egypt,
            R.drawable.icon_israel,
            R.drawable.icon_dubai,
            R.drawable.icon_iraq,
            R.drawable.icon_barcelona,
            R.drawable.icon_santorini,

            R.drawable.icon_paris,
            R.drawable.icon_rome,
            R.drawable.icon_napoli,
            R.drawable.icon_england,
            R.drawable.icon_nederland,
            R.drawable.icon_iceland,

            R.drawable.icon_peru,
            R.drawable.icon_brazil,
            R.drawable.icon_cuba,
            R.drawable.icon_canada,
            R.drawable.icon_losangeles,
            R.drawable.icon_lasvagas

    };

    Len len = new Len(0, 0);

    private Len getScreenSize() {
        Point point = new Point();
        getActivity().getWindowManager().getDefaultDisplay().getRealSize(point);

        len.height = point.x / 3;
        len.width = point.x / 3;
//        len.width = point.y / 3;
        return len;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_images, null);
        GridView gv = (GridView) rootView.findViewById(R.id.gridView_image);
        final Image_Adapter iA = new Image_Adapter(getActivity());
        gv.setAdapter(iA);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                iA.callImageViewer(position);
            }
        });

        return rootView;
    }


    class Image_Adapter extends BaseAdapter {

        Image_Adapter(Context context) {
            mContext = context;
        }

        public final void callImageViewer(int selectedIndex) {
            Intent i = new Intent(mContext, image_inlarge.class);
            //String imgPath = getImageInfo(imgData, geoData, thumbsIDList.get(selectedIndex));
            i.putExtra("arr_position", selectedIndex);
            Log.v("FileName : ", String.valueOf(selectedIndex));
            startActivityForResult(i, 1);
        }

        @Override
        public int getCount() {
            Log.v("FileName : ", String.valueOf(image_travel_arr.length));
            return image_travel_arr.length;
        }

        @Override
        public Object getItem(int position) {
            return image_travel_arr[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView _imageView;
            if (convertView == null) {
                _imageView = new ImageView(mContext);
                _imageView.setLayoutParams(new GridView.LayoutParams(getScreenSize().height, getScreenSize().width));
                _imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                _imageView.setPadding(4, 4, 4, 4);
            } else {
                _imageView = (ImageView) convertView;
            }
            Log.v("accept..!", String.valueOf(position));
//            ImageView iv = (ImageView)convertView.findViewById(R.id.image_large);
            _imageView.setImageResource(image_travel_arr[position]);

            return _imageView;
        }
    }


    public class Len {
        int height;
        int width;

        public Len(int height, int width) {
            this.height = height;
            this.width = width;
        }
    }

}
