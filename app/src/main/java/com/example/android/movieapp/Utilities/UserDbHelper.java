package com.example.android.movieapp.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.android.movieapp.Models.Movie;

/**
 * Created by Demo on 2016-12-03.
 */
public class UserDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "USERFAVORITE.DB";
    private static final int DATABASE_VERSION = 1;
    private static final String CREATE_QUERY =
            "CREATE TABLE " + UserFavoritesContract.NewUserFavorite.TABLE_NAME + "(" +
                    UserFavoritesContract.NewUserFavorite.TITLE + " TEXT," +
                    UserFavoritesContract.NewUserFavorite.ID + " TEXT," +
                    UserFavoritesContract.NewUserFavorite.OVERVIEW + " TEXT," +
                    UserFavoritesContract.NewUserFavorite.POSTER_PATH + " TEXT," +
                    UserFavoritesContract.NewUserFavorite.RELEASE_DATE + " TEXT," +
                    UserFavoritesContract.NewUserFavorite.VOTE + " TEXT);";

    public UserDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e("DATABASE OPERATIONS", "Datebase created /opened ");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_QUERY);
        Log.e("DATABASE OPERATIONS", "Table created");

    }

    public void add(Movie movie, SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();

        contentValues.put(UserFavoritesContract.NewUserFavorite.ID, movie.getId());
        contentValues.put(UserFavoritesContract.NewUserFavorite.OVERVIEW, movie.getOverview());
        contentValues.put(UserFavoritesContract.NewUserFavorite.POSTER_PATH, movie.getPosterPath());
        contentValues.put(UserFavoritesContract.NewUserFavorite.RELEASE_DATE, movie.getRelease_date());
        contentValues.put(UserFavoritesContract.NewUserFavorite.TITLE, movie.getOriginalTitle());
        contentValues.put(UserFavoritesContract.NewUserFavorite.VOTE, movie.getVoteAverage());

        db.insert(UserFavoritesContract.NewUserFavorite.TABLE_NAME, null, contentValues);
        Log.e("DATABASE OPERATIONS", "One row inserted");

    }

    public Cursor getCursor(SQLiteDatabase db) {
        Cursor cursor;
        String[] columns = {
                UserFavoritesContract.NewUserFavorite.ID,
                UserFavoritesContract.NewUserFavorite.TITLE,
                UserFavoritesContract.NewUserFavorite.OVERVIEW,
                UserFavoritesContract.NewUserFavorite.POSTER_PATH,
                UserFavoritesContract.NewUserFavorite.RELEASE_DATE,
                UserFavoritesContract.NewUserFavorite.VOTE
        };

        cursor = db.query(UserFavoritesContract.NewUserFavorite.TABLE_NAME, columns, null, null, null, null, null);
        return cursor;

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
