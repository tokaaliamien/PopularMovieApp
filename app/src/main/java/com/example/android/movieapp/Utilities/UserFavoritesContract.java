package com.example.android.movieapp.Utilities;

/**
 * Created by Demo on 2016-12-03.
 */
public class UserFavoritesContract {
    public static abstract class NewUserFavorite{

        public static final String POSTER_PATH ="poster_path";
        public static final String TITLE="original_title";
        public static final String RELEASE_DATE ="release_date";
        public static final String VOTE="vote_average";
        public static final String OVERVIEW="overview";
        public static final String ID="id";
        public static final String TABLE_NAME="user_favorite";

    }
}
