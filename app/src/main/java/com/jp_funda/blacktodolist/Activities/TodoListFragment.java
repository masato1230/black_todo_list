package com.jp_funda.blacktodolist.Activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.Models.Todo;
import com.jp_funda.blacktodolist.R;
import com.jp_funda.blacktodolist.Recycler.TodoRecyclerViewAdapter;

public class TodoListFragment extends Fragment {
    // DB
    private TodoDatabaseHandler todoDB;
    // Views
    private View root;
    private RecyclerView recyclerView;
    public TodoRecyclerViewAdapter adapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Initialize DB
        todoDB = new TodoDatabaseHandler(getActivity());

        // todo delete test
        Todo todo = new Todo();
        todo.setTitle("TEST");
        todo.setMemo("memomemo");
        todoDB.addTodo(todo);

        // Initialize views
        root =  inflater.inflate(R.layout.activity_main, container, false);
        recyclerView = root.findViewById(R.id.todo_recycler);

        // RecyclerView Setting
        adapter = new TodoRecyclerViewAdapter(getActivity());
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(adapter);

        return root;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        view.findViewById(R.id.button_first).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(TodoListFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
//            }
//        });
    }
}