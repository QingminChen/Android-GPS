package com.certusnet.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.os.Build;

import com.certusnet.LocationApplication;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.UpdateBuilder;

/**
 * DBManager 数据库操作类
 * 
 * @author mawei
 * @version 1.0.0
 */
@SuppressWarnings("unchecked")
public class DBManager<T> {

	/* 获取全局context */
	private static DBHelper helper = DBHelper.getInstance(LocationApplication.getInstance());

	/** 新增一条记录 */
	public int create(Object po) {
		try {
			Dao<Object, Long> dao = (Dao<Object, Long>) helper.getDao(po.getClass());
			return dao.create(po);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public boolean exists(Object po, Map<String, Object> where) {
		try {
			Dao<Object, Long> dao = (Dao<Object, Long>) helper.getDao(po.getClass());
			if (dao.queryForFieldValues(where).size() > 0) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int createIfNotExists(Object po, Map<String, Object> where) {
		try {
			Dao<Object, Long> dao = (Dao<Object, Long>) helper.getDao(po.getClass());
			if (dao.queryForFieldValues(where).size() < 1) {
				return dao.create(po);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** 查询一条记录 */
	public List<T> queryForEq(Class<T> c, String fieldName, Object value) {
		try {
			Dao<T, Long> dao = (Dao<T, Long>) helper.getDao(c);
			return dao.queryForEq(fieldName, value);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}

	/** 删除一条记录 */
	public int remove(Object po) {
		try {
			Dao<Object, Long> dao = (Dao<Object, Long>) helper.getDao(po.getClass());
			return dao.delete(po);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 根据特定条件更新特定字段
	 * 
	 * @param c
	 * @param values
	 * @param columnName
	 *            where字段
	 * @param value
	 *            where值
	 * @return
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public int update(Class<Object> c, ContentValues values, String columnName, Object value) {
		try {
			Dao<Object, Long> dao = (Dao<Object, Long>) helper.getDao(c);
			UpdateBuilder<Object, Long> updateBuilder = dao.updateBuilder();
			updateBuilder.where().eq(columnName, value);
			for (String key : values.keySet()) {
				updateBuilder.updateColumnValue(key, values.get(key));
			}
			return updateBuilder.update();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** 更新一条记录 */
	public int update(Object po) {
		try {
			Dao<Object, Long> dao = (Dao<Object, Long>) helper.getDao(po.getClass());
			return dao.update(po);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/** 查询所有记录 */
	public List<T> queryForAll(Class<T> c) {
		try {
			Dao<T, Long> dao = (Dao<T, Long>) helper.getDao(c);
			return dao.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<T>();
	}
}