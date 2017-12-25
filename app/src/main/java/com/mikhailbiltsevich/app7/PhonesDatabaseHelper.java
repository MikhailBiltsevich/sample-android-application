package com.mikhailbiltsevich.app7;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PhonesDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "phones";
    private static final int DB_VERSION = 1;

    public PhonesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public static ContentValues convertPhoneToContentValues(Phone phone){
        ContentValues values = new ContentValues();
        values.put("firstname", phone.getFirstname());
        values.put("surname", phone.getSurname());
        values.put("patronymic", phone.getPatronymic());
        values.put("address", phone.getAddress());
        values.put("credit_card", phone.getCreditCard());
        values.put("time_long_distance_calls", phone.getTimeLongDistanceCalls());
        values.put("time_city_calls", phone.getTimeCityCalls());
        values.put("debit", phone.getDebit());

        return values;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE PHONE ("
                                    + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                                    + "firstname TEXT NOT NULL, "
                                    + "surname TEXT NOT NULL, "
                                    + "patronymic TEXT NOT NULL, "
                                    + "address TEXT NOT NULL, "
                                    + "credit_card TEXT NOT NULL, "
                                    + "time_long_distance_calls REAL NOT NULL, "
                                    + "time_city_calls REAL NOT NULL, "
                                    + "debit REAL NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
