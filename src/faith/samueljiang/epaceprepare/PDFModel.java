package faith.samueljiang.epaceprepare;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;

public class PDFModel {
    public PDDocument pdf;
    private PDFRenderer renderer;
    private Image[] buffer;

    PDFModel(PDFModel model) {
        this.pdf = new PDDocument(model.pdf.getDocument());
        this.renderer = new PDFRenderer(this.pdf);
        buffer = new Image[pdf.getNumberOfPages()];
    }

    PDFModel(File pdf) {
        try {
            this.pdf = PDDocument.load(pdf);
            this.renderer = new PDFRenderer(this.pdf);
            buffer = new Image[this.pdf.getNumberOfPages()];
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public int numPages() {
        return pdf.getPages().getCount();
    }

    public Image getImage(int pageNumber) {
        // See if the buffer has content
        if (buffer[pageNumber] != null) {
            return buffer[pageNumber];
        }

        BufferedImage pageImage;
        try {
            pageImage = renderer.renderImage(pageNumber);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDFRenderer throws IOException", ex);
        }
        Image output = SwingFXUtils.toFXImage(pageImage, null);
        // Add output to buffer to speed up the process
        if (buffer[pageNumber] == null) {
            buffer[pageNumber] = output;
        }
        return output;
    }

    public void closePDF() {
        try {
            pdf.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void clone(PDFModel input) {
        this.pdf = new PDDocument(input.pdf.getDocument());
        this.renderer = new PDFRenderer(this.pdf);
    }
}
