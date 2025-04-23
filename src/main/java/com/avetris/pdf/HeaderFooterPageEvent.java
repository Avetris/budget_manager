package com.avetris.pdf;

import java.io.IOException;

import com.avetris.managers.ConfigManager;
import com.avetris.models.Config;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper {    
    public void onStartPage(PdfWriter writer, Document document) {
        
        Config config = ConfigManager.getInstance().getConfig();

        try {
            Image image = Image.getInstance(config.getIcon());
            image.setAlignment(Element.ALIGN_RIGHT);
            image.setAbsolutePosition(20, 790);
            image.scalePercent(7.5f, 7.5f);
            writer.getDirectContent().addImage(image, true);    
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }

        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), 30, 800, 0);
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(config.getWeb()), 400, 800, 0);

    }

    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("page " + document.getPageNumber()), 550, 30, 0);
    }    
}
