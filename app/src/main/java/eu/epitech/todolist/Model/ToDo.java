package eu.epitech.todolist.Model;

/**
 * Created by levuoj on 30/01/18.
 */

public class ToDo {
    private String id;
    private String title;
    private String description;
    private String date;
    private String status;

    public ToDo() {
    }

    public ToDo(String id, String title, String description, String date, String status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getStatus() { return status; }
}
