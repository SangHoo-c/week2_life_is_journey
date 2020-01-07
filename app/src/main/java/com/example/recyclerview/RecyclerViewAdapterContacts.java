package com.example.recyclerview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

public class RecyclerViewAdapterContacts extends RecyclerView.Adapter<RecyclerViewAdapterContacts.MyViewHolder> implements Filterable {
    static Context mContext;
    static ArrayList<ContactItem> mData;
    static ArrayList<ContactItem> mDataShow;
    static String accountName;
    static FragmentManager fragmentManager;

    public RecyclerViewAdapterContacts(String accountName, Context context, ArrayList<ContactItem> list, FragmentManager fragmentManager) {
        this.mContext = context;
        this.mData = list;
        this.mDataShow = list;
        this.accountName = accountName;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.item_contact, parent, false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_name.setText(mDataShow.get(position).getName());
        holder.tv_phone.setText(mDataShow.get(position).getPhone_number());
        holder.img.setImageBitmap(mDataShow.get(position).getPhoto_bitmap());
        holder.call_button.setImageResource(mDataShow.get(position).getCall_photo_id());
    }

    @Override
    public int getItemCount() {
        if (mDataShow == null) return 0;
        else return mDataShow.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    mDataShow = mData;
                } else {
                    ArrayList<ContactItem> filteredContacts = new ArrayList<>();
                    for (ContactItem name : mData) {
                        String tmpName = name.getName();
                        if (hangulToJaso(tmpName).contains(hangulToJaso(charString))) {
                            filteredContacts.add(name);
                        }
                    }
                    mDataShow = filteredContacts;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mDataShow;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mDataShow = (ArrayList<ContactItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView img;
        private TextView tv_name;
        private TextView tv_phone;
        private ImageView call_button;

        public MyViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            tv_name = (TextView) itemView.findViewById(R.id.name);
            tv_phone = (TextView) itemView.findViewById(R.id.phone_number);
            call_button = (ImageView) itemView.findViewById(R.id.call_button);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("true_photo_id", Integer.toString(mDataShow.get(position).getPhoto_id()));
                    FragmentContactsAdd fragmentContactsAdd = new FragmentContactsAdd(accountName, mDataShow.get(position).getName(), mDataShow.get(position).getPhone_number(), mDataShow.get(position).getPhoto_id());
                    fragmentContactsAdd.show(fragmentManager, "Contacts add fragment");
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    String result = "";
                    AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                    builder.setTitle("연락처 삭제");
                    builder.setMessage("연락처를 삭제하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                     deleteContact(mDataShow.get(position).getName(), mDataShow.get(position).getPhone_number().replace("-", ""), mDataShow.get(position).getPhoto_id());
                                }
                            });
                    builder.setNegativeButton("아니요",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();
                    return true;
                }
            });
            call_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    String phone_number_with_tel = "tel:" + mDataShow.get(position).getPhone_number();
                    Uri number = Uri.parse(phone_number_with_tel);
                    Intent callIntent = new Intent(Intent.ACTION_CALL, number);
                    v.getContext().startActivity(callIntent);
                }
            });
        }
    }

    public static String hangulToJaso(String s) { // 유니코드 한글 문자열을 입력 받음

        final char[] ChoSung = {0x3131, 0x3132, 0x3134, 0x3137, 0x3138, 0x3139, 0x3141, 0x3142, 0x3143, 0x3145, 0x3146, 0x3147, 0x3148, 0x3149, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};
        final char[] JwungSung = {0x314f, 0x3150, 0x3151, 0x3152, 0x3153, 0x3154, 0x3155, 0x3156, 0x3157, 0x3158, 0x3159, 0x315a, 0x315b, 0x315c, 0x315d, 0x315e, 0x315f, 0x3160, 0x3161, 0x3162, 0x3163};
        final char[] JongSung = {0, 0x3131, 0x3132, 0x3133, 0x3134, 0x3135, 0x3136, 0x3137, 0x3139, 0x313a, 0x313b, 0x313c, 0x313d, 0x313e, 0x313f, 0x3140, 0x3141, 0x3142, 0x3144, 0x3145, 0x3146, 0x3147, 0x3148, 0x314a, 0x314b, 0x314c, 0x314d, 0x314e};

        int a, b, c;
        String result = "";

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch >= 0xAC00 && ch <= 0xD7A3) {
                c = ch - 0xAC00;
                a = c / (21 * 28);
                c = c % (21 * 28);
                b = c / 28;
                c = c % 28;
                result = result + ChoSung[a] + JwungSung[b];
                if (c != 0) result = result + JongSung[c];
            } else {
                result = result + ch;
            }
        }
        return result;
    }

    public static String deleteContact(String name, String phone_number, int photo_id) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        IRetrofit iRetrofit;
        Retrofit accountClient = AccountClient.getInstance();
        iRetrofit = accountClient.create(IRetrofit.class);
        compositeDisposable.add(iRetrofit.deleteContact(accountName, name, phone_number, photo_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String response) throws Exception {
                        Log.d("!!!!!!!!!!!!", response);
                        Toast.makeText(mContext, "Deleted", Toast.LENGTH_SHORT).show();
                    }
                }));
        return "success";
    }
}