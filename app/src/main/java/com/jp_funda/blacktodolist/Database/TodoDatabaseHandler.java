package com.jp_funda.blacktodolist.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.jp_funda.blacktodolist.Constants.TodoConstants;
import com.jp_funda.blacktodolist.Models.Task;
import com.jp_funda.blacktodolist.Models.Todo;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class TodoDatabaseHandler extends SQLiteOpenHelper {
    private Context context;
    private TaskDatabaseHandler taskDB;

    public TodoDatabaseHandler(@Nullable Context context) {
        super(context, TodoConstants.DB_NAME, null, TodoConstants.DB_VERSION);
        this.context = context;
        this.taskDB = new TaskDatabaseHandler(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TODO_TABLE = "CREATE TABLE " + TodoConstants.TABLE_NAME + "("
                + TodoConstants.KEY_ID + " INTEGER PRIMARY KEY,"
                + TodoConstants.KEY_TITLE + " TEXT,"
                + TodoConstants.KEY_MEMO + " TEXT,"
                + TodoConstants.KEY_REMIND + " TEXT"
                + ")";

        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TodoConstants.TABLE_NAME);
        onCreate(db);
    }

    // add
    public long addTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoConstants.KEY_TITLE, todo.getTitle());
        if (todo.getMemo() != null) {
            values.put(TodoConstants.KEY_MEMO, todo.getMemo());
        }
        if (todo.getRemindDate() != null) {
            String remindDateString = TodoConstants.dateFormat.format(todo.getRemindDate());
            values.put(TodoConstants.KEY_REMIND, remindDateString);
        }
        if (todo.getTasks() != null) {
            StringBuilder builder = new StringBuilder();
            for (Task task: todo.getTasks()) {
                String taskIdString = String.valueOf(task.getId());
                builder.append(taskIdString + ",");
            }
            builder.setLength(builder.length() - 1);
        }
        return db.insert(TodoConstants.TABLE_NAME, null, values);
    }

    // get all
    public List<Todo> getAllTodo() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<Todo> todoList = new ArrayList<>();

        Cursor cursor = db.query(
                TodoConstants.TABLE_NAME,
                new String[] {TodoConstants.KEY_ID, TodoConstants.KEY_TITLE, TodoConstants.KEY_MEMO, TodoConstants.KEY_REMIND},
                null, null, null, null, null
        );

        if (cursor.moveToFirst()) {
            do {
                Todo todo = new Todo();
                todo.setId(cursor.getInt(cursor.getColumnIndex(TodoConstants.KEY_ID)));
                todo.setTitle(cursor.getString(cursor.getColumnIndex(TodoConstants.KEY_TITLE)));
                todo.setMemo(cursor.getString(cursor.getColumnIndex(TodoConstants.KEY_MEMO)));
                try {
                    if (cursor.getString(cursor.getColumnIndex(TodoConstants.KEY_REMIND)) != null) {
                        todo.setRemindDate(TodoConstants.dateFormat.parse(cursor.getString(cursor.getColumnIndex(TodoConstants.KEY_REMIND))));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                todo.setTasks(taskDB.getTasksByTodoId(todo.getId()));
                todoList.add(todo);
            } while (cursor.moveToNext());
        }
        return todoList;
    }

    // update
    public int updateTodo(Todo todo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoConstants.KEY_TITLE, todo.getTitle());
        if (todo.getMemo() != null) {
            values.put(TodoConstants.KEY_MEMO, todo.getMemo());
        }
        if (todo.getRemindDate() != null) {
            values.put(TodoConstants.KEY_REMIND, TodoConstants.dateFormat.format(todo.getRemindDate()));
        }

        return db.update(TodoConstants.TABLE_NAME, values, TodoConstants.KEY_ID + "=?", new String[]{String.valueOf(todo.getId())});
    }

    // delete
    public void delete(int todoId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TodoConstants.TABLE_NAME, TodoConstants.KEY_ID, new String[] {String.valueOf(todoId)});
    }
}
