package com.avetris.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.avetris.listeners.IBudgetListener;
import com.avetris.models.Budget;
import com.avetris.utils.FileManager;
import com.google.gson.Gson;

public class BudgetManager {

    private final String BILLS_PATH = FileManager.getFilePath("budgets");

    private static BudgetManager _instance;

    private IBudgetListener listener;

    private HashMap<String, Budget> budgets = new HashMap<String, Budget>();

    public static BudgetManager getInstance() {
        if (_instance == null) {
            _instance = new BudgetManager();
        }
        return _instance;
    }

    private BudgetManager() {
        readBugdgets(); 
    }

    public void attach(IBudgetListener listener) {
        this.listener = listener;
    }

    public void readBugdgets() {
        for (String fileName : FileManager.getFilesInDirectory(BILLS_PATH)) 
        {
            try {
                String content = FileManager.readFile(BILLS_PATH + "/" + fileName, "{}");
                Gson gson = new Gson();  
                Budget budget = gson.fromJson(content, Budget.class);
                budgets.put(budget.getId(), budget);
                if(listener != null) {
                    listener.onBudgetListUpdated();
                }
            } catch(Exception e) {

            }            
        }
    }
    
    public Budget[] getBudgets(String filter) {
        if(filter == null || filter.isEmpty()) {
            return budgets.values().toArray(new Budget[budgets.size()]);
        }
        List<Budget> filtered = new ArrayList<>();
        filter = filter.toLowerCase();
        for(Budget budget : budgets.values()) {
            if(budget.getProject().toLowerCase().contains(filter) || budget.getId().toLowerCase().contains(filter) || budget.getClient().containsFilter(filter)) {
                filtered.add(budget);
            }
        }
        return filtered.toArray(new Budget[filtered.size()]);
    }

    public Budget getBudget(String id) {
        return budgets.get(id);
    }
    
    public boolean addBudget(Budget budget, boolean isNew) {
        if(budgets.containsKey(budget.getId()) && isNew) {
            return false;
        }
        budgets.put(budget.getId(), budget); 
        save(budget.getId());
        return true;
    }

    private void save(String id) {        
        String content = new Gson().toJson(budgets.get(id));
        FileManager.saveFile(BILLS_PATH + "/" + id + ".json", content);
    }

    public void removeBudget(String id) {
        if(budgets.containsKey(id)) {
            FileManager.removeFile(String.format("%s/%s", BILLS_PATH, budgets.get(id).getId()));
            budgets.remove(id);
        }
    }
}
