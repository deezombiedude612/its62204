package note.light.sun.sunlightnote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Fat Woman on 11/11/2017.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DATABASE_NAME = "SUNNOTE.db";
    private static String TABLE_NAME = "SUNLOG";
    private static String COL_1 = "ID";
    private static String COL_2 = "BRIGHT";
    private static String COL_3 = "DATE";
    private static String COL_4 = "TIME";
    private static String COL_5 = "NOTE";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    /*
     * Instantiate with Singleton
     */
    public static DatabaseHelper sInstance;
    public static synchronized DatabaseHelper getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new DatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECORDS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT," + COL_2 + " TEXT,"
                + COL_3 + " TEXT," + COL_4 + " TEXT," + COL_5 + " TEXT" + ")";
        db.execSQL(CREATE_RECORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public Boolean insertData(String bright, String date, String time, String note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, bright);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, note);
        long result = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        if (result==-1) //result will be returned -1 if data is not successfully inserted in the table
            return false;
        else
            return true;
    }

    public Cursor retrieveAll(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor res=db.rawQuery("SELECT ID, BRIGHT, DATE, TIME, SUBSTR(NOTE, 0, 40) FROM "+ TABLE_NAME  + " ORDER BY ID DESC", null);
        return res;
    }

    public String[] retrieveData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE ID = ?", new String[]{Integer.toString(id)});

        String res[] = new String[5];
        res[0] = "NOPE";

        if(cur!=null && cur.moveToFirst()) {
            cur.moveToFirst();

            do {
                res[0] = cur.getString(0);
                res[1] = cur.getString(1);
                res[2] = cur.getString(2);
                res[3] = cur.getString(3);
                res[4] = cur.getString(4);
            } while (cur.moveToNext());

            cur.close();
        }
        return res;
    }

    public boolean updateData(String id, String bright, String date, String time, String note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_1, id);
        contentValues.put(COL_2, bright);
        contentValues.put(COL_3, date);
        contentValues.put(COL_4, time);
        contentValues.put(COL_5, note);
        long result = db.update(TABLE_NAME, contentValues, "ID = ?", new String[]{id});
        db.close();
        if (result==-1)
            return false;
        else
            return true;
    }

    public int deleteData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[] {Integer.toString(id)});
    }
}
