package com.avetris.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.avetris.managers.ConfigManager;
import com.avetris.models.Budget;
import com.avetris.models.Config;
import com.avetris.models.Task;
import com.avetris.utils.FileManager;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfManager {

    static final int FONT_SIZE = 10;
    public static void createPDF(Budget budget) {
        try {
            String path = FileManager.getFilePath("presupuestos/" + budget.getId() + ".pdf");
            File f = new File(path);
            if(!f.exists()) {
                f.getParentFile().mkdirs();
            } else {
                f.delete();
            }
            Document document = new Document(PageSize.A4, 0, 0, 100, 50);
            try {
                PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
                HeaderFooterPageEvent headerEvent = new HeaderFooterPageEvent();
                writer.setPageEvent(headerEvent);
                document.setMargins(10, 10, headerEvent.getHeaderTableHeight() + 40, 50);
            } catch (FileNotFoundException fileNotFoundException) {
                System.out.println("No such file was found to generate the PDF "
                        + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
            }
            document.open();

            writeMetadata(document, budget);
            writeBudgetInfo(document, budget);
            writeTasks(document, budget);

            document.close();   
            Desktop.getDesktop().open(new File(path));
        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        } catch (IOException exception) {
            System.out.println("The file cannot be opened: " + exception);
        }        
    }

    private static void writeMetadata(Document document, Budget budget) {
        document.addTitle(String.format("Presupuesto %s", budget.getId()));
        Config config = ConfigManager.getInstance().getConfig();
        document.addAuthor(config.getName());
        document.addCreator(config.getName());
    }

    private static void writeBudgetInfo(Document document, Budget budget) throws DocumentException {
        PdfPTable table = new PdfPTable(3);

        PdfPCell idCell = new PdfPCell();
        Phrase idParagraph = new Phrase();       
        idParagraph.add(new Chunk("Nº ", new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD)));
        idParagraph.add(new Chunk(budget.getId(), new Font(FontFamily.TIMES_ROMAN, FONT_SIZE)));
        idCell.addElement(idParagraph);
        idCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(idCell); 

        
        PdfPCell budgetCell = new PdfPCell();
        Phrase budgetParagraph = new Phrase("PRESUPUESTO ", new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD));
        budgetCell.addElement(budgetParagraph);
        budgetCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(budgetCell); 
        
        PdfPCell dateCell = new PdfPCell();
        Phrase dateParagraph = new Phrase(); 
        dateParagraph.add(new Chunk("FECHA ", new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD)));
        dateParagraph.add(new Chunk(budget.getDate(), new Font(FontFamily.TIMES_ROMAN, FONT_SIZE)));        
        dateCell.addElement(dateParagraph);
        dateCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(dateCell); 

        table.setSpacingAfter(10);

        document.add(table);
    }
    
    private static void writeTasks(Document document, Budget budget) throws DocumentException {
        Integer numColumns = 3;
        // We create the table (Creamos la tabla).
        PdfPTable table = new PdfPTable(numColumns);
        table.setTotalWidth(new float[]{ 22, 200, 28});

        table.setSplitLate(false);
        table.setSplitRows(false);
        // Now we fill the PDF table 
        // Fill table rows (rellenamos las filas de la tabla).
        table.addCell("");
        table.addCell(getProjectCell(budget.getProject()));
        table.addCell("");

        for (Task task : budget.getTasks()) {
            table.addCell(new PdfPCell());
            table.addCell(createTaskCell(task));
            table.addCell(createPriceCell(task));
        }
        // We add the paragraph with the table (Añadimos el elemento con la tabla).
        document.add(table);
    }

    private static PdfPCell getProjectCell(String project) {
        PdfPCell cell = new PdfPCell();
        Paragraph paragraph = new Paragraph(project, new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD));
        cell.addElement(paragraph);
        cell.setPaddingBottom(10);
        return cell;    
    }

    private static PdfPCell createTaskCell(Task task) {
        PdfPCell cell = new PdfPCell();
        Paragraph taskTitle = new Paragraph(task.getTitle(), new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.UNDERLINE));
        cell.addElement(taskTitle);
        if(!task.getDescription().isEmpty()) {
            Paragraph taskDescription = new Paragraph(task.getDescription(), new Font(FontFamily.TIMES_ROMAN, FONT_SIZE));
            cell.addElement(taskDescription);
        }
        cell.setPaddingBottom(10);    
        return cell;        
    }

    private static PdfPCell createPriceCell(Task task) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        Paragraph price = new Paragraph(task.getPrice() + " €", new Font(FontFamily.TIMES_ROMAN, FONT_SIZE));
        price.setAlignment(Paragraph.ALIGN_RIGHT);
        cell.addElement(price);
        cell.setPaddingBottom(10);
        return cell;        
    }
}
