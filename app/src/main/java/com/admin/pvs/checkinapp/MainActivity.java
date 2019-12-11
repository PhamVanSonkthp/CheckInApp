package com.admin.pvs.checkinapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
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

    TextView txtVerOs , txtDevices , txtModel , txtProduct , txtHardware , txtRAM , txtROM , txtCores;
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

        String  details =  "VERSION.RELEASE : "+Build.VERSION.RELEASE
                +"\nVERSION.INCREMENTAL : "+Build.VERSION.INCREMENTAL
                +"\nVERSION.SDK.NUMBER : "+Build.VERSION.SDK_INT
                +"\nBOARD : "+Build.BOARD
                +"\nBOOTLOADER : "+Build.BOOTLOADER
                +"\nBRAND : "+Build.BRAND
                +"\nCPU_ABI : "+Build.CPU_ABI
                +"\nCPU_ABI2 : "+Build.CPU_ABI2
                +"\nDISPLAY : "+Build.DISPLAY
                +"\nFINGERPRINT : "+Build.FINGERPRINT
                +"\nHARDWARE : "+Build.HARDWARE
                +"\nHOST : "+Build.HOST
                +"\nID : "+Build.ID
                +"\nMANUFACTURER : "+Build.MANUFACTURER
                +"\nMODEL : "+Build.MODEL
                +"\nPRODUCT : "+Build.PRODUCT
                +"\nSERIAL : "+Build.SERIAL
                +"\nTAGS : "+Build.TAGS
                +"\nTIME : "+Build.TIME
                +"\nTYPE : "+Build.TYPE
                +"\nUNKNOWN : "+Build.UNKNOWN
                +"\nUSER : "+Build.USER;
            //txtCPU.setText(pkgAppsList.toString());
//        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
//        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//        activityManager.getMemoryInfo(memoryInfo);
//
//        String TAG = "AAAA";
//
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
//
//        Map<Integer, String> pidMap = new TreeMap<Integer, String>();
//        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses)
//        {
//            pidMap.put(runningAppProcessInfo.pid, runningAppProcessInfo.processName);
//        }
//
//        Collection<Integer> keys = pidMap.keySet();
//
//        for(int key : keys)
//        {
//
//
//            int pids[] = new int[1];
//            pids[0] = key;
//            android.os.Debug.MemoryInfo[] memoryInfoArray = activityManager.getProcessMemoryInfo(pids);
//            for(android.os.Debug.MemoryInfo pidMemoryInfo: memoryInfoArray)
//            {
//                Log.i(TAG, String.format("** MEMINFO in pid %d [%s] **\n",pids[0],pidMap.get(pids[0])));
//                Log.i(TAG, " pidMemoryInfo.getTotalPrivateDirty(): " + pidMemoryInfo.getTotalPrivateDirty() + "\n");
//                Log.i(TAG, " pidMemoryInfo.getTotalPss(): " + pidMemoryInfo.getTotalPss() + "\n");
//                Log.i(TAG, " pidMemoryInfo.getTotalSharedDirty(): " + pidMemoryInfo.getTotalSharedDirty() + "\n");
//            }
//        }
//


        //getPackages();

//        Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
//        Debug.getMemoryInfo(memoryInfo);
//
//        String memMessage = String.format("App Memory: Pss=%.2f MB\nPrivate=%.2f MB\nShared=%.2f MB",
//                memoryInfo.getTotalPss() / 1024.0,
//                memoryInfo.getTotalPrivateDirty() / 1024.0,
//                memoryInfo.getTotalSharedDirty() / 1024.0);
//
//        Toast.makeText(this,memMessage,Toast.LENGTH_LONG).show();
//        Log.i("AAAA", memMessage);

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
        timer.schedule(timerTask, 1000); // 1000 = 1 second.

//        new CountDownTimer(30000, 2000) {
//
//            public void onTick(long millisUntilFinished) {
//                adapterApp.notifyDataSetChanged();
//                process.setVisibility(View.INVISIBLE);
//                //Toast.makeText(getApplicationContext() , "123" , Toast.LENGTH_SHORT).show();
//            }
//
//            public void onFinish() {
//                //mTextField.setText("done!");
//            }
//
//        }.start();


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

            arrApp.add(new ContactApp(newInfo.appname , newInfo.pname  , newInfo.versionName , newInfo.versionCode , newInfo.path , newInfo.icon , "RAMMb" , "Apk cài đặt : "+sizeinMb+" Mb" , targetSdkVersion+"" , percent+""));
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
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            blockSize = stat.getBlockSizeLong();
        }
        long totalBlocks = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            totalBlocks = stat.getBlockCountLong();
        }
        return formatSize(totalBlocks * blockSize);
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
