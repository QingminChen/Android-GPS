package com.certusnet.db;

import java.sql.SQLException;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.certusnet.dao.LocationPoint;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * DBHelper 数据库操作类
 * 
 * @author mawei
 * @version 1.0.0
 */
public class DBHelper extends OrmLiteSqliteOpenHelper {

	/* DBHelper类 */
	private static final String TAG = "DBHelper";
	/* 数据库名称 */
	private static final String DATABASE_NAME = "location.db";
	/* 数据库版本号 */
	private static final int DATABASE_VERSION = 1;
	/* 单例 */
	private static DBHelper instance;

	public DBHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	public static DBHelper getInstance(Context context) {
		if (instance == null)
			instance = new DBHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			/* 创建表 */
			TableUtils.createTable(connectionSource, LocationPoint.class);
		} catch (SQLException e) {
			Log.e(TAG, "创建数据库失败", e);
			e.printStackTrace();
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			/* 更新表 */
			TableUtils.dropTable(connectionSource, LocationPoint.class, true);
			TableUtils.createTable(connectionSource, LocationPoint.class);
			/*
			 * 如果DAO给你的功能不够灵活的话，你也可以发出数据的原生更新语句。例如：
			 */
			// getDao(UserAccount.class).executeRaw("ALTER TABLE T_USER_ACCOUNT DROP COLUMN password");
			// getDao(UserAccount.class).executeRaw("ALTER TABLE T_USER_ACCOUNT ADD COLUMN password");
		} catch (SQLException e) {
			Log.e(TAG, "更新数据库失败", e);
			e.printStackTrace();
		}
	}
}