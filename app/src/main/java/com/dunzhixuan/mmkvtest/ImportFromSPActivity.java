package com.dunzhixuan.mmkvtest;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.tencent.mmkv.MMKV;

import java.io.File;
import java.util.ArrayList;

public class ImportFromSPActivity extends Activity {

  static final String TAG = "ImportFromSPActivity";
  static final String paht =
      Environment.getExternalStorageState() + "com.dunzhixuan.mmkvtest.shared_prefs";

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_import_from_sp);
    Button button1 = findViewById(R.id.btn_write_sp_int);
    button1.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            SharedPreferences sharedPreferences = getSharedPreferences("vipkid", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("name", 28).apply();
          }
        });

    Button button2 = findViewById(R.id.btn_read_int_by_sp);
    button2.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            SharedPreferences sharedPreferences = getSharedPreferences("vipkid", MODE_PRIVATE);
            int value = sharedPreferences.getInt("name", 0);
            Log.e(TAG, "" + value);
          }
        });

    Button button3 = findViewById(R.id.btn_improt);
    button3.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            for (int i = 0; i < 1000; i++) {
              SharedPreferences sharedPreferences = getSharedPreferences("vipkid", MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putInt("testInt" + i, 2).commit();
            }

            for (int i = 0; i < 1000; i++) {
              SharedPreferences sharedPreferences = getSharedPreferences("vipkid2", MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putInt("testInt" + i, 1).commit();
            }

            for (int i = 0; i < 1000; i++) {
              SharedPreferences sharedPreferences =
                  getSharedPreferences("vipkid@43245", MODE_PRIVATE);
              SharedPreferences.Editor editor = sharedPreferences.edit();
              editor.putString("testInt" + i, "asd").commit();
            }

            // 迁移代码
            String path = getFilesDir().getParent() + File.separator + "shared_prefs";
            long time = System.currentTimeMillis();
            ArrayList<String> spNames = getAllFiles(path, ".xml");
            if (spNames == null || spNames.size() <= 0) return;

            MMKV mmkv = MMKV.defaultMMKV();
            for (String spName : spNames) {
              if (spName.contains("@")) {
                mmkv = MMKV.mmkvWithID(spName);
              }
              SharedPreferences oldValue = getSharedPreferences(spName, MODE_PRIVATE);
              mmkv.importFromSharedPreferences(oldValue);
              boolean isSuccess = deleteFile(path + "/" + spName + ".xml");
              Log.e(TAG, "isSuccess==" + isSuccess);
            }
            Log.e(TAG, "time == " + (System.currentTimeMillis() - time));
          }
        });

    Button button4 = findViewById(R.id.btn_read_int_by_mmkv);
    button4.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            int MMKVvalue = MMKV.mmkvWithID("vipkid").decodeInt("name");
            Log.e(TAG, "" + MMKVvalue);
          }
        });

    Button button5 = findViewById(R.id.btn_improt_empty);
    button5.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            long time = System.currentTimeMillis();
            MMKV mmkv = MMKV.mmkvWithID("vipkid2");
            SharedPreferences oldValue = getSharedPreferences("vipkid", MODE_PRIVATE);
            mmkv.importFromSharedPreferences(oldValue);
            Log.e(TAG, "time1 == " + (System.currentTimeMillis() - time));
            oldValue.edit().clear().apply();
            Log.e(TAG, "time2 == " + (System.currentTimeMillis() - time));
          }
        });

    Button button6 = findViewById(R.id.btn_remove);
    button6.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            MMKV mmkv = MMKV.mmkvWithID("vipkid2");
            mmkv.encode("name", "123");
            Log.e(TAG, "" + mmkv.decodeString("name"));
            mmkv.remove("name");
            Log.e(TAG, "" + mmkv.decodeString("name"));
            mmkv.clear();
            Log.e(TAG, "" + mmkv.decodeString("name"));
          }
        });

    Button button7 = findViewById(R.id.btn_improt_by_file);
    button7.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            Log.e(TAG, "" + getFilesDir().getParent() + File.separator + "shared_prefs");
            String path = getFilesDir().getParent() + File.separator + "shared_prefs";
            getAllFiles(path, ".xml");
          }
        });
  }

  /**
   * 获取指定目录内所有文件名
   *
   * @param dirPath 需要查询的文件目录
   * @param _type 查询类型，比如mp3什么的
   */
  public static ArrayList<String> getAllFiles(String dirPath, String _type) {
    File f = new File(dirPath);
    if (!f.exists()) { // 判断路径是否存在
      return null;
    }

    File[] files = f.listFiles();

    if (files == null) { // 判断权限
      return null;
    }

    ArrayList<String> fileList = new ArrayList<>();
    for (File _file : files) { // 遍历目录
      if (_file.isFile() && _file.getName().endsWith(_type)) {
        String _name = _file.getName();
        String fileName = _file.getName().substring(0, _name.length() - 4); // 获取文件名
        fileList.add(fileName);
      }
    }
    return fileList;
  }

  /**
   * 删除单个文件
   *
   * @param filePath 被删除文件的文件名
   * @return 文件删除成功返回true，否则返回false
   */
  public boolean deleteFile(String filePath) {
    Log.e(TAG, "filePath == " + filePath);
    File file = new File(filePath);
    if (file.isFile() && file.exists()) {
      Log.e(TAG, "delete");
      return file.delete();
    }
    return false;
  }
}
