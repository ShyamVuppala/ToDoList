package com.utilities.shyamv.todolist;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ToDoContent {

    public static List<ToDoTaskItem> ITEMS = new ArrayList<ToDoTaskItem>();

    /*static {
        // Add 3 sample items.
        addItem(new ToDoTaskItem("1", "Item 1"));
        addItem(new ToDoTaskItem("2", "Item 2"));
        addItem(new ToDoTaskItem("3", "Item 3"));
    }*/

    public static void addItem(ToDoTaskItem item)
    {
        ITEMS.add(item);
    }

    public static class ToDoTaskItem
    {
        private long id;
        private String title;
        private String description;
        private String date;
        private String time;

        public ToDoTaskItem(long id, String title, String description, String date, String time)
        {
            setAll(id, title, description, date, time);
        }

        public ToDoTaskItem(String title, String description, String date, String time)
        {
            setAll(getUniqueID(), title, description, date, time);
        }

        private long getUniqueID()
        {
            if(ITEMS.size() != 0)
            {
                ToDoTaskItem lastItem = ITEMS.get(ITEMS.size() - 1);
                return lastItem.id + 1;
            }
            else
                return 0;
        }

        void setAll(String title, String description, String date, String time)
        {
            this.title = title;
            this.description = description;
            this.date = date;
            this.time = time;
        }

        void setAll(long id, String title, String description, String date, String time)
        {
            this.id = id;
            this.title = title;
            this.description = description;
            this.date = date;
            this.time = time;
        }

        @Override
        public String toString() {
            return "ToDoTaskItem{" +
                    "content='" + description + '\'' +
                    ", id=" + id +
                    ", title='" + title + '\'' +
                    '}';
        }

        public long getId()
        {
            return id;
        }

        public void setId(long id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getDescription()
        {
            return description;
        }

        public void setDescription(String description)
        {
            this.description = description;
        }

        public String getDate()
        {
            return date;
        }

        public void setDate(String date)
        {
            this.date = date;
        }

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }
    }
}
