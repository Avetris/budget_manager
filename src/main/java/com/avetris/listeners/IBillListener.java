package com.avetris.listeners;

import com.avetris.models.Bill;

public interface IBillListener {
    public void onSubmit(Bill newBill);
    public void setFilter(String filter);
    public void onBillListUpdated();
    public void showDialog(String id);
    public void onRemoveBill(String id);
    
}
