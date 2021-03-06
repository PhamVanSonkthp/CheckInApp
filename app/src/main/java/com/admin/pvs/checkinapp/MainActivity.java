package com.admin.pvs.checkinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Debug;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.admin.pvs.checkinapp.adapter.AdapterApp;
import com.admin.pvs.checkinapp.model.ContactApp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    TextView txtVerOs , txtDevices , txtModel , txtProduct , txtHardware , txtRAM , txtROM , txtCores , txtCamera , txtGPS , txtNFC , txtWIFI;
    ProgressBar process;
    ListView lvApp;
    ArrayList<ContactApp> arrApp;
    AdapterApp adapterApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        anhXa();
        try {
            events();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final Handler handler = new Handler();
        Timer timer = new Timer(false);
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        getPackages();
                    }
                });
            }
        };
        timer.schedule(timerTask, 1000);
        test();
    }



    private void test() {
        PackageManager pm = getPackageManager();

        if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            txtCamera.setText("Có");
            txtCamera.setTextColor(Color.GREEN);
        }else {
            txtCamera.setText("Không");
            txtCamera.setTextColor(Color.RED);
        }

        if (pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS)) {
            txtGPS.setTextColor(Color.GREEN);
            txtGPS.setText("Có");
        }else {
            txtGPS.setText("Không");
            txtGPS.setTextColor(Color.RED);
        }

        if (pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            txtNFC.setTextColor(Color.GREEN);
            txtNFC.setText("Có");
        }else {
            txtNFC.setText("Không");
            txtNFC.setTextColor(Color.RED);
        }
        if (pm.hasSystemFeature(PackageManager.FEATURE_WIFI)) {
            txtWIFI.setTextColor(Color.GREEN);
            txtWIFI.setText("Có");
        }else {
            txtWIFI.setText("Không");
            txtWIFI.setTextColor(Color.RED);
        }
    }

    private ArrayList<PInfo> getPackages() {
        ArrayList<PInfo> apps = getInstalledApps(false);
        return apps;
    }

    private boolean isSystemPackage(PackageInfo packageInfo) {
        return ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    private ArrayList<PInfo> getInstalledApps(boolean getSysPackages) {
        ArrayList<PInfo> res = new ArrayList<PInfo>();
        List<PackageInfo> packs = getPackageManager().getInstalledPackages(0);
        for(int i=0;i<packs.size();i++) {
            PackageInfo p = packs.get(i);
            if ( isSystemPackage(p) || (!getSysPackages) && (p.versionName == null)) {
                continue ;
            }
            PInfo newInfo = new PInfo();

            newInfo.appname = p.applicationInfo.loadLabel(getPackageManager()).toString();
            newInfo.pname = p.packageName;
            newInfo.versionName = p.versionName;
            newInfo.versionCode = p.versionCode;
            newInfo.path = p.applicationInfo.sourceDir;
            newInfo.icon = p.applicationInfo.loadIcon(getPackageManager());

            String minSdk = "Unkown";
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                minSdk = p.applicationInfo.minSdkVersion+"";
            }

            File file = new File(newInfo.path);
            long sizeinByte = file.length();
            long sizeinMb = sizeinByte / (1024*1024);

            int targetSdkVersion= 0;
            PackageManager manager = getPackageManager();
            ApplicationInfo applicationInfo = null;
            try {
                applicationInfo = manager.getApplicationInfo(newInfo.appname, 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            if (applicationInfo != null) {
                targetSdkVersion= applicationInfo.targetSdkVersion;
            }

            float percent = 0f;

            if(Integer.parseInt(txtCores.getText().toString()) <= 1){
                percent += 7.2f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 2){
                percent += 10.3f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 3){
                percent += 13.5f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 4){
                percent += 18.3f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 5){
                percent += 21.7f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 6){
                percent += 24.5f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 7){
                percent += 29.3f;
            }else if(Integer.parseInt(txtCores.getText().toString()) <= 8){
                percent += 30.5f;
            }else{
                percent += 34.0f;
            }

            if(txtROM.getText().toString().contains("GB")){
                if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 1){
                    percent += 15.2f;
                }else if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 4){
                    percent += 21.1f;
                }else if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 8){
                    percent += 27.4f;
                }else if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 16){
                    percent += 31.1f;
                }else if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 32){
                    percent += 36.7f;
                }else if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 64){
                    percent += 42.0f;
                }else if(Float.parseFloat(txtROM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 128){
                    percent += 44.3f;
                }else{
                    percent += 48.3f;
                }
            }else{
                percent +=11.6f;
            }

            if(txtRAM.getText().toString().contains("GB")){
                if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 1){
                    percent += 15.2f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 2){
                    percent += 18.1f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 3){
                    percent += 23.4f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 4){
                    percent += 27.1f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 5){
                    percent += 31.7f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 6){
                    percent += 36.0f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 7){
                    percent += 42.3f;
                }else if(Float.parseFloat(txtRAM.getText().toString().replace(" GB" , "").replace("," , ".")) <= 8){
                    percent += 44.8f;
                }else{
                    percent += 45.2f;
                }
            }else{
                percent +=2.3f;
            }


            if(newInfo.versionCode <= 1){
                percent += 29.3f;
            }else if(newInfo.versionCode <= 2){
                percent +=26.2f;
            }else if(newInfo.versionCode <= 3){
                percent +=22.5f;
            }else if(newInfo.versionCode <= 4){
                percent +=19.0f;
            }else if(newInfo.versionCode <= 5){
                percent +=15.3f;
            }else if(newInfo.versionCode <= 6){
                percent +=12.2f;
            }else if(newInfo.versionCode <= 7){
                percent +=7.2f;
            }else{
                percent +=5.8f;
            }

            if(sizeinMb <= 10){
                percent -= 5.3f;
            }else if(sizeinMb <= 20){
                percent -= 8.8f;
            }else if(sizeinMb <= 30){
                percent -= 11.9f;
            }else if(sizeinMb <= 40){
                percent -= 14.4f;
            }else if(sizeinMb <= 50){
                percent -= 17.7f;
            }else if(sizeinMb <= 60){
                percent -= 21.4f;
            }else if(sizeinMb <= 70){
                percent -= 22.9f;
            }else if(sizeinMb <= 80){
                percent -= 26.3f;
            }else if(sizeinMb <= 90){
                percent -= 29.1f;
            }else{
                percent -= 31.0f;
            }

            final PackageManager pm = getPackageManager();
            StringBuffer permissions = new StringBuffer();

            try {
                PackageInfo packageInfo = pm.getPackageInfo(newInfo.pname, PackageManager.GET_PERMISSIONS);

                String[] requestedPermissions = packageInfo.requestedPermissions;
                if ( requestedPermissions != null ) {
                    for (String requestedPermission : requestedPermissions) {
                        permissions.append(requestedPermission).append("\n");
                    }
                    //Log.d(TAG, "Permissions: " + permissions);
                }
            }
            catch ( PackageManager.NameNotFoundException e ) {
                e.printStackTrace();
            }



            arrApp.add(new ContactApp(newInfo.appname , newInfo.pname  , newInfo.versionName , newInfo.versionCode , newInfo.path , newInfo.icon , "RAMMb" , "Apk cài đặt : "+sizeinMb+" Mb" , targetSdkVersion+"" , permissions.toString(),minSdk, percent+""));
            adapterApp.notifyDataSetChanged();
            process.setVisibility(View.INVISIBLE);
            res.add(newInfo);
        }
        return res;
    }
    private class PInfo {
        private String appname = "";
        private String pname = "";
        private String versionName = "";
        private int versionCode = 0;
        private String path = "";
        private Drawable icon;
    }

    private void events() throws IOException {
        txtVerOs.setText(Build.VERSION.RELEASE);
        txtDevices.setText(Build.VERSION.INCREMENTAL);
        txtProduct.setText(Build.BRAND);
        txtModel.setText(Build.MODEL);
        txtHardware.setText(Build.HARDWARE);

        txtCores.setText(getNumberOfCores()+"");
        txtRAM.setText(getTotalRAM());
        txtROM.setText(getTotalInternalMemorySize());
    }

    private void anhXa() {
        txtWIFI = findViewById(R.id.txtWIFI);
        txtNFC = findViewById(R.id.txtNFC);
        txtGPS = findViewById(R.id.txtGPS);
        txtCamera = findViewById(R.id.txtCamera);
        txtVerOs = findViewById(R.id.txtVerOs);
        txtDevices = findViewById(R.id.txtDevices);
        txtModel = findViewById(R.id.txtModel);
        txtProduct = findViewById(R.id.txtProduct);
        txtHardware = findViewById(R.id.txtHardware);
        txtRAM = findViewById(R.id.txtRAM);
        txtROM = findViewById(R.id.txtROM);
        txtCores = findViewById(R.id.txtCores);
        process = findViewById(R.id.proWait);
        lvApp = findViewById(R.id.lvApp);
        arrApp = new ArrayList<>();
        adapterApp = new AdapterApp(this , R.layout.item_lv_app , arrApp);
        lvApp.setAdapter(adapterApp);

    }
    private int getNumberOfCores() {
        if(Build.VERSION.SDK_INT >= 17) {
            return Runtime.getRuntime().availableProcessors();
        }
        else {
            // Use saurabh64's answer
            return getNumCoresOldPhones();
        }
    }

    private int getNumCoresOldPhones() {
        //Private Class to display only CPU devices in the directory listing
        class CpuFilter implements FileFilter {
            @Override
            public boolean accept(File pathname) {
                //Check if filename is "cpu", followed by a single digit number
                if(Pattern.matches("cpu[0-9]+", pathname.getName())) {
                    return true;
                }
                return false;
            }
        }

        try {
            //Get directory containing CPU info
            File dir = new File("/sys/devices/system/cpu/");
            //Filter to only list the devices we care about
            File[] files = dir.listFiles(new CpuFilter());
            //Return the number of cores (virtual CPU devices)
            return files.length;
        } catch(Exception e) {
            //Default to return 1 core
            return 1;
        }
    }

    public static String getTotalInternalMemorySize() {
//        File path = Environment.getDataDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = 0;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            blockSize = stat.getBlockSizeLong();
//        }
//        long totalBlocks = 0;
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            totalBlocks = stat.getBlockCountLong();
//        }

        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long bytesAvailable = (long)stat.getBlockSize() *(long)stat.getBlockCount();
        long megAvailable = bytesAvailable / 1048576;

        //return formatSize(megAvailable);
        return megAvailable / 1024 +" GB";
    }

    public static String getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        }
        long availableBlocks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            availableBlocks = stat.getAvailableBlocksLong();
        }
        return formatSize(availableBlocks * blockSize);
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = "KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = "MB";
                size /= 1024;
            }
        }

        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }

        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public String getTotalRAM() {

        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            try {
                reader = new RandomAccessFile("/proc/meminfo", "r");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert reader != null;
            load = reader.readLine();

            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
                // System.out.println("Ram : " + value);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            // totRam = totRam / 1024;

            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" KB");
            }



        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lastValue;
    }

}
