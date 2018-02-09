package eu.epitech.todolist.Model;

/**
 * Created by levuoj on 30/01/18.
 */

/**
 * Taske ToDo
 * @author  Anthony
 * @version 2.0
 */
public class ToDo {
    /**
     * The Id from a Task is unique
     * @see ToDo#ToDo(String, String, String, String, Integer)
     * @see ToDo#getId()
     */
    private String id;

    /**
     * Title
     * @see ToDo#ToDo(String, String, String, String, Integer)
     * @see ToDo#getTitle()
     */
    private String title;

    /**
     * Description
     * @see ToDo#ToDo(String, String, String, String, Integer)
     * @see ToDo#getDescription()
     */
    private String description;

    /**
     * Date
     * @see ToDo#ToDo(String, String, String, String, Integer)
     * @see ToDo#getDate()
     */
    private String date;

    /**
     * Date
     * @see ToDo#ToDo(String, String, String, String, Integer)
     * @see ToDo#getStatus()
     */
    private Integer status;

    /**
     * Constructor with all the parameter
     * @param id
     * each Task has an unique ID
     * @param title
     * Title
     * @param description
     * Describe the Task
     * @param date
     * Date to remember something
     * @param status
     * Status toDo/doing/done
     */
    public ToDo(String id, String title, String description, String date, Integer status) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.status = status;
    }

    /**
     * Get the Date
     * @return
     * date
     */
    public String getDate() {
        return date;
    }

    /**
     * Get the Description
     * @return
     * Desciption
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the ID
     * @return
     * ID
     */
    public String getId() {
        return id;
    }

    /**
     * Get the Title
     * @return
     * Title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Get the status
     * @return
     * Status
     */
    public Integer getStatus() { return status; }
}
