package io.github.installalogs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import io.github.installalogs.HandlePackageService;

public class ActionPackage extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String packageName = intent.getDataString();
        Intent intentService = new Intent(context, HandlePackageService.class);
        intentService.putExtra("packageName", packageName.replace("package:", ""));
        ContextCompat.startForegroundService(context, intentService);
    }
}
