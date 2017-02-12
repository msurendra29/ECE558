package com.example.hiral.myapplication;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Hiral on 7/28/2016.
 * Edited by Richa and Surendra on 11/24/2016.
 */

/**
 * Contains the Helper class for creating and upgrading the user information (Login) database.
 * The table is altered to add new columns of latitude and longitude.
 */
public class DataBaseHelper extends SQLiteOpenHelper
{
    public DataBaseHelper(Context context, String name,CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }
    // Called when no database exists in disk and the helper class needs
    // to create a new one.
    @Override
    public void onCreate(SQLiteDatabase _db)
    {
        _db.execSQL(LoginDataBaseAdapter.DATABASE_CREATE);

    }
    // Called when there is a database version mismatch meaning that the version
    // of the database on disk needs to be upgraded to the current version.
    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
    {
        // Log the version upgrade.
        Log.w("TaskDBAdapter", "Upgrading from version " +_oldVersion + " to " +_newVersion + ", which will destroy all old data");

        // Upgrade the existing database to conform to the new version. Multiple
        // previous versions can be handled by comparing _oldVersion and _newVersion
        // values.
        // The simplest case is to drop the old table and create a new one.
       // _db.execSQL("DROP TABLE IF EXISTS " + "TEMPLATE");
        // Create a new one.
       // onCreate(_db);

        //changed the version of the database so that we can alter the table and add latitude and longitude feilds.
         if(_oldVersion==1){
             _db.execSQL("ALTER TABLE LOGIN ADD COLUMN LONGITUDE text");
             _db.execSQL("ALTER TABLE LOGIN ADD COLUMN LATITUDE text");
         }

    }

}
