package com.ranger.client.huaban.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.ranger.client.huaban.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WaterFallAdapter extends BaseAdapter {

	Context _context;
	ArrayList<String> mUrls;
	LayoutInflater inflater;
	DisplayImageOptions option;

	WaterFallItem wfi;

	public WaterFallAdapter(Context convert, ArrayList<String> urls, DisplayImageOptions op) {

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

		if (convertView == null) {
			wfi = new WaterFallItem();

			View imageLayout = inflater.inflate(R.layout.item_pager_image, null, false);

			wfi.ivThumb = (ImageView) imageLayout.findViewById(R.id.image);

			wfi.pbSpinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			wfi.url = mUrls.get(position);

			imageLayout.setTag(wfi);

			convertView = imageLayout;

		} else {
			wfi = (WaterFallItem) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(mUrls.get(position), wfi.ivThumb, option, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
//				wfi.pbSpinner.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				wfi.pbSpinner.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				wfi.pbSpinner.setVisibility(View.GONE);
			}
		});

		return convertView;

	}

	public class WaterFallItem {
		ImageView ivThumb;
		ProgressBar pbSpinner;
		String url;
	}
}
