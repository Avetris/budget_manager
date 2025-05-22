package com.avetris.controllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.avetris.listeners.ITaskListener;
import com.avetris.managers.TasksManager;
import com.avetris.models.Task;
import com.avetris.ui.dialogs.ModifyTaskDialog;
import com.avetris.ui.views.TasksTab;

public class TaskController implements ITaskListener {

    private TasksTab view;

    private ModifyTaskDialog dialog;
    private Task model;

    private String filter = "";

    public TaskController(TasksTab view) {
        this.view = view;
        this.view.addListener(this);
        TasksManager.getInstance().attach(this);
        TasksManager.getInstance().readTasks();
    }

    @Override
    public void onSubmit(Task newTask) {
        model.copy(newTask);
        TasksManager.getInstance().addTask(getModel());
    }

    public Task getModel() {
        return this.model;
    }

    public void setFilter(String filter) {
        this.filter = filter;
        onTaskListUpdate();
    }

    public void showDialog(int taskId) {
        if(this.dialog != null) {
            this.dialog.dispose();
        }
        if(taskId == -1) {
            this.model = new Task();
        } else {
            this.model = TasksManager.getInstance().getTask(taskId);
        }
        this.dialog = new ModifyTaskDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Modificar Trabajo", this, true);
    }

    public void closeDialog(){
        this.dialog = null;
        if(this.model != null) {
            this.model = null;
        }
    }

    @Override
    public void onTaskListUpdate() {
        if(filter.isEmpty()){
            this.view.updateView(TasksManager.getInstance().getTasks().toArray(new Task[0]), false);
        } else {            
            this.view.updateView(TasksManager.getInstance().filterTask(filter).toArray(new Task[0]), true);
        }
    }

    @Override
    public void onRemoveTask(int id) {
        TasksManager.getInstance().removeTask(id);
    }
}
