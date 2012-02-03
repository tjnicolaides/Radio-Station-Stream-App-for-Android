package com.gmp.wxyz;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.webkit.WebView;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class Main extends TabActivity {
	/** Called when the activity is first created. */

	private static Drawable playIcon;
	private static Drawable pauseIcon;
	private static Drawable stopIcon;
	private static Drawable backgroundHD1;
	private static Drawable backgroundHD2;
	private static Drawable loadingIcon;
	public static final String PREFS_NAME = "MyPrefsFile";
	public static int activeStation;
	private Boolean bgPlay;
	private final Handler adHandler = new Handler();

	WebView webview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		pauseIcon = getResources().getDrawable(R.drawable.control_pause_icon);
		playIcon = getResources().getDrawable(R.drawable.control_play_icon);
		stopIcon = getResources().getDrawable(R.drawable.control_stop_icon);
		loadingIcon = getResources().getDrawable(
				R.drawable.control_loading_icon);
		backgroundHD1 = getResources().getDrawable(R.drawable.background);
		backgroundHD2 = getResources().getDrawable(R.drawable.background_hd2);
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost(); // The activity TabHost
		tabHost.setup();

		TabHost.TabSpec spec; // Resusable TabSpec for each tab
		Intent intent; // Reusable Intent for each tab

		setVolumeControlStream(AudioManager.STREAM_MUSIC);
		// Create an Intent to launch an Activity for the tab (to be reused)
		intent = new Intent().setClass(this, streamHD1.class);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("streamHD1").setIndicator(
				this.getString(R.string.tab_name_HD1),
				res.getDrawable(R.drawable.ic_tab_hdone)).setContent(intent);
		tabHost.addTab(spec);

		// Initialize a TabSpec for each tab and add it to the TabHost
		intent = new Intent().setClass(this, streamHD2.class);
		spec = tabHost.newTabSpec("streamHD2").setIndicator(
				this.getString(R.string.tab_name_HD2),
				res.getDrawable(R.drawable.ic_tab_hdtwo)).setContent(intent);
		tabHost.addTab(spec);

		// Initialize a TabSpec for each tab and add it to the TabHost
		/*
		 * intent = new Intent().setClass(this, podcastList.class); spec =
		 * tabHost
		 * .newTabSpec("podcastList").setIndicator(this.getString(R.string
		 * .tab_name_podcasts), res.getDrawable(R.drawable.ic_tab_stream))
		 * .setContent(intent); tabHost.addTab(spec);
		 */

		// Initialize a TabSpec for each tab and add it to the TabHost
		intent = new Intent().setClass(this, infoScreen.class);
		spec = tabHost.newTabSpec("infoScreen").setIndicator(
				this.getString(R.string.tab_name_info),
				res.getDrawable(R.drawable.ic_tab_options)).setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(0);

		tabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				LinearLayout container = (LinearLayout) findViewById(R.id.container);
				if (tabId == "streamHD1") {
					container.setBackgroundDrawable(backgroundHD1);
				}
				if (tabId == "streamHD2") {
					container.setBackgroundDrawable(backgroundHD2);
				}
				if (tabId == "podcastList") {
					container.setBackgroundDrawable(backgroundHD1);
				}
				if (tabId == "infoScreen") {
					container.setBackgroundDrawable(backgroundHD1);
				}
			}
		});

		notifyBGPlay();
		// loadAd();
		//adHandler.postDelayed(loadAd, 5 * 1000);

	}

	@SuppressWarnings("null")
	@Override
	protected void onPause() {
		playIcon = getResources().getDrawable(R.drawable.control_play_icon);
		// bgPlay = infoScreen.bgPlay;

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		bgPlay = settings.getBoolean("bgPlay", false);

		Intent svc = new Intent(this, streamService.class);
		MediaPlayer stream = streamService.stream;

		// deallocate all memory
		if (bgPlay == false) {
			if (stream != null) {
				stopService(svc);
				if (streamHD1.controlButton != null) {
					streamHD1.controlButton
							.setCompoundDrawablesWithIntrinsicBounds(playIcon,
									null, null, null);
					streamHD1.controlButton
							.setText(R.string.button_control_play);
				}
				if (streamHD2.controlButton != null) {
					streamHD2.controlButton
							.setCompoundDrawablesWithIntrinsicBounds(playIcon,
									null, null, null);
					streamHD2.controlButton
							.setText(R.string.button_control_play);
				}
			} else if (bgPlay == true) {
				if (stream.isPlaying() != true) {
					stopService(svc);
					if (streamHD1.controlButton != null) {
						streamHD1.controlButton
								.setCompoundDrawablesWithIntrinsicBounds(
										playIcon, null, null, null);
						streamHD1.controlButton
								.setText(R.string.button_control_play);
					}
					if (streamHD2.controlButton != null) {
						streamHD2.controlButton
								.setCompoundDrawablesWithIntrinsicBounds(
										playIcon, null, null, null);
						streamHD2.controlButton
								.setText(R.string.button_control_play);
					}
				} else {
					if (streamService.bgPlay != null) {
						streamService.bgPlay = bgPlay;
					}
				}
			}

		}
		super.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onResume() {
		super.onResume(); // deallocate all memory
		notifyBGPlay();
	}

	public void notifyBGPlay() {
		SharedPreferences settings = getSharedPreferences(
				infoScreen.PREFS_NAME, 0);
		bgPlay = settings.getBoolean("bgPlay", false);
		Toast t = null;
		if (bgPlay == true) {
			t = Toast.makeText(this,
					getString(R.string.bg_play_notification_on),
					Toast.LENGTH_LONG);
		} else {
			t = Toast.makeText(this,
					getString(R.string.bg_play_notification_off),
					Toast.LENGTH_LONG);
		}
		t.setGravity(Gravity.TOP | Gravity.CENTER, 0, 100);
		t.show();
	}

	public static void toggleControlButton(Button button, String state) {

		Button controlButton = button;
		if (button != null) {
			if (state == "play") {
				controlButton.setCompoundDrawablesWithIntrinsicBounds(playIcon,
						null, null, null);
				controlButton.setText(R.string.button_control_play);
			} else if (state == "pause") {
				controlButton.setCompoundDrawablesWithIntrinsicBounds(
						pauseIcon, null, null, null);
				controlButton.setText(R.string.button_control_pause);
			} else if (state == "stop") {
				controlButton.setCompoundDrawablesWithIntrinsicBounds(stopIcon,
						null, null, null);
				controlButton.setText(R.string.button_control_stop);
			} else if (state == "loading") {
				controlButton.setCompoundDrawablesWithIntrinsicBounds(
						loadingIcon, null, null, null);
				controlButton.setText(R.string.button_control_loading);
			}
		}
	}

	private final Runnable loadAd = new Runnable() {
		public void run() {
			webview = (WebView) findViewById(R.id.webview);
			webview.getSettings().setJavaScriptEnabled(true);
			webview.getSettings().setSupportZoom(false);
			webview.getSettings().setLayoutAlgorithm(
					LayoutAlgorithm.SINGLE_COLUMN);
			webview.setInitialScale(0);
			try {
				webview.loadUrl(getString(R.string.ad_zone_url));
			} catch (Exception e) {
				webview.clearView();
			}
		}

	};

}
