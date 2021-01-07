package faith.samueljiang.epaceprepare;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

public class PDFControl {
    private PDDocument pdf;
    private PDFRenderer renderer;
    private Image[] buffer;
    private ResourceBundle localeResource;
    private Label rightLabel;

    PDFControl(File pdf, ResourceBundle locale, Label rightLabel) {
        localeResource = locale;
        this.rightLabel = rightLabel;
        try {
            this.pdf = PDDocument.load(pdf);
            this.renderer = new PDFRenderer(this.pdf);
            buffer = new Image[this.pdf.getNumberOfPages()];
        } catch (IOException e) {
            System.err.println("Cannot open PDF File:");
            e.printStackTrace();
        }
    }
    
    
    public Integer getNumPages() {
        return pdf.getNumberOfPages();
    }
    
    
    public Image getPage(int page) {
        page--; // Convert to 0-X
        // Check the buffer
        if (buffer[page] != null) {
            return buffer[page];
        }
        
        // Render the page and return it from the buffer
        renderPage(page);
        return buffer[page];
    }
    
    
    protected void renderPage(int page) {
        try {
            BufferedImage image = renderer.renderImage(page);
            buffer[page] = SwingFXUtils.toFXImage(image, null);
        } catch (IOException e) {
            System.err.println("Error while rendering PDF page " + (page + 1));
        }
    }


    public void bufferImage(int page) {
        // Buffer the next 5 and 5 previous pages on another thread
        page--; // Convert to 0-X
        int backwards = page - 1;
        int forwards = page + 1;
        int count = 0;
        while (count < 5) {
            // Buffer the next page if valid
            if (forwards < pdf.getNumberOfPages()) {
                if (buffer[forwards] == null) {
                    BufferPdfTask task = new BufferPdfTask(this, forwards);
                    new Thread(task).start();
                }
            }

            // Buffer the previous page if valid
            if (backwards >= 0) {
                if (buffer[backwards] == null) {
                    BufferPdfTask task = new BufferPdfTask(this, backwards);
                    new Thread(task).start();
                }
            }

            forwards++;
            backwards--;
            count++;
        }
    }


    public Point2D getPageDimen(int page) {
        page--;

        PDPage pdfPage = pdf.getPage(page);
        return new Point2D(pdfPage.getMediaBox().getWidth(), pdfPage.getMediaBox().getHeight());
    }

    public Label getRightLabel() {
        return rightLabel;
    }

    public ResourceBundle getLocaleResource() {
        return localeResource;
    }

    public Double getPDFWidth(int page) {
        return (double) pdf.getPage(page).getMediaBox().getWidth();
    }

    public Double getPDFHeight(int page) {
        return (double) pdf.getPage(page).getMediaBox().getHeight();
    }
}
