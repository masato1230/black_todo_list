package com.jp_funda.blacktodolist.Recycler;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jp_funda.blacktodolist.Models.Todo;
import com.jp_funda.blacktodolist.R;

import org.w3c.dom.Text;

public class TodoRecyclerViewHolder extends RecyclerView.ViewHolder {
    public Todo todo;
    public TextView title;
    public TextView memo;
    public TextView reminder;
    public TextView editButton;
    public ConstraintLayout foregroundView;
    public ConstraintLayout backgroundView;

    public TodoRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.row_todo_title);
        memo = itemView.findViewById(R.id.row_todo_memo);
        reminder = itemView.findViewById(R.id.row_todo_reminder);
        editButton = itemView.findViewById(R.id.row_todo_edit);
        foregroundView = itemView.findViewById(R.id.row_todo_foreground);
        backgroundView = itemView.findViewById(R.id.todo_row_background);
    }
}
