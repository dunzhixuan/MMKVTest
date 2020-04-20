package com.dunzhixuan.mmkvtest;

import android.app.Application;
import android.content.Context;

import com.tencent.mmkv.MMKV;

import java.lang.reflect.InvocationTargetException;

import dalvik.system.DexFile;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		String rootDir = MMKV.initialize(this);
		System.out.println("mmkv root: " + rootDir);
	}

	private void importDataFromSP(){
		try {
			Context context = (Context) Class.forName("android.content.Context").getConstructor().newInstance();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

	}
}
