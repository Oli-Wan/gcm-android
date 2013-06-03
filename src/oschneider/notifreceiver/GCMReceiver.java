package oschneider.notifreceiver;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GCMReceiver extends android.content.BroadcastReceiver
{

	@Override
	public void onReceive(Context context, Intent intent)
	{
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(context);
		String messageType = gcm.getMessageType(intent);
		System.out.println(messageType);
		
		if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType))
		{
			sendNotification(context, "Send error: "
					+ intent.getExtras().toString(), null);
		} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
				.equals(messageType))
		{
			sendNotification(context, "Deleted messages on server: "
					+ intent.getExtras().toString(), null);
		} else
		{
			sendNotification(context, intent.getExtras().getString("content"), intent.getExtras().getString("url"));
		}
		setResultCode(Activity.RESULT_OK);
	}

	private void sendNotification(Context context, String notif, String url)
	{
		System.out.println("Sending notification, notif:"+notif+" url:"+url);
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);

		int icon = R.drawable.ic_launcher;
		Intent notificationIntent;
		if(url != null && !url.isEmpty()) {
			  notificationIntent = new Intent(Intent.ACTION_VIEW);
			  notificationIntent.setData(Uri.parse(url));
		} else {
			PackageManager pm = context.getPackageManager();
	        notificationIntent = pm.getLaunchIntentForPackage(context.getPackageName());
	        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		}
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
				context).setSmallIcon(icon).setContentTitle("GCM notification")
				.setContentText(notif)
				.setPriority(NotificationCompat.PRIORITY_HIGH)
				.setAutoCancel(true);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context,
				0, notificationIntent, 0);
		mBuilder.setContentIntent(resultPendingIntent);
		mNotificationManager.notify(1, mBuilder.build());
		
	}
}
