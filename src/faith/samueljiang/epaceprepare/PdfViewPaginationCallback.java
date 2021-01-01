package faith.samueljiang.epaceprepare;

import javafx.scene.image.ImageView;
import javafx.util.Callback;
import org.apache.pdfbox.pdmodel.PDDocument;

public class PdfViewPaginationCallback implements Callback<Integer, ImageView> {

    private PDFModel pdfModel;
    private double windowHeight;
    private ImageView pdfPage;

    PdfViewPaginationCallback(PDFModel pdf, double height) {
        this.pdfModel = new PDFModel(pdf);
        this.windowHeight = height;
    }

    @Override
    public ImageView call(Integer index) {
        long start = System.nanoTime();
        this.pdfPage = new ImageView(pdfModel.getImage(index));
        long end = System.nanoTime();
        System.out.println("Render PDF Time: " + ((end - start) / 1000000000.00));

        pdfPage.setFitHeight(windowHeight - 140);
        pdfPage.setPreserveRatio(true);
        return pdfPage;
    }

    public void setPdfFit(double windowHeight, double leftPaneWidth, double rightPaneWidth, double windowWidth) {
        this.windowHeight = windowHeight;
        double usableWidth = windowWidth - (leftPaneWidth + rightPaneWidth + 10);
        double pdfRatio = pdfPage.getImage().getHeight() / pdfPage.getImage().getWidth();
        double futureWidth = (windowHeight - 140) / pdfRatio;
        if (usableWidth > futureWidth) {
            pdfPage.setFitHeight(windowHeight - 140);
        } else {
            pdfPage.setFitWidth(usableWidth);
        }
        pdfPage.setPreserveRatio(true);
    }
}
