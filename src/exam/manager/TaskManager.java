package exam.manager;

import java.time.LocalDate;
import java.util.List;

public class TaskManager {
    private String currentDate;
    private String month;
    private List<LocalDate> days;
    private List<Task> tasks;

    public TaskManager(LocalDate date, List<LocalDate> days, List<Task> tasks) {
        this.currentDate = date.toString();
        this.month = date.getMonth().toString();
        this.days = days;
        this.tasks = tasks;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public String getMonth() {
        return month;
    }

    public List<LocalDate> getDays() {
        return days;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setDays(List<LocalDate> days) {
        this.days = days;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
