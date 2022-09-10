package exam.manager;

import java.util.List;

public class AddTask {
    private String date;
    private List<String> types;

    public AddTask(String date, List<String> types) {
        this.date = date;
        this.types = types;
    }

    public String getDate() {
        return date;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }
}
