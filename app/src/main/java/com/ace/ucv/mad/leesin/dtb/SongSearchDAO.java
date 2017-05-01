package com.ace.ucv.mad.leesin.dtb;

/**
 * Created by ctotolin on 01-May-17.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SongSearchDAO {

    // Database fields
    private SQLiteDatabase database;
    private SQLManager dbHelper;
    private String[] allColumns = { SQLManager.COLUMN_ID,
            SQLManager.COLUMN_COMMENT };

    public SongSearchDAO(Context context) {
        dbHelper = new SQLManager(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public SongEntry createComment(String comment) {
        ContentValues values = new ContentValues();
        values.put(SQLManager.COLUMN_COMMENT, comment);
        long insertId = database.insert(SQLManager.TABLE_COMMENTS, null,
                values);
        Cursor cursor = database.query(SQLManager.TABLE_COMMENTS,
                allColumns, SQLManager.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        SongEntry newSongEntry = cursorToComment(cursor);
        cursor.close();
        return newSongEntry;
    }

    public void deleteComment(SongEntry songEntry) {
        long id = songEntry.getId();
        System.out.println("SongEntry deleted with id: " + id);
        database.delete(SQLManager.TABLE_COMMENTS, SQLManager.COLUMN_ID
                + " = " + id, null);
    }

    public List<SongEntry> getAllComments() {
        List<SongEntry> songEntries = new ArrayList<SongEntry>();

        Cursor cursor = database.query(SQLManager.TABLE_COMMENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            SongEntry songEntry = cursorToComment(cursor);
            songEntries.add(songEntry);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return songEntries;
    }

    private SongEntry cursorToComment(Cursor cursor) {
        SongEntry songEntry = new SongEntry();
        songEntry.setId(cursor.getLong(0));
        songEntry.setComment(cursor.getString(1));
        return songEntry;
    }
}