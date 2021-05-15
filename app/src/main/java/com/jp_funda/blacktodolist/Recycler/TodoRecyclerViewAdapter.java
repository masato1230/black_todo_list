package com.jp_funda.blacktodolist.Recycler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jp_funda.blacktodolist.Constants.TodoConstants;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
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
        Todo todo = todoList.get(position);
        holder.title.setText(todo.getTitle());
        if (todo.getMemo() != null) {
            holder.memo.setText(todo.getMemo());
        }
        if (todo.getRemindDate() != null) {
            holder.reminder.setText(TodoConstants.dateFormat.format(todo.getRemindDate()));
        }
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }

    public void updateTodoList() {
        this.todoList = todoDB.getAllTodo();
        notifyDataSetChanged();
    }
}
