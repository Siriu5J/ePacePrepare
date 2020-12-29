package faith.samueljiang.epaceprepare;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;

public class PDFModel {
    private PDDocument PDFDocument;
    private PDFRenderer renderer;

    PDFModel(File pdf) {
        try {
            this.PDFDocument = PDDocument.load(pdf);
            this.renderer = new PDFRenderer(PDFDocument);
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public int numPages() {
        return PDFDocument.getPages().getCount();
    }

    public Image getImage(int pageNumber) {
        BufferedImage pageImage;
        try {
            pageImage = renderer.renderImage(pageNumber);
        } catch (IOException ex) {
            throw new UncheckedIOException("PDFRenderer throws IOException", ex);
        }
        return SwingFXUtils.toFXImage(pageImage, null);
    }

    public void closePDF() {
        try {
            PDFDocument.close();
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
