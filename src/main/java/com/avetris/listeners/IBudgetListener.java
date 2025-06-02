package com.avetris.listeners;

import com.avetris.models.Budget;

public interface IBudgetListener {
    public void onSubmit(Budget newBudget);
    public void onGeneratePdf();
    public void setFilter(String filter);
    public void onBudgetListUpdated();
    public void showDialog(String id);
    public void onRemoveBudget(String id);    
}
