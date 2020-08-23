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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.github.installalogs.db.Log;

public class HandlePackage {

    Context mContext;

    public HandlePackage(Context context) {
        mContext = context;
    }

    public boolean run(PackageInfo packageInfo) throws IOException, NoSuchAlgorithmException {
        PackageManager packageManager = mContext.getPackageManager();
        ApplicationInfo applicationInfo;
        applicationInfo = packageInfo.applicationInfo;
        String packageName = packageInfo.packageName;
        String versionName = packageInfo.versionName;
        long versionCode = packageInfo.versionCode;

        String label = packageManager.getApplicationLabel(applicationInfo).toString();
        String md5;
        String sha1;
        String sha256;
        String sha512;

        FileInputStream is = new FileInputStream(applicationInfo.publicSourceDir);
        MessageDigest mdMd5 = MessageDigest.getInstance("MD5");
        MessageDigest mdSha1 = MessageDigest.getInstance("SHA-1");
        MessageDigest mdSha256 = MessageDigest.getInstance("SHA-256");
        MessageDigest mdSha512 = MessageDigest.getInstance("SHA-512");
        byte[] bytes = new byte[1024];
        int sizeRead;
        do {
            sizeRead = is.read(bytes);
            if (sizeRead > 0) {
                mdMd5.update(bytes, 0, sizeRead);
                mdSha1.update(bytes, 0, sizeRead);
                mdSha256.update(bytes, 0, sizeRead);
                mdSha512.update(bytes, 0, sizeRead);
            }
        } while (sizeRead != -1);

        md5 = new BytesToString().bytesToString(mdMd5.digest());
        sha1 = new BytesToString().bytesToString(mdSha1.digest());
        sha256 = new BytesToString().bytesToString(mdSha256.digest());
        sha512 = new BytesToString().bytesToString(mdSha512.digest());

        Log logPackageNewest = Log.getPackageNewest(packageName, mContext);
        boolean modified = false;
        if (logPackageNewest != null && !logPackageNewest.deleted) {
            if (logPackageNewest.packageName.equals(packageName) && logPackageNewest.versionCode == versionCode
                    && logPackageNewest.versionName.equals(versionName) && logPackageNewest.md5.equals(md5)
                    && logPackageNewest.sha1.equals(sha1) && logPackageNewest.sha256.equals(sha256)
                    && logPackageNewest.sha512.equals(sha512) && logPackageNewest.verify())
                return false;
            else
                modified = true;
        }

        Log newLog = new Log();
        newLog.label = label;
        newLog.packageName = packageName;
        newLog.versionName = versionName;
        newLog.versionCode = versionCode;
        newLog.md5 = md5;
        newLog.sha1 = sha1;
        newLog.sha256 = sha256;
        newLog.sha512 = sha512;
        newLog.modified = modified;
        newLog.insert(mContext);

        return true;
    }

    public boolean run(String packageName)
            throws PackageManager.NameNotFoundException, IOException, NoSuchAlgorithmException {
        PackageManager packageManager = mContext.getPackageManager();
        return run(packageManager.getPackageInfo(packageName, 0));
    }

}
