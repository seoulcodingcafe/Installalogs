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

package io.github.installalogs.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import java.util.Date;

import io.github.installalogs.MainActivity;
import io.github.installalogs.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class Notification {
	private Context mContext;

	public Notification(Context context) {
		mContext = context;
	}

	public Notification createCleanProcessChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
			if (notificationManager.getNotificationChannel("CLEANPROCESS") != null)
				return this;
			CharSequence name = mContext.getString(R.string.clean_process);
			String description = mContext.getString(R.string.clean_process_in_background);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("CLEANPROCESS", name, importance);
			channel.setDescription(description);
			channel.enableLights(false);
			channel.enableVibration(false);
			channel.setSound(null, null);
			notificationManager.createNotificationChannel(channel);
		}
		return this;
	}

	public Notification createNewInstallationChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
			if (notificationManager.getNotificationChannel("NEWINSTALLATION") != null)
				return this;
			CharSequence name = mContext.getString(R.string.new_installation);
			String description = mContext.getString(R.string.new_installation_notification);
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel("NEWINSTALLATION", name, importance);
			channel.setDescription(description);
			channel.enableLights(true);
			channel.enableVibration(true);
			notificationManager.createNotificationChannel(channel);
		}
		return this;
	}

	public Notification createNoChangeChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
			if (notificationManager.getNotificationChannel("NOCHANGE") != null)
				return this;
			CharSequence name = mContext.getString(R.string.no_change);
			String description = mContext.getString(R.string.no_change_notification);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("NOCHANGE", name, importance);
			channel.setDescription(description);
			channel.enableLights(false);
			channel.enableVibration(false);
			channel.setSound(null, null);
			notificationManager.createNotificationChannel(channel);
		}
		return this;
	}

	public Notification createScanProcessChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
			if (notificationManager.getNotificationChannel("SCANPROCESS") != null)
				return this;
			CharSequence name = mContext.getString(R.string.scan_process);
			String description = mContext.getString(R.string.scan_process_in_background);
			int importance = NotificationManager.IMPORTANCE_DEFAULT;
			NotificationChannel channel = new NotificationChannel("SCANPROCESS", name, importance);
			channel.setDescription(description);
			channel.enableLights(false);
			channel.enableVibration(false);
			channel.setSound(null, null);
			notificationManager.createNotificationChannel(channel);
		}
		return this;
	}

	public Notification createServiceErrorChannel() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
			if (notificationManager.getNotificationChannel("SERVICEERROR") != null)
				return this;
			CharSequence name = mContext.getString(R.string.Service_error);
			String description = mContext.getString(R.string.Service_error_notification);
			int importance = NotificationManager.IMPORTANCE_HIGH;
			NotificationChannel channel = new NotificationChannel("SERVICEERROR", name, importance);
			channel.setDescription(description);
			channel.enableLights(true);
			channel.enableVibration(true);
			notificationManager.createNotificationChannel(channel);
		}
		return this;
	}

	public android.app.Notification getCleanProgress() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "CLEANPROCESS")
				.setAutoCancel(false).setOngoing(true).setOnlyAlertOnce(true).setSound(null)
				.setPriority(android.app.Notification.PRIORITY_LOW).setProgress(1, 0, true)
				.setContentTitle(mContext.getString(R.string.app_name))
				.setContentText(mContext.getString(R.string.scanning_deleted_application))
				.setSmallIcon(R.drawable.scanning);
		return builder.build();
	}

	public android.app.Notification getScanProgress() {
		NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext, "SCANPROCESS")
				.setAutoCancel(false).setOngoing(true).setOnlyAlertOnce(true).setSound(null)
				.setPriority(android.app.Notification.PRIORITY_LOW).setProgress(1, 0, true)
				.setContentTitle(mContext.getString(R.string.app_name))
				.setContentText(mContext.getString(R.string.scanning)).setSmallIcon(R.drawable.scanning);
		return builder.build();
	}

	public void showNewInstallation(int newInstallation) {
		android.app.Notification notification = new NotificationCompat.Builder(mContext, "NEWINSTALLATION")
				.setAutoCancel(true).setOngoing(false).setOnlyAlertOnce(false)
				.setPriority(android.app.Notification.PRIORITY_HIGH)
				.setContentTitle(mContext.getString(R.string.app_name))
				.setContentText(mContext.getString(R.string.detected_new_installation) + ": " + newInstallation)
				.setSmallIcon(R.drawable.warning)
				.setContentIntent(PendingIntent.getActivity(mContext, 1, new Intent(mContext, MainActivity.class), 0))
				.build();

		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify((int) System.currentTimeMillis() / 1000, notification);
	}

	public void showNoChange() {
		android.app.Notification notification = new NotificationCompat.Builder(mContext, "NOCHANGE").setAutoCancel(true)
				.setOngoing(false).setOnlyAlertOnce(false).setPriority(android.app.Notification.PRIORITY_DEFAULT)
				.setContentTitle(mContext.getString(R.string.app_name))
				.setContentText(mContext.getString(R.string.no_change_at) + " "
						+ new Date(System.currentTimeMillis()).toLocaleString())
				.setSmallIcon(R.drawable.nochange)
				.setContentIntent(PendingIntent.getActivity(mContext, 1, new Intent(mContext, MainActivity.class), 0))
				.build();

		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify(10, notification);
	}

	public void showServiceError(String error) {
		android.app.Notification notification = new NotificationCompat.Builder(mContext, "SERVICEERROR")
				.setAutoCancel(true).setOngoing(false).setOnlyAlertOnce(false)
				.setPriority(android.app.Notification.PRIORITY_HIGH)
				.setContentTitle(
						mContext.getString(R.string.app_name) + " " + mContext.getString(R.string.service_error))
				.setContentText(error).setSmallIcon(R.drawable.warning).build();

		NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
		notificationManager.notify((int) System.currentTimeMillis() / 1000, notification);
	}

}
