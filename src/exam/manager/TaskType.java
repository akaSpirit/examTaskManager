package exam.manager;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum TaskType {
    ROUTINE("routine"),
    URGENT("urgent"),
    WORK("work"),
    SHOPPING("shopping"),
    OTHER("other");

    private String str;

    TaskType(String str) {
        this.str = str;
    }

    public static List<String> getValues(){
        return  Arrays.stream(TaskType.values())
                .map(TaskType::toString)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return str;
    }
}
