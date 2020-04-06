package com.android.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;

public class PermissionUtils {
    public  static int EXTERNAL_STORAGE_REQUEST_CODE = 111;

    public static String[] EX_STORAGE_PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};


    public static boolean hasPermission(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static void requestPermissions(Activity context, String[] arrayPermission, int REQUEST_CODE) {
        ActivityCompat.requestPermissions(
                context, arrayPermission,
                REQUEST_CODE
        );
    }

}
