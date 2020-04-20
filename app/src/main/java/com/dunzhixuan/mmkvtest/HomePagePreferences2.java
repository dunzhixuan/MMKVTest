// This codes are generated automatically. Do not modify!
package com.dunzhixuan.mmkvtest;

import android.content.Context;
import android.content.SharedPreferences;

import com.dunzhixuan.mmkvtest.Person;
import com.tencent.mmkv.MMKV;

public class HomePagePreferences2 {
	private static String FILE_NAME = "home_page";

	private static MMKV mmkv = MMKV.mmkvWithID(FILE_NAME);

	public static String getPreferencesFileName() {
		return FILE_NAME;
	}

	public static void setPreferencesFileName(String name) {
		FILE_NAME = name;
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
	}

	/**
	 * 是否迁移过
	 */
	private static boolean isImport() {
		return mmkv.decodeBool("improtDataFromSharedPreference", false);
	}

	/**
	 * 迁移
	 */
	private static void importFromSP(Context context) {
		SharedPreferences sharedPreferences = getPreferences(context);
		mmkv.importFromSharedPreferences(sharedPreferences);
		sharedPreferences.edit().clear().apply();
		mmkv.encode("improtDataFromSharedPreference", true);
	}

	/**
	 * 全部功能列表
	 */
	public static String getFunctionList(Context context) {
		if (!isImport()) {
			importFromSP(context);
		}
		return mmkv.decodeString("functionList", "");
	}

	/**
	 * 全部功能列表
	 */
	public static void setFunctionList(Context context, String value) {
		if (!isImport()) {
			importFromSP(context);
		}
		mmkv.encode("functionList", value);
	}

	/**
	 * 全部功能列表
	 */
	public static void removeFunctionList(Context context){
		if (!isImport()) {
			importFromSP(context);
		}
		mmkv.remove("functionList");
	}

	public static Person getPerson(Context context){
		return mmkv.decodeParcelable("person",null);
	}

	public static void setPerson(Context context, Person person){
		mmkv.encode("person",person);
	}
}
