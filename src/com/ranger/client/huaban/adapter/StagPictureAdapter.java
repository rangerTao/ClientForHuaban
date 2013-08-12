package com.ranger.client.huaban.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.ranger.client.huaban.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class StagPictureAdapter extends BaseAdapter {

	Context _context;
	ArrayList<String> mUrls;
	LayoutInflater inflater;
	DisplayImageOptions option;

	public StagPictureAdapter(Context convert, ArrayList<String> urls,
			DisplayImageOptions op) {

		_context = convert;
		mUrls = urls;
		inflater = LayoutInflater.from(_context);
		option = op;
	}

	@Override
	public int getCount() {
		return mUrls.size();
	}

	@Override
	public Object getItem(int position) {
		return mUrls.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		PicItem pi = null;

		if (convertView == null) {

			pi = new PicItem();

			View imageLayout = inflater.inflate(R.layout.item_stag_image, null);
			pi.iView = (ImageView) imageLayout.findViewById(R.id.image);
			pi.pbLoad  = (ProgressBar) imageLayout
					.findViewById(R.id.loading);
			imageLayout.setTag(pi);

			convertView = imageLayout;
		} else {
			pi = (PicItem) convertView.getTag();
		}
		
		pi.pbLoad.setVisibility(View.GONE);
		
		ImageLoader.getInstance().displayImage(mUrls.get(position), pi.iView, option,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "Input/Output error";
							break;
						case DECODING_ERROR:
							message = "Image can't be decoded";
							break;
						case NETWORK_DENIED:
							message = "Downloads are denied";
							break;
						case OUT_OF_MEMORY:
							message = "Out Of Memory error";
							break;
						case UNKNOWN:
							break;
						}
						Toast.makeText(_context, message, Toast.LENGTH_SHORT)
								.show();
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
					}
				});

		return convertView;
	}

}

class PicItem {
	ImageView iView;
	ProgressBar pbLoad;
}
