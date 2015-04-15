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
	 * 数据库名
	 */
	public static final String DB_NAME = "hanashi_weather";

	/*
	 * 数据库版本
	 */
	public static final int VERSION = 1;

	private static HanashiWeatherDB hanashiWeatherDB;

	private SQLiteDatabase db;

	/*
	 * 构造方法私有化
	 */
	private HanashiWeatherDB(Context context) {
		HanashiWeatherOpenHelper dbHelper = new HanashiWeatherOpenHelper(
				context, DB_NAME, null, VERSION);
		db = dbHelper.getWritableDatabase();
	}

	/*
	 * 获取HanashiWeatherDB的实例
	 */
	public synchronized static HanashiWeatherDB getInstance(Context context) {
		if (hanashiWeatherDB == null) {
			hanashiWeatherDB = new HanashiWeatherDB(context);
		}
		return hanashiWeatherDB;
	}

	/*
	 * 将province保存到数据库
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
	 * 从数据库读取全国所有的省份信息
	 */
	public List<Province> loadProvinces() {// 读取省份的方法
		List<Province> list = new ArrayList<Province>();// 新建一个List
		Cursor cursor = db
				.query("Province", null, null, null, null, null, null);// 声明一个指标
		if (cursor.moveToFirst()) {// 如果指标在第一个位置
			do {
				Province province = new Province();// 声明一个province
				province.setId(cursor.getInt(cursor.getColumnIndex("id")));// 这个province的ID，名字，编号，分别指定为，指标所在行的对应数据；
				province.setProvinceName(cursor.getString(cursor
						.getColumnIndex("province_name")));//
				province.setProvinceCode(cursor.getString(cursor
						.getColumnIndex("province_code")));
				list.add(province);// 把province递交到List
			} while (cursor.moveToNext());
		}
		return list;
	}

	/*
	 * 将City保存到数据库
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
	 * 从数据库读取某省份下所有城市的信息
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
	 * 将county保存到数据库
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
	 * 从数据库读取某城市下所有县的信息
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
