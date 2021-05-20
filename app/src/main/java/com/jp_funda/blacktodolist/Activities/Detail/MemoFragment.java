package com.jp_funda.blacktodolist.Activities.Detail;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.jp_funda.blacktodolist.Activities.Main.MainActivity;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.R;
import com.jp_funda.blacktodolist.ViewModels.DetailActivityViewModel;
import com.jp_funda.blacktodolist.ViewModels.MainActivityViewModel;

import org.w3c.dom.Text;


public class MemoFragment extends Fragment {
    private TodoDatabaseHandler todoDB;

    private View root;
    private EditText memoEditText;

    public MemoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // initialize DB
        todoDB = new TodoDatabaseHandler(getActivity());

        // initialize viewModel
        DetailActivityViewModel detailActivityViewModel = new ViewModelProvider(getActivity()).get(DetailActivityViewModel.class);

        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_memo, container, false);
        memoEditText = root.findViewById(R.id.memo_edit_text);

        // memo settings
        memoEditText.setText(detailActivityViewModel.handlingTodo.getMemo());
        memoEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                detailActivityViewModel.handlingTodo.setMemo(s.toString());
                todoDB.updateTodo(detailActivityViewModel.handlingTodo);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        return root;
    }
}