package eu.epitech.todolist.Adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import eu.epitech.todolist.Model.ToDo;
import eu.epitech.todolist.R;
import eu.epitech.todolist.Todolist;

/**
 * Created by levuoj on 30/01/18.
 */

/**
 * Listener on an Item
 * @author  Anthony
 * @version 2.0
 * @see  ItemClickListener
 * */
class ListItemViewHolder extends ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener
{
    /**
     * Click Listener
     * @see ListItemViewHolder#setItemClickListener(ItemClickListener itemClickListener)
     * @see ListItemViewHolder#onClick(View)
     */
    private ItemClickListener itemClickListener;

    /**
     * Title
     * @see ListItemViewHolder#ListItemViewHolder(View)
     */
    TextView item_title;

    /**
     * Description
     * @see ListItemViewHolder#ListItemViewHolder(View)
     */
    TextView item_description;

    /**
     * Date
     * @see ListItemViewHolder#ListItemViewHolder(View)
     */
    TextView item_date;

    /**
     * Status
     * @see ListItemViewHolder#ListItemViewHolder(View)
     */
    TextView item_status;

    /**
     * Constructor
     *  @param itemView
     *  View from list_item.xml
     *  Get the data from the View
    * */
    public ListItemViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);

        item_title = itemView.findViewById(R.id.item_title);
        item_description = itemView.findViewById(R.id.item_description);
        item_date = itemView.findViewById(R.id.item_date);
        item_status = itemView.findViewById(R.id.item_status);
    }

    /**
     * Set the Click Listenner
     * @param itemClickListener
     * the ItemListener
     */
    void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * Called when a view has been clicked.
     * @param v
     * The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        itemClickListener.onClick(v, getAdapterPosition(), false);
    }

    /**
     * Event when the Context Menu is created
     * @param menu
     * The context menu that is being built
     * @param v
     * The view for which the context menu is being built
     * @param menuInfo
     * Extra information about the item for which the context menu should be shown. This information will vary depending on the class of v.
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("Select the action");
        menu.add(0, 0, getAdapterPosition(), "DELETE");
    }
}

/**
 * Listener on Holder
 * @author  Anthony
 * @version 2.0
 * @see  ListItemViewHolder
 */
public class ListItemAdapter extends RecyclerView.Adapter<ListItemViewHolder> {

    /**
     * The core
     * @see ListItemAdapter#ListItemAdapter(Todolist, List)
     * @see ListItemAdapter#onCreateViewHolder(ViewGroup, int)
     * @see ListItemAdapter#onBindViewHolder(ListItemViewHolder, int)
     */
    private Todolist todolist;

    /**
     * List of Tasks
     * @see ListItemAdapter#ListItemAdapter(Todolist, List)
     * @see ListItemAdapter#onCreateViewHolder(ViewGroup, int)
     * @see ListItemAdapter#onBindViewHolder(ListItemViewHolder, int)
     * @see ListItemAdapter#getItemCount()
     */
    private List<ToDo> todoList;

    /**
     *
     * @param todolist
     * The Core
     * @param todoList
     * the list of Tasks
     */
    public ListItemAdapter(Todolist todolist, List<ToDo> todoList) {
        this.todolist = todolist;
        this.todoList = todoList;
    }

    /**
     * Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     * This new ViewHolder should be constructed with a new View that can represent the items of the given type. You can either create a new View manually or inflate it from an XML layout file.
     * @param parent
     * The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType
     * The view type of the new View.
     * @return
     * A new ViewHolder that holds a View of the given view type.
     */
    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(todolist.getBaseContext());
        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ListItemViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should update the contents of the itemView to reflect the item at the given position.
     * @param holder
     * The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position
     * The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        holder.item_title.setText(todoList.get(position).getTitle());
        holder.item_description.setText(todoList.get(position).getDescription());
        holder.item_date.setText(todoList.get(position).getDate());
        holder.item_status.setText("" + todoList.get(position).getStatus());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                todolist.title.setText(todoList.get(position).getTitle());
                todolist.description.setText(todoList.get(position).getDescription());
                todolist.date.setText(todoList.get(position).getDate());
                todolist.seekBar.setProgress(todoList.get(position).getStatus());

                todolist.isUpdate = true;
                todolist.idUpdate = todoList.get(position).getId();
            }
        });
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     * @return
     * The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return todoList.size();
    }
}
