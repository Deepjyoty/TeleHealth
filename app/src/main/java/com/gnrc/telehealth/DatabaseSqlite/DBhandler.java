package com.gnrc.telehealth.DatabaseSqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.gnrc.telehealth.Adapter.Family_Head_Adapter;

import java.util.ArrayList;

public class DBhandler extends SQLiteOpenHelper {
    ArrayList<String> addFm;
    private Family_Head_Adapter adapter;

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "SurveyDB.db";

    // below int is our database version
    private static final int DB_VERSION = 4;

    // below variable is for our table name.
    private static final String TBL_STATE_MASTER = "tbl_state_master";
    private static final String TBL_DISTRICT_MASTER = "tbl_district_master";
    private static final String TBL_FAMILY_MASTER = "tbl_family_master";
    private static final String TBL_ADDFAMILY_MASTER = "tbl_family_member";
    private static final String TBL_GENERAL_HABITS_ALCOHOL = "tbl_general_habits_alcohol";
    private static final String TBL_GENERAL_HABITS_SMOKING = "tbl_general_habits_smoking";
    private static final String TBL_TEST_FINDINGS = "tbl_test_findings";
    private static final String TBL_HCI_ATAL_AMRIT = "tbl_hci_atal_amrit";
    private static final String TBL_HCI_AYUSHMAN_BHARAT = "tbl_hci_ayushman_bharat";
    private static final String TBL_COVID_FACTS = "tbl_covid_facts";
    private static final String TBL_SYMPTOMS_MASTER = "tbl_symptoms_master";
    private static final String TBL_SYMPTOMS_MEMBER = "tbl_symptoms_member";
    private static final String TBL_OTHER_INFO_TELEMED = "tbl_other_info_telemed";
    private static final String TBL_OTHER_INFO_OPD = "tbl_other_info_opd";
    private static final String TBL_OTHER_INFO_AMBULANCE = "tbl_other_info_ambulance";


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
        //String query = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " (SSFM_ID String primary key, SSFM_HEAD_NAME String, SSFM_CONTACT_NO String, SSFM_HOUSE_NO String, SSFM_ADDR String, SSFM_GAON_PNCHYT String, SSFM_BLOCK_CODE String, SSFM_CITY_CODE String, SSFM_DIST_CODE String, SSFM_STATE_CODE String, SSFM_PIN String) ";
        String q_create_state_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_STATE_MASTER +
                " (id String primary key, value String) ";
        String q_create_dist_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_DISTRICT_MASTER +
                " (id String primary key, value String) ";
        String q_create_family_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_FAMILY_MASTER +
                " (SSFM_ID String primary key, SSFM_HEAD_NAME String, SSFM_CONTACT_NO String, " +
                "SSFM_HOUSE_NO String, SSFM_ADDR String, SSFM_GAON_PNCHYT String, SSFM_BLOCK_CODE String," +
                " SSFM_CITY_CODE String, SSFM_DIST_CODE String, SSFM_STATE_CODE String, SSFM_PIN String) ";
        String q_create_addfamily_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_ADDFAMILY_MASTER +
                " (SSR_REGN_NUM String primary key, " +
                "SSR_REGN_DATE String,SSR_REGN_STATUS String, SSR_PATIENT_NAME String, SSR_GENDER String, " +
                "SSR_DOB String, SSR_AGE_YR String, SSR_AGE_MN String," +
                "SSR_AGE_DY String, SSR_CONTACT_NO String, SSR_AREA_LOCALITY String, " +
                "SSR_DISTRICT_CD String, SSR_BLOCK_NAME String, SSR_PANCHAYAT_NAME String," +
                "SSR_VILLAGE_NAME String, SSR_CRT_DT String, SSR_CRT_USER_ID String, " +
                "SSR_LST_UPD_DT String, family_id String) ";
        String q_create_gHabits_alcohol_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_GENERAL_HABITS_ALCOHOL +
                " (member_id String primary key, family_id String, value String) ";
        String q_create_gHabits_smoking_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_GENERAL_HABITS_SMOKING +
                " (member_id String primary key, family_id String, value String) ";
        String q_create_test_findings_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_TEST_FINDINGS +
                " (member_id String primary key, family_id String, memberName String, sys String," +
                " dia String, type String, value String) ";
        String q_create_hci_atalAmrit_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_HCI_ATAL_AMRIT +
                " (member_id String primary key, family_id String, value String) ";
        String q_create_hci_ayushmanBharat_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_HCI_AYUSHMAN_BHARAT +
                " (member_id String primary key, family_id String, value String) ";
        String q_create_covid_facts_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_COVID_FACTS +
                " (member_id String primary key, family_id String, name String, covid String, dose String, reason String) ";
        String q_create_symptoms_master_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_SYMPTOMS_MASTER +
                " (ATR_CODE String primary key, PRT_CODE String, PRT_DESC String," +
                " PRT_DESC_ALT String, PRT_DESC_BENG String, PRT_SLNO String, " +
                "ATR_DESC String, ATR_DESC_ALT String, ATR_DESC_BENG String,ATR_SLNO String,Image_URL String) ";
        String q_create_symptoms_member_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_SYMPTOMS_MEMBER +
                " (ATR_CODE String, member_id String, family_id String, name String, PRT_DESC String," +
                "ATR_DESC String, checkState String) ";
        String q_create_other_info_telemed_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_OTHER_INFO_TELEMED +
                " (member_id String primary key, family_id String, value String) ";
        String q_create_other_info_opd_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_OTHER_INFO_OPD +
                " (member_id String primary key, family_id String, value String) ";
        String q_create_other_info_ambulance_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_OTHER_INFO_AMBULANCE +
                " (member_id String primary key, family_id String, value String) ";




