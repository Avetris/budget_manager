package com.avetris.controllers;

import com.avetris.interfaces.ITaskEdit;
import com.avetris.managers.TasksManager;
import com.avetris.models.Task;
import com.avetris.views.ModifyTaskDialog;

public class TaskController extends AbstractController implements ITaskEdit {

    public TaskController(ModifyTaskDialog view, Task model) {
        super(view, model);
        view.attach(this);
    }

    @Override
    protected void finalize() throws Throwable {
        removeModel();
        super.finalize();
    }

    @Override
    public void onSubmit() {
        TasksManager.getInstance().addTask((Task)getModel());
    }
}
