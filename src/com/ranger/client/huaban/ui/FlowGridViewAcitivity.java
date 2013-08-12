package com.ranger.client.huaban.ui;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.ranger.client.huaban.R;
import com.ranger.client.huaban.adapter.ImagePagerAdapter;
import com.ranger.client.huaban.utils.Constant;
import com.ranger.client.huaban.utils.HuabanUtil;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.InputDevice;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class FlowGridViewAcitivity extends BaseActivity implements OnPageChangeListener {

	public static final int UPDATE_PROGRESS_TEXT = 999;

	DisplayImageOptions options;
	ViewPager sgv;
	ArrayList<String> urlList;

	ProgressBar pbLoading;
	static TextView tvProgress;
	ImagePagerAdapter spa;

	public static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {

			switch (msg.what) {
			case UPDATE_PROGRESS_TEXT:
				String message = (String) msg.obj;

				if (tvProgress != null && tvProgress.getVisibility() == View.VISIBLE) {
					tvProgress.setText(message);
				}
				break;

			default:
				break;
			}

			super.handleMessage(msg);
		}

	};

	ImageLoader imageLoader = ImageLoader.getInstance();

	DisplayMetrics dm = new DisplayMetrics();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindowManager().getDefaultDisplay().getMetrics(dm);
		Constant.screen_height = dm.heightPixels;
		Constant.screen_width = dm.widthPixels;

		setContentView(R.layout.huaban_list);

		urlList = new ArrayList<String>();

		options = new DisplayImageOptions.Builder().showImageForEmptyUri(R.drawable.ic_empty).showImageOnFail(R.drawable.ic_error).resetViewBeforeLoading(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY).bitmapConfig(Bitmap.Config.RGB_565).displayer(new FadeInBitmapDisplayer(300)).build();

		pbLoading = (ProgressBar) findViewById(R.id.pbLoading);
		sgv = (ViewPager) findViewById(R.id.pager);
		tvProgress = (TextView) findViewById(R.id.tvProgress);

		new HuabanListAsyncTask().execute(0);

		sgv.setOnPageChangeListener(this);
	}

	public static Handler getHandler() {
		return mHandler;
	}

	class HuabanListAsyncTask extends AsyncTask {

		int pinid = 0;

		@Override
		protected void onPostExecute(Object result) {

			if (pinid == 0) {
				spa = new ImagePagerAdapter(getApplicationContext(), urlList, options);
				sgv.setAdapter(spa);
			}

			spa.notifyDataSetChanged();

			pbLoading.setVisibility(View.GONE);
			tvProgress.setVisibility(View.GONE);

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Object doInBackground(Object... params) {

			pinid = (Integer) params[0];

			urlList.addAll(HuabanUtil.getPicList(pinid));

			return null;
		}

	}

	@Override
	public boolean onGenericMotionEvent(MotionEvent event) {

		if (0 != (event.getSource() & InputDevice.SOURCE_CLASS_POINTER)) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_SCROLL:

				if (event.getAxisValue(MotionEvent.AXIS_VSCROLL) < 0.0f)
					selectNext();
				else
					selectPrev();
				return true;
			}
		}
		return super.onGenericMotionEvent(event);
	}

	private void selectNext() {

		if (sgv.getCurrentItem() < urlList.size())
			sgv.setCurrentItem(sgv.getCurrentItem() + 1);
	}

	private void selectPrev() {

		if (sgv.getCurrentItem() > 0)
			sgv.setCurrentItem(sgv.getCurrentItem() - 1);

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

	}

	@Override
	public void onPageSelected(int position) {
		setTitle(position + "/" + urlList.size());
		if (position == urlList.size() - 1) {
			pbLoading.setVisibility(View.VISIBLE);
			tvProgress.setVisibility(View.VISIBLE);
			new HuabanListAsyncTask().execute(1);
		}
	}
}
