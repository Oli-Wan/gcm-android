package oschneider.notifreceiver;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.intuitive.notifreceiver.R;

public class MainActivity extends Activity
{
	public static final String PROPERTY_REG_ID = "registration_id";

	private static final String GCM_SENDER_ID = "YOUR_SENDER_ID";

	private String regid;
	private GoogleCloudMessaging gcm;
	private SharedPreferences prefs;
	private TextView text;
	private ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		prefs = getSharedPreferences(MainActivity.class.getSimpleName(),
				Context.MODE_PRIVATE);

		text = (TextView) findViewById(R.id.text);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

		regid = prefs.getString(PROPERTY_REG_ID, null);

		gcm = GoogleCloudMessaging.getInstance(this);

		if (regid == null)
			register();
		else
		{
			text.setText("Enregistrement auprès de Google Cloud Messaging réussi");
			System.out.println(regid);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void register()
	{
		new AsyncTask()
		{

			@Override
			protected Object doInBackground(Object... arg0)
			{
				String msg = "";
				try
				{
					regid = gcm.register(GCM_SENDER_ID);
					System.out.println(regid);
					msg = "Enregistrement auprès de Google Cloud Messaging réussi";
					SharedPreferences.Editor editor = prefs.edit();
					editor.putString(PROPERTY_REG_ID, regid);
					editor.commit();
				} catch (IOException ex)
				{
					msg = "Enregistrement auprès de Google Cloud Messaging échoué :"
							+ ex.getMessage();
				}
				return msg;
			}

			@Override
			protected void onPostExecute(Object result)
			{
				progressBar.setVisibility(View.INVISIBLE);
				text.setText((String) result + "\n");
			}
		}.execute(null, null, null);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
