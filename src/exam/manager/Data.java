package exam.manager;

import java.time.LocalDate;
import java.util.List;

public class Data {
    private LocalDate currentDate;
    private String month;
    private List<LocalDate> days;
    private List<Task> tasks;

    public Data(LocalDate currentDate, List<LocalDate> days, List<Task> tasks) {
        this.currentDate = currentDate;
        this.month = currentDate.getMonth().toString();
        this.days = days;
        this.tasks = tasks;
    }

    public LocalDate getCurrentDate() {
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

    public void setCurrentDate(LocalDate currentDate) {
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
