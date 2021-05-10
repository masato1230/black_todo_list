package com.jp_funda.blacktodolist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.jp_funda.blacktodolist.Constants.TaskConstants;
import com.jp_funda.blacktodolist.Constants.TodoConstants;
import com.jp_funda.blacktodolist.Models.Task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TaskDatabaseHandler extends SQLiteOpenHelper {
    private Context context;

    public TaskDatabaseHandler(@Nullable Context context) {
        super(context, TaskConstants.DB_NAME, null, TaskConstants.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TaskConstants.TABLE_NAME + "("
                + TaskConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + TaskConstants.KEY_TITLE + " TEXT,"
                + TaskConstants.KEY_MEMO + " TEXT,"
                + TaskConstants.KEY_REMIND + " TEXT,"
                + TaskConstants.KEY_TODO_ID + " INTEGER"
                + ")";
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TaskConstants.TABLE_NAME);
        onCreate(db);
    }

    // add
    public long addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskConstants.KEY_TITLE, task.getTitle());
        values.put(TaskConstants.KEY_MEMO, task.getMemo());
        values.put(TaskConstants.KEY_REMIND, TodoConstants.dateFormat.format(task.getRemindDate()));
        values.put(TaskConstants.KEY_TODO_ID, task.getTodoId());

        return db.insert(TaskConstants.TABLE_NAME, null, values);
    }

    // get by todoId
    public List<Task> getTasksByTodoId(int todoId) {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Task> tasks = new ArrayList<>();

        Cursor cursor = db.query(
                TaskConstants.TABLE_NAME,
                new String[] {TaskConstants.KEY_ID, TaskConstants.KEY_TITLE, TaskConstants.KEY_MEMO, TaskConstants.KEY_REMIND, TaskConstants.KEY_TODO_ID},
                TaskConstants.KEY_TODO_ID + "=?", new String[] {String.valueOf(todoId)},
                null, null, null
                );

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getInt(cursor.getColumnIndex(TaskConstants.KEY_ID)));
                task.setTitle(cursor.getString(cursor.getColumnIndex(TaskConstants.KEY_TITLE)));
                task.setMemo(cursor.getString(cursor.getColumnIndex(TaskConstants.KEY_MEMO)));
                String remindDateString = cursor.getString(cursor.getColumnIndex(TaskConstants.KEY_REMIND));
                try {
                    task.setRemindDate(TodoConstants.dateFormat.parse(remindDateString));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                task.setTodoId(cursor.getInt(cursor.getColumnIndex(TaskConstants.KEY_TODO_ID)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        db.close();
        return tasks;
    }

    // update
    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskConstants.KEY_TITLE, task.getTitle());
        values.put(TaskConstants.KEY_MEMO, task.getMemo());
        String remindDateString = TodoConstants.dateFormat.format(task.getRemindDate());
        values.put(TaskConstants.KEY_REMIND, remindDateString);
        values.put(TaskConstants.KEY_TODO_ID, task.getTodoId());

        return db.update(TaskConstants.TABLE_NAME, values, TaskConstants.KEY_ID + "=?", new String[] {String.valueOf(task.getId())});
    }

    // delete
    public void deleteTask(int taskId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TaskConstants.TABLE_NAME, TaskConstants.KEY_ID + "=?", new String[] {String.valueOf(taskId)});
    }
}
