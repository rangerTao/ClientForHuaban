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

	static String base_url = "http://api.huaban.com/favorite/beauty?limit=100";
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

				if (pins.length() <= 0) {
					break;
				}

				for (int i = 0; i < pins.length(); i++) {
					JSONObject pin = pins.getJSONObject(i);

					String text = pin.getString("raw_text");
					Object orig_url = pin.get("orig_source");

					JSONObject file = pin.getJSONObject("file");

					String width = file.getString("width");
					String height = file.getString("height");

					if (width == null || height == null || width.equals("") || height.equals("")) {
						continue;
					} else {
						try {
							int swidth = Integer.parseInt(width);
							int sheight = Integer.parseInt(height);

							if ((swidth / Constant.screen_width) < 0.5 && (sheight / Constant.screen_height) < 0.5) {
								continue;
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (orig_url instanceof String) {
						if (orig_url != null && orig_url.equals("")) {
							if (file != null) {
								String pic_url = "img.hb.aicdn.com/" + file.getString("key");
								putPicIntoList(pinlist, pic_url);
							}
						} else {
							// System.out.println(orig_url);
							putPicIntoList(pinlist, orig_url.toString());
						}
					} else {
						file = pin.getJSONObject("file");

						if (file != null) {
							String pic_url = "img.hb.aicdn.com/" + file.getString("key");
							// System.out.println(pic_url);
							putPicIntoList(pinlist, pic_url);
						}
						// }
					}

					dealsize++;

					maxPinId = pin.getInt("pin_id");
				}

				result = NetworkUtil.connect(base_url + "&max=" + maxPinId);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return pinlist;
	}

	public static void putPicIntoList(ArrayList list, String pic) {
		
		pinlist.add(pic);

		Message msg = new Message();
		msg.what = FlowGridViewAcitivity.UPDATE_PROGRESS_TEXT;
		msg.obj = pinlist.size() + "/" + totalSize;
		FlowGridViewAcitivity.getHandler().sendMessage(msg);
		// }

	}
}
