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

public class Todolist extends AppCompatActivity {

    LinearLayout one;

    List<ToDo> toDoList = new ArrayList<>();
    FirebaseFirestore db;

    RecyclerView listItem;
    RecyclerView.LayoutManager layoutManager;

    FloatingActionButton fab;

    public MaterialEditText title;
    public MaterialEditText description;
    public MaterialEditText date;
    public MaterialEditText status;
    public SeekBar seekBar;

    public boolean isUpdate = false;
    public String idUpdate = "";

    ListItemAdapter adapter;

    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);

        db = FirebaseFirestore.getInstance();

        int currentMax = 20;
        int currentStep = 10;

        one = (LinearLayout) findViewById(R.id.layout_info);

        dialog = new SpotsDialog(this);
        title = (MaterialEditText) findViewById(R.id.title);
        description = (MaterialEditText) findViewById(R.id.description);
        date = (MaterialEditText) findViewById(R.id.date);
        seekBar = (SeekBar) findViewById(R.id.status);
        fab = (FloatingActionButton) findViewById(R.id.fab);

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

        listItem = (RecyclerView) findViewById(R.id.listTodo);
        listItem.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        listItem.setLayoutManager(layoutManager);

        loadData();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "DELETE")
            deleteItem(item.getOrder());
        return super.onContextItemSelected(item);
    }

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
