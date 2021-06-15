package com.test.testsql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public class SQLiteDataBaseHelper extends SQLiteOpenHelper {

    String TableName;

    //建構子
    public SQLiteDataBaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version,String TableName) {
        super(context, name, factory, version);
        this.TableName = TableName;
    }


    //sqlite 主要是自己打sql語法or他提供的方式輸入crud
    /*欄位為TEXT的話就是一般字串格式
        其他還有
        INTEGER ： 整數，對應Java 的byte、short、int 和long
        REAL ： 小數，對應Java 的float 和double
        另外經過實測
        TEXT欄位可以放JSON陣列！！！！！
    */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQLTable = "CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "Name TEXT, " +
                "Phone TEXT," +
                "Hobby TEXT," +
                "ElseInfo TEXT" +
                ");";
        db.execSQL(SQLTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        final String SQL = "DROP TABLE " + TableName;
        db.execSQL(SQL);
    }

    /*如何取得資料表資訊以及新增資料表
    在Android內，因為SQLite通常不會存放巨量資料
    因此這種操作相對少見*/
    //檢查資料表狀態，若無指定資料表則新增
    public void chickTable(){
//        getWritableDatabase().rawQuery()
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + TableName + "'", null);
        if (cursor != null) {
            if (cursor.getCount() == 0)
                getWritableDatabase().execSQL("CREATE TABLE IF NOT EXISTS " + TableName + "( " +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "Name TEXT, " +
                        "Phone TEXT," +
                        "Hobby TEXT," +
                        "ElseInfo TEXT" +
                        ");");
            cursor.close();
        }
    }

    //取得有多少資料表,並以陣列回傳
    public ArrayList<String> getTables(){
        Cursor cursor = getWritableDatabase().rawQuery(
                "select DISTINCT tbl_name from sqlite_master", null);
        ArrayList<String> tables = new ArrayList<>();
        while (cursor.moveToNext()){
            String getTab = new String (cursor.getBlob(0));
            if (getTab.contains("android_metadata")){}
            else if (getTab.contains("sqlite_sequence")){}
            else tables.add(getTab.substring(0,getTab.length()-1));

        }
        return tables;
    }

    //新增資料
    public void addData(String name, String phone, String hobby, String elseInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Phone", phone);
        values.put("Hobby", hobby);
        values.put("ElseInfo", elseInfo);
        db.insert(TableName, null, values);
//        db.insert()

//        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("");//這邊寫入SQL語法
    }

    //顯示所有資料
    public ArrayList<HashMap<String, String>> showAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName, null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String hobby = c.getString(3);
            String elseInfo = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("hobby", hobby);
            hashMap.put("elseInfo", elseInfo);
            arrayList.add(hashMap);
        }
        return arrayList;

    }

    //以id搜尋特定資料
    public ArrayList<HashMap<String,String>> searchById(String getId){
        SQLiteDatabase db = getReadableDatabase();

        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE _id =" + "'" + getId + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String hobby = c.getString(3);
            String elseInfo = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("hobby", hobby);
            hashMap.put("elseInfo", elseInfo);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //以興趣篩選資料
    public ArrayList<HashMap<String, String>> searchByHobby(String getHobby) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM " + TableName
                + " WHERE Hobby =" + "'" + getHobby + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String id = c.getString(0);
            String name = c.getString(1);
            String phone = c.getString(2);
            String hobby = c.getString(3);
            String elseInfo = c.getString(4);

            hashMap.put("id", id);
            hashMap.put("name", name);
            hashMap.put("phone", phone);
            hashMap.put("hobby", hobby);
            hashMap.put("elseInfo", elseInfo);
            arrayList.add(hashMap);
        }
        return arrayList;
    }

    //修改資料(麻煩)
    public void modify(String id, String name, String phone, String hobby, String elseInfo) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(" UPDATE " + TableName
                + " SET Name=" + "'" + name + "',"
                + "Phone=" + "'" + phone + "',"
                + "Hobby=" + "'" + hobby + "',"
                + "ElseInfo=" + "'" + elseInfo + "'"
                + " WHERE _id=" + "'" + id + "'");
    }

    //修改資料(簡單)
    public void modifyEZ(String id, String name, String phone, String hobby, String elseInfo) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("Name", name);
        values.put("Phone", phone);
        values.put("Hobby", hobby);
        values.put("ElseInfo", elseInfo);
        db.update(TableName, values, "_id = " + id, null);
    }

    //刪除全部資料
    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM"+TableName);
    }

    //以id刪除資料(簡單)
    public void deleteByIdEZ(String id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TableName,"_id = " + id,null);
    }
}
