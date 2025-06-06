package com.avetris.listeners;

import com.avetris.models.Budget;

public interface IBudgetListener {
    public boolean onSubmit(Budget newBudget);
    public void onGeneratePdf();
    public void onGeneratePdf(String id);
    public void setFilter(String filter);
    public void onBudgetListUpdated();
    public void showDialog(String id);
    public void onRemoveBudget(String id);    
}
