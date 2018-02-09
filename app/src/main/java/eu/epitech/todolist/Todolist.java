package eu.epitech.todolist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.*;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import dmax.dialog.SpotsDialog;
import eu.epitech.todolist.Adapter.ListItemAdapter;
import eu.epitech.todolist.Model.ToDo;

/**
 * Todolist is the Core of the project
 * @author  Anthony
 * @version 2.0
 */
public class Todolist extends AppCompatActivity {

    /**
     * Layout of activity_todolist
     * @see Todolist#onCreate(Bundle)
     * @see Todolist#updateData(String, String, String, Integer)
     * @see Todolist#setData(String, String, String, Integer)
     */
    LinearLayout one;

    /**
     * List of Tasks
     * @see Todolist#deleteItem(int)
     * @see Todolist#loadData()
     */
    List<ToDo> toDoList = new ArrayList<>();

    /**
     * The DataBase using Firebase
     * @see Todolist#onCreate(Bundle)
     * @see Todolist#deleteItem(int)
     * @see Todolist#updateData(String, String, String, Integer)
     * @see Todolist#setData(String, String, String, Integer)
     * @see Todolist#loadData()
     */
    FirebaseFirestore db;

    /**
     * The items in the View
     * @see Todolist#onCreate(Bundle)
     * @see Todolist#loadData()
     */
    RecyclerView listItem;

    /**
     * Manage the Layout
     * @see Todolist#onCreate(Bundle)
     */
    RecyclerView.LayoutManager layoutManager;

    /**
     * Button to update/create a Task
     * @see Todolist#onCreate(Bundle)
     */
    FloatingActionButton fab;

    /**
     * Task's Title
     * @see Todolist#onCreate(Bundle)
     */
    public MaterialEditText title;

    /**
     * Task's Description
     * @see Todolist#onCreate(Bundle)
     */
    public MaterialEditText description;

    /**
     * Task's Description
     * @see Todolist#onCreate(Bundle)
     */
    public MaterialEditText date;

    /**
     * Task's Description
     * @see Todolist#onCreate(Bundle)
     */
    public SeekBar seekBar;

    /**
     * True if is Update, False otherwise
     * @see Todolist#onCreate(Bundle)
     */
    public boolean isUpdate = false;

    /**
     * Unique ID of the version
     * @see Todolist#onCreate(Bundle)
     */
    public String idUpdate = "";

    /**
     * Adapter used when DataBase is loaded
     * @see Todolist#db
     * @see Todolist#loadData()
     */
    ListItemAdapter adapter;

    /**
     * Dialog used when DataBase is loaded
     * @see Todolist#db
     * @see Todolist#loadData()
     */
    SpotsDialog dialog;

    /**
     * Constructor
     */
    public Todolist() {
    }

    /**
     * Called when the activity is starting.
     * @param savedInstanceState
     * If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        db = FirebaseFirestore.getInstance();

        int currentMax = 20;
        int currentStep = 10;

        one = findViewById(R.id.layout_info);

        dialog = new SpotsDialog(this);
        title = findViewById(R.id.title);
        date = findViewById(R.id.date);
        fab = findViewById(R.id.fab);
        description = findViewById(R.id.description);
        seekBar = findViewById(R.id.status);

        date.addTextChangedListener(new TextWatcher() {
            int len = 0;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = date.getText().toString();
                len = str.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = date.getText().toString();
                if ((str.length() == 2 && len < str.length()) ||
                        str.length() == 5) {
                    date.append("/");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        seekBar.setMax(currentMax / currentStep);
        seekBar.incrementProgressBy(currentStep);
        seekBar.setProgress(0);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isUpdate)
                {
                    setData(title.getText().toString(), description.getText().toString(), date.getText().toString(), seekBar.getProgress());
                }
                else
                {
                    updateData(title.getText().toString(), description.getText().toString(), date.getText().toString(), seekBar.getProgress());
                    isUpdate = !isUpdate;
                }
            }
        });

        listItem = findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        loadData();
    }

    /**
     * This hook is called whenever an item in a context menu is selected.
     * @param item
     * The context menu item that was selected.
     * @return
     * boolean Return false to allow normal context menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "DELETE")
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

    /**
     * Delete a Task from DataBase
     * @param index
     * The position of the Task to delete in DataBase
     */
    private void deleteItem(int index) {
        db.collection("ToDoList")
                .document(toDoList.get(index).getId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loadData();
                    }
                });
    }

    /**
     * Udpddate a Task
     * @param title
     * Task's title
     * @param description
     * Task's description
     * @param date
     * Task's date
     * @param status
     * Task's status
     */
    private void updateData(String title, String description, String date, Integer status) {
        if (title.length() == 0) {
            Snackbar snackbar = Snackbar
                    .make(one, "Title is mandatory", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        db.collection("ToDoList").document(idUpdate)
                .update("title", title, "description", description, "date", date, "status", status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(Todolist.this, "Updated !", Toast.LENGTH_SHORT).show();
                    }
                });

        db.collection("ToDoList").document(idUpdate)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                        loadData();
                    }
                });
    }

    /**
     * Create a Task
     * @param title
     * Task's title
     * @param description
     * Task's description
     * @param date
     * Task's date
     * @param status
     * Task's status
     */
    private void setData(String title, String description, String date, Integer status) {
        if (title.length() == 0) {
            Snackbar snackbar = Snackbar
                    .make(one, "Title is mandatory", Snackbar.LENGTH_LONG);
            snackbar.show();
            return;
        }
        String id = UUID.randomUUID().toString();
        Map<String, Object> todo = new HashMap<>();
        todo.put("id", id);
        todo.put("title", title);
        todo.put("description", description);
        todo.put("date", date);
        todo.put("status", status);

        db.collection("ToDoList").document(id)
                .set(todo).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                loadData();
            }
        });
    }

    /**
     * Load the DataBase
     */
    private void loadData() {
        dialog.show();
        if (toDoList.size() > 0)
            toDoList.clear();
        db.collection("ToDoList")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot doc: task.getResult())
                        {
                            ToDo toDo = new ToDo(doc.getString("id"),
                                                doc.getString("title"),
                                                doc.getString("description"),
                                                doc.getString("date"),
                                                doc.getLong("status").intValue());
                            toDoList.add(toDo);
                        }
                        adapter = new ListItemAdapter(Todolist.this, toDoList);
                        listItem.setAdapter(adapter);
                        dialog.dismiss();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Todolist.this, "+e.getMessage()", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    //   one.setVisibility(View.GONE);

}
