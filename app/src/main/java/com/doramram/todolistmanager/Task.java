package com.doramram.todolistmanager;


import java.io.Serializable;
import java.util.Date;

public class Task implements Serializable{

    private int _id;
    private String _title;
    private Date _date;

    public Task() {
    }

    public Task(String _task, Date _date) {
        this._title = _task;
        this._date = _date;
    }

    public Task(int _id, String _task, Date _date) {
        this._id = _id;
        this._title = _task;
        this._date = _date;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_title() {
        return _title;
    }

    public void set_title(String _title) {
        this._title = _title;
    }

    public Date get_date() {
        return _date;
    }

    public void set_date(Date _date) {
        this._date = _date;
    }
}
