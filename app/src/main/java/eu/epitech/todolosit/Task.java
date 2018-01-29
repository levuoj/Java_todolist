package eu.epitech.todolosit;

import java.util.Date;

/**
 * Created by levuoj on 29/01/18.
 */

public class        Task {
    private String  title_;
    private String  content_;
    private Date    date_;

    public Task() {
    }

    public Task(String title, String content, Date date) {
        title_ = title;
        content_ = content;
        date_ = date;
    }

    public void setContent_(String content_) {
        this.content_ = content_;
    }

    public void setDate_(Date date_) {
        this.date_ = date_;
    }

    public void setTitle_(String title_) {
        this.title_ = title_;
    }

    public Date getDate_() {
        return date_;
    }

    public String getContent_() {
        return content_;
    }

    public String getTitle_() {
        return title_;
    }
}
