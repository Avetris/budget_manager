package com.avetris.listeners;

import com.avetris.models.Task;

public interface ITaskListener {
    public void onSubmit(Task newTask);
    public void setFilter(String filter);
    public void onTaskListUpdate();
    public void showDialog(int id);
    public void onRemoveTask(int id);
    
}
