package com.goeuro.ab.endpoints;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONObject;

import android.net.Uri;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.goeuro.ab.network.ApiClient;

public class LocationEndpoint {

	private static final String POSITION = "position";
	private static final String SUGGEST = "suggest";

	/**
	 * Executes a location request based on locale and passed param
	 * 
	 * @return The submitted request.
	 */
	public static JsonObjectRequest requestLocation(ApiClient client,
			String locale, String paramToSearch,
			Response.Listener<JSONObject> listener,
			Response.ErrorListener errorListener) {

		Uri requestLocationUri = client.getBaseUrl().buildUpon()
				.appendPath(POSITION).appendPath(SUGGEST).appendPath(locale)
				.appendPath(paramToSearch).build();

		JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestLocationUri.toString(), null, listener, errorListener);
		client.execute(request);

		return request;
	}

	// always verify the host - dont check for certificate
	public static final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - don't check for any certificate
	 */
	public static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}

			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
