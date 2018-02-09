package eu.epitech.todolist.Adapter;

import android.view.View;

/**
 * Created by levuoj on 30/01/18.
 */

/**
 * Interface Item Click Listener
 * @author  Anthony
 * @version 2.0
 * @see ListItemViewHolder
 */
public interface ItemClickListener {
    /**
     *
     * @param view
     *
     * @param position
     * position on click
     * @param isLongClick
     * if long click True, otherwise false
     */
    void onClick(View view, int position, boolean isLongClick);
}
