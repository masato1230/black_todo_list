package com.jp_funda.blacktodolist.Recycler;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.jp_funda.blacktodolist.Activities.Detail.DetailActivity;
import com.jp_funda.blacktodolist.Activities.Main.MainActivity;
import com.jp_funda.blacktodolist.Constants.TodoConstants;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.Dialog.MyDatePickerDialog;
import com.jp_funda.blacktodolist.Models.Todo;
import com.jp_funda.blacktodolist.R;
import com.jp_funda.blacktodolist.ViewModels.MainActivityViewModel;

import java.util.Comparator;
import java.util.List;

public class TodoRecyclerViewAdapter extends RecyclerView.Adapter<TodoRecyclerViewHolder> {
    private List<Todo> todoList;
    private TodoDatabaseHandler todoDB;
    private Context context;
    private MainActivityViewModel mainActivityViewModel;

    public TodoRecyclerViewAdapter(Context context) {
        this.todoDB = new TodoDatabaseHandler(context);
        this.context = context;
        updateTodoList();

        mainActivityViewModel = new ViewModelProvider((MainActivity) context).get(MainActivityViewModel.class);
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
        holder.todo = todo;

        // set data to views
        holder.title.setText(todo.getTitle());
        if (todo.getMemo() != null) {
            holder.memo.setText(todo.getMemo());
        }
        if (todo.getRemindDate() != null) {
            holder.reminder.setText("reminder: " + TodoConstants.dateFormat.format(todo.getRemindDate()));
        } else {
            holder.reminder.setText("reminder: No reminders set");
        }
        // set long click listener
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mainActivityViewModel.handlingTodo = todo;
                mainActivityViewModel.rowHeight = v.getHeight();
                v.startDrag(null, new View.DragShadowBuilder(v), v, 0);
                return true;
            }
        });

        // click listeners
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivityViewModel.handlingTodo = todo;
                Intent intent = new Intent(context, DetailActivity.class);
                context.startActivity(intent);
            }
        });
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle(R.string.edit);
                builder.setMessage("Change title or set reminder");
                // dialog view
                View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_edit_todo, null);
                EditText titleEditText = dialogView.findViewById(R.id.edit_dialog_title);
                EditText memoEditText = dialogView.findViewById(R.id.edit_dialog_memo);
                titleEditText.setText(todo.getTitle());
                memoEditText.setText(todo.getMemo());
                builder.setView(dialogView);

                // click listeners
                builder.setNeutralButton(R.string.cancel, null);
                if (todo.getRemindDate() != null) {
                    builder.setNeutralButton(R.string.reset_reminder, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // todo reset reminder
                            // update todoDB
                            todo.setRemindDate(null);
                            todoDB.updateTodo(todo);
                            dialog.dismiss();
                            updateTodoList();
                        }
                    });
                }
                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // update todo_data
                        todo.setTitle(titleEditText.getText().toString());
                        todo.setMemo(memoEditText.getText().toString());
                        todoDB.updateTodo(todo);
                        updateTodoList();
                    }
                });
                builder.setNegativeButton(R.string.set_reminder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mainActivityViewModel.handlingTodo = todo;
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
        todoList.sort(Comparator.comparing(Todo::getOrderNumber));
        notifyDataSetChanged();
    }
}
