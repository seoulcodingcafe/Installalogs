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

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import io.github.installalogs.db.Log;

public class AllScan {
    Context mContext;

    public AllScan(Context context) {
        mContext = context;
    }

    public int run() throws IOException, NoSuchAlgorithmException {
        runDeleted();
        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        int trues = 0;
        for (PackageInfo packageInfo : packageInfos) {
            if (new HandlePackage(mContext).run(packageInfo) == true)
                trues++;
        }
        return trues;
    }

    public void runDeleted() {
        List<Log> logs = Log.allPlain(mContext);
        for (Log log : logs) {
            try {
                mContext.getPackageManager().getApplicationIcon(log.packageName);
            } catch (PackageManager.NameNotFoundException n) {
                log.deleted = true;
                log.update(mContext);
            }
        }
    }

}
