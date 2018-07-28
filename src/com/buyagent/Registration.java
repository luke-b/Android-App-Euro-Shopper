package com.buyagent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;

public class Registration extends Thread {

	private final static String REGISTRATION_URL = "https://script.google.com/macros/s/AKfycbzXTAicsP1RtCG817_f0SfMnG3eq5AOUxRb73eisVLobapGiDg/exec";
	SharedPreferences preferences;
	SimpleDateFormat sdf = new SimpleDateFormat("K:mm a, z");

	private String possibleEmail;
	private String iso3Country;
	private String iso3Language;
	private String localTime;

	private String html = "";
	private Handler mHandler;

	SharedPreferences settings;

	private static Registration instance = null;

	public static void check(Context appContext) {

		if (instance == null) {
			instance = new Registration(appContext);
		}
	}

	public Registration(Context context) {

		settings = context.getSharedPreferences("RegistrationPreferences",
				context.MODE_PRIVATE);

		possibleEmail = getUsername(context);
		iso3Country = Locale.getDefault().getISO3Country();
		iso3Language = Locale.getDefault().getISO3Language();
		Date d = new Date();
		localTime = sdf.format(d);

		if (!isRegistered()) {
			this.start();
		}
	}

	public void run() {

		while (!isRegistered()) {

			doRegistration();

			if (!isRegistered()) {

				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	private void doRegistration() {

		doPostRequest(REGISTRATION_URL, localTime, possibleEmail, iso3Language,
				iso3Country);
	}

	

	private void doPostRequest(String postUrl, String client_time,
			String client_email_src, String client_iso3_language,
			String client_iso3_country) {

		// Create a new HttpClient and Post Header
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(postUrl);

		String client_email = client_email_src;
		if (client_email == null)
			client_email = "null@mail";

		try {
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("client_time",
					client_time));
			nameValuePairs.add(new BasicNameValuePair("client_email",
					client_email));
			nameValuePairs.add(new BasicNameValuePair("client_iso3_language",
					client_iso3_language));
			nameValuePairs.add(new BasicNameValuePair("client_iso3_country",
					client_iso3_country));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			HttpResponse response = httpclient.execute(httppost);

			String code = inputStreamToString(response.getEntity().getContent());
			String install_id = null;

			try {
				install_id = code.split(":")[1];
			} catch (Throwable t) {
			}

			System.out.println("response: " + code);

			if (code.startsWith("200") && install_id != null) {
				this.storeInstallId(install_id);
				this.storeRegistationSuccess();

			} else {
				this.storeRegistationFailure();
			}

		} catch (ClientProtocolException e) {

			this.storeRegistationFailure();
		} catch (IOException e) {

			this.storeRegistationFailure();
		}

	}

	public String getUsername(Context context) {
		AccountManager manager = AccountManager.get(context);
		Account[] accounts = manager.getAccountsByType("com.google");
		List<String> possibleEmails = new LinkedList<String>();

		for (Account account : accounts) {
			// TODO: Check possibleEmail against an email regex or treat
			// account.name as an email address only for certain account.type
			// values.
			possibleEmails.add(account.name);
		}

		if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
			String email = possibleEmails.get(0);
			String[] parts = email.split("@");
			if (parts.length > 0 && parts[0] != null)
				return email;
			else
				return null;
		} else
			return null;
	}

	private String inputStreamToString(InputStream is) {
		String s = "";
		String line = "";

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));

		try {
			while ((line = rd.readLine()) != null) {
				s += line;
			}
		} catch (IOException e) {
		}

		return s;
	}
	
	
	private boolean isRegistered() {
		
		return settings.getBoolean("registered", false);
	}
	

	private void storeRegistationSuccess() {

		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putBoolean("registered", true);
		prefEditor.commit();
	}

	private void storeRegistationFailure() {

		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putBoolean("registered", false);
		prefEditor.commit();
	}

	private void storeInstallId(String install_id) {

		SharedPreferences.Editor prefEditor = settings.edit();
		prefEditor.putString("installId", install_id);
		prefEditor.commit();
	}

}
