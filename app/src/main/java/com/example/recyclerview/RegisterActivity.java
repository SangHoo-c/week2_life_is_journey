package com.example.recyclerview;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RegisterActivity extends Activity {
    ImageView register_photo;
    TextView register_name, register_phone_number, register_email, register_password;
    Button btn_register;

    String imgPath = "";
    int photo_id = 0;

    final String[] regions = new String[]{"Seoul", "Osaka", "Sapporo", "Beijing", "Bangkok", "Danang", "Taipei", "Guam", "Sydney", "Alaska"
            , "Barcelona", "Santorini", "Paris", "Rome", "Napoli", "England", "Nederland", "Iceland", "Peru", "Brazil", "Cuba", "Canada"
            , "LosAngeles", "LasVagas", "Congo", "Madagascar", "Egypt", "Israel", "Dubai", "Iraq"};

    ArrayList<String> regions_visited = new ArrayList<>();

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IRetrofit iRetrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Retrofit accountClient = AccountClient.getInstance();
        iRetrofit = accountClient.create(IRetrofit.class);

        register_photo = (ImageView) findViewById(R.id.modify_image_button);
        register_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, 3);
            }
        });

        register_email = (TextView) findViewById(R.id.register_email);
        register_name = (TextView) findViewById(R.id.register_name);
        register_phone_number = (TextView) findViewById(R.id.register_phone_number);
        register_password = (TextView) findViewById(R.id.register_password);


        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckBox checkBox;
                int resID;
                for (String region : regions) {
                    resID = getResources().getIdentifier(region, "id", getPackageName());
                    checkBox = (CheckBox) findViewById(resID);
                    if (checkBox.isChecked()) regions_visited.add(region);
                }
                Log.d("photo_id", Integer.toString(photo_id));
                Log.d("name", register_name.getText().toString());
                Log.d("register_email", register_email.getText().toString());
                Log.d("register_password", register_password.getText().toString());
                Log.d("regions_visited", regions_visited.toArray(new String[regions_visited.size()])[0]);
                registerUser(photo_id,
                        register_name.getText().toString(),
                        register_phone_number.getText().toString().replace("-", ""),
                        register_email.getText().toString(),
                        register_password.getText().toString(),
                        regions_visited.toArray(new String[regions_visited.size()]));
            }
        });
    }

    private void registerUser(int photo_id, String name, String phone_number, String email, String password, String[] regions) {
        if (name.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        } else if (phone_number.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Phone number cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        } else if (email.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (password.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (regions.length == 0) {
            Toast.makeText(RegisterActivity.this, "Please select at least one region", Toast.LENGTH_SHORT).show();
        }
        compositeDisposable.add(iRetrofit.registerUser(photo_id, name, phone_number, email, password, regions)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Toast.makeText(RegisterActivity.this, "" + response, Toast.LENGTH_SHORT).show();
                    }
                }));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 3 && data != null) {
            File tempFile;
            Uri photoUri = data.getData();

            Log.d("original_uri", photoUri.toString());

            Cursor cursor = null;
            try {
                String[] proj = new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                        ContactsContract.Contacts.PHOTO_ID,
                        ContactsContract.Contacts._ID
                };
                String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
//                cursor = getContentResolver().query(photoUri, proj, null, null, null);
//                cursor.moveToFirst();
//                photo_id = cursor.getInt(2);
                setImage(getPathFromUri(photoUri));
            } finally {
                if (cursor != null) cursor.close();
            }
        }
    }

    public void setImage(String newImgPath) {
        imgPath = newImgPath;
        Log.d("last", imgPath);
        ExifInterface exif;
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap originalBm = BitmapFactory.decodeFile(imgPath, options);

        int height = (originalBm.getHeight() * 512 / originalBm.getWidth());
        Bitmap scale = Bitmap.createScaledBitmap(originalBm, 512, height, true);
        int rotate = 0;
        try {
            exif = new ExifInterface(imgPath);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
            switch (orientation) {
                case ExifInterface.ORIENTATION_NORMAL:
                    rotate = 0;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotate = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotate = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotate = 90;
                    break;
            }
            Matrix matrix = new Matrix();
            matrix.postRotate(rotate);
            Bitmap rotateBitmap = Bitmap.createBitmap(scale, 0, 0, scale.getWidth(), scale.getHeight(), matrix, true);
            register_photo.setImageBitmap(rotateBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Uri getUriFromPath(String path) {
        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor cursor = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + path + "'", null, null);
        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }

    public String getPathFromUri(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex("_data"));
        cursor.close();
        return path;
    }
}
