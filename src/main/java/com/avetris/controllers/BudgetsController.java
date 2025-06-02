package com.avetris.controllers;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.avetris.listeners.IBudgetListener;
import com.avetris.managers.BudgetManager;
import com.avetris.models.Budget;
import com.avetris.pdf.PdfManager;
import com.avetris.ui.dialogs.ModifyBudgetDialog;
import com.avetris.ui.views.BudgetsTab;

public class BudgetsController implements IBudgetListener {

    private BudgetsTab view;

    private ModifyBudgetDialog dialog;
    private Budget model;

    private String filter = "";

    public BudgetsController(BudgetsTab view) {
        this.view = view;
        this.view.addListener(this);
        BudgetManager.getInstance().attach(this);
        BudgetManager.getInstance().readBugdgets();
    }

    @Override
    public void onSubmit(Budget newBudget) {
        model.copy(newBudget);
        BudgetManager.getInstance().addBudget(getModel());
    }

    @Override
    public void onGeneratePdf() {
        if (model != null) {
            PdfManager.createPDF(model);            
        }
    }

    public Budget getModel() {
        return this.model;
    }

    public void setFilter(String filter) {
        this.filter = filter;
        onBudgetListUpdated();
    }

    public void showDialog(String budgetId) {
        if(this.dialog != null) {
            this.dialog.dispose();
        }
        if(budgetId == null || budgetId.isEmpty()) {
            this.model = new Budget();
        } else {
            this.model = BudgetManager.getInstance().getBudget(budgetId);
        }
        this.dialog = new ModifyBudgetDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Modificar Presupuesto", this, true);
    }

    public void closeDialog(){
        this.dialog = null;
        if(this.model != null) {
            this.model = null;
        }
    }

    @Override
    public void onBudgetListUpdated() {
        this.view.updateView(BudgetManager.getInstance().getBudgets(filter), filter.isEmpty());
    }

    @Override
    public void onRemoveBudget(String id) {
        BudgetManager.getInstance().removeBudget(id);
    }
}
