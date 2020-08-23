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
import android.os.IBinder;

import io.github.installalogs.tools.AllScan;
import io.github.installalogs.tools.CPUWakeLock;
import io.github.installalogs.ui.Notification;

public class CleanService extends Service {

    private CPUWakeLock mCPUWakeLock;

    public CleanService() {
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
        startForeground(2, new Notification(this).getCleanProgress());
        mCPUWakeLock = new CPUWakeLock(this);
        mCPUWakeLock.acquire();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(() -> {
            new AllScan(this).runDeleted();
            closeMyself();
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
}
