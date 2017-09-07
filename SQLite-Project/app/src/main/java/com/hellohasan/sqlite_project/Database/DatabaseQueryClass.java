package com.hellohasan.sqlite_project.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.hellohasan.sqlite_project.Config;
import com.hellohasan.sqlite_project.Student;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;


public class DatabaseQueryClass {

    private SQLiteDatabase sqLiteDatabase;
    private DatabaseHandler databaseHandler;
    private Context context;

    public DatabaseQueryClass(Context context){
        databaseHandler = new DatabaseHandler(context);
        this.context = context;
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    private void open() throws SQLException {
        sqLiteDatabase = databaseHandler.getWritableDatabase();
    }

    private void close() {
        databaseHandler.close();
    }

    public void insertStudent(String name, long registrationNum, String phone, String email){
        open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(Config.COLUMN_STUDENT_NAME, name);
        contentValues.put(Config.COLUMN_STUDENT_REGISTRATION, registrationNum);
        contentValues.put(Config.COLUMN_STUDENT_PHONE, phone);
        contentValues.put(Config.COLUMN_STUDENT_EMAIL, email);
        sqLiteDatabase.insert(Config.TABLE_STUDENT, null, contentValues);

        sqLiteDatabase.close();
        close();
    }

    public List<Student> getAllStudent(){
        open();

        String SELECT_QUERY = String.format("SELECT %s, %s FROM %s", Config.COLUMN_STUDENT_NAME, Config.COLUMN_REGISTRATION_NUMBER, Config.TABLE_STUDENT);

        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
        if(cursor!=null)
            if(cursor.moveToFirst()){
                List<Student> studentList = new ArrayList<>();
                do {
                    String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_NAME));
                    long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_STUDENT_REGISTRATION));
                    studentList.add(new Student(name, registrationNumber, "", ""));
                }   while (cursor.moveToNext());
                cursor.close();
                close();
                return studentList;
            }

        return null;
    }

    public Student getStudentByRegNum(long registrationNum){
        open();

//        String SELECT_QUERY = "SELECT * FROM " + Config.TABLE_STUDENT + " WHERE " + Config.COLUMN_STUDENT_REGISTRATION + " = " + registrationNum;
        String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION, String.valueOf(registrationNum));

        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);

        if(cursor!=null)
            if(cursor.moveToFirst()){
                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_NAME));
                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_STUDENT_REGISTRATION));
                String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_PHONE));
                String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_EMAIL));
                cursor.close();

                return new Student(name, registrationNumber, phone, email);
            }
        close();

        return null;
    }

    public void deleteStudentByRegNum(long registrationNum) {
        open();
        sqLiteDatabase.delete(Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION + " = ? ", new String[]{ String.valueOf(registrationNum)});
        sqLiteDatabase.close();
        close();

        deleteAllSubjectsByRegNum(registrationNum);
    }

    private void deleteAllSubjectsByRegNum(long registrationNum) {
        open();
        String DELETE_QUERY = "DELETE FROM " + Config.TABLE_SUBJECT + " WHERE " + Config.COLUMN_REGISTRATION_NUMBER + " = " + registrationNum;
        sqLiteDatabase.execSQL(DELETE_QUERY);
        sqLiteDatabase.close();
        close();
    }

//    public void insertSubject(String name, long registrationNum, String phone, String email){
//        open();
//
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(Config.COLUMN_STUDENT_NAME, name);
//        contentValues.put(Config.COLUMN_STUDENT_REGISTRATION, registrationNum);
//        contentValues.put(Config.COLUMN_STUDENT_PHONE, phone);
//        contentValues.put(Config.COLUMN_STUDENT_EMAIL, email);
//        sqLiteDatabase.insert(Config.TABLE_STUDENT, null, contentValues);
//
//        sqLiteDatabase.close();
//        close();
//    }
//
//    public List<Student> getAllStudent(){
//        open();
//
//        String SELECT_QUERY = String.format("SELECT %s, %s FROM %s", Config.COLUMN_STUDENT_NAME, Config.COLUMN_REGISTRATION_NUMBER, Config.TABLE_STUDENT);
//
//        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
//        if(cursor!=null)
//            if(cursor.moveToFirst()){
//                List<Student> studentList = new ArrayList<>();
//                do {
//                    String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_NAME));
//                    long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_STUDENT_REGISTRATION));
//                    studentList.add(new Student(name, registrationNumber, "", ""));
//                }   while (cursor.moveToNext());
//                cursor.close();
//                close();
//                return studentList;
//            }
//
//        return null;
//    }
//
//    public Student getStudentByRegNum(long registrationNum){
//        open();
//
////        String SELECT_QUERY = "SELECT * FROM " + Config.TABLE_STUDENT + " WHERE " + Config.COLUMN_STUDENT_REGISTRATION + " = " + registrationNum;
//        String SELECT_QUERY = String.format("SELECT * FROM %s WHERE %s = %s", Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION, String.valueOf(registrationNum));
//
//        Cursor cursor = sqLiteDatabase.rawQuery(SELECT_QUERY, null);
//
//        if(cursor!=null)
//            if(cursor.moveToFirst()){
//                String name = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_NAME));
//                long registrationNumber = cursor.getLong(cursor.getColumnIndex(Config.COLUMN_STUDENT_REGISTRATION));
//                String phone = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_PHONE));
//                String email = cursor.getString(cursor.getColumnIndex(Config.COLUMN_STUDENT_EMAIL));
//                cursor.close();
//
//                return new Student(name, registrationNumber, phone, email);
//            }
//        close();
//
//        return null;
//    }
//
//    public void deleteStudentByRegNum(long registrationNum) {
//        open();
//        sqLiteDatabase.delete(Config.TABLE_STUDENT, Config.COLUMN_STUDENT_REGISTRATION + " = ? ", new String[]{ String.valueOf(registrationNum)});
//        sqLiteDatabase.close();
//        close();
//
//        deleteAllSubjectsByRegNum(registrationNum);
//    }


}