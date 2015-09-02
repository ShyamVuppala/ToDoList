package com.utilities.shyamv.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


/**
 * An activity representing a single ToDoTask detail screen. This
 * activity is only used on handset devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ToDoTaskListActivity}.
 * <p/>
 * This activity is mostly just a 'shell' activity containing nothing
 * more than a {@link ToDoTaskDetailFragment}.
 */
public class ToDoTaskDetailActivity extends AppCompatActivity {

    private int currentItemPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todotask_detail);

        // Show the Up button in the action bar.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null)
        {
            currentItemPos = getIntent().getIntExtra(ToDoTaskDetailFragment.ARG_ITEM_POS, -1);

            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putInt(ToDoTaskDetailFragment.ARG_ITEM_POS, currentItemPos);

            ToDoTaskDetailFragment fragment = new ToDoTaskDetailFragment();
            fragment.setArguments(arguments);

            getSupportFragmentManager().beginTransaction().add(R.id.todotask_detail_container, fragment).commit();
        }
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode)
    {
        intent.putExtra(ToDoTaskListActivity.ARG_REQUEST_CODE, requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_to_do_task_detail, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == android.R.id.home)
        {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            setResult(RESULT_CANCELED);
            NavUtils.navigateUpTo(this, new Intent(this, ToDoTaskListActivity.class));
            return true;
        }
        else if(id == R.id.action_edit_task)
        {
            ToDoContent.ToDoTaskItem curItem  =  ((ToDoTaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_detail_container)).getCurrentItem();
            Intent addTask = new Intent(this, CreateToDoTaskActivity.class);
            addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE, curItem.getTitle());
            addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC, curItem.getDescription());
            addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE, curItem.getDate());
            addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME, curItem.getTime());
            startActivityForResult(addTask, ToDoTaskListActivity.REQUEST_EDIT_ITEM);
        }
        else if(id == R.id.action_delete_task)
        {
            Intent intent = new Intent();
            intent.putExtra(ToDoTaskDetailFragment.ARG_ITEM_POS, currentItemPos);
            setResult(ToDoTaskListActivity.RESULT_DELETE_ITEM, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == ToDoTaskListActivity.REQUEST_EDIT_ITEM)
        {
            if(resultCode == RESULT_OK)
            {
                String title = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE);
                String desc = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC);
                String date = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE);
                String time = data.getStringExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME);
                ToDoContent.ToDoTaskItem item = ((ToDoTaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_detail_container)).getCurrentItem();
                item.setAll(title, desc, date, time);

                ToDoTaskListActivity.pInstance.updateToDoTask(item);

                ((ToDoTaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_detail_container)).updateView();
            }
            else if(resultCode == RESULT_CANCELED)
            {
            }
        }
    }

    public void onClickScreen(View v)
    {
        ToDoContent.ToDoTaskItem curItem  =  ((ToDoTaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.todotask_detail_container)).getCurrentItem();
        Intent addTask = new Intent(this, CreateToDoTaskActivity.class);
        addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_TITLE, curItem.getTitle());
        addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_DESC, curItem.getDescription());
        addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_DATE, curItem.getDate());
        addTask.putExtra(ToDoTaskDetailFragment.ARG_ITEM_TIME, curItem.getTime());
        startActivityForResult(addTask, ToDoTaskListActivity.REQUEST_EDIT_ITEM);
    }
}
