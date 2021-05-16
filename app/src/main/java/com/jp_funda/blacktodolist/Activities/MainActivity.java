package com.jp_funda.blacktodolist.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.Models.Todo;
import com.jp_funda.blacktodolist.R;
import com.jp_funda.blacktodolist.Recycler.TodoRecyclerViewAdapter;
import com.jp_funda.blacktodolist.ViewModels.MainActivityViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {
    private TodoDatabaseHandler todoDB;
    private RecyclerView recyclerView;
    public TodoRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize DB
        todoDB = new TodoDatabaseHandler(this);

        // initialize viewModel
        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);

        // float button
        FloatingActionButton floatButton = findViewById(R.id.fab);
        floatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // create dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
                builder.setTitle(R.string.add_todo);
                // set up dialog view
                View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_todo, null);
                EditText titleEditText = dialogView.findViewById(R.id.add_dialog_title);
                EditText memoEditText = dialogView.findViewById(R.id.add_dialog_memo);
                builder.setView(dialogView);

                builder.setNeutralButton(R.string.cancel, null);
                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // insert to DB
                        Todo newTodo = new Todo();
                        newTodo.setTitle(titleEditText.getText().toString());
                        newTodo.setMemo(memoEditText.getText().toString());
                        todoDB.addTodo(newTodo);
                        adapter.updateTodoList();
                        dialog.dismiss();

                        Snackbar.make(view, titleEditText.getText().toString() + " Created!" , Snackbar.LENGTH_SHORT)
                                .setAction("Action", null).show();
                    }
                });
                builder.create().show();
            }
        });

        recyclerView = findViewById(R.id.todo_recycler);

        // RecyclerView Setting
        adapter = new TodoRecyclerViewAdapter(this);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}