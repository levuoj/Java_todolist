package eu.epitech.todolosit.Adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eu.epitech.todolosit.Model.ToDo;
import eu.epitech.todolosit.R;
import eu.epitech.todolosit.Todolist;

/**
 * Created by levuoj on 30/01/18.
 */

class ListItemViewHolder extends ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
{
    ItemClickListener itemClickListener;
    TextView item_title;
    TextView item_description;
    TextView item_date;

    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = (TextView) itemView.findViewById(R.id.item_title);
        item_description = (TextView) itemView.findViewById(R.id.item_description);
        item_date = (TextView) itemView.findViewById(R.id.item_date);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0, 0, getAdapterPosition(), "DELETE");
    }
}

public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    Todolist todolist;
    List<ToDo> todoList;

    public ListItemAdapter(Todolist todolist, List<ToDo> todoList) {
        this.todolist = todolist;
        this.todoList = todoList;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(todolist.getBaseContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        holder.item_title.setText(todoList.get(position).getTitle());
        holder.item_description.setText(todoList.get(position).getDescription());
        holder.item_date.setText(todoList.get(position).getDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                todolist.title.setText(todoList.get(position).getTitle());
                todolist.description.setText(todoList.get(position).getDescription());
                todolist.date.setText(todoList.get(position).getDate());

                todolist.isUpdate = true;
                todolist.idUpdate = todoList.get(position).getId();
            }
        });
    }

    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
