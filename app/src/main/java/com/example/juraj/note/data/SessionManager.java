package com.example.juraj.note.data;

import android.content.Context;

import org.greenrobot.greendao.database.Database;

/**
 * Created by Michal on 24.11.2017.
 */

public class SessionManager {
    private static SessionManager instance;
    private static DaoMaster.DevOpenHelper helper;
    private static  Database db;
    private static  DaoMaster dMaster;
    private static DaoSession daoSession;

    private SessionManager(){

    }

    public static SessionManager getInstance(){
        if(instance == null){
            getInstance(null);
        }
        return instance;
    }

    public static SessionManager getInstance(Context context){
        if(instance == null){
            instance = new SessionManager();
            initialize(context);
        }
            return instance;

    }

    private static void initialize(Context context){
        helper = new DaoMaster.DevOpenHelper(context, "note", null);
        db  = helper.getWritableDb();
        dMaster = new DaoMaster(db);
        daoSession = dMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return daoSession;
    }

    public Database getDb(){
        return db;
    }
}
