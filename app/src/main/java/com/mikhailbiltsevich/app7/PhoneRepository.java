package com.mikhailbiltsevich.app7;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class PhoneRepository {

    private final static  String[] mFields = new String[]{
            "_id",
            "firstname",
            "surname",
            "patronymic",
            "address",
            "time_long_distance_calls",
            "time_city_calls",
            "credit_card",
            "debit"
    };

    public static Cursor getPhonesCursor(SQLiteDatabase db){
        Cursor cursor = db.query("PHONE", mFields, null, null, null, null, "surname, firstname, patronymic");

        return cursor;
    }

    public static void getPhoneNames(SQLiteDatabase db, List<String> names){
        Cursor cursor = db.query(true, "PHONE", new String[]{mFields[0], mFields[1]}, null, null, mFields[1], null, "firstname", null);

        names.clear();
        if(cursor.moveToFirst()){
            do {
                names.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        cursor.close();
    }

    public static Phone getPhoneById(SQLiteDatabase db, long id){
        Cursor cursor = db.query("PHONE", mFields, "_id = ?", new String[]{Long.toString(id)}, null, null, null, null);

        Phone phone = null;

        if(cursor.moveToFirst()){
            phone = new Phone(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getDouble(5), cursor.getDouble(6), cursor.getString(7), cursor.getDouble(8));
        }

        cursor.close();

        return phone;
    }

    public static Cursor getPhonesCursorByTimeLongDistanceCalls(SQLiteDatabase db, double value){
        Cursor cursor = db.query("PHONE", mFields, "time_long_distance_calls > ?", new String[]{Double.toString(value)}, null, null, null, null);

        return cursor;
    }

    public static Cursor getPhonesCursorByDebit(SQLiteDatabase db, double value) {
        Cursor cursor = db.query("PHONE", mFields, "debit < ?", new String[]{Double.toString(value)}, null, null, null, null);

        return cursor;
    }

    public static Cursor getPhonesCursorByTimeCityCalls(SQLiteDatabase db, double value){
        Cursor cursor = db.query("PHONE", mFields, "time_city_calls > ?", new String[]{Double.toString(value)}, null, null, null, null);

        return cursor;
    }

    public static Cursor getPhonesCursorByAddress(SQLiteDatabase db, String value){
        Cursor cursor = db.query("PHONE", mFields, "address like ?", new String[]{"%" + value + "%"}, null, null, null, null);

        return cursor;
    }
}