package com.jp_funda.blacktodolist.Models;

import java.util.Calendar;
import java.util.List;

public class Todo {
    private int id;
    private String title;
    private String memo;
    private Calendar remindCalender;
    private List<Task> tasks;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Calendar getRemindCalender() {
        return remindCalender;
    }

    public void setRemindCalender(Calendar remindCalender) {
        this.remindCalender = remindCalender;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
