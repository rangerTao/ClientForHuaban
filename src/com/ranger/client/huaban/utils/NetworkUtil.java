package com.ranger.client.huaban.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtil {

	/**
	 * down load content.
	 * 
	 * @param url
	 * @return
	 */
	public static String connect(String url) {

		try {
			URL urlCon = new URL(url);

			HttpURLConnection conn = (HttpURLConnection) urlCon
					.openConnection();

			conn.setRequestMethod("GET");

			conn.setConnectTimeout(5 * 1000);

			InputStream inStream = conn.getInputStream();
			byte[] data = readFromInput(inStream);

			String html = new String(data, "UTF-8");

			return html;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	/**
	 * Get bytes from input stream.
	 * 
	 * @param inStream
	 * @return
	 * @throws Exception
	 */
	public static byte[] readFromInput(InputStream inStream) throws Exception {

		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		byte[] buffer = new byte[1024];

		int len = 0;

		while ((len = inStream.read(buffer)) != -1) {

			outStream.write(buffer, 0, len);

		}

		inStream.close();

		return outStream.toByteArray();

	}

}
