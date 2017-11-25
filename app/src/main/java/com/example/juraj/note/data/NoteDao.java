package com.example.juraj.note.data;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "NOTE".
*/
public class NoteDao extends AbstractDao<Note, Long> {

    public static final String TABLENAME = "NOTE";

    /**
     * Properties of entity Note.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Text = new Property(2, String.class, "text", false, "TEXT");
        public final static Property Created = new Property(3, java.util.Date.class, "created", false, "CREATED");
        public final static Property Date_from = new Property(4, java.util.Date.class, "date_from", false, "DATE_FROM");
        public final static Property Date_to = new Property(5, java.util.Date.class, "date_to", false, "DATE_TO");
        public final static Property Latitude = new Property(6, Double.class, "latitude", false, "LATITUDE");
        public final static Property Longitude = new Property(7, Double.class, "longitude", false, "LONGITUDE");
        public final static Property Notification = new Property(8, java.util.Date.class, "notification", false, "NOTIFICATION");
    }


    public NoteDao(DaoConfig config) {
        super(config);
    }
    
    public NoteDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"NOTE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY ," + // 0: id
                "\"TITLE\" TEXT NOT NULL ," + // 1: title
                "\"TEXT\" TEXT NOT NULL ," + // 2: text
                "\"CREATED\" INTEGER NOT NULL ," + // 3: created
                "\"DATE_FROM\" INTEGER," + // 4: date_from
                "\"DATE_TO\" INTEGER," + // 5: date_to
                "\"LATITUDE\" REAL," + // 6: latitude
                "\"LONGITUDE\" REAL," + // 7: longitude
                "\"NOTIFICATION\" INTEGER);"); // 8: notification
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NOTE\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
        stmt.bindString(3, entity.getText());
        stmt.bindLong(4, entity.getCreated().getTime());
 
        java.util.Date date_from = entity.getDate_from();
        if (date_from != null) {
            stmt.bindLong(5, date_from.getTime());
        }
 
        java.util.Date date_to = entity.getDate_to();
        if (date_to != null) {
            stmt.bindLong(6, date_to.getTime());
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(7, latitude);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(8, longitude);
        }
 
        java.util.Date notification = entity.getNotification();
        if (notification != null) {
            stmt.bindLong(9, notification.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Note entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getTitle());
        stmt.bindString(3, entity.getText());
        stmt.bindLong(4, entity.getCreated().getTime());
 
        java.util.Date date_from = entity.getDate_from();
        if (date_from != null) {
            stmt.bindLong(5, date_from.getTime());
        }
 
        java.util.Date date_to = entity.getDate_to();
        if (date_to != null) {
            stmt.bindLong(6, date_to.getTime());
        }
 
        Double latitude = entity.getLatitude();
        if (latitude != null) {
            stmt.bindDouble(7, latitude);
        }
 
        Double longitude = entity.getLongitude();
        if (longitude != null) {
            stmt.bindDouble(8, longitude);
        }
 
        java.util.Date notification = entity.getNotification();
        if (notification != null) {
            stmt.bindLong(9, notification.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Note readEntity(Cursor cursor, int offset) {
        Note entity = new Note( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // title
            cursor.getString(offset + 2), // text
            new java.util.Date(cursor.getLong(offset + 3)), // created
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // date_from
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)), // date_to
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // latitude
            cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7), // longitude
            cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)) // notification
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Note entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.getString(offset + 1));
        entity.setText(cursor.getString(offset + 2));
        entity.setCreated(new java.util.Date(cursor.getLong(offset + 3)));
        entity.setDate_from(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setDate_to(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
        entity.setLatitude(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setLongitude(cursor.isNull(offset + 7) ? null : cursor.getDouble(offset + 7));
        entity.setNotification(cursor.isNull(offset + 8) ? null : new java.util.Date(cursor.getLong(offset + 8)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Note entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Note entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Note entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
