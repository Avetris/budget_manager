package com.avetris.managers;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.avetris.listeners.IBudgetListener;
import com.avetris.models.Budget;
import com.avetris.utils.FileManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

class LocalDateDeserializer implements JsonDeserializer<LocalDate>, JsonSerializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), DateTimeFormatter.ofPattern("yyyy/MM/dd"));
    }

    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) throws JsonParseException {
        return context.serialize(src.format(DateTimeFormatter.ofPattern("yyyy/MM/dd")));
    }

}

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

    private Gson getGson() {
        var builder = new GsonBuilder();
        builder.registerTypeAdapter(LocalDate.class, new LocalDateDeserializer());
        return builder.create();
    }

    public void readBugdgets() {
        if(budgets.size() == 0) {
            Gson gson = getGson();
            for (String fileName : FileManager.getFilesInDirectory(BILLS_PATH)) 
            {
                try {
                    String content = FileManager.readFile(BILLS_PATH + "/" + fileName, "{}");
                    Budget budget = gson.fromJson(content, Budget.class);
                    budgets.put(budget.getId(), budget);
                    if(listener != null) {
                        listener.onBudgetListUpdated();
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                }            
            }
        } else {
            if(listener != null) {
                listener.onBudgetListUpdated();
            }
        }
    }
    
    public Budget[] getBudgets(String filter) {
        List<Budget> filtered = new ArrayList<>();
        if(filter == null || filter.isEmpty()) {
            filtered = new ArrayList<Budget>(budgets.values());
        } else {
            filter = filter.toLowerCase();
            for(Budget budget : budgets.values()) {
                if(budget.getProject().toLowerCase().contains(filter) || budget.getId().toLowerCase().contains(filter) || budget.getClient().containsFilter(filter)) {
                    filtered.add(budget);
                }
            }
        }
        filtered.sort((a, b) -> b.getDate().compareTo(a.getDate()));
        return filtered.toArray(new Budget[filtered.size()]);
    }

    public String getNewId(int year) {
        int lastId = 1;
        String id = String.format("%d-%03d", year, lastId);
        boolean exists = budgets.containsKey(id);
        while(exists) {
            lastId++;
            id = String.format("%d-%03d", year, lastId);
            exists = budgets.containsKey(id);
        }
        return id;
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
        String content = getGson().toJson(budgets.get(id));
        FileManager.saveFile(BILLS_PATH + "/" + id + ".json", content);        
        if(listener != null) {
            listener.onBudgetListUpdated();
        }
    }

    public void removeBudget(String id) {
        if(budgets.containsKey(id)) {
            FileManager.removeFile(String.format("%s/%s.json", BILLS_PATH, id));
            budgets.remove(id);
            if(listener != null) {
                listener.onBudgetListUpdated();
            }
        }
    }
}
