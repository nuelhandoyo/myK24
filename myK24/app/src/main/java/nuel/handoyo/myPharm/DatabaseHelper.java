package nuel.handoyo.myPharm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String db_name = "myK24.db";
    private static final int db_version = 1;
    public DatabaseHelper(Context context) {
        super(context, db_name, null,db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE session(id integer PRIMARY KEY, login text)");
        db.execSQL("CREATE TABLE users(uid integer PRIMARY KEY AUTOINCREMENT, membercode integer, fullname text null, birthdate text null, gender text null, address text null, username text, password text, referralcode text, sign integer)");
        db.execSQL("INSERT INTO session(id, login) VALUES(1, 'kosong')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS session");
        db.execSQL("DROP TABLE IF EXISTS users");
        onCreate(db);
    }

    public String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    //check session
    public Boolean checkSession(String sessionValues) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM session WHERE login = ?", new String[]{sessionValues});
        if (cursor.getCount() > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    //get membercode
    public String getNewMemberCode() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String param = String.valueOf(year)+String.format("%02d" , month);
        int memberNum = 1;
        String memberCode = param + String.format("%06d" , memberNum);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorx = db.rawQuery("SELECT max(uid) from users where substr(membercode,1,6) = ? ", new String[]{param});
        cursorx.moveToFirst();

        if (cursorx!=null && cursorx.getCount()>0 && cursorx.getString(0)!=null) {
            cursorx.moveToPosition(0);
            Cursor cursory = db.rawQuery("SELECT membercode FROM users WHERE uid = ? ", new String[]{cursorx.getString(0)});
            cursory.moveToFirst();

            if (cursory!=null && cursory.getCount()>0 && cursory.getString(0)!=null){
                cursory.moveToPosition(0);
                String strTemp = cursory.getString(0);
                String strA = strTemp.substring(0,6);
                String strB = strTemp.substring(6);
                int strX = 0;
                try {
                    strX = Integer.parseInt(strB);
                } catch(NumberFormatException nfe) {
                    Log.i("xyz", "new number erooor");
                }
                strX++;
                memberCode = strA + String.format("%06d" , strX);
            }
        }

        return memberCode;
    }


    public Boolean validateNewUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ? and sign=1", new String[]{username});
        if (cursor.getCount() > 0) {
            return false;
        }else {
            return true;
        }
    }

    //upgrade session
    public Boolean upgradeSession(String sessionValues, int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("login", sessionValues);
        long update = db.update("session", contentValues, "id="+id, null);
        if (update == -1) {
            return false;
        }else {
            return true;
        }
    }

    //insert user
    public Boolean insertUser(String fullname, String birthdate, String gender, String address, String referralcode, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String membercode = getNewMemberCode(); //generate
        Integer sign = 1;
        String passhash = md5(password);

        contentValues.put("membercode", membercode);
        contentValues.put("fullname", fullname);
        contentValues.put("birthdate", birthdate);
        contentValues.put("gender", gender);
        contentValues.put("address", address);
        contentValues.put("username", username);
        contentValues.put("password", passhash);
        contentValues.put("referralcode", referralcode);
        contentValues.put("sign", sign);
        long insert = db.insert("users", null, contentValues);
        if (insert == -1) {
            return false;
        }else {
            return true;
        }
    }

    //check login
    public String checkLogin(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String passhash = md5(password);
        Cursor cursorx = db.rawQuery("SELECT * FROM users WHERE username = ? AND password = ?", new String[]{username, passhash});
        cursorx.moveToFirst();
        if (cursorx!=null && cursorx.getCount()>0 && cursorx.getString(0)!=null) {
            cursorx.moveToPosition(0);
            String code = cursorx.getString(1);
            String fname = cursorx.getString(2);
            String ref = cursorx.getString(8);
            String keyuser = "0";
            if(ref.length()>0 && ref.equals("myk24admin")){
                keyuser = "1";
            }
            return keyuser+code+fname;
        } else {
            return "0";
        }
    }

    public Boolean resetPassword(String membercode) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorx = db.rawQuery("SELECT * FROM users WHERE membercode = ? and sign=1", new String[]{membercode});
        cursorx.moveToFirst();

        if (cursorx!=null && cursorx.getCount()>0 && cursorx.getString(0)!=null) {
            cursorx.moveToPosition(0);
            String pass = cursorx.getString(6);
            String passhash = md5(pass);
            db.execSQL("update users set password='"+passhash+"' where membercode='"+membercode+"'");

            return true;
        }
        else {
            return false;
        }
    }

    public Boolean changePassword(String membercode, String passNew) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorx = db.rawQuery("SELECT * FROM users WHERE membercode = ? and sign=1", new String[]{membercode});
        cursorx.moveToFirst();

        if (cursorx!=null && cursorx.getCount()>0 && cursorx.getString(0)!=null) {
            cursorx.moveToPosition(0);
            String passhash = md5(passNew);
            db.execSQL("update users set password='"+passhash+"' where membercode='"+membercode+"'");

            return true;
        }
        else {
            return false;
        }
    }

    public Boolean checkEmpty(String strValue) {
        if(strValue != null && !strValue.isEmpty() && !strValue.equals("null")){
            return true;
        }else {
            return false;
        }
    }
}
