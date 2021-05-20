package com.jp_funda.blacktodolist.Activities.Detail;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.jp_funda.blacktodolist.Activities.Main.MainActivity;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.R;
import com.jp_funda.blacktodolist.ViewModels.DetailActivityViewModel;
import com.jp_funda.blacktodolist.ViewModels.MainActivityViewModel;

public class DetailActivity extends AppCompatActivity {
    private TodoDatabaseHandler todoDB;
    private DetailActivityViewModel detailActivityViewModel;

    private TabLayout tabLayout;
    private ConstraintLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        // initialize DB
        todoDB = new TodoDatabaseHandler(this);

        // initialize ViewModel
        detailActivityViewModel = new ViewModelProvider(this).get(DetailActivityViewModel.class);

        // get todoId and retrieve data
        detailActivityViewModel.handlingTodo = todoDB.getById(getIntent().getIntExtra("todoId", 0));

        // initialize Views
        tabLayout = findViewById(R.id.detail_tabs);
        fragmentContainer = findViewById(R.id.detail_fragment_container);
        getSupportActionBar().hide();

        // Show task list fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.detail_fragment_container, new TaskListFragment());
        transaction.commit();

        // tab change listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction tabTransaction = getSupportFragmentManager().beginTransaction();
                if (tab.getPosition() == 0) {
                    tabTransaction.replace(R.id.detail_fragment_container, new TaskListFragment());
                } else {
                    tabTransaction.replace(R.id.detail_fragment_container, new MemoFragment());
                }
                tabTransaction.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}