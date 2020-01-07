package com.example.recyclerview;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    private Bundle bundle;
    private ArrayList<Uri> uris = new ArrayList<>();
    private ArrayList<Bitmap> bitmaps = new ArrayList<>();
    public FragmentContacts fragmentContacts;
    public FragmentGallery fragmentGallery;
    private ArrayList<String> tmpList;
    private ArrayList<ImageCard> imgCard = new ArrayList<>();
    private int requestCode;
    private int resultCode;
    private Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String accountName = intent.getStringExtra("Name");

        fragmentContacts = new FragmentContacts(accountName);
        fragmentGallery = new FragmentGallery();

        tabLayout = (TabLayout) findViewById(R.id.tablayout_id);
        viewPager = (ViewPager) findViewById(R.id.viewpager_id);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());


        // Add Fragment and set tab name
        adapter.AddFragment(fragmentContacts, "");
        adapter.AddFragment(fragmentGallery, "");
        adapter.AddFragment(new FragmentRestaurant(), "");
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        // Set tab image
        tabLayout.getTabAt(0).setIcon(R.drawable.address_book);
        tabLayout.getTabAt(1).setIcon(R.drawable.gallery);
        tabLayout.getTabAt(2).setIcon(R.drawable.restaurant_tab);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                checkView(pos);
                changeView(pos);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // do nothing
            }
        });

        // Remove Shadow From the action bar
        ActionBar actionBar = getSupportActionBar();
//        actionBar.setElevation(0);
    }

    private void changeView(int index) {
        switch (index) {
            case 0:
                viewPager.setCurrentItem(0);
                break;
            case 1:
                viewPager.setCurrentItem(1);
                checkView(index);
                break;
            case 2:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    private boolean flag = false; // 아직 선택되지 않은 초기상태이면

    private void checkView(int index) {
        if (index == 1) {
            if (flag == false) {
                showSelect(); // 이미지 선택하는 액티비티 띄우기
                flag = true; // 이미지 선택되어 출력됨
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSelect() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
            return;
        }
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setType("image/*");
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            super.onActivityResult(requestCode, resultCode, data);
            ClipData clipData = data.getClipData();
            if (clipData != null) { // 이미지가 여러장 선택되었을 경우
                for (int i = 0; i < clipData.getItemCount(); i++) {
                    Uri imageUri = clipData.getItemAt(i).getUri();
                    uris.add(imageUri);
                    try {
                        InputStream is = getContentResolver().openInputStream(imageUri);
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        bitmaps.add(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            } else { // 이미지가 한 장만 선택되었을 경우
                Uri imageUri = data.getData();
                uris.add(imageUri);
                try {
                    InputStream is = getContentResolver().openInputStream(imageUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(is);
                    bitmaps.add(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            Log.d("@@@@@TAG@@@@", "MainActivity1 called");
            bundle = new Bundle();
            tmpList = new ArrayList<String>();
            for (Uri uri : uris) {
                tmpList.add(uri.toString());
            }
            bundle.putStringArrayList("result_images", tmpList);
            fragmentGallery.setArguments(bundle);
            adapter.notifyDataSetChanged();
            tabLayout.getTabAt(0).setIcon(R.drawable.address_book);
            tabLayout.getTabAt(1).setIcon(R.drawable.gallery);
            tabLayout.getTabAt(2).setIcon(R.drawable.restaurant_tab);
        }

        if (requestCode == 2 && data != null) {
            File tempFile;
            Uri photoUri = data.getData();

            Cursor cursor = null;
            try {
                String[] proj = {MediaStore.Images.Media.DATA};
                cursor = getContentResolver().query(photoUri, proj, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                tempFile = new File(cursor.getString(column_index));
                Log.d("1290437810934723905", tempFile.getAbsolutePath());
                bundle = new Bundle();
                bundle.putString("imgPath", tempFile.getAbsolutePath());
                fragmentContacts.putArguments(bundle);
//                ImageView imageView = findViewById(R.id.modify_image_button);
//                imageView.setImageBitmap(originalBm);
            } finally {
                if (cursor != null) cursor.close();
            }
        }
    }
}

