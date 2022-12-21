package com.gnrc.telehealth.DatabaseSqlite;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
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
    /*private static final String TBL_GENERAL_HABITS_SMOKING = "tbl_general_habits_smoking";*/
    private static final String TBL_TEST_FINDINGS = "tbl_test_findings";
    private static final String TBL_HCI_ATAL_AMRIT = "tbl_hci_atal_amrit";
    private static final String TBL_HCI_AYUSHMAN_BHARAT = "tbl_hci_ayushman_bharat";
    private static final String TBL_COVID_FACTS = "tbl_covid_facts";
    private static final String TBL_SYMPTOMS_MASTER = "tbl_symptoms_master";
    private static final String TBL_SYMPTOMS_MEMBER = "tbl_symptoms_member";
    private static final String TBL_OTHER_INFO = "tbl_other_info";
    private static final String TBL_VIDEO_STORE = "tbl_video_store";
    private static final String TBL_OVERALL_FLAG = "tbl_overall_flag";
    private static final String TBL_SURVEY_TYPE_FLAG = "tbl_survey_type_flag";


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
                " (group_surveyid String, member_id String primary key, family_id String, " +
                "member_name String, smoking String, alcohol String," +
                "memberSurvey_id String, latitude String, longtitude String, timeStamp String) ";



        String q_create_test_findings_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_TEST_FINDINGS +
                " (group_surveyid String, member_id String primary key, family_id String, " +
                "member_name String, sys String," +
                " dia String, type String, value String, memberSurvey_id String, timeStamp String) ";


        String q_create_hci_atalAmrit_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_HCI_ATAL_AMRIT +
                " (group_surveyid String, member_id String primary key, family_id String," +
                " member_name String, atal_amrit String, ayushman_bharat String," +
                "memberSurvey_id String, timeStamp String) ";


       /* String q_create_covid_facts_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_COVID_FACTS +
                " (member_id String primary key, family_id String, name String, covid String, dose String, reason String) ";*/
        String q_create_symptoms_master_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_SYMPTOMS_MASTER +
                " (ATR_CODE String primary key, PRT_CODE String, PRT_DESC String," +
                " PRT_DESC_ALT String, PRT_DESC_BENG String, PRT_SLNO String, " +
                "ATR_DESC String, ATR_DESC_ALT String, ATR_DESC_BENG String,ATR_SLNO String,Image_URL String) ";


        String q_create_symptoms_member_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_SYMPTOMS_MEMBER +
                " (group_surveyid String, ATR_CODE String, member_id String, family_id String," +
                " member_name String, PRT_DESC String," +
                "ATR_DESC String, checkState String, memberSurvey_id String, timeStamp String) ";


        String q_create_other_info_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_OTHER_INFO +
                " (group_surveyid String, member_id String primary key, family_id String," +
                " member_name String, memberSurvey_id String, " +
                "telemedicine_booked String, opd_booked String, ambulance_booked String, timeStamp String) ";


        String q_create_video_store_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_VIDEO_STORE +
                " (group_surveyid String primary key,family_id String,  video_path String, timeStamp String) ";

        String q_create_overall_flag_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_OVERALL_FLAG +
                " (group_surveyid String primary key,  tbl_general_habits_alcohol INTEGER, tbl_symptoms_member INTEGER," +
                "tbl_test_findings INTEGER, tbl_hci_atal_amrit INTEGER, tbl_other_info INTEGER, " +
                "tbl_video_store INTEGER, final_save INTEGER) ";

        String q_create_survey_type_tbl = "CREATE TABLE IF NOT EXISTS " + TBL_SURVEY_TYPE_FLAG +
                " (group_surveyid String , family_id String, member_id String, survey_type String) ";


        // at last we are calling a exec sql
        // method to execute above sql query
        //db.execSQL(query);
        db.execSQL(q_create_state_tbl);
        db.execSQL(q_create_dist_tbl);
        db.execSQL(q_create_family_tbl);
        db.execSQL(q_create_addfamily_tbl);
        db.execSQL(q_create_gHabits_alcohol_tbl);
        db.execSQL(q_create_test_findings_tbl);
        db.execSQL(q_create_hci_atalAmrit_tbl);

        db.execSQL(q_create_symptoms_master_tbl);
        db.execSQL(q_create_symptoms_member_tbl);
        db.execSQL(q_create_other_info_tbl);
        db.execSQL(q_create_video_store_tbl);
        db.execSQL(q_create_overall_flag_tbl);
        db.execSQL(q_create_survey_type_tbl);

    }

    public void addSurveyTypeFlag(String group_surveyid, String family_id, String member_id, String survey_type ){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("family_id", family_id);
        values.put("member_id", member_id);
        values.put("survey_type", survey_type);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_SURVEY_TYPE_FLAG, null, values);
        //db.close();
    }

    public void addOverallFlag(String group_surveyid, int tbl_general_habits_alcohol, int tbl_symptoms_member,
                               int tbl_test_findings, int tbl_hci_atal_amrit, int tbl_other_info,
                               int tbl_video_store, int final_save){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("tbl_general_habits_alcohol", tbl_general_habits_alcohol);
        values.put("tbl_symptoms_member", tbl_symptoms_member);
        values.put("tbl_test_findings", tbl_test_findings);
        values.put("tbl_hci_atal_amrit", tbl_hci_atal_amrit);
        values.put("tbl_other_info", tbl_other_info);
        values.put("tbl_video_store", tbl_video_store);
        values.put("final_save", final_save);


        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_OVERALL_FLAG, null, values);
        //db.close();
    }

    public void addVideoPath(String group_surveyid, String family_id, Uri video_path, String timeStamp  ){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("family_id", family_id);
        values.put("video_path", String.valueOf(video_path));
        values.put("timeStamp", timeStamp);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_VIDEO_STORE, null, values);
        //db.close();
    }
    public void addOtherInfo(String group_surveyid, String member_id, String family_id, String name,String telemedicine,
                             String opd, String ambulance, String memberSurvey_id, String timeStamp  ){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("member_id", member_id);
        values.put("family_id", family_id);
        values.put("member_name", name);
        values.put("telemedicine_booked", telemedicine);
        values.put("opd_booked", opd);
        values.put("ambulance_booked", ambulance);
        values.put("memberSurvey_id", memberSurvey_id);
        values.put("timeStamp", timeStamp);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_OTHER_INFO, null, values);
        //db.close();
    }

    public void addSymptomsMember( String group_surveyid, String ATR_CODE, String member_id, String family_id, String name,
                                   String PRT_DESC, String ATR_DESC, String checkState,String memberSurvey_id ,
                                   String timeStamp){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("ATR_CODE", ATR_CODE);
        values.put("member_id", member_id);
        values.put("family_id", family_id);
        values.put("member_name", name);
        values.put("PRT_DESC", PRT_DESC);
        values.put("ATR_DESC", ATR_DESC);
        values.put("checkState", checkState);
        values.put("memberSurvey_id", memberSurvey_id);
        values.put("timeStamp", timeStamp);

        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_SYMPTOMS_MEMBER, null, values);
        //db.close();
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
        //db.close();
    }
