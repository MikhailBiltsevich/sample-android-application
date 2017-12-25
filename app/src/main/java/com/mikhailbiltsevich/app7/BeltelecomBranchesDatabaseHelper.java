package com.mikhailbiltsevich.app7;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BeltelecomBranchesDatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "beltelecomBranches";
    private static final int DB_VERSION = 1;

    public BeltelecomBranchesDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE BELTELECON_BRANCH ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "name TEXT NOT NULL, "
                + "address TEXT NOT NULL, "
                + "latitude REAL NOT NULL, "
                + "longitude REAL NOT NULL)");

        insertBeltelecomBranch(db, new BeltelecomBranch("Белтелеком РУП Гомельский Филиал", "пр Ленина 1, Гомель, 246050, Гомель", 52.3929655, 31.0062932));
        insertBeltelecomBranch(db, new BeltelecomBranch("Сервисный центр Гомельского района", "г. Гомель, ул. Склезнёва, 3", 52.389047,31.0283921));
        insertBeltelecomBranch(db, new BeltelecomBranch("Сервисный центр Добрушского района", "г. Добруш, ул.Комарова, 5", 52.420822, 31.311524));
        insertBeltelecomBranch(db, new BeltelecomBranch("Сервисный центр Ветковского района", "г. Ветка, ул. Советская, 4", 52.559986, 31.173490));
    }

    private ContentValues ParseBranchToContentValues(BeltelecomBranch branch){
        ContentValues values = new ContentValues();
        values.put("name", branch.getName());
        values.put("address", branch.getAddress());
        values.put("latitude", branch.getLatitude());
        values.put("longitude", branch.getLongitude());

        return values;
    }

    private void insertBeltelecomBranch(SQLiteDatabase db, BeltelecomBranch branch){
        db.insert("BELTELECON_BRANCH", null, ParseBranchToContentValues(branch));
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}