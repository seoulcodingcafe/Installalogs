//    The GNU General Public License does not permit incorporating this program
//    into proprietary programs.
//
//    This program is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    This program is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with this program.  If not, see <https://www.gnu.org/licenses/>.

package io.github.installalogs;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import io.github.installalogs.tools.CPUWakeLock;
import io.github.installalogs.tools.HandlePackage;
import io.github.installalogs.ui.Notification;

public class HandlePackageService extends Service {

    private CPUWakeLock mCPUWakeLock;

    private boolean progress = false;
    private List<String> packageNames;

    public HandlePackageService() {
        packageNames = new ArrayList<>();
    }

    private void closeMyself() {
        mCPUWakeLock.releaseIfIsHeld();
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startForeground(3, new Notification(this).getScanProgress());
        mCPUWakeLock = new CPUWakeLock(this);
        mCPUWakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!intent.hasExtra("packageName")) {
            ContextCompat.startForegroundService(this, new Intent(this, ScanService.class));
            if (!progress)
                closeMyself();
            return super.onStartCommand(intent, flags, startId);
        }
        packageNames.add(intent.getStringExtra("packageName"));
        if (progress)
            return super.onStartCommand(intent, flags, startId);
        progress = true;
        while (packageNames.size() > 0) {
            String packageName = packageNames.get(packageNames.size() - 1);
            packageNames.remove(packageName);
            try {
                if (new HandlePackage(this).run(packageName))
                    new Notification(this).showNewInstallation(1);
            } catch (IOException | NoSuchAlgorithmException | PackageManager.NameNotFoundException e) {
                new Notification(this).showServiceError(e.getMessage());
                progress = false;
            }
        }
        progress = false;
        closeMyself();
        return super.onStartCommand(intent, flags, startId);
    }
}
