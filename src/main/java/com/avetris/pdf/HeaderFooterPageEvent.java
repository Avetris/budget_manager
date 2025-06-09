package com.avetris.pdf;

import java.io.IOException;

import com.avetris.managers.ConfigManager;
import com.avetris.models.Config;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {  

    final int FONT_SIZE = 10;
    
    private PdfPTable headerTable;
    private PdfPTable firstHeaderTable;

    private float headerTableHeight;
    private float firstHeaderTableHeight;

    public HeaderFooterPageEvent() {
        Config config = ConfigManager.getInstance().getConfig();
        Font configFont = new Font(FontFamily.TIMES_ROMAN, FONT_SIZE);
        Font infoFont = new Font(FontFamily.TIMES_ROMAN, 6);

        // Create your header table here
        headerTable = new PdfPTable(3); // One column for the header
        firstHeaderTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100); // Make it span the page width
        firstHeaderTable.setWidthPercentage(100); // Make it span the page width

        try {
            // Define relative column widths. These will apply to the total table width.
            // e.g., 100 parts for col1, 75 for col2, 75 for col3
            headerTable.setWidths(new float[]{100f, 75f, 75f});
            firstHeaderTable.setWidths(new float[]{100f, 75f, 75f});

            // --- First Page Header: Cell 1 (Logo + Centered Config Text) ---
            PdfPCell firstPageMainCell = new PdfPCell();
            firstPageMainCell.setHorizontalAlignment(Element.ALIGN_CENTER); // Center content block within this cell
            firstPageMainCell.setVerticalAlignment(Element.ALIGN_TOP);    // Align content block to the top of the cell
            firstPageMainCell.setBorder(Rectangle.NO_BORDER);
            firstPageMainCell.setPaddingBottom(5f); // Optional: add some padding at the bottom of this cell

            Image logo = Image.getInstance(ConfigManager.getInstance().getLogoPath());
            logo.scaleToFit(75, 75); // Scale image to fit (width, height)
            logo.setAlignment(Element.ALIGN_CENTER); // Center the image itself within its allocated space
            firstPageMainCell.addElement(logo);

            // Add config text below the logo, each centered
            if (config.getStreet() != null && !config.getStreet().isEmpty()) {
                Paragraph p = new Paragraph(config.getStreet(), configFont);
                p.setAlignment(Element.ALIGN_CENTER);
                firstPageMainCell.addElement(p);
            }
            if (config.getCity() != null && !config.getCity().isEmpty()) {
                Paragraph p = new Paragraph(config.getCity(), configFont);
                p.setAlignment(Element.ALIGN_CENTER);
                firstPageMainCell.addElement(p);
            }
            if (config.getPhone() != null && !config.getPhone().isEmpty()) {
                Paragraph p = new Paragraph("Movil: " + config.getPhone(), configFont);
                p.setAlignment(Element.ALIGN_CENTER);
                firstPageMainCell.addElement(p);
            }
            if (config.getEmail() != null && !config.getEmail().isEmpty()) {
                Chunk anchor = new Chunk(config.getEmail(), configFont);
                anchor.setAnchor("mailto:" + config.getEmail());
                Paragraph p = new Paragraph(anchor);
                p.setAlignment(Element.ALIGN_CENTER);
                firstPageMainCell.addElement(p);
            }
            if (config.getNif() != null && !config.getNif().isEmpty()) {
                Paragraph p = new Paragraph("NIF: " + config.getNif(), configFont);
                p.setAlignment(Element.ALIGN_CENTER);
                firstPageMainCell.addElement(p);
            }
            firstHeaderTable.addCell(firstPageMainCell);

            // --- Subsequent Pages Header: Cell 1 (Logo only or simpler) ---
            Image logoSubsequent = Image.getInstance(ConfigManager.getInstance().getLogoPath()); // Use a new instance
            logoSubsequent.scaleToFit(75, 75);
            PdfPCell subsequentPageMainCell = new PdfPCell(logoSubsequent);
            subsequentPageMainCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            subsequentPageMainCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            subsequentPageMainCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(subsequentPageMainCell);
            
        } catch (IOException | DocumentException e) {
            System.err.println("Error processing header content: " + e.getMessage());
            PdfPCell errorCell = new PdfPCell(new Phrase("Header Error"));
            errorCell.setBorder(Rectangle.NO_BORDER);
            headerTable.addCell(new PdfPCell(errorCell)); // Add to both tables to maintain column structure
            firstHeaderTable.addCell(new PdfPCell(errorCell));
        }       
        
        // --- Common Cells for Columns 2 and 3 (Title/Web and Empty) ---
        Chunk webChunk;
        if (config.getWeb() != null && !config.getWeb().isEmpty()) {
            webChunk = new Chunk(config.getWeb(), configFont);
            // Ensure the anchor has a protocol
            if (!config.getWeb().toLowerCase().startsWith("http://") && !config.getWeb().toLowerCase().startsWith("https://")) {
                webChunk.setAnchor("http://" + config.getWeb());
            } else {
                webChunk.setAnchor(config.getWeb());
            }
        } else {
            webChunk = new Chunk(""); // Use an empty chunk if web is not set
        }

        PdfPCell webCellTemplate = new PdfPCell(new Phrase(webChunk));
        webCellTemplate.setHorizontalAlignment(Element.ALIGN_CENTER);
        webCellTemplate.setVerticalAlignment(Element.ALIGN_TOP);
        webCellTemplate.setBorder(Rectangle.NO_BORDER);
        webCellTemplate.setPaddingBottom(5);

        headerTable.addCell(new PdfPCell(webCellTemplate)); // Add a copy to headerTable
        firstHeaderTable.addCell(new PdfPCell(webCellTemplate)); // Add a copy to firstHeaderTable
        
        PdfPCell emptyCellTemplate = new PdfPCell(new Phrase(""));
        emptyCellTemplate.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(emptyCellTemplate);

        if(config.getInfo().length == 0) { 
            firstHeaderTable.addCell(new PdfPCell(emptyCellTemplate));            
        } else {
            PdfPCell infoCell = new PdfPCell();
            infoCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            infoCell.setVerticalAlignment(Element.ALIGN_TOP);
            infoCell.setBorder(Rectangle.NO_BORDER);
            for(var info : config.getInfo()) {
                Paragraph p = new Paragraph(info, infoFont);
                p.setAlignment(Element.ALIGN_CENTER);
                infoCell.addElement(p);
            }
            firstHeaderTable.addCell(infoCell);
        }

        // Set the total width of the tables. This is important for writeSelectedRows.
        // 523 is a common value for A4 width (595pt) minus default L/R margins (e.g., 36pt each).
        headerTable.setTotalWidth(523);
        firstHeaderTable.setTotalWidth(523);

        // Calculate actual heights after cells are added and widths are set.
        // Calling getRows() helps iText compute the layout and height.
        headerTable.getRows(); 
        headerTableHeight = headerTable.getTotalHeight();

        firstHeaderTable.getRows();
        firstHeaderTableHeight = firstHeaderTable.getTotalHeight();

        // Fallback check if heights are still zero (though getRows() should prevent this)
        if (firstHeaderTableHeight == 0 && firstHeaderTable.size() > 0) {
            firstHeaderTableHeight = firstHeaderTable.getTotalHeight();
        }
        if (headerTableHeight == 0 && headerTable.size() > 0) {
            headerTableHeight = headerTable.getTotalHeight();
        }
    }

    public float getFirstHeaderTableHeight() {
        // Return the maximum height to ensure enough margin space is allocated
        return firstHeaderTableHeight;
    }
    

    public float getHeaderTableHeight() {
        // Return the maximum height to ensure enough margin space is allocated
        return headerTableHeight;
    }
    
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte canvas = writer.getDirectContent();
        float yPosition = document.getPageSize().getTop() - 20;

        if (writer.getPageNumber() == 1) {
            firstHeaderTable.writeSelectedRows(0, -1, document.left(), yPosition, canvas);
        } else {
            headerTable.writeSelectedRows(0, -1, document.left(), yPosition, canvas);
        }
    }    
}
