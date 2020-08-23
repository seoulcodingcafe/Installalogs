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

package io.github.installalogs.tools;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.pixplicity.easyprefs.library.Prefs;

import io.github.installalogs.ScanService;

public class Scheduler {
    private Context mContext;

    public Scheduler(Context context) {
        mContext = context;
    }

    public void scheduleScanService() {
        if (!Prefs.getBoolean("force background rescans", false))
            return;
        if (Integer.parseInt(Prefs.getString("force background rescans minutes", "60")) == 0)
            return;
        Intent intent = new Intent(mContext, ScanService.class);
        PendingIntent pIntent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            pIntent = PendingIntent.getForegroundService(mContext, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        else
            pIntent = PendingIntent.getService(mContext, 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager aManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        aManager.cancel(pIntent);
        long time = System.currentTimeMillis()
                + 1000 * 60 * Integer.parseInt(Prefs.getString("force background rescans minutes", "60"));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            aManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, time, pIntent);
        else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            aManager.setExact(AlarmManager.RTC_WAKEUP, time, pIntent);
        else
            aManager.set(AlarmManager.RTC_WAKEUP, time, pIntent);
    }
}
