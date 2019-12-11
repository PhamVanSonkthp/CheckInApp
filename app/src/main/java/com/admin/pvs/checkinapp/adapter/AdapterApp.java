package com.admin.pvs.checkinapp.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.admin.pvs.checkinapp.R;
import com.admin.pvs.checkinapp.model.ContactApp;
import com.bumptech.glide.Glide;

import java.util.List;

public class AdapterApp extends ArrayAdapter<ContactApp> {

    private Activity context;
    private int resource;
    private List<ContactApp> objects;

    public AdapterApp(@NonNull Activity context, int resource, @NonNull List<ContactApp> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.objects = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.context.getLayoutInflater();
        @SuppressLint("ViewHolder") View row = inflater.inflate(this.resource , null);

        ImageView imgIcon = row.findViewById(R.id.item_img_icon);
        TextView txtAppName = row.findViewById(R.id.item_txt_app_name);
        TextView txtSupport = row.findViewById(R.id.item_txt_support);
        TextView txtSizeApp = row.findViewById(R.id.item_txt_size);
        TextView txtVerName = row.findViewById(R.id.item_txt_ver_name);
        TextView txtPermission = row.findViewById(R.id.item_txt_permission);
        TextView txtMinSDK = row.findViewById(R.id.item_txt_minSDK);

        ContactApp contactApp =this.objects.get(position);

        Glide.with(context).load(contactApp.getIcon()).into(imgIcon);
        txtAppName.setText(contactApp.getAppname());
        txtSizeApp.setText(contactApp.getSizeApp());
        txtVerName.setText("Tên phiên bản : " +contactApp.getVersionName());
        txtMinSDK.setText("Ver Android >= : " + contactApp.getMinSDK());

        String permission = "Ứng dụng yêu cầu : ";
        if(contactApp.getPermisions().contains("android.permission.CAMERA")){
            permission += "Camera , ";
        }
        if(contactApp.getPermisions().contains("android.permission.RECORD_AUDIO")){
            permission += "Micro , ";
        }
        if(contactApp.getPermisions().contains("android.permission.READ_EXTERNAL_STORAGE")){
            permission += "Truy cập bộ nhớ , ";
        }
        if(contactApp.getPermisions().contains("android.permission.ACCESS_WIFI_STATE")){
            permission += "WIFI , ";
        }
        if(contactApp.getPermisions().contains("android.permission.INTERNET")){
            permission += "Mạng di động , ";
        }
        if(contactApp.getPermisions().contains("android.permission.BLUETOOTH")){
            permission += "BLUETOOTH , ";
        }
        if(contactApp.getPermisions().contains("android.permission.BIND_NFC_SERVICE")){
            permission += "NFC , ";
        }
        if(contactApp.getPermisions().contains("android.permission.ACCESS_FINE_LOCATION")){
            permission += "GPS , ";
        }

        txtPermission.setText(permission);

        if(Float.parseFloat( contactApp.getSupport()) <= 30){
            txtSupport.setTextColor(Color.RED);
        }else if(Float.parseFloat( contactApp.getSupport()) <= 50){
            //txtSupport.setTextColor(Color.YELLOW);
        }else{
            txtSupport.setTextColor(Color.GREEN);
        }

        if(contactApp.getSupport().length() > 5){
            txtSupport.setText("Khả năng tương thích + hỗ trợ : " + contactApp.getSupport().substring(0,5) +" %");
        }else{
            txtSupport.setText("Khả năng tương thích + hỗ trợ : " + contactApp.getSupport() +" %");
        }


        return row;
    }
}
