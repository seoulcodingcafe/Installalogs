package io.github.installalogs.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import io.github.installalogs.ScanService;

public class BootCompleted extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ContextCompat.startForegroundService(context, new Intent(context, ScanService.class));
    }
}
