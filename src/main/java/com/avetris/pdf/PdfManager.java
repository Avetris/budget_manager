package com.avetris.pdf;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.avetris.managers.ConfigManager;
import com.avetris.models.Bill;
import com.avetris.models.Config;
import com.avetris.utils.FileManager;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

public class PdfManager {
    public static void createPDF(Bill bill) {
        try {
            String path = FileManager.getFilePath("facturas/" + bill.getId() + ".pdf");
            File f = new File(path);
            if(!f.exists()) {
                f.getParentFile().mkdirs();
            } else {
                f.delete();
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4, 20, 20, 50, 25);
            PdfWriter writer = PdfWriter.getInstance(document, bos);
            HeaderFooterPageEvent event = new HeaderFooterPageEvent();
            writer.setPageEvent(event);

            writeMetadata(document, bill);
            writeTasks(document, bill);
        
            document.close();
            System.out.println("Your PDF file has been generated!(¡Se ha generado tu hoja PDF!");
        } catch (DocumentException documentException) {
            System.out.println("The file not exists (Se ha producido un error al generar un documento): " + documentException);
        }
    }

    private static void writeMetadata(Document document, Bill bill) {
        document.addTitle(String.format("Factura %s", bill.getId()));
        Config config = ConfigManager.getInstance().getConfig();
        document.addAuthor(config.getName());
        document.addCreator(config.getName());
    }

    private static void writeTasks(Document document, Bill bill) {
        
    }        
}
