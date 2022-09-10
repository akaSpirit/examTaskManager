package exam.manager;

import com.sun.net.httpserver.HttpExchange;
import exam.server.BasicServer;
import exam.server.ContentType;
import exam.server.ResponseCodes;
import exam.server.Utils;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class TaskManagerServer extends BasicServer {
    private final static Configuration freemarker = initFreeMarker();
    private final Data data;

    public TaskManagerServer(String host, int port) throws IOException {
        super(host, port);
        data = new Data(LocalDate.now(), generateDays(LocalDate.now()), generateTasks(generateDays(LocalDate.now())));
        registerGet("/", this::taskManagerHandler);
        registerGet("/tasks", this::tasksGet);
        registerGet("/task/add", this::addGet);
        registerGet("/task/edit", this::editHandle);
        registerGet("/task", this::taskGet);
        registerPost("/task/add", this::addPost);
        registerPost("/task/edit", this::editHandle);
    }

    private void taskManagerHandler(HttpExchange exchange) {
        renderTemplate(exchange, "taskmanager.html", new TaskManager(LocalDate.now(), data.getDays(), data.getTasks()));
    }

    private void responseNotFound(HttpExchange exchange, String query, String value) {
        try {
            String response = String.format("<p>%s with parameter %s not found</p><a href=\"/\">Back to main</a>",
                    query, value);
            var data = response.getBytes();
            sendByteData(exchange, ResponseCodes.NOT_FOUND, ContentType.TEXT_HTML, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void taskGet(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        String query = params.getOrDefault("query", "");
        if (query.equals("delete")) {
            String strId = params.getOrDefault("id", "");

            data.setTasks(data.getTasks().stream()
                    .filter(t -> !String.valueOf(t.getId()).equals(strId))
                    .collect(Collectors.toList()));
            String date = params.get("date");
            String url = String.format("/tasks?query=list&date=%s", date);
            redirect303(exchange, url);
        }
        if (query.isEmpty()) {
            responseNotFound(exchange, query, "");
            return;
        }
        redirect303(exchange, "/");
    }

    private void tasksGet(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        String query = params.getOrDefault("query", "");
        if (query.isEmpty()) {
            responseNotFound(exchange, query, "");
            return;
        }
        if (query.equals("list")) {
            String str = params.getOrDefault("date", "");
            LocalDate date = data.getDays().stream()
                    .filter(d -> d.toString().equals(str))
                    .findFirst()
                    .orElse(null);
            if (Objects.isNull(date)) {
                responseNotFound(exchange, query, str);
                return;
            }
            List<Task> tasks = data.getTasks().stream()
                    .filter(t -> t.getDate().equals(date))
                    .collect(Collectors.toList());
            renderTemplate(exchange, "task.html", new Day(date, tasks));
        }
    }

    private void addGet(HttpExchange exchange) {
        String queryParams = getQueryParams(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
        String date = params.getOrDefault("date", "");
        List<String> types = TaskType.getValues();
        renderTemplate(exchange, "add.html", new AddTask(date, types));
    }

    private void addPost(HttpExchange exchange) {
        String raw = getBody(exchange);
        Map<String, String> params = Utils.parseUrlEncoded(raw, "&");
        String strDate = params.getOrDefault("date", "0000-00-00");
        String name = params.getOrDefault("name", "");
        String desc = params.getOrDefault("desc", "");
        String type = params.getOrDefault("type", "other");
        LocalDate date = data.getDays().stream()
                .filter(d -> d.toString().equals(strDate))
                .findFirst()
                .orElse(null);
        Integer i = data.getTasks().stream()
                .max(Comparator.comparingInt(Task::getId))
                .get()
                .getId() + 1;
        if (date == null) {
            redirect303(exchange, "/");
            return;
        }
        data.getTasks().add(new Task(i, date, name, type, desc));
        String url = String.format("/tasks?query=list&date=%s", date);
        redirect303(exchange, url);
    }

    private void editHandle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        if (method.equalsIgnoreCase("GET")) {
            String queryParams = getQueryParams(exchange);
            Map<String, String> params = Utils.parseUrlEncoded(queryParams, "&");
            String strId = params.get("id");
            Integer id = Integer.parseInt(strId);
            Task task = data.getTasks().stream()
                    .filter(t -> t.getId().equals(id))
                    .findFirst()
                    .get();
            List<String> types = TaskType.getValues();
            renderTemplate(exchange, "edit.html", new EditTask(task, types));
            return;
        }

        if (method.equalsIgnoreCase("POST")) {
            String raw = getBody(exchange);
            Map<String, String> parsed = Utils.parseUrlEncoded(raw, "&");
            String strId = parsed.get("id");
            Integer id = Integer.parseInt(strId);
            String name = parsed.get("name");
            String desc = parsed.get("desc");
            String type = parsed.get("type");
            for (Task task : data.getTasks()) {
                if (task.getId().equals(id)) {
                    task.setName(name);
                    task.setDescription(desc);
                    task.setType(type);
                }
            }
            String date = parsed.get("date");
            String url = String.format("/tasks?query=list&date=%s", date);
            redirect303(exchange, url);
            return;
        }
        redirect303(exchange, "/");
    }

    private List<LocalDate> generateDays(LocalDate today) {
        List<LocalDate> days = new ArrayList<>();
        int count = 1;
        int year = today.getYear();
        int monthValue = today.getMonthValue();
        while (count <= today.lengthOfMonth()) {
            LocalDate date = LocalDate.of(year, monthValue, count);
            days.add(date);
            count += 1;
        }
        return days;
    }

    private List<Task> generateTasks(List<LocalDate> days) {
        int count = 0;
        List<TaskType> types = List.of(TaskType.values());
        List<Task> tasks = new ArrayList<>();
        while (count < 60) {
            LocalDate date = days.get(new Random().nextInt(days.size()));
            TaskType type = types.get(new Random().nextInt(types.size()));
            String taskName = Generator.makeName();
            String taskDesc = Generator.makeDescription();
            count += 1;
            Task task = new Task(count, date, taskName, type.toString(), taskDesc);
            tasks.add(task);
        }
        return tasks;
    }

    protected String getQueryParams(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        return Objects.nonNull(query) ? query : "";
    }

    private static Configuration initFreeMarker() {
        try {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_29);
            cfg.setDirectoryForTemplateLoading(new File("data"));
            cfg.setDefaultEncoding("UTF-8");
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            cfg.setLogTemplateExceptions(false);
            cfg.setWrapUncheckedExceptions(true);
            cfg.setFallbackOnNullLoopVariable(false);
            return cfg;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected void renderTemplate(HttpExchange exchange, String templateFile, Object dataModel) {
        try {
            Template temp = freemarker.getTemplate(templateFile);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            try (OutputStreamWriter writer = new OutputStreamWriter(stream)) {
                temp.process(dataModel, writer);
                writer.flush();
                var data = stream.toByteArray();
                sendByteData(exchange, ResponseCodes.OK, ContentType.TEXT_HTML, data);
            }
        } catch (TemplateException | IOException e) {
            e.printStackTrace();
        }
    }
}
