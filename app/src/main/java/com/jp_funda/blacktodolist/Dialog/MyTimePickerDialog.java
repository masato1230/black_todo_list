package com.jp_funda.blacktodolist.Dialog;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.jp_funda.blacktodolist.Activities.MainActivity;
import com.jp_funda.blacktodolist.Activities.TodoListFragment;
import com.jp_funda.blacktodolist.Database.TodoDatabaseHandler;
import com.jp_funda.blacktodolist.Recycler.TodoRecyclerViewAdapter;
import com.jp_funda.blacktodolist.ViewModels.MainActivityViewModel;

import java.util.Calendar;

public class MyTimePickerDialog extends DialogFragment {
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

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        // update ViewModel:handlingTask
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        mainActivityViewModel.handlingTodo.setRemindDate(calendar.getTime());
                        // update DB
                        todoDB.updateTodo(mainActivityViewModel.handlingTodo);
                        ((MainActivity) getActivity()).adapter.updateTodoList();
                        dismiss();
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        return timePickerDialog;
    }
}
