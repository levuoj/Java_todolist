package eu.epitech.todolosit.Adapter;

import android.view.View;

/**
 * Created by levuoj on 30/01/18.
 */

public interface ItemClickListener {
    void onClick(View view, int position, boolean isLongClick);
}
