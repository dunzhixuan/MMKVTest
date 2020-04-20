package com.dunzhixuan.mmkvtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

	static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button1 = findViewById(R.id.btn_write_int);
		button1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();
				for (int i = 0; i < 1000; i++){
					SharedPreferences sharedPreferences = getSharedPreferences("dzx",MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putInt("testInt" + i,2).commit();
				}
				Log.e(TAG,"time == " + (System.currentTimeMillis() - time));
			}
		});

		Button button2 = findViewById(R.id.btn_read_int);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getSharedPreferences("dzx",MODE_PRIVATE);
				int value = sharedPreferences.getInt("testInt",0);
				Log.e("value","" + value);
			}
		});

		Button button3 = findViewById(R.id.btn_write_string);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();
				for (int i = 0; i < 1000; i++){
					SharedPreferences sharedPreferences = getSharedPreferences("dzx2",MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putString("testString" + i,"abc").commit();
				}
				Log.e(TAG,"time == " + (System.currentTimeMillis() - time));
			}
		});

		Button button4 = findViewById(R.id.btn_read_string);
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getSharedPreferences("dzx2",MODE_PRIVATE);
				String value = sharedPreferences.getString("testString","");
				Log.e("value",value);
			}
		});

	}
}