        // at last we are calling a exec sql
        // method to execute above sql query
        //db.execSQL(query);
        db.execSQL(q_create_state_tbl);
        db.execSQL(q_create_dist_tbl);
        db.execSQL(q_create_family_tbl);
        db.execSQL(q_create_addfamily_tbl);
        db.execSQL(q_create_gHabits_alcohol_tbl);
        db.execSQL(q_create_gHabits_smoking_tbl);
        db.execSQL(q_create_test_findings_tbl);
        db.execSQL(q_create_hci_atalAmrit_tbl);
        db.execSQL(q_create_hci_ayushmanBharat_tbl);
        db.execSQL(q_create_covid_facts_tbl);
        db.execSQL(q_create_symptoms_master_tbl);
        db.execSQL(q_create_symptoms_member_tbl);
        db.execSQL(q_create_other_info_telemed_tbl);
        db.execSQL(q_create_other_info_opd_tbl);
        db.execSQL(q_create_other_info_ambulance_tbl);

    }
    public void addOtherInfoTelemed(String member_id, String family_id, String name){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", member_id);
        values.put("family_id", family_id);
        values.put("value", name);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_OTHER_INFO_TELEMED, null, values);
        db.close();
    }
    public void addOtherInfoOpd(String member_id, String family_id, String name){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", member_id);
        values.put("family_id", family_id);
        values.put("value", name);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_OTHER_INFO_OPD, null, values);
        db.close();
    }
    public void addOtherInfoAmbulance(String member_id, String family_id, String name){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", member_id);
        values.put("family_id", family_id);
        values.put("value", name);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_OTHER_INFO_AMBULANCE, null, values);
        db.close();
    }
    public void addSymptomsMember( String ATR_CODE, String member_id, String family_id, String name,
                                   String PRT_DESC, String ATR_DESC, String checkState){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("ATR_CODE", ATR_CODE);
        values.put("member_id", member_id);
        values.put("family_id", family_id);
        values.put("name", name);
        values.put("PRT_DESC", PRT_DESC);
        values.put("ATR_DESC", ATR_DESC);
        values.put("checkState", checkState);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_SYMPTOMS_MEMBER, null, values);
        db.close();
    }
    public void addSymptomsMaster(String ATR_CODE, String PRT_CODE, String PRT_DESC, String PRT_DESC_ALT,
                                  String PRT_DESC_BENG, String PRT_SLNO, String ATR_DESC, String ATR_DESC_ALT,
                                  String ATR_DESC_BENG, String ATR_SLNO, String ImageUrl){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("ATR_CODE", ATR_CODE);
        values.put("PRT_CODE", PRT_CODE);
        values.put("PRT_DESC", PRT_DESC);
        values.put("PRT_DESC_ALT", PRT_DESC_ALT);
        values.put("PRT_DESC_BENG", PRT_DESC_BENG);
        values.put("PRT_SLNO", PRT_SLNO);
        values.put("ATR_DESC", ATR_DESC);
        values.put("ATR_DESC_ALT", ATR_DESC_ALT);
        values.put("ATR_DESC_BENG", ATR_DESC_BENG);
        values.put("ATR_SLNO", ATR_SLNO);
        values.put("Image_URL", ImageUrl);
        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_SYMPTOMS_MASTER, null, values);
        db.close();
    }
    public void addCovidFacts(String id, String f_id, String name, String covid, String dose, String reason){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("name", name);
        values.put("covid", covid);
        values.put("dose", dose);
        values.put("reason", reason);
        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_COVID_FACTS, null, values);
        db.close();
    }
    public void addHCIAtalAmrit(String id, String f_id, String value){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("value", value);
        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_HCI_ATAL_AMRIT, null, values);
        db.close();
    }
    public void addHCIAyushmanBharat(String id, String f_id, String value){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("value", value);
        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_HCI_AYUSHMAN_BHARAT, null, values);
        db.close();
    }
    public void addTestFindings(String id, String f_id, String memberName, String sys, String dia, String typeSpinner, String value){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("memberName",memberName);
        values.put("sys",sys);
        values.put("dia",dia);
        values.put("type",typeSpinner);
        values.put("value", value);
        //  db.delete(TBL_GENERAL_HABITS_ALCOHOL,null,null);
        db.insert(TBL_TEST_FINDINGS, null, values);
        db.close();
    }

    public void addGeneralHabitsAlcohol(String id, String f_id, String value){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("value", value);
      //  db.delete(TBL_GENERAL_HABITS_ALCOHOL,null,null);
        db.insert(TBL_GENERAL_HABITS_ALCOHOL, null, values);
        db.close();
    }
    public void addGeneralHabitsSmoking(String id, String f_id, String value){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("value", value);
     //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_GENERAL_HABITS_SMOKING, null, values);
        db.close();
    }
    public void addfamilymember( String regnum, String regDt, String regStatus, String ptName,
                                String gender, String dob,String year, String month,String day,String contact,
                                String areaLocality, String distCode, String blockName, String pancName,
                                String villName,String createDate, String loginId, String updDate,String f_id){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("SSR_REGN_NUM", regnum);
        values.put("SSR_REGN_DATE", regDt);
        values.put("SSR_REGN_STATUS", regStatus);
        values.put("SSR_PATIENT_NAME", ptName);
        values.put("SSR_GENDER", gender);
        values.put("SSR_DOB", dob);
        values.put("SSR_AGE_YR", year);
        values.put("SSR_AGE_MN", month);
        values.put("SSR_AGE_DY", day);
        values.put("SSR_CONTACT_NO", contact);
        values.put("SSR_AREA_LOCALITY", areaLocality);
        values.put("SSR_DISTRICT_CD", distCode);
        values.put("SSR_BLOCK_NAME", blockName);
        values.put("SSR_PANCHAYAT_NAME", pancName);
        values.put("SSR_VILLAGE_NAME", villName);
        values.put("SSR_CRT_DT", createDate);
        values.put("SSR_CRT_USER_ID", loginId);
        values.put("SSR_LST_UPD_DT",updDate);
        values.put("family_id", f_id);

        // after adding all values we are passing
        // content values to our table.
        db.insert(TBL_ADDFAMILY_MASTER, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
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
        //db.insert(TBL_ADDFAMILY_MASTER, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    public void addFamily_head_db(String id, String familyHead, String phone, String house, String address,
                                  String gaonp, String block_code, String city, String dist, String state, String pin) {

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
/*        values.put(Pinid, id2);
        values.put(Pinvalue, value);*/

        // after adding all values we are passing
        // content values to our table.
        /*db.replace(TABLE_NAME4,null,values2);*/
        /*String queryreplace = "INSERT OR REPLACE INTO " + TABLE_NAME4 + " (SSFM_STATE_CODE , SSFM_DIST_CODE) VALUES ( " + Dist + " , " + dist + " ) ";
        db.execSQL(queryreplace);*/
        db.insert(TBL_FAMILY_MASTER, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    //Inserting State data to Sqlite table
    public void setStateDb(String id, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Pinid, id);
        values.put(Pinvalue, value);
        db.insert(TBL_STATE_MASTER, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    //Inserting District data to sqlite table
    public void setDistDb(String id, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Pinid, id);
        values.put(Pinvalue, value);
        db.insert(TBL_DISTRICT_MASTER, null, values);
        db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
/*    public void update(Family_Head_Model dataModel) {
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
    }*/

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        //db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
/*    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }*/
    //Fetching data from State table
    public Cursor getStateDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_STATE_MASTER,null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    //Fetching data from District table
    public Cursor getDistDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_DISTRICT_MASTER,null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    //Fetching data from Family table
    public Cursor getFamilyDbData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_FAMILY_MASTER,null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getFamilyMemberList(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_ADDFAMILY_MASTER + " where family_id = '" + familyId +"'",null);
        Log.d("val1", "getFamilyMemberList: " + familyId);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getGeneralHabitsAlcohol(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_GENERAL_HABITS_ALCOHOL + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getGeneralHabitsSmoking(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_GENERAL_HABITS_SMOKING + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getTestFindings(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_TEST_FINDINGS + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getHCIAtalAmrit(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_HCI_ATAL_AMRIT + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getHCIAyushmanBharat(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_HCI_AYUSHMAN_BHARAT + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getCovidFacts(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_COVID_FACTS + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getSymptomsMaster(String prt_code) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_SYMPTOMS_MASTER + " where PRT_CODE = '" + prt_code +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getSymptomsMember(String memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_SYMPTOMS_MEMBER + " where member_id = '" + memberId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getOtherInfoTelemed(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_OTHER_INFO_TELEMED + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getOtherInfoOpd(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_OTHER_INFO_OPD + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getOtherInfoAmbulance(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_OTHER_INFO_AMBULANCE + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
}