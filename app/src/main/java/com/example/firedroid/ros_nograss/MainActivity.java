package com.example.firedroid.ros_nograss;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.IntentCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    Button enableGrass;
    Button disableGrass;
    TextView labelForNotRooted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enableGrass = findViewById(R.id.enableGrass);
        disableGrass = findViewById(R.id.disableGrass);
        labelForNotRooted = findViewById(R.id.labelForNotRooted);

        checkFileExist();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkFileExist();
    }

    public void enableGrass(View v) {
        String stringCommand = "su && mount -o remount, rw /system && mv /system/lib/libGLESv3.sok /system/lib/libGLESv3.so";
        if (executeShellCommand(stringCommand)) {
            Toast.makeText(this, "Grass has been activated. Good Job. Play Fair.", Toast.LENGTH_SHORT).show();
            checkFileExist();
        }
    }

    public void disableGrass(View v) {
        String stringCommand = "su && mount -o remount, rw /system && mv /system/lib/libGLESv3.so /system/lib/libGLESv3.sok";
        if (executeShellCommand(stringCommand)) {
            Toast.makeText(this, "Grass has been removed. Enjoy!", Toast.LENGTH_SHORT).show();
            checkFileExist();
        }
    }

    public void runApp(View v){
        ComponentName componentName = getPackageManager().getLaunchIntentForPackage("com.netease.chiji").getComponent();
        Intent intent = IntentCompat.makeRestartActivityTask(componentName);
        ComponentName cn = intent.getComponent();
        Intent.makeRestartActivityTask(cn);
        startActivity(intent);
    }

    private void checkFileExist() {
//        // Check Device if rooted
//        String suCommand = "su";
        String filePath= "/system/lib/libGLESv3.so";
//
//        if (executeShellCommand(suCommand)) {
//            // Rooted
            enableGrass.setVisibility(View.VISIBLE);
            disableGrass.setVisibility(View.VISIBLE);
            labelForNotRooted.setVisibility(View.GONE);
            File file = new File(filePath);
            if (file.exists()) {
                // Grass Enabled
                enableGrass.setEnabled(false);
                disableGrass.setEnabled(true);
            } else {
                // Grass Disabled
                enableGrass.setEnabled(true);
                disableGrass.setEnabled(false);
            }
//        } else {
//            // Not Rooted
//            enableGrass.setVisibility(View.GONE);
//            disableGrass.setVisibility(View.GONE);
//            labelForNotRooted.setVisibility(View.VISIBLE);
//            labelForNotRooted.setText("");
//            labelForNotRooted.setText(getString(R.string.notRootedMessage, getDeviceName(), Build.VERSION.RELEASE));
//        }
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    private boolean executeShellCommand(String command) {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            process.waitFor();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            if (process != null) {
                try {
                    process.destroy();
                } catch (Exception e) {
                }
            }
        }
    }


}
