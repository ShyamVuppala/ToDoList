package com.utilities.shyamv.todolist;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by ShyamV on 8/24/2015.
 */

public class ToDoListAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private List<ToDoContent.ToDoTaskItem> toDoItems;

    public ToDoListAdapter(Activity activity, List<ToDoContent.ToDoTaskItem> toDoItems)
    {
        this.activity = activity;
        this.toDoItems = toDoItems;

        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return toDoItems.size();
    }

    @Override
    public Object getItem(int location)
    {
        return toDoItems.get(location);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_item, null);

        TextView title = (TextView) convertView.findViewById(R.id.titleText);
        TextView description = (TextView) convertView.findViewById(R.id.descriptionText);
        TextView time = (TextView) convertView.findViewById(R.id.timeDateText);

        ToDoContent.ToDoTaskItem m = toDoItems.get(position);

        title.setText(m.getTitle());
        description.setText(m.getDescription());
        time.setText(m.getDate() + " " + m.getTime());

        return convertView;
    }

}
