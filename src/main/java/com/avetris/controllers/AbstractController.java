package com.avetris.controllers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;

import com.avetris.models.AbstractModel;
import com.avetris.views.IViewPanel;

public abstract class AbstractController implements PropertyChangeListener {

    private IViewPanel view;
    private AbstractModel model;

    public AbstractController(IViewPanel view, AbstractModel model) {
        this.model = model;
        this.model.addPropertyChangeListener(this);
    }

    public void removeModel() {
        this.model.removePropertyChangeListener(this);
        this.model = null;
    }

    public AbstractModel getModel() {
        return this.model;
    }


    public void propertyChange(PropertyChangeEvent evt) {
        view.modelPropertyChange(evt);
    }


    /**
     * This is a convenience method that subclasses can call upon
     * to fire property changes back to the models. This method
     * uses reflection to inspect each of the model classes
     * to determine whether it is the owner of the property
     * in question. If it isn't, a NoSuchMethodException is thrown,
     * which the method ignores.
     *
     * @param propertyName = The name of the property.
     * @param newValue = An object that represents the new value
     * of the property.
     */
    protected void setModelProperty(String propertyName, Object newValue) {
        try {
            Method method = model.getClass().getMethod(
                "set"+propertyName, 
                new Class[] {
                    newValue.getClass()
                });
            method.invoke(model, newValue);

        } catch (Exception ex) {
            //  Handle exception.
        }
    }
}