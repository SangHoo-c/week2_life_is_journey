package com.example.recyclerview;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.io.IOException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


public class FragmentContactsAdd extends DialogFragment {
    private View v;
    ImageView profile_button;
    TextView edit_name;
    TextView edit_phone_number;
    Button cancel_button;
    Button ok_button;

    String imgPath = "";

    static String accountName;
    String previous_name;
    String previous_phone_number;
    int previous_photo_id;
    String new_name;
    String new_phone_number;
    int new_photo_id = 0;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IRetrofit iRetrofit;

    public FragmentContactsAdd(String accountName, @Nullable String previous_name, @Nullable String previous_phone_number, @Nullable int previous_photo_id) {
        this.accountName = accountName;
        this.previous_name = previous_name;
        this.previous_phone_number = previous_phone_number;
        this.previous_photo_id = previous_photo_id;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.activity_custome_dialogue, container, false);

        Retrofit accountClient = AccountClient.getInstance();
        iRetrofit = accountClient.create(IRetrofit.class);

        if (previous_name != null) {
            edit_name = v.findViewById(R.id.contact_name);
            edit_name.setText(previous_name);
        }

        if (previous_phone_number != null) {
            edit_phone_number = v.findViewById(R.id.contact_phone_number);
            edit_phone_number.setText(previous_phone_number.replace("-", ""));
        }

        profile_button = v.findViewById(R.id.modify_image_button);
        Log.d("previous_photo_id", Integer.toString(previous_photo_id));
        if (previous_photo_id != 2131165381 && previous_photo_id != 0) {
            profile_button.setImageBitmap(queryContactImage(previous_photo_id));
        } else profile_button.setImageResource(R.drawable.true_profile);
        profile_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                getActivity().startActivityForResult(intent, 2);
            }
        });

        cancel_button = v.findViewById(R.id.btn_cancel);
        cancel_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ok_button = v.findViewById(R.id.btn_ok);
        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = "";
                new_name = ((TextView) v.findViewById(R.id.contact_name)).getText().toString();
                Log.d("321321321", new_name);
                new_phone_number = ((TextView) v.findViewById(R.id.contact_phone_number)).getText().toString();
                String[] projection = new String[]{ContactsContract.CommonDataKinds.Phone.PHOTO_ID};
                Cursor cursor = getContext().getContentResolver().query(getUriFromPath(imgPath), projection, null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                cursor.moveToFirst();
                new_photo_id = cursor.getInt(0);
                Log.d("Here!", imgPath);
                Log.d("Here2!", accountName);
                Log.d("Here3! : photo_id", Integer.toString(new_photo_id));
                Log.d("Here4!", new_phone_number);
//                Log.d("Here4!", previous_phone_number);
                if (previous_phone_number == null) { // 새로 생성하는 경우
                    Log.d("123-81-20178-", Integer.toString(new_photo_id));
                    result = createContact(new_name, new_phone_number, new_photo_id);
                } else {
                    result = updateContact(new_name, new_phone_number, new_photo_id);
                }
                if (result.equals("success")) {
                    Toast.makeText(getContext(), "Contact created successfully", Toast.LENGTH_SHORT).show();
                    dismiss();
                }
            }
        });
        return v;
    }

    public String createContact(String name, String phone_number, int photo_id) {
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return "fail";
        } else if (phone_number.isEmpty()) {
            Toast.makeText(getContext(), "Phone number cannot be empty.", Toast.LENGTH_SHORT).show();
            return "fail";
        }
        compositeDisposable.add(iRetrofit.putContact(accountName, name, phone_number, photo_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.d("!!!!!!!!!!!!", response);
                    }
                }));
        return "success";
    }

    public String updateContact(String name, String phone_number, int photo_id) {
        if (name.isEmpty()) {
            Toast.makeText(getContext(), "Name cannot be empty.", Toast.LENGTH_SHORT).show();
            return "fail";
        } else if (phone_number.isEmpty()) {
            Toast.makeText(getContext(), "Phone number cannot be empty.", Toast.LENGTH_SHORT).show();
            return "fail";
        }
        compositeDisposable.add(iRetrofit.updateContact(accountName, previous_name, previous_phone_number, previous_photo_id, name, phone_number, photo_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.d("!!!!!!!!!!!!", response);
                    }
                }));
        return "success";
    }

    public void putArguments(Bundle args) {
        imgPath = args.getString("imgPath");
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
            profile_button.setImageBitmap(rotateBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Uri getUriFromPath(String path) {
        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor cursor = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + path + "'", null, null);
        cursor.moveToNext();
        int id = cursor.getInt(cursor.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }

    // Photo id -> Bitmap
    private Bitmap queryContactImage(int imageDataRow) {
        Cursor c = getContext().getContentResolver().query(ContactsContract.Data.CONTENT_URI, new String[]{
                ContactsContract.CommonDataKinds.Photo.PHOTO
        }, ContactsContract.Data._ID + "=?", new String[]{
                Integer.toString(imageDataRow)
        }, null);
        byte[] imageBytes = null;
        if (c != null) {
            if (c.moveToFirst()) {
                imageBytes = c.getBlob(0);
            }
            c.close();
        }

        if (imageBytes != null) {
            return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        } else {
            return null;
        }
    }
}