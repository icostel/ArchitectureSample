package com.icostel.arhitecturesample;

public class Config {
    public final static String REST_ENDPOINT = BuildConfig.HOST_NAME;

    public interface Pref {
        String PREF_USER = "user";
        String PREF_APP = "app";
    }

    public interface Db {
        String APP_DB = "app.db";
        String USER_TABLE = "users";
    }

    public interface Data {
        String USER_ID = "user_id";
    }
}
