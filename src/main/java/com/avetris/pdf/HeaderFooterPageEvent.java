package com.avetris.pdf;

import java.io.IOException;

import com.avetris.managers.ConfigManager;
import com.avetris.models.Config;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {  
    
    private PdfPTable headerTable;
    private float headerTableHeight;

    public HeaderFooterPageEvent() {
        // Create your header table here
        headerTable = new PdfPTable(3); // One column for the header
        headerTable.setWidthPercentage(100); // Make it span the page width
        try {
            Image logo = Image.getInstance(ConfigManager.getInstance().getLogoPath());
            logo.scaleToFit(50, 50); // Scale image to fit (width, height)

            PdfPCell headerCell = new PdfPCell(logo);
            headerCell.setHorizontalAlignment(Element.ALIGN_LEFT); // Align image within its cell
            headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            headerCell.setBorder(Rectangle.NO_BORDER); // Remove border if not needed
            headerCell.setPadding(0); // Adjust padding as needed
            headerCell.setPaddingLeft(50);
            headerTable.addCell(headerCell);

            // writer.getDirectContent().addImage(image, true);    
        } catch (IOException | com.itextpdf.text.BadElementException e) {
            System.err.println("Error loading image: " + e.getMessage());
            PdfPCell errorCell = new PdfPCell(new Phrase("Image Not Found"));
            errorCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(errorCell);
        }       
        
         // --- Add the Company Name/Document Title Cell ---
       PdfPCell titleCell = new PdfPCell(new Phrase(ConfigManager.getInstance().getConfig().getWeb()));
            titleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            titleCell.setBorder(Rectangle.NO_BORDER);
            titleCell.setPaddingBottom(5);
            headerTable.addCell(titleCell);

            PdfPCell emptyCell = new PdfPCell(new Phrase(""));
            emptyCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(emptyCell);

            headerTable.setTotalWidth(523);
            headerTableHeight = headerTable.getTotalHeight();
    }

    public float getHeaderTableHeight() {
        return headerTableHeight;
    }

    /*public void onStartPage(PdfWriter writer, Document document) {
        
        Config config = ConfigManager.getInstance().getConfig();

        try {
            Image image = Image.getInstance(ConfigManager.getInstance().getLogoPath());
            image.setAlignment(Element.ALIGN_RIGHT);
            image.setAbsolutePosition(50, 760);
            image.scalePercent(10f, 10f);
            writer.getDirectContent().addImage(image, true);    
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), 30, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(config.getWeb()), 400, 800, 0);
    }*/
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        float yPosition = document.getPageSize().getTop() - 20;
        headerTable.writeSelectedRows(0, -1, document.left(), yPosition, canvas);
    }    
}
