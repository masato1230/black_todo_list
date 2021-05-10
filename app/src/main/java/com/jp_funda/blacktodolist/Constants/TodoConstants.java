package com.jp_funda.blacktodolist.Constants;

import java.text.SimpleDateFormat;

public class TodoConstants {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "todoDB";
    public static final String TABLE_NAME = "todoTable";
    // table columns
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MEMO = "memo";
    public static final String KEY_REMIND = "remind";
    public static final String KEY_TASK_IDS = "taskIds";
    // date format
    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm yyyy/MM/dd EEE");
}
