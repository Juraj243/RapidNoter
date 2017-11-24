package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MainDbGenerator {
    public static void main(String[] args)  throws Exception {

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1,"com.example.juraj.note.data");

        //Entity i.e. Class to be stored in the database // ie table LOG
        Entity note_item= schema.addEntity("Note");

        note_item.addIdProperty(); //It is the primary key for uniquely identifying a row
        note_item.addStringProperty("title").notNull();
        note_item.addStringProperty("text").notNull();  //Not null is SQL constrain
        note_item.addDateProperty("created").notNull();
        note_item.addDateProperty("notification");
        note_item.addStringProperty("latlon");

        //  ./app/src/main/java/   ----   com/codekrypt/greendao/db is the full path
        new DaoGenerator().generateAll(schema, "./app/src/main/java");

    }
}
