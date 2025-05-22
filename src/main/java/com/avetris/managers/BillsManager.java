package com.avetris.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.avetris.listeners.IBillListener;
import com.avetris.models.Bill;
import com.avetris.models.Task;
import com.avetris.utils.FileManager;
import com.google.gson.Gson;

public class BillsManager {

    private final String BILLS_PATH = FileManager.getFilePath("bills");

    private static BillsManager _instance;

    private IBillListener listener;

    private HashMap<String, Bill> bills = new HashMap<String, Bill>();

    public static BillsManager getInstance() {
        if (_instance == null) {
            _instance = new BillsManager();
        }
        return _instance;
    }

    private BillsManager() {
        readBills(); 
    }

    public void attach(IBillListener listener) {
        this.listener = listener;
    }

    public void readBills() {
        for (String fileName : FileManager.getFilesInDirectory(BILLS_PATH)) 
        {
            try {
                String content = FileManager.readFile(fileName, "{}");
                Gson gson = new Gson();  
                Bill bill = gson.fromJson(content, Bill.class);
                bills.put(bill.getId(), bill);
                if(listener != null) {
                    listener.onBillListUpdated();
                }
            } catch(Exception e) {

            }            
        }
    }
    
    public Bill[] getBills(String filter) {
        if(filter == null || filter.isEmpty()) {
            return bills.values().toArray(new Bill[bills.size()]);
        }
        List<Bill> filtered = new ArrayList<>();
        filter = filter.toLowerCase();
        for(Bill bill : bills.values()) {
            if(bill.getProject().toLowerCase().contains(filter) || bill.getId().toLowerCase().contains(filter) || bill.getClient().containsFilter(filter)) {
                filtered.add(bill);
            }
        }
        return filtered.toArray(new Bill[filtered.size()]);
    }

    public Bill getBill(String id) {
        return bills.get(id);
    }
    
    public boolean addBill(Bill bill) {
        if(bills.containsKey(bill.getId())) {
            return false;
        }
        bills.put(bill.getId(), bill);
        return true;
    }

    public void removeBill(String id) {
        if(bills.containsKey(id)) {
            FileManager.removeFile(String.format("%s/%s", BILLS_PATH, bills.get(id).getId()));
            bills.remove(id);
        }
    }
}
