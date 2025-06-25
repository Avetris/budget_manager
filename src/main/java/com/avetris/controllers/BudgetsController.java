package com.avetris.controllers;

import java.time.LocalDate;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.avetris.listeners.IBudgetListener;
import com.avetris.managers.BudgetManager;
import com.avetris.models.Budget;
import com.avetris.pdf.PdfManager;
import com.avetris.ui.dialogs.InfoDialog;
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
    public boolean onSubmit(Budget newBudget) {
        model.copy(newBudget);
        return BudgetManager.getInstance().addBudget(getModel(), (model.getId() == null || model.getId().length() == 0));
    }

    @Override
    public void onGeneratePdf(String id) {
        Budget budget = BudgetManager.getInstance().getBudget(id);
        if (budget != null) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(null);
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {                
                if(!PdfManager.createPDF(budget, fileChooser.getSelectedFile().getAbsolutePath())) {
                    new InfoDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Error", "Ha habido un error al generar el documento. Cierra el documento si lo tienes abierto y vuelve a intentarlo.", true);        
                }
            } else {
                new InfoDialog((JFrame) SwingUtilities.getWindowAncestor(view), "Error", "No has seleccionado el archivo donde guardar el presupuesto.", true);                        
            }
        }
    }

    @Override
    public void onGeneratePdf() {
        if(this.model != null) {
            onGeneratePdf(this.model.getId());
        }
    }

    public Budget getModel() {
        return this.model;
    }

    public String getNewId(LocalDate date) {
        return BudgetManager.getInstance().getNewId(date.getYear());
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
        onBudgetListUpdated();
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
