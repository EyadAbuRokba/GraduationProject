package com.example.eyad.ministryofinteriorproject.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.eyad.ministryofinteriorproject.Model.Fisher;
import java.util.ArrayList;

public class MySQLiteHelper extends SQLiteOpenHelper {

    SQLiteDatabase database;

    public MySQLiteHelper(Context context) {
        super(context, Fisher.DB_NAME, null, 14);
        database = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Fisher.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL("drop table if exists " + Fisher.TABLE_NAME);
        this.onCreate(db);

    }
    public boolean addFisher(Fisher f){
        ContentValues contentValues = new ContentValues();
        contentValues.put(Fisher.COL_ID,f.getId());
        contentValues.put(Fisher.COL_NAME,f.getName());
        contentValues.put(Fisher.COL_GOVERNORATE,f.getGovernorate());
        return database.insert(Fisher.TABLE_NAME,null,contentValues) > 0;
    }

    public ArrayList<Fisher> getAllFishers(){

        ArrayList<Fisher> fisher = new ArrayList<>();
        String sqlQuery = "select * from fishers order by id desc";
        Cursor cursor = database.rawQuery(sqlQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Fisher FisherObject = new Fisher();
                FisherObject.setId(cursor.getString(cursor.getColumnIndex(Fisher.COL_ID)));
                FisherObject.setName(cursor.getString(cursor.getColumnIndex(Fisher.COL_NAME)));
                FisherObject.setGovernorate(cursor.getString(cursor.getColumnIndex(Fisher.COL_GOVERNORATE)));
                fisher.add(FisherObject);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return fisher;
    }


    public boolean deleteFishers(String id) {
        return database.delete(Fisher.TABLE_NAME, "id = ?", new String[]{id}) > 0;
    }

    public void deleteAll() {
        database.execSQL("delete from "+ Fisher.TABLE_NAME);
    }



}
