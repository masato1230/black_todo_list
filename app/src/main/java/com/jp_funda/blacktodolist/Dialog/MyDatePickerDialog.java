package com.jp_funda.blacktodolist.Dialog;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.jp_funda.blacktodolist.Activities.Main.MainActivity;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.ViewModels.MainActivityViewModel;

import java.util.Calendar;

public class MyDatePickerDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // initialize ViewModel
        MainActivityViewModel mainActivityViewModel = new ViewModelProvider(getActivity()).get(MainActivityViewModel.class);

        // initialize todoDB
        TodoDatabaseHandler todoDB = new TodoDatabaseHandler(getActivity());

        // get calender instance
        Calendar calendar = Calendar.getInstance();
        if (mainActivityViewModel.handlingTodo.getRemindDate() != null) {
            calendar.setTime(mainActivityViewModel.handlingTodo.getRemindDate());
        }

        // create date dialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // update ViewModel:handlingTask
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mainActivityViewModel.handlingTodo.setRemindDate(calendar.getTime());
                        // update DB
                        todoDB.updateTodo(mainActivityViewModel.handlingTodo);
                        // show timePickerDialog
                        new MyTimePickerDialog().show(((MainActivity) getActivity()).getSupportFragmentManager(), null);
                        // update DB
                        todoDB.updateTodo(mainActivityViewModel.handlingTodo);
                        ((MainActivity) getActivity()).adapter.updateTodoList();
                        // todo set background reminder
                        dismiss();
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        return datePickerDialog;
    }
}
