package com.gmp.wxyz;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;

public class infoScreen extends Activity {

	private CheckBox backgroundCheck;
	private Button siteButton;
	private Button phoneButton;
	public static Boolean bgPlay = false;
	public static final String PREFS_NAME = "MyPrefsFile";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.infoscreen);
		backgroundCheck = (CheckBox) findViewById(R.id.bg_checkbox);
		siteButton = (Button) findViewById(R.id.site_button);
		phoneButton = (Button) findViewById(R.id.phone_button);

		// Restore preferences
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		bgPlay = settings.getBoolean("bgPlay", false);

		if (bgPlay == true) {
			backgroundCheck.setChecked(true);
		}

		backgroundCheck.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks, depending on whether it's now
				// checked
				if (((CheckBox) v).isChecked()) {
					// backgroundCheck.setText("Background play enabled!");
					bgPlay = true;
				} else {
					// backgroundCheck.setText("Background play disabled!");
					bgPlay = false;
				}

				// We need an Editor object to make preference changes.
				// All objects are from android.context.Context
				SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putBoolean("bgPlay", bgPlay);
				// Commit the edits!
				editor.commit();

			}
		});

		siteButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks, depending on whether it's now
				// checked
				Intent i = new Intent(Intent.ACTION_VIEW);
				i.setData(Uri.parse(getString(R.string.info_url)));
				startActivity(i);
			}
		});

		phoneButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				// Perform action on clicks, depending on whether it's now
				// checked
				if (v == phoneButton) {
					Intent i = new Intent(Intent.ACTION_CALL);
					i.setData(Uri.parse(getString(R.string.info_phone_no)));
					startActivity(i);
				}
			}
		});

	}

}
