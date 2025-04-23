package com.avetris.managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avetris.models.Task;
import com.avetris.utils.FileManager;
import com.google.gson.Gson;

public class TasksManager {

    private final String TASK_PATH = "tasks.json";

    private static TasksManager _instance;

    private List<Task> tasks = new ArrayList<Task>();

    public static TasksManager getInstance() {
        if (_instance == null) {
            _instance = new TasksManager();
        }
        return _instance;
    }
    

    private TasksManager() {
        readTasks();
    }

    private void readTasks() {
        try {
            String content = FileManager.readFile(TASK_PATH);
            Gson gson = new Gson();
            tasks = Arrays.asList(gson.fromJson(content, Task[].class)); 
        } catch (Exception exception) {
        }
        
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> filterTask(String filter) {
        List<Task> filtered = new ArrayList<>();
        for(Task task : tasks) {
            if(task.getTitle().contains(filter) || task.getDescription().contains(filter)) {
                filtered.add(task);
            }
        }
        return filtered;
    }

    public Task getTask(int id) {
        for(Task task : tasks) {
            if(task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public void saveTasks() {        
        String content = new Gson().toJson(tasks);
        FileManager.saveFile(TASK_PATH, content);
    }

    private int getLastId() {
        int lastId = 0;
        for(Task task : tasks) {
            if(task.getId() > lastId) {
                lastId = task.getId();
            }
        }
        return lastId;
    }

    public void addTask(Task task) {
        if(task.getId() == 0) {
            task.setId(getLastId() + 1);
            tasks.add(task);
        } else {
            for(Task t : tasks) {
                if(t.getId() == task.getId()) {
                    tasks.set(tasks.indexOf(t), task);
                    break;
                }
            }
        }
        saveTasks();
    }

    public void removeTask(Task task) {
        tasks.remove(task);
        saveTasks();
    }
}
