package fi.msrn.sinto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import fi.msrn.sinto.database.models.Note;

public class DatabaseHelper extends SQLiteOpenHelper {
    //db version
    private static final int DATABASE_VERSION = 1;

    //db name
    private static final String DATABASE_NAME = "sintos_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Note.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //if table already exists then drop it
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        onCreate(db);

    }

    public long insertNote(String title, String body) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Note.COLUMN_TITLE, title);
        values.put(Note.COLUMN_BODY, body);

        long id = db.insert(Note.TABLE_NAME, null, values);

        db.close();

        return id;

    }

    public Note getNote(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_TITLE, Note.COLUMN_BODY, Note.COLUMN_DATE},
                Note.COLUMN_ID + "=?",
                new String[] {String.valueOf(id)}, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
        }

        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_BODY)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATE)));

        cursor.close();
        return note;

    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + Note.TABLE_NAME + " ORDER BY " + Note.COLUMN_DATE + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)));
                note.setTitle(cursor.getString(cursor.getColumnIndex(Note.COLUMN_TITLE)));
                note.setBody( cursor.getString(cursor.getColumnIndex(Note.COLUMN_BODY)));
                note.setDate(cursor.getString(cursor.getColumnIndex(Note.COLUMN_DATE)));

                notes.add(note);
            } while (cursor.moveToNext());
        }

        db.close();

        return notes;

    }

    public int getNotesCount() {
        String countQuery = "SELECT * FROM " + Note.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        return count;
    }

    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Note.COLUMN_TITLE, note.getTitle());
        values.put(Note.COLUMN_BODY, note.getBody());

        return db.update(Note.TABLE_NAME, values, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

    public void deleteNode (Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Note.TABLE_NAME, Note.COLUMN_ID + " = ?",
                new String[]{String.valueOf(note.getId())});
    }

}
