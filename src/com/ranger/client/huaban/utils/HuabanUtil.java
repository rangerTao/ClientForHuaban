package com.ranger.client.huaban.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.integer;
import android.os.Message;
import android.util.Log;
import android.view.ViewDebug.FlagToString;

import com.ranger.client.huaban.ui.FlowGridViewAcitivity;

public class HuabanUtil {

	static String base_url = "http://api.huaban.com/favorite/beauty?limit=200";
	static long maxPinId = 0;
	static int totalSize = 20;
	static ArrayList<String> pinlist;

	public static ArrayList<String> getPicList(int pinid) {

		pinlist = new ArrayList<String>();

		String result = "";
		if (pinid != 0)
			result = NetworkUtil.connect(base_url + "&max=" + maxPinId);
		else
			result = NetworkUtil.connect(base_url);

		try {

			int dealsize = 0;

			while (pinlist.size() < totalSize) {

				JSONObject huaban = new JSONObject(result);

				JSONArray pins = huaban.getJSONArray("pins");
				
				if(pins.length() <=0){
					break;
				}

				for (int i = 0; i < pins.length(); i++) {
					JSONObject pin = pins.getJSONObject(i);

					String text = pin.getString("raw_text");
//					if (text.contains("ΠΨ") || text.contains("²¨")
//							|| text.contains("ίδίδ")) {
						Object orig_url = pin.get("orig_source");

						if (orig_url instanceof String) {
							if (orig_url != null && orig_url.equals("")) {
								JSONObject file = pin.getJSONObject("file");
								if (file != null) {
									String pic_url = "img.hb.aicdn.com/"
											+ file.getString("key");
									putPicIntoList(pinlist, pic_url);
								}
							} else {
								// System.out.println(orig_url);
								putPicIntoList(pinlist, orig_url.toString());
							}
						} else {
							JSONObject file = pin.getJSONObject("file");

							if (file != null) {
								String pic_url = "img.hb.aicdn.com/"
										+ file.getString("key");
								// System.out.println(pic_url);
								putPicIntoList(pinlist, pic_url);
							}
//						}
					}

					dealsize++;

					maxPinId = pin.getInt("pin_id");
				}

				Log.d("TAG", "size of pic has been dealed : " + dealsize);

				result = NetworkUtil.connect(base_url + "&max=" + maxPinId);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pinlist;
	}

	public static void putPicIntoList(ArrayList list, String pic) {
		// if (pic.toLowerCase().endsWith("jpg")
		// || pic.toLowerCase().endsWith("png")
		// || pic.toLowerCase().endsWith("gif")) {
		pinlist.add(pic);

		Message msg = new Message();
		msg.what = FlowGridViewAcitivity.UPDATE_PROGRESS_TEXT;
		msg.obj = pinlist.size() + "/" + totalSize;
		FlowGridViewAcitivity.getHandler().sendMessage(msg);
		// }

	}
}
