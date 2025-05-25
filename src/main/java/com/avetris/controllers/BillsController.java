package com.avetris.controllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.avetris.listeners.IBillListener;
import com.avetris.managers.BillsManager;
import com.avetris.models.Bill;
import com.avetris.pdf.PdfManager;
import com.avetris.ui.dialogs.ModifyBillDialog;
import com.avetris.ui.views.BillsTab;

public class BillsController implements IBillListener {

    private BillsTab view;

    private ModifyBillDialog dialog;
    private Bill model;

    private String filter = "";

    public BillsController(BillsTab view) {
        this.view = view;
        this.view.addListener(this);
        BillsManager.getInstance().attach(this);
        BillsManager.getInstance().readBills();
    }

    @Override
    public void onSubmit(Bill newBill) {
        model.copy(newBill);
        BillsManager.getInstance().addBill(getModel());
    }

    @Override
    public void onGeneratePdf() {
        if (model != null) {
            PdfManager.createPDF(model);            
        }
    }

    public Bill getModel() {
        return this.model;
    }

    public void setFilter(String filter) {
        this.filter = filter;
        onBillListUpdated();
    }

    public void showDialog(String billId) {
        if(this.dialog != null) {
            this.dialog.dispose();
        }
        if(billId == null || billId.isEmpty()) {
            this.model = new Bill();
        } else {
            this.model = BillsManager.getInstance().getBill(billId);
        }
        this.dialog = new ModifyBillDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Modificar Factura", this, true);
    }

    public void closeDialog(){
        this.dialog = null;
        if(this.model != null) {
            this.model = null;
        }
    }

    @Override
    public void onBillListUpdated() {
        this.view.updateView(BillsManager.getInstance().getBills(filter), filter.isEmpty());
    }

    @Override
    public void onRemoveBill(String id) {
        BillsManager.getInstance().removeBill(id);
    }
}
