package com.gmp.wxyz;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class streamHD2 extends Activity {

	public static MediaPlayer stream;
	public static String url;
	public static Button controlButton;
	public static Intent svc;
	public static String status;
	public int activeStation;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.screenhd2);

		controlButton = (Button) findViewById(R.id.stream_control);
		controlButton.setOnClickListener(controlClickListener);

		url = getString(R.string.stream_pls_HD2);
		stream = streamService.stream;

		svc = new Intent(this, streamService.class);
		svc.putExtra("url", url);
		svc.putExtra("activeStation", 2);

	}

	public void toggleControlButton(Button button, String state) {
		Main.toggleControlButton(button, state);
	}

	public OnClickListener controlClickListener = new OnClickListener() {
		public void onClick(View v) {
			stream = streamService.stream;
			activeStation = Main.activeStation;
			status = "STATUS";
			if (stream != null) {
				if (activeStation == 2) {
					if (stream.isPlaying() == true) {
						stopService(svc);
						toggleControlButton(controlButton, "play");
					} else {
						toggleControlButton(controlButton, "loading");
						startService(svc);
					}
				}

				else {
					toggleControlButton(streamHD1.controlButton, "play");
					toggleControlButton(controlButton, "loading");

					status = "Switching stations - Stream initialized";
					stopService(svc);
					startService(svc);
				}
			} else {
				toggleControlButton(streamHD1.controlButton, "play");
				toggleControlButton(controlButton, "loading");

				status = "Stream initialized";
				startService(svc);
			}
		}
	};

};