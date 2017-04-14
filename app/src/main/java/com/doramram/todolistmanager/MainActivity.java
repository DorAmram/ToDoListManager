package com.doramram.todolistmanager;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.design.widget.FloatingActionButton;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static final String TAG = "MainActivity";

    List<Task> tasks;
    TaskAdapter adapter;
    DataBaseHelper helper;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new DataBaseHelper(this);

        tasks = helper.getAllTasks();

        adapter = new TaskAdapter(tasks, this, helper);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewTask();
            }
        });

    }

    private void addNewTask() {

        new AlertDialog.Builder(this).setView(
                getLayoutInflater().inflate(R.layout.new_task, null))
                .setTitle("Add New Task")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) ((AlertDialog) dialog).findViewById(R.id.task_edit_text);
                        DatePicker datePicker = (DatePicker) ((AlertDialog) dialog).findViewById(R.id.datePicker);

                        int id = helper.generateNewId();

                        String text = editText.getText().toString();
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                        Task task = new Task(id, text, calendar.getTime());

                        tasks.add(task);
                        helper.createTask(task);
                        Log.v(TAG, helper.getTableAsString("e_task"));

                        adapter.notifyDataSetChanged();

                    }
                }).setNegativeButton("Cancel", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            //open new Popping window (AlertDialog)
            case R.id.action_add_task:
                addNewTask();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
