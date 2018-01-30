package eu.epitech.todolosit.Model;

/**
 * Created by levuoj on 30/01/18.
 */

public class ToDo {
    private String id;
    private String title;
    private String description;

    public ToDo(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
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
}
