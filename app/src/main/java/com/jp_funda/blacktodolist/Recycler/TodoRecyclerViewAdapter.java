package com.jp_funda.blacktodolist.Recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jp_funda.blacktodolist.Activities.MainActivity;
import com.jp_funda.blacktodolist.Constants.TodoConstants;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.Dialog.MyDatePickerDialog;
import com.jp_funda.blacktodolist.Models.Todo;
import com.jp_funda.blacktodolist.R;

import java.util.List;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewHolder> {
    private List<Todo> todoList;
    private TodoDatabaseHandler todoDB;
    private Context context;

    public TodoRecyclerViewAdapter(Context context) {
        this.todoDB = new TodoDatabaseHandler(context);
        this.context = context;
        updateTodoList();
    }

    @NonNull
    @Override
    public TodoRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(context).inflate(R.layout.row_todo, parent, false);
        TodoRecyclerViewHolder viewHolder = new TodoRecyclerViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TodoRecyclerViewHolder holder, int position) {
        // get the data and store it to todoObject
        Todo todo = todoList.get(position);

        // set data to views
        holder.title.setText(todo.getTitle());
        if (todo.getMemo() != null) {
            holder.memo.setText(todo.getMemo());
        }
        if (todo.getRemindDate() != null) {
            holder.reminder.setText(TodoConstants.dateFormat.format(todo.getRemindDate()));
        }

        // click listeners
        holder.itemView.setOnClickListener(this::onRowClick);
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle(R.string.edit);
                builder.setMessage("Edit title or set reminder");
                EditText titleEditText = new EditText(context);
                titleEditText.setText(todo.getTitle());
                titleEditText.setTextColor(context.getResources().getColor(R.color.white));
                builder.setView(titleEditText);
                builder.setNeutralButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update todo_data
                        todo.setTitle(titleEditText.getText().toString());
                        todoDB.updateTodo(todo);
                        updateTodoList();
                    }
                });
                builder.setNegativeButton(R.string.set_reminder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new MyDatePickerDialog().show(((MainActivity) context).getSupportFragmentManager(), null);
                    }
                });
                builder.create().show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void updateTodoList() {
        this.todoList = todoDB.getAllTodo();
        notifyDataSetChanged();
    }

    public void onRowClick(View view) {
        // todo
    }
}
