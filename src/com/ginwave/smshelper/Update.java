package com.ginwave.smshelper;

import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Update extends Activity {
	
	TextView mUpdateText;
	Button mUpdateButton;
	private static final String BASE_URL = "http://202.201.0.162/";
	public static final String VERSION_URL = BASE_URL
			+ "media/smsassistant.version";
	private static final String DB_URL = BASE_URL + "media/smsassistant.db";
	public static final String APP_URL = BASE_URL + "media/smsassistant.apk";
	protected static String SMSPACKAGE_URL=BASE_URL+"";
	protected static String SMSPACKAGE_VERSION="1";
	public static final String APP_VERSION = "12";
	SharedPreferences mSetting;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSetting = getSharedPreferences("SMSHelper", 0);
		updateConnectedFlags();
		setContentView(R.layout.activity_update);
		setTitle("检查更新");
		mUpdateText = (TextView) findViewById(R.id.update_text);
		mUpdateButton = (Button) findViewById(R.id.update_button);

	}

	private void updateConnectedFlags() {
		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeInfo = connMgr.getActiveNetworkInfo();
		if (activeInfo != null && activeInfo.isConnected()) {
			if (activeInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				new UpdateTask().execute();
			} else if (activeInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
				Log.v("heiheifang", "TYPE_MOBILE");
				if (isApnAvailable(getContentResolver())) {
					new UpdateTask().execute();
				} else {
					Toast.makeText(getApplicationContext(),
							"请使用中国移动的数据网络或者WIFI更新短信库", Toast.LENGTH_LONG)
							.show();
					finish();
				}
			}

		} else {
			Toast.makeText(getApplicationContext(), "未连接网络，请先打开网络",
					Toast.LENGTH_SHORT).show();
			finish();
		}
	}

	Uri apn_table = Uri.parse("content://telephony/carriers/preferapn");

	public boolean isApnAvailable(ContentResolver resolver) {
		boolean a = false;
		Cursor cmwap = resolver.query(apn_table, null, "apn=?",
				new String[] { "cmwap" }, null);
		Cursor cmnet = resolver.query(apn_table, null, "apn=?",
				new String[] { "cmnet" }, null);
		if (cmwap.moveToFirst() || cmnet.moveToFirst()) {
			a = true;
		}
		Log.v("heiheifang", "isApnAvailable " + a);
		return a;
	}

	public void downloadFile(String url, String path, String fileName)
			throws IOException {
		// 下载函数
		URL myURL = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) myURL.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();
		InputStream is = conn.getInputStream();
		if (is == null)
			throw new RuntimeException("stream is null");

		// 把文件存到path
		File dir = new File("/data/data/" + getPackageName() + "/" + path);
		dir.mkdir();
		String path1 = dir.toString();

		File file = new File(path1 + "/" + fileName);
		file.createNewFile();
		OutputStream os = new FileOutputStream(file);

		byte buf[] = new byte[1024];

		int len = -1;
		while ((len = is.read(buf)) != -1) {
			os.write(buf, 0, len);
		}

		is.close();
		os.close();
	}

	public void finish(View v) {
		this.finish();
	}

	private class UpdateTask extends AsyncTask<Void, String, String> {

		@Override
		protected String doInBackground(Void... params) {
			String db_version = mSetting.getString("db_version", "2");

			// download file
			HttpClient client = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(VERSION_URL);

			HttpResponse response;
			try {
				response = client.execute(httpget);

				String versions = EntityUtils.toString(response.getEntity());
				String[] r = versions.split(" ");
				if (!APP_VERSION.equals(r[0])
						&& response.getStatusLine().getStatusCode() == 200) {
					Log.i("XXX", "app update");
					Uri uri = Uri.parse(APP_URL);
					Intent i = new Intent(Intent.ACTION_DEFAULT, uri);
					startActivity(i);
					finish();
				} else if (!db_version.equals(r[1])) {
					downloadFile(DB_URL, "/", "db.zip");
					ZipUtils.unZipFolder("/data/data/" + getPackageName()
							+ "/db.zip", "/data/data/" + getPackageName()
							+ "/files/");
					Editor editor = mSetting.edit();
					editor.putString("db_version", r[1]);
					editor.commit();

				} else {
				}
			} catch (ClientProtocolException e) {

				e.printStackTrace();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}

			// unzip file
			return null;
		}

		public void onPostExecute(String Re) {
			mUpdateText.setText("更新完成");
			mUpdateButton.setText("确定");

		}
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
