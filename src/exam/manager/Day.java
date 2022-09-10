package exam.manager;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

public class Day {
    private final LocalDate date;
    private final Integer day;
    private final Month month;
    private final Integer year;
    List<Task> tasks;

    public Day(LocalDate date, List<Task> tasks) {
        this.date = date;
        this.month = date.getMonth();
        this.day = date.getDayOfMonth();
        this.year = date.getYear();
        this.tasks = tasks;
    }

    public LocalDate getDate() {
        return date;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Integer getDay() {
        return day;
    }

    public Month getMonth() {
        return month;
    }

    public Integer getYear() {
        return year;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
