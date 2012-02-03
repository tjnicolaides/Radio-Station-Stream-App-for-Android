package com.gmp.wxyz;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class podcastList extends Activity {
	 public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);

	        TextView textview = new TextView(this);
	        textview.setText("This is the Podcast tab");
	        setContentView(textview);
	    }
}
