package com.gnrc.telehealth.DatabaseSqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;


import com.gnrc.telehealth.Adapter.RvAdapter;
import com.gnrc.telehealth.Model.DataModel;

public class DBhandler extends SQLiteOpenHelper {
    private RvAdapter adapter;

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "SurveyDB.db";

    // below int is our database version
    private static final int DB_VERSION = 4;

    // below variable is for our table name.
    private static final String TABLE_NAME = "family";
    private static final String TABLE_NAME2 = "spinnerstate";
    private static final String TABLE_NAME3 = "spinnerdist";

    // below variable is for our id column.
    private static final String ID_COL = "SSFM_ID";
    private static final String FamilyHead = "SSFM_HEAD_NAME";
    private static final String Phone = "SSFM_CONTACT_NO";
    private static final String House = "SSFM_HOUSE_NO";
    private static final String Address = "SSFM_ADDR";
    private static final String Gaonp = "SSFM_GAON_PNCHYT";
    private static final String Block_code = "SSFM_BLOCK_CODE";
    private static final String City = "SSFM_CITY_CODE";
    private static final String Dist = "SSFM_DIST_CODE";
    private static final String State = "SSFM_STATE_CODE";
    private static final String Pin = "SSFM_PIN";
    private static final String Pinid = "id";
    private static final String Pinvalue = "value";


    // creating a constructor for our database handler.
    public DBhandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // below method is for creating a database by running a sqlite query
    @Override
    public void onCreate(SQLiteDatabase db) {
        // on below line we are creating
        // an sqlite query and we are
        // setting our column names
        // along with their data types.
        String query = "CREATE TABLE " + TABLE_NAME + " (SSFM_ID String primary key, SSFM_HEAD_NAME String, SSFM_CONTACT_NO String, SSFM_HOUSE_NO String, SSFM_ADDR String, SSFM_GAON_PNCHYT String, SSFM_BLOCK_CODE String, SSFM_CITY_CODE String, SSFM_DIST_CODE String, SSFM_STATE_CODE String, SSFM_PIN String) ";
        String query2 = "CREATE TABLE " + TABLE_NAME2 + " (id String primary key, value String) ";
        String query3 = "CREATE TABLE " + TABLE_NAME3 + " (id String primary key, value String) ";


        // at last we are calling a exec sql
        // method to execute above sql query
        db.execSQL(query);
        db.execSQL(query2);
        db.execSQL(query3);

    }

    // this method is use to add new course to our sqlite database.
    public void addnewprod(String id, String familyHead, String phone,String house, String address,
    String gaonp, String block_code,String city, String dist,String state,String pin) {

        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(ID_COL, id);
        values.put(FamilyHead, familyHead);
        values.put(Phone, phone);
        values.put(House, house);
        values.put(Address, address);
        values.put(Gaonp, gaonp);
        values.put(Block_code, block_code);
        values.put(City, city);
        values.put(Dist, dist);
        values.put(State, state);
        values.put(Pin, pin);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TABLE_NAME, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    public void addspinner(String id, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Pinid, id);
        values.put(Pinvalue, value);
        db.insert(TABLE_NAME2, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    public void addspinnerdist(String id, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Pinid, id);
        values.put(Pinvalue, value);
        db.insert(TABLE_NAME3, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    public void update(DataModel dataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ID_COL, dataModel.getId());
        values.put(FamilyHead, dataModel.getFamilyhead());
        values.put(Phone, dataModel.getPhone());
        values.put(House, dataModel.getHouse());
        values.put(Address, dataModel.getAddress());
        values.put(Gaonp, dataModel.getGaon_panchayat());
        values.put(Block_code, dataModel.getBlock_code());
        values.put(City, dataModel.getCity());
        values.put(Dist, dataModel.getDist());
        values.put(State, dataModel.getState());
        values.put(Pin, dataModel.getPin());
        db.update(TABLE_NAME, values, ID_COL + " = ?", new String[]{String.valueOf(dataModel.getId())});
        //String sql = "UPDATE " + TABLE_NAME + " SET count = ? WHERE id = ?;";
        //db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);

        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getspinnerdata() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME2,null);

        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getspinnerdatadist() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME3,null);

        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
}