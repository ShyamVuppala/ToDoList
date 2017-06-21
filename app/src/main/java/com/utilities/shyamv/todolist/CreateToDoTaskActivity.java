package com.utilities.shyamv.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class CreateToDoTaskActivity extends AppCompatActivity implements View.OnClickListener
{
    EditText titleText;
    EditText toDoTaskText;
    Button datePickerBtn;
    Button timePickerBtn;

    // Variable for storing current date and time
    private int mYear, mMonth, mDay, mHour, mMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do_task);

        titleText = (EditText) findViewById(R.id.titleText);
        toDoTaskText = (EditText) findViewById(R.id.toDoTaskTxt);
        datePickerBtn = (Button) findViewById(R.id.datePickerBtn);
        datePickerBtn.setOnClickListener(this);
        timePickerBtn = (Button) findViewById(R.id.timePickerBtn);
        timePickerBtn.setOnClickListener(this);

        int requestId = getIntent().getIntExtra(ToDoTaskListActivity.ARG_REQUEST_CODE, -1);

        if(requestId == ToDoTaskListActivity.REQUEST_ADD_ITEM)
        {
            Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            String date = DateTimeUtils.getDateString(mDay, mMonth + 1, mYear);
            datePickerBtn.setText(date);

            String time = DateTimeUtils.getTimeString(mHour, mMinute);
            timePickerBtn.setText(time);
        }
        else if(requestId == ToDoTaskListActivity.REQUEST_EDIT_ITEM)
        {
            String title = getIntent().getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE);
            titleText.setText(title);

            String desc = getIntent().getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC);
            toDoTaskText.setText(desc);

            String date = getIntent().getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE);
            datePickerBtn.setText(date);

            String time = getIntent().getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME);
            timePickerBtn.setText(time);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_to_do_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cancel_task)
        {
            setResult(RESULT_CANCELED);
            finish();
        }
        else if (id == R.id.action_save_task)
        {
            String title = titleText.getText().toString();
            String desc = toDoTaskText.getText().toString();
            String date = datePickerBtn.getText().toString();
            String time = timePickerBtn.getText().toString();
            Calendar calendar = DateTimeUtils.getCalendar(date, time);

            if(title.length() == 0)
                Toast.makeText(this, "Title should be set", Toast.LENGTH_LONG).show();
            else if(calendar == null)
                Toast.makeText(this, "Date and Time should be set", Toast.LENGTH_LONG).show();
            else if(calendar.before(Calendar.getInstance()))
                Toast.makeText(this, "Please select upcoming date and time", Toast.LENGTH_LONG).show();
            else
            {
                Intent intent = new Intent();
                intent.putExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE, title);
                intent.putExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC, desc);
                intent.putExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE, date);
                intent.putExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME, time);
                setResult(RESULT_OK, intent);
                finish();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        if(v == datePickerBtn)
        {
            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener()
                    {
                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth)
                        {
                            datePickerBtn.setText(DateTimeUtils.getDateString(dayOfMonth, monthOfYear + 1, year));
                        }
                    }, mYear, mMonth, mDay);
            dpd.show();
        }
        else if(v == timePickerBtn)
        {
            // Process to get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener()
                    {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                        {
                            timePickerBtn.setText(DateTimeUtils.getTimeString(hourOfDay, minute));
                        }
                    }, mHour, mMinute, false);
            tpd.show();
        }
    }
}