/*    public void addCovidFacts(String id, String f_id, String name, String covid, String dose, String reason){
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
        //db.close();
    }*/
    public void addHCI(String group_surveyid, String id, String f_id, String value, String atal_amrit,
                       String ayushman_bharat, String memberSurvey_id , String timeStamp){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("member_name", value);
        values.put("atal_amrit", atal_amrit);
        values.put("ayushman_bharat", ayushman_bharat);
        values.put("memberSurvey_id", memberSurvey_id);
        values.put("timeStamp", timeStamp);
        //   db.delete(TBL_GENERAL_HABITS_SMOKING,null,null);
        db.insert(TBL_HCI_ATAL_AMRIT, null, values);
        //db.close();
    }

    public void addTestFindings(String group_surveyid, String id, String f_id, String memberName, String sys, String dia,
                                String typeSpinner, String value, String memberSurvey_id , String timeStamp){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("member_name",memberName);
        values.put("sys",sys);
        values.put("dia",dia);
        values.put("type",typeSpinner);
        values.put("value", value);
        values.put("memberSurvey_id", memberSurvey_id);
        values.put("timeStamp", timeStamp);
        //  db.delete(TBL_GENERAL_HABITS_ALCOHOL,null,null);
        db.insert(TBL_TEST_FINDINGS, null, values);
        //db.close();
    }

    public void addGeneralHabitsAlcohol( String group_surveyid, String id, String f_id, String value, String smoking,
                                         String alcohol,String memberSurvey_id ,String  latitude ,String longtitude ,
                                         String timeStamp){
        // on below line we are creating a variable for
        // our sqlite database and calling writable method
        // as we are writing data in our database.
        SQLiteDatabase db = this.getWritableDatabase();
        // on below line we are creating a
        // variable for content values.
        ContentValues values = new ContentValues();
        // on below line we are passing all values
        // along with its key and value pair.
        values.put("group_surveyid", group_surveyid);
        values.put("member_id", id);
        values.put("family_id", f_id);
        values.put("member_name", value);
        values.put("smoking", smoking);
        values.put("alcohol", alcohol);
        values.put("memberSurvey_id", memberSurvey_id);
        values.put("latitude", latitude);
        values.put("longtitude", longtitude);
        values.put("timeStamp", timeStamp);
      //  db.delete(TBL_GENERAL_HABITS_ALCOHOL,null,null);
        db.insert(TBL_GENERAL_HABITS_ALCOHOL, null, values);
        //db.close();
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
        //db.close();
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
        //db.close();
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
        db.insert(TBL_FAMILY_MASTER, null, values);
        //db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    //Inserting State data to Sqlite table
    public void setStateDb(String id, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Pinid, id);
        values.put(Pinvalue, value);
        db.insert(TBL_STATE_MASTER, null, values);
        //db.close();
        Log.d("strrrrr", "Inserted Successfully" + values );
    }
    //Inserting District data to sqlite table
    public void setDistDb(String id, String value) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Pinid, id);
        values.put(Pinvalue, value);
        db.insert(TBL_DISTRICT_MASTER, null, values);
        //db.close();
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

    public Cursor getFamilyMemberList(String familyId,String memberIdList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_ADDFAMILY_MASTER + " where family_id = '" + familyId +"'"
                + " and SSR_REGN_NUM in (" + memberIdList  +")",null);
        Log.d("val1", "getFamilyMemberList: " + familyId);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getFamilyMemberListWithoutMember(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_ADDFAMILY_MASTER + " where family_id = '" + familyId +"'",null);
        Log.d("val1", "getFamilyMemberList: " + familyId);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getGeneralHabitsAlcoholByMember(String group_surveyid) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select smoking, alcohol from "+ TBL_GENERAL_HABITS_ALCOHOL +
                " where group_surveyid = '" + group_surveyid +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getGeneralHabitsAlcohol(String group_surveyid,String memberIdList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_GENERAL_HABITS_ALCOHOL + " where group_surveyid = '" + group_surveyid +"'"
                + " and member_id in (" + memberIdList  +")",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getTestFindings(String groupSurveyID,String memberIdList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_TEST_FINDINGS +
                " where group_surveyid = '" + groupSurveyID +"'" + " and member_id in (" + memberIdList  +")",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getHCIAtalAmritByMember(String groupSurveyID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select atal_amrit, ayushman_bharat from "+ TBL_HCI_ATAL_AMRIT +
                " where group_surveyid = '" + groupSurveyID +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getHCIAtalAmrit(String groupSurveyID,String memberIdList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_HCI_ATAL_AMRIT +
                " where group_surveyid = '" + groupSurveyID + "'" + " and member_id in (" + memberIdList  +")",null);
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

    public Cursor getSymptomsMemberByFamilyId(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_SYMPTOMS_MEMBER + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getSymptomsMember(String memberId,String memberIdList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_SYMPTOMS_MEMBER + " where member_id = '" + memberId +"'"
                + " and member_id in (" + memberIdList  +")",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getOtherInfoByMember(String groupSurveyID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select telemedicine_booked, opd_booked, ambulance_booked from "+
                TBL_OTHER_INFO + " where group_surveyid = '" + groupSurveyID +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getOtherInfo(String groupSurveyID,String memberIdList) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_OTHER_INFO +
                " where group_surveyid = '" + groupSurveyID +"'"+ " and member_id in (" + memberIdList  +")",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getOverallFlag() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_OVERALL_FLAG,null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getSurveyTypeFlag() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_SURVEY_TYPE_FLAG,null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }

    public Cursor getVideoPath(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_VIDEO_STORE + " where family_id = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getVideoPathAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+ TBL_VIDEO_STORE,null);        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getFamilyMasterNameAddress(String familyId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select SSFM_HEAD_NAME, SSFM_ADDR  from "+ TBL_FAMILY_MASTER + " where SSFM_ID = '" + familyId +"'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
    public Cursor getSurveyTypeFlag2(String memberId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select group_surveyid from tbl_survey_type_flag where member_id  = " +
                "'" + memberId + "'",null);
        //Cursor res = db.rawQuery("delete from "+TABLE_NAME,null);
        return res;
    }
}