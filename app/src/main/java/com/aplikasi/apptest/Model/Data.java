package com.aplikasi.apptest.Model;

public class Data {
    public int id;
    public int Order;
    public byte data;
    public String Note;
    public String date;
    public int done;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setOrder(int order) {
        Order = order;
    }

    public int getOrder() {
        return Order;
    }

    public void setData(byte data) {
        this.data = data;
    }

    public byte getData() {
        return data;
    }

    public void setNote(String note) {
        Note = note;
    }

    public String getNote() {
        return Note;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDone(int done) {
        this.done = done;
    }

    public int getDone() {
        return done;
    }
}
