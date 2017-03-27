package mickael.koc.channelmessaging2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by kocm on 06/02/2017.
 */
public class UserDataSource {

    private SQLiteDatabase database;
    private FriendsDB dbHelper;
    private String[] allColumns = { FriendsDB.USER_ID,FriendsDB.USER_NAME, FriendsDB.IMAGEURL };
    public UserDataSource(Context context) { dbHelper = new FriendsDB(context); }
    public void open() throws SQLException { database = dbHelper.getWritableDatabase(); }
    public void close() { dbHelper.close(); }

    public User createUser(int id,String nom, String image) {
        ContentValues values = new ContentValues();
        values.put(FriendsDB.USER_NAME, image);
        values.put(FriendsDB.IMAGEURL, nom);
        values.put(FriendsDB.USER_ID, id);
        database.insert(FriendsDB.USER_TABLE_NAME, null, values);
        Cursor cursor = database.query(FriendsDB.USER_TABLE_NAME, allColumns, FriendsDB.USER_ID + " = \"" + id +"\"", null, null, null, null);
        cursor.moveToFirst();
        User newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    public List<User> getAllUser() {
        List<User> Users = new ArrayList<User>();

    Cursor cursor = database.query(FriendsDB.USER_TABLE_NAME, allColumns, null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User unUser = cursorToUser(cursor);

            Users.add(unUser);
            cursor.moveToNext();
        }

    cursor.close();
    return Users; }

    private User cursorToUser(Cursor cursor) {
        User comment = new User();
        int result = cursor.getInt(0);
        comment.setId(result);
        comment.setName(cursor.getString(1));
        comment.setImage(cursor.getString(2));
        return comment;
    }
}
