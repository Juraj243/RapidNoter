package com.example;

import org.greenrobot.greendao.generator.DaoGenerator;

import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Property;
import org.greenrobot.greendao.generator.Schema;
import org.greenrobot.greendao.generator.ToMany;

public class MainDbGenerator {
    public static void main(String[] args)  throws Exception {

        //place where db folder will be created inside the project folder
        Schema schema = new Schema(1,"com.example.juraj.note.data");

        //Entity i.e. Class to be stored in the database // ie table LOG
        Entity note_item= schema.addEntity("Note");
        Entity cart_table =schema.addEntity("Cart");
        Entity cart_item =schema.addEntity("CartItem");


        note_item.addIdProperty(); //It is the primary key for uniquely identifying a row
        note_item.addStringProperty("title").notNull();
        note_item.addStringProperty("text").notNull();  //Not null is SQL constrain
        note_item.addDateProperty("created").notNull();
        note_item.addDateProperty("date_from");
        note_item.addDateProperty("date_to");
        note_item.addDoubleProperty("latitude");
        note_item.addDoubleProperty("longitude");
        note_item.addDateProperty("notification");

        cart_table.addIdProperty();
        cart_table.addDateProperty("created").notNull();
        cart_table.addStringProperty("name").notNull();

        cart_item.addIdProperty();
        cart_item.addStringProperty("name").notNull();
        cart_item.addDateProperty("created").notNull();


        Entity cart = schema.addEntity("Cart");
        cart.addIdProperty();
        cart.addStringProperty("name").notNull();

        Entity cartItem = schema.addEntity("CartItem");
        cartItem.setTableName("CART_ITEMS"); // "ORDER" is a reserved keyword
        cartItem.addIdProperty();
        cartItem.addStringProperty("name");
        Property orderDate = cartItem.addDateProperty("date").getProperty();
        Property cartId = cartItem.addLongProperty("cartId").notNull().getProperty();
        cartItem.addToOne(cart, cartId);

        ToMany cartToItems = cart.addToMany(cartItem, cartId);
        cartToItems.setName("cartItems");
        cartToItems.orderAsc(orderDate);


        //  ./app/src/main/java/   ----   com/codekrypt/greendao/db is the full path
        new DaoGenerator().generateAll(schema, "./app/src/main/java");

    }
}
