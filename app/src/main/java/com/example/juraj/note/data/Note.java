package com.example.juraj.note.data;

import org.greenrobot.greendao.annotation.*;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit.

/**
 * Entity mapped to table "NOTE".
 */
@Entity
public class Note {

    @Id
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private String text;

    @NotNull
    private java.util.Date created;
    private java.util.Date date_from;
    private java.util.Date date_to;
    private Double latitude;
    private Double longitude;
    private java.util.Date notification;

    @Generated
    public Note() {
    }

    public Note(Long id) {
        this.id = id;
    }

    @Generated
    public Note(Long id, String title, String text, java.util.Date created, java.util.Date date_from, java.util.Date date_to, Double latitude, Double longitude, java.util.Date notification) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.created = created;
        this.date_from = date_from;
        this.date_to = date_to;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notification = notification;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @NotNull
    public String getTitle() {
        return title;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setTitle(@NotNull String title) {
        this.title = title;
    }

    @NotNull
    public String getText() {
        return text;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setText(@NotNull String text) {
        this.text = text;
    }

    @NotNull
    public java.util.Date getCreated() {
        return created;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setCreated(@NotNull java.util.Date created) {
        this.created = created;
    }

    public java.util.Date getDate_from() {
        return date_from;
    }

    public void setDate_from(java.util.Date date_from) {
        this.date_from = date_from;
    }

    public java.util.Date getDate_to() {
        return date_to;
    }

    public void setDate_to(java.util.Date date_to) {
        this.date_to = date_to;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public java.util.Date getNotification() {
        return notification;
    }

    public void setNotification(java.util.Date notification) {
        this.notification = notification;
    }

}
