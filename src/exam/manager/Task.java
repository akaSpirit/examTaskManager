package exam.manager;

import java.time.LocalDate;

public class Task {
    private Integer id;
    private LocalDate date;
    private String name;
    private String type;
    private String description;

    public Task(Integer id, LocalDate date, String name, String type, String description) {
        this.id = id;
        this.date = date;
        this.name = name;
        this.type = type;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
