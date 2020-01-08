package com.example.recyclerview;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class FragmentContacts extends Fragment implements TextWatcher {
    View v;
    private RecyclerView myRecyclerView;
    private ArrayList<ContactItem> listContact;

    static String accountName;
    RecyclerViewAdapterContacts recyclerAdapter;

    // for searching
    EditText editText_search;
    ImageView imageView_cancel;
    ImageView imageView_add; // contact_add

    // for image passing
    FragmentContactsAdd fragmentContactsAdd;

    // for connection to server
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IRetrofit iRetrofit;


    // Constructor
    public FragmentContacts(String name) {
        this.accountName = name;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.contact_fragment, container, false);
        myRecyclerView = (RecyclerView) v.findViewById(R.id.my_recycler_view);


        recyclerAdapter = new RecyclerViewAdapterContacts(accountName, getContext(), listContact, getFragmentManager());
        myRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.drawable.divider)); // Add RecyclerView divider
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(recyclerAdapter);

        editText_search = v.findViewById(R.id.editText_search);
        editText_search.addTextChangedListener(this);

        imageView_cancel = v.findViewById(R.id.edittext_cancel_button);
        imageView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText_search.setText("");
            }
        });

//        final Bundle extra = this.getArguments();

//        fragmentContactsAdd = new FragmentContactsAdd(accountName, null, null, 0);

        imageView_add = v.findViewById(R.id.contact_add_button);
        imageView_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("before add tab", accountName);
                fragmentContactsAdd = new FragmentContactsAdd(accountName,null, null, 0); // 여기서 생성자로 전달하면 되겠다.
                fragmentContactsAdd.show(getFragmentManager(), "Contacts add fragment");
//                if (extra != null) fragmentContactsAdd.setArguments(extra);
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        doInBackground("http://192.249.19.250:7880/post");

        Retrofit accountClient = AccountClient.getInstance();
        iRetrofit = accountClient.create(IRetrofit.class);

        // getContactList 함수는 주소록에 접근하여 ArrayList<ContactItem> 형식의 데이터를 제공
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            listContact = new ArrayList<ContactItem>();
        } else listContact = getContactList();
//        listContact = getContactList();
//        listContact = new ArrayList<ContactItem>();
    }

    public ArrayList<ContactItem> getContactList() {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[]{
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.Contacts.PHOTO_ID,
                ContactsContract.Contacts._ID
        };
        Log.d("12340983215-138-", uri.toString());
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, selectionArgs, sortOrder);
        LinkedHashSet<ContactItem> hashlist = new LinkedHashSet<>();
        Bitmap defaultBitmap = getBitmap(R.drawable.true_profile);
        if (cursor.moveToFirst()) {
            do {
                int photo_id = cursor.getInt(2);
                int person_id = cursor.getInt(3);
                ContactItem contactItem = new ContactItem("", "", 0);

                // Set up the phone number
                String s = cursor.getString(0);
                if (s.length() == 11) {
                    contactItem.setPhone_number(s.substring(0, 3) + "-" + s.substring(3, 7) + "-" + s.substring(7, 11));
                } else contactItem.setPhone_number(s);

                contactItem.setName(cursor.getString(1));

                // Set up the photo
                if (photo_id == 0) {
                    contactItem.setPhoto_id(R.drawable.true_profile);
                    contactItem.setPhoto_bitmap(defaultBitmap);
                } else {
                    Bitmap image = queryContactImage(photo_id);
                    contactItem.setPhoto_id(photo_id);
                    contactItem.setPhoto_bitmap(image);
                }

                // Set up the person id
                contactItem.setPerson_id(person_id);
                contactItem.setCall_photo_id(R.drawable.call);
                if (contactItem.getPhone_number().startsWith("010")) {
                    hashlist.add(contactItem);
                    Log.d("<<CONTACT>>", "name=" + contactItem.getName() + ", phone=" + contactItem.getPhone_number());
                }
                // upload example
                String accountServer = "Changhun Kim";
                String nameServer = cursor.getString(1);
                String phone_numberServer = s;
                String photo_idServer = Integer.toString(photo_id);
//                compositeDisposable.add(iRetrofit.putContact(accountServer, nameServer, phone_numberServer, photo_idServer)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Consumer<String>() {
//                        @Override
//                        public void accept(String response) throws Exception {
//                            Log.d("*************!!", response);
//                        }
//                    }));
//                compositeDisposable.add(iRetrofit.getContact("Changhun Kim")
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(new Consumer<String>() {
//                            @Override
//                            public void accept(String response) throws Exception {
//                                Log.d("123234345", response);
//                            }
//                        }));
            } while (cursor.moveToNext());
        }
        compositeDisposable.add(iRetrofit.getContact("Changhun Kim")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.d("*************!!", response);
                    }
                }));
        ArrayList<ContactItem> contactItems = new ArrayList<>(hashlist);
        for (int i = 0; i < contactItems.size(); i++) {
            contactItems.get(i).setId(i);
        }
        if (cursor != null) cursor.close();
        System.out.println(contactItems.size());
        return contactItems;
    }

    // Drawable -> Bitmap
    private Bitmap getBitmap(int drawableRes) {
        Drawable drawable = getResources().getDrawable(drawableRes);
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
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

    public String doInBackground(String... urls) {
        final String urlString = urls[0];
        Thread thread = new Thread(new Runnable() {
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.accumulate("user_id", "androidTest");
                    jsonObject.accumulate("name", "yun");

                    HttpURLConnection con = null;
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(urlString);
                        Log.d("12309390275===", urlString);
                        con = (HttpURLConnection) url.openConnection();
                        Log.d("1=3=5=7=9", urlString);
                        con.setRequestMethod("POST");
                        con.setRequestProperty("Cache-Control", "No-cache");
                        con.setRequestProperty("Content-Type", "application/json");
                        con.setRequestProperty("Accept", "text/html");
                        con.setDoOutput(true);
                        con.setDoInput(true);
                        con.connect();

                        Log.d("12342839015-=", "Connection success");

                        OutputStream outStream = con.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outStream));
                        writer.write(jsonObject.toString());
                        writer.flush();
                        writer.close();

                        InputStream stream = con.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(stream));
                        StringBuffer buffer = new StringBuffer();

                        String line;
                        while ((line = reader.readLine()) != null) {
                            buffer.append(line);
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (con != null) {
                            con.disconnect();
                        }
                        try {
                            if (reader != null) {
                                reader.close();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void putArguments(Bundle args) {
        if (fragmentContactsAdd != null) fragmentContactsAdd.putArguments(args); // update에서 에러가 뜨는 이유는 이게 RecyclerViewAdapterContacts에서 초기화되고 여기서 초기화되지 않기 때문임.
//        else {
//            fragmentContactsAdd = new FragmentContactsAdd(accountName, null, null, 0);
//            fragmentContactsAdd.putArguments(args);
//        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        recyclerAdapter.getFilter().filter(s);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}