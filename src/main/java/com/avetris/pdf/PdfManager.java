package com.avetris.pdf;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

import com.avetris.managers.ConfigManager;
import com.avetris.models.Budget;
import com.avetris.models.Client;
import com.avetris.models.Config;
import com.avetris.models.Task;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfManager {

    static final int FONT_SIZE = 10;
    static Font FONT_BOLD = new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD);
    static Font FONT_NORMAL = new Font(FontFamily.TIMES_ROMAN, FONT_SIZE);
    public static boolean createPDF(Budget budget, String path) {
        try {
            File f = new File(path);
            if(!f.exists()) {
                f.getParentFile().mkdirs();
            } else {
                f.delete();
            }
            // Initialize document with PageSize. Specific margins will be set before open().
            Document document = new Document(PageSize.A4); 

            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
            HeaderFooterPageEvent headerEvent = new HeaderFooterPageEvent();
            writer.setPageEvent(headerEvent);
            
            // Set margins for the first page BEFORE opening the document.
            // This ensures the first page is initialized with the correct space for its header.
            document.setMargins(10, 10, headerEvent.getFirstHeaderTableHeight(), 50);
            document.open();

            writeMetadata(document, budget);
            writeClientInfo(document, budget.getClient());
            writeBudgetInfo(document, budget);
            document.setMargins(10, 10, headerEvent.getHeaderTableHeight() + 40, 50);
            writeTasks(document, budget);
            writeTotal(document, budget);
            writeConditions(document);
            writeGaranty(document);

            document.close();   
            Desktop.getDesktop().open(new File(path));
            return true;
        } catch (FileNotFoundException fileNotFoundException) {
            System.out.println("No such file was found to generate the PDF "
                    + "(No se encontró el fichero para generar el pdf)" + fileNotFoundException);
            return false;
        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
            return false;
        } catch (IOException exception) {
            System.out.println("The file cannot be opened: " + exception);
            return false;
        }        
    }

    private static void writeMetadata(Document document, Budget budget) {
        document.addTitle(String.format("Presupuesto %s", budget.getId()));
        Config config = ConfigManager.getInstance().getConfig();
        document.addAuthor(config.getName());
        document.addCreator(config.getName());
    }

    private static void writeClientInfo(Document document, Client client) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        
        PdfPCell emptyCellTemplate = new PdfPCell(new Phrase(""));
        emptyCellTemplate.setBorder(Rectangle.NO_BORDER);
        table.addCell(new PdfPCell(emptyCellTemplate));
        table.addCell(new PdfPCell(emptyCellTemplate));
                
        PdfPCell clientCell = new PdfPCell();
        clientCell.setBorder(PdfPCell.NO_BORDER);
        clientCell.addElement(new Phrase(client.getName(), FONT_BOLD));
        clientCell.addElement(new Phrase((client.isCompany() ? "NIF " : "DNI ") + client.getNif(), FONT_BOLD));
        if(client.getAddress() != null && !client.getAddress().isEmpty()) {
            clientCell.addElement(new Phrase(client.getAddress(), FONT_BOLD));
        }
        table.addCell(clientCell); 
        table.setSpacingAfter(20);

        document.add(table);
    }

    private static void writeBudgetInfo(Document document, Budget budget) throws DocumentException {
        PdfPTable table = new PdfPTable(3);        

        PdfPCell idCell = new PdfPCell();
        Phrase idParagraph = new Phrase();       
        idParagraph.add(new Chunk("Nº ", FONT_BOLD));
        idParagraph.add(new Chunk(budget.getId(), FONT_NORMAL));
        idCell.addElement(idParagraph);
        idCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(idCell); 

        
        PdfPCell budgetCell = new PdfPCell();
        Phrase budgetParagraph = new Phrase(budget.getProject(), FONT_BOLD);
        budgetCell.addElement(budgetParagraph);
        budgetCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(budgetCell); 
        
        PdfPCell dateCell = new PdfPCell();
        Phrase dateParagraph = new Phrase(); 
        dateParagraph.add(new Chunk("FECHA ", FONT_BOLD));
        dateParagraph.add(new Chunk(budget.getDate().format(DateTimeFormatter.ofPattern("dd/MM/YYYY")), FONT_NORMAL));        
        dateCell.addElement(dateParagraph);
        dateCell.setBorder(PdfPCell.NO_BORDER);
        table.addCell(dateCell); 

        table.setSpacingAfter(10);

        document.add(table);
    }
    
    private static void writeTasks(Document document, Budget budget) throws DocumentException {
        PdfPTable table = new PdfPTable(4);
        table.setTotalWidth(new float[]{ 70, 6, 12, 12});

        table.setSplitLate(false);
        table.setSplitRows(false);
        
        table.addCell(createHeaderCell("Descripción"));
        table.addCell(createHeaderCell("Nº"));
        table.addCell(createHeaderCell("Precio"));
        table.addCell(createHeaderCell("Total"));

        for (Task task : budget.getTasks()) {
            if(task.getCount() > 0){
                table.addCell(createTaskCell(task));
                table.addCell(createCountCell(task));
                table.addCell(createPriceCell(task.getPrice()));
                table.addCell(createPriceCell(task.getPrice() * task.getCount()));
            }
        }
        table.setSpacingAfter(20);
        
        table.setHeaderRows(1);
        document.add(table);
    }



    private static void writeTotal(Document document, Budget budget) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        table.setTotalWidth(new float[] {30f, 30f, 40f});
        
        PdfPCell emptyCellTemplate = new PdfPCell(new Phrase(""));
        emptyCellTemplate.setBorder(Rectangle.NO_BORDER);
        table.addCell(new PdfPCell(emptyCellTemplate));
        table.addCell(new PdfPCell(emptyCellTemplate));
                
        // Base Label
        PdfPTable pricesSubTable = new PdfPTable(3);
        pricesSubTable.setTotalWidth(new float[] {30f, 10f, 20f});
        Paragraph base = new Paragraph("Base Imponible:", FONT_BOLD);
        base.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell baseCell = new PdfPCell(base);
        baseCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        baseCell.setBorder(PdfPCell.NO_BORDER);
        pricesSubTable.addCell(baseCell);        
        // Space
        pricesSubTable.addCell(new PdfPCell(emptyCellTemplate));
        // Price
        Paragraph basePrice = new Paragraph(formatPrice(budget.getTotal()), FONT_NORMAL);
        basePrice.setAlignment(Element.ALIGN_RIGHT);        
        PdfPCell basePriceCell = new PdfPCell(basePrice);
        basePriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        basePriceCell.setBorder(PdfPCell.NO_BORDER);
        pricesSubTable.addCell(basePriceCell);        
        // IVA Label
        Paragraph iva = new Paragraph("I.V.A:", FONT_BOLD);
        iva.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell ivaCell = new PdfPCell(iva);
        ivaCell.setBorder(PdfPCell.NO_BORDER);
        ivaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pricesSubTable.addCell(ivaCell);
        // IVA value
        Paragraph ivaValue = new Paragraph(budget.getIva() + "%", FONT_BOLD);
        ivaValue.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell ivaValueCell = new PdfPCell(ivaValue);
        ivaValueCell.setBorder(PdfPCell.NO_BORDER);
        ivaValueCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        pricesSubTable.addCell(ivaValueCell);
        // IVA Price
        Paragraph ivaPrice = new Paragraph(formatPrice(budget.getTotalIva()), FONT_NORMAL);
        ivaPrice.setAlignment(Element.ALIGN_RIGHT);        
        PdfPCell ivaPriceCell = new PdfPCell(ivaPrice);
        ivaPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        ivaPriceCell.setBorder(PdfPCell.NO_BORDER);
        pricesSubTable.addCell(ivaPriceCell);

        PdfPCell preTotalCell = new PdfPCell(pricesSubTable);
        preTotalCell.setBorderWidth(1);
        preTotalCell.setPaddingBottom(3);
        table.addCell(preTotalCell); 

        // TOTAL
        table.addCell(new PdfPCell(emptyCellTemplate));
        table.addCell(new PdfPCell(emptyCellTemplate));
        PdfPTable totalSubTable = new PdfPTable(3);
        totalSubTable.setTotalWidth(new float[] {30f, 10f, 20f});
        Paragraph total = new Paragraph("TOTAL", FONT_BOLD);
        total.setAlignment(Element.ALIGN_RIGHT);
        PdfPCell totalLabelCell = new PdfPCell(total);
        totalLabelCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalLabelCell.setBorder(PdfPCell.NO_BORDER);
        totalSubTable.addCell(totalLabelCell);        
        // Space
        totalSubTable.addCell(new PdfPCell(emptyCellTemplate));
        // Price
        Paragraph totalPrice = new Paragraph(formatPrice(budget.getTotalWithIva()), FONT_NORMAL);
        totalPrice.setAlignment(Element.ALIGN_RIGHT);        
        PdfPCell totalPriceCell = new PdfPCell(totalPrice);
        totalPriceCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        totalPriceCell.setBorderWidth(1);
        totalSubTable.addCell(totalPriceCell);

        PdfPCell totalCell = new PdfPCell(totalSubTable);
        totalCell.setBorder(PdfPCell.NO_BORDER);
        totalCell.setPaddingBottom(3);
        table.addCell(totalCell); 

        document.add(table);
    }

    private static String formatPrice(double price) {
        return String.format("%1$,.2f €", price);
    }

    private static PdfPCell createHeaderCell(String title) {
        PdfPCell cell = new PdfPCell();
        Paragraph header = new Paragraph(title, new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.BOLD));
        header.setAlignment(Paragraph.ALIGN_CENTER);
        cell.addElement(header);
        cell.setPaddingBottom(10);
        return cell;        
    }

    private static PdfPCell createTaskCell(Task task) {
        PdfPCell cell = new PdfPCell();
        Paragraph taskTitle = new Paragraph(task.getTitle(), new Font(FontFamily.TIMES_ROMAN, FONT_SIZE, Font.UNDERLINE));
        cell.addElement(taskTitle);
        if(!task.getDescription().isEmpty()) {
            Paragraph taskDescription = new Paragraph(task.getDescription(), FONT_NORMAL);
            cell.addElement(taskDescription);
        }
        cell.setPaddingBottom(10);    
        return cell;        
    }

    private static PdfPCell createCountCell(Task task) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        Paragraph count = new Paragraph(task.getCount() + "", FONT_NORMAL);
        count.setAlignment(Paragraph.ALIGN_CENTER);
        cell.addElement(count);
        cell.setPaddingBottom(10);
        return cell;        
    }

    private static PdfPCell createPriceCell(double price) {
        PdfPCell cell = new PdfPCell();
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        cell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
        Paragraph paragraph = new Paragraph(price + " €", FONT_NORMAL);
        paragraph.setAlignment(Paragraph.ALIGN_RIGHT);
        cell.addElement(paragraph);
        cell.setPaddingBottom(10);
        return cell;        
    }
    
    private static void writeConditions(Document document) throws DocumentException {
        document.newPage();
        PdfPTable table = new PdfPTable(1);

        PdfPCell cell = new PdfPCell();
        Paragraph conditionsTitle = new Paragraph("CONDICIONES GENERALES:", FONT_BOLD);
        conditionsTitle.setSpacingAfter(20); 
        cell.addElement(conditionsTitle);
        Paragraph conditions = new Paragraph(ConfigManager.getInstance().getConfig().getConditions(), FONT_NORMAL);
        conditions.setExtraParagraphSpace(10);
        cell.addElement(conditions);
        cell.setPaddingBottom(10);   

        table.addCell(cell);

        table.setExtendLastRow(true);
        document.add(table);
    }
    
    private static void writeGaranty(Document document) throws DocumentException {
        document.newPage();
        PdfPTable table = new PdfPTable(1);

        PdfPCell cell = new PdfPCell();
        Paragraph conditionsTitle = new Paragraph("GARANTÍA DE LOS TRABAJOS:", FONT_BOLD);
        conditionsTitle.setSpacingAfter(20); 
        cell.addElement(conditionsTitle);
        Paragraph conditions = new Paragraph(ConfigManager.getInstance().getConfig().getGaranty(), FONT_NORMAL);
        conditions.setExtraParagraphSpace(10);
        cell.addElement(conditions);
        cell.setPaddingBottom(10);   

        table.addCell(cell);

        table.setExtendLastRow(true);
        document.add(table);
    }
}
