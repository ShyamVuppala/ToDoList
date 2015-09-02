package com.utilities.shyamv.todolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A fragment representing a single ToDoTask detail screen.
 * This fragment is either contained in a {@link ToDoTaskListActivity}
 * in two-pane mode (on tablets) or a {@link ToDoTaskDetailActivity}
 * on handsets.
 */
public class ToDoTaskDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_POS = "item_pos";
    public static final String ARG_ITEM_TITLE = "item_title";
    public static final String ARG_ITEM_DESC = "item_description";
    public static final String ARG_ITEM_DATE = "item_date";
    public static final String ARG_ITEM_TIME = "item_time";

    /**
     * The dummy content this fragment is presenting.
     */
    private ToDoContent.ToDoTaskItem mItem;
    private TextView mTitleTxt;
    private TextView mDescriptionTxt;
    private TextView mTimeTxt;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ToDoTaskDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_POS)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ToDoContent.ITEMS.get(getArguments().getInt(ARG_ITEM_POS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_todotask_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            mTitleTxt = (TextView) rootView.findViewById(R.id.titleText);
            mTitleTxt.setText(mItem.getTitle());

            mDescriptionTxt = (TextView) rootView.findViewById(R.id.descriptionText);
            mDescriptionTxt.setText(mItem.getDescription());

            mTimeTxt = (TextView) rootView.findViewById(R.id.dateTimeText);
            mTimeTxt.setText(mItem.getDate() + " " + mItem.getTime());
        }

        return rootView;
    }

    public ToDoContent.ToDoTaskItem getCurrentItem()
    {
        return mItem;
    }

    public void updateView()
    {
        mTitleTxt.setText(mItem.getTitle());
        mDescriptionTxt.setText(mItem.getDescription());
        mTimeTxt.setText(mItem.getDate() + " " + mItem.getTime());
    }
}
