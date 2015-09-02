package com.utilities.shyamv.todolist;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Calendar;

//TODO: implement swipe to scroll between messages

public class ToDoTaskListActivity extends AppCompatActivity
        implements ToDoTaskListFragment.Callbacks
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    public final static String ARG_REQUEST_CODE = "requestCode";
    public final static String ARG_TASK_ID = "task_id";
    public final static String ARG_TASK_TITLE = "task_title";
    public final static int REQUEST_ADD_ITEM = 1;
    public final static int REQUEST_EDIT_ITEM = 2;
    public final static int RESULT_DELETE_ITEM = RESULT_FIRST_USER + 1;

    public static ToDoTaskListActivity pInstance = null;

    private DatabaseHandler mDatabase = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todotask_list);

        if (findViewById(R.id.todotask_detail_container) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-large and
            // res/values-sw600dp). If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            // In two-pane mode, list items should be given the
            // 'activated' state when touched.
            ((ToDoTaskListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.todotask_list))
                    .setActivateOnItemClick(true);
        }

        pInstance = this;

        mDatabase = new DatabaseHandler(this);

        loadToDoTaskListFromDB();
    }

    @Override
    protected void onDestroy()
    {
        pInstance = null;
        mDatabase.close();
        super.onDestroy();
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        intent.putExtra(ARG_REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_do_task_list, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_add_task)
        {
            Intent addTask = new Intent(this, CreateToDoTaskActivity.class);
            startActivityForResult(addTask, REQUEST_ADD_ITEM);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_ADD_ITEM && resultCode == RESULT_OK)
        {
            String title = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE);
            String desc = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC);
            String date = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE);
            String time = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME);

            addToDoTask(new ToDoContent.ToDoTaskItem(title, desc, date, time));
        }
        else if(requestCode == REQUEST_EDIT_ITEM)
        {
            if(resultCode == RESULT_DELETE_ITEM)
            {
                int itemPos = data.getIntExtra(ToDoTaskDetailFragment.ARG_ITEM_POS, -1);
                deleteToDoTask(ToDoContent.ITEMS.get(itemPos));
            }
            else if(resultCode == RESULT_OK)
            {
                String title = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE);
                String desc = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC);
                String date = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE);
                String time = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME);
                int itemPos = data.getIntExtra(ToDoTaskDetailFragment.ARG_ITEM_POS, -1);

                ToDoContent.ToDoTaskItem item = ToDoContent.ITEMS.get(itemPos);
                item.setAll(title, desc, date, time);
                updateToDoTask(item);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Callback method from {@link ToDoTaskListFragment.Callbacks}
     * indicating that the item with the given ID was selected.
     */
    @Override
    public void onItemSelected(int itemPos)
    {
        if (mTwoPane)
        {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ToDoTaskDetailFragment.ARG_ITEM_POS, itemPos);
            ToDoTaskDetailFragment fragment = new ToDoTaskDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.todotask_detail_container, fragment)
                    .commit();

        }
        else
        {
            // In single-pane mode, simply start the detail activity
            // for the selected item ID.
            Intent detailIntent = new Intent(this, ToDoTaskDetailActivity.class);
            detailIntent.putExtra(ToDoTaskDetailFragment.ARG_ITEM_POS, itemPos);
            startActivityForResult(detailIntent, REQUEST_EDIT_ITEM);
        }
    }

    public void addToDoTask(ToDoContent.ToDoTaskItem task)
    {
        long id = mDatabase.addToDoTask(task);
        task.setId(id);
        ToDoContent.ITEMS.add(task);
        ((ToDoTaskListFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_list)).updateView();

        setTaskReminder(task);
    }

    public void updateToDoTask(ToDoContent.ToDoTaskItem task)
    {
        mDatabase.updateToDoItem(task);
        ((ToDoTaskListFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_list)).updateView();

        setTaskReminder(task);
    }

    public void deleteToDoTask(ToDoContent.ToDoTaskItem task)
    {
        cancelTaskReminder(task);

        mDatabase.deleteToDoItem(task);
        ToDoContent.ITEMS.remove(task);
        ((ToDoTaskListFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_list)).updateView();
    }

    public void loadToDoTaskListFromDB()
    {
        mDatabase.loadAllToDoItems();
        ((ToDoTaskListFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_list)).updateView();
    }

    public void setTaskReminder(ToDoContent.ToDoTaskItem task)
    {
        Calendar calendar = DateTimeUtils.getCalendar(task.getDate(), task.getTime());

        if(calendar != null)
        {
            Intent myIntent = new Intent("com.utilities.shyamv.todolist.action.notify", Uri.parse("Notification:" + task.getId()), this, NotifyService.class);
            myIntent.putExtra(ARG_TASK_ID, task.getId());
            myIntent.putExtra(ARG_TASK_TITLE, task.getTitle());
            PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

    public void cancelTaskReminder(ToDoContent.ToDoTaskItem task)
    {
        Intent myIntent = new Intent("com.utilities.shyamv.todolist.action.notify", Uri.parse("Notification:" + task.getId()), this, NotifyService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);
    }
}
