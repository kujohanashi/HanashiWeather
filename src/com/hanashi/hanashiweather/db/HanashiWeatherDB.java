package com.hanashi.hanashiweather.db;

import java.util.ArrayList;
import java.util.List;

import com.hanashi.hanashiweather.model.City;
import com.hanashi.hanashiweather.model.County;
import com.hanashi.hanashiweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HanashiWeatherDB {

	/*
	 * ���ݿ���
	 */
	public static final String DB_NAME = "hanashi_weather";

	/*
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;

	private static HanashiWeatherDB hanashiWeatherDB;

	private SQLiteDatabase db;

	/*
	 * ���췽��˽�л�
	 */
	private HanashiWeatherDB(Context context) {
		HanashiWeatherOpenHelper dbHelper = new HanashiWeatherOpenHelper(
				context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/*
	 * ��ȡHanashiWeatherDB��ʵ��
	 */
	public synchronized static HanashiWeatherDB getInstance(Context context) {
		if (hanashiWeatherDB == null) {
			hanashiWeatherDB = new HanashiWeatherDB(context);
		}
		return hanashiWeatherDB;
	}

	/*
	 * ��province���浽���ݿ�
	 */
	public void saveProvince(Province province) {
		if (province != null) {
			ContentValues values = new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}

	/*
	 * �����ݿ��ȡȫ�����е�ʡ����Ϣ
	 */
	public List<Province> loadProvinces() {// ��ȡʡ�ݵķ���
		List<Province> list = new ArrayList<Province>();// �½�һ��List
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);// ����һ��ָ��
		if (cursor.moveToFirst()) {// ���ָ���ڵ�һ��λ��
			do {
				Province province = new Province();// ����һ��province
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));// ���province��ID�����֣���ţ��ֱ�ָ��Ϊ��ָ�������еĶ�Ӧ���ݣ�
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));//
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);// ��province�ݽ���List
			} while (cursor.moveToNext());
		}
		return list;
	}

	/*
	 * ��City���浽���ݿ�
	 */
	public void saveCity(City city) {
		if (city != null) {
			ContentValues values = new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}

	/*
	 * �����ݿ��ȡĳʡ�������г��е���Ϣ
	 */
	public List<City> loadCities(int ProvinceId) {
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?",
				new String[] { String.valueOf(ProvinceId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				City city = new City();
				city.setId(cursor.getInt(cursor.getColumnIndex("id")));
				city.setCityName(cursor.getString(cursor
						.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor
						.getColumnIndex("city_code")));
				city.setProvinceId(ProvinceId);
				list.add(city);
			} while (cursor.moveToNext());
		}
		return list;
	}

	/*
	 * ��county���浽���ݿ�
	 */
	public void saveCounty(County county) {
		if (county != null) {
			ContentValues values = new ContentValues();
			values.put("county_name", county.getCountyName());
			values.put("county_code", county.getCountyCode());
			values.put("city_id", county.getCityId());
			db.insert("County", null, values);
		}
	}

	/*
	 * �����ݿ��ȡĳ�����������ص���Ϣ
	 */
	public List<County> loadCounties(int cityId) {
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?",
				new String[] { String.valueOf(cityId) }, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				County county = new County();
				county.setId(cursor.getInt(cursor.getColumnIndex("id")));
				county.setCountyName(cursor.getString(cursor
						.getColumnIndex("county_name")));
				county.setCountyCode(cursor.getString(cursor
						.getColumnIndex("county_code")));
				county.setCityId(cityId);
				list.add(county);
			} while (cursor.moveToNext());
		}
		return list;
	}
}
