package com.dunzhixuan.mmkvtest;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tencent.mmkv.MMKV;

public class MMKVActivity extends AppCompatActivity {

	static final String TAG = "MainActivity";
	MMKV mmkv = MMKV.defaultMMKV();

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
					mmkv.encode("testInt" + i,2);
				}
				Log.e(TAG,"time == " + (System.currentTimeMillis() - time));
			}
		});

		Button button2 = findViewById(R.id.btn_read_int);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int value = mmkv.decodeInt("testInt0",0);
				Log.e("value","" + value);
			}
		});

		Button button3 = findViewById(R.id.btn_write_string);
		button3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();
				for (int i = 0; i < 1000; i++){
					mmkv.encode("testString" + i,"abc");
				}
				Log.e(TAG,"time == " + (System.currentTimeMillis() - time));
			}
		});

		Button button4 = findViewById(R.id.btn_read_string);
		button4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String value = mmkv.decodeString("testString0");
				Log.e("value",value);
			}
		});

		Button button5 = findViewById(R.id.btn_write_large_data);
		button5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				long time = System.currentTimeMillis();
				for (int i = 0; i < 1000000; i++){
					mmkv.encode("testString" + i,"W3siYXZhdGFyIjoiaHR0cHM6Ly9yZXNvdXJjZS52aXBraWQuY29tLmNuL3N0YXRpYy9pbWFnZXMv");
				}
				Log.e(TAG,"time == " + (System.currentTimeMillis() - time));
			}
		});
	}
}
