package faith.samueljiang.epaceprepare;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

public class PDFDisplayPane implements Initializable {

    private ResourceBundle locale;
    private MainController mainController;

    private Integer intTotalPages;
    private Integer currentPage;
    private Double initY;
    private HashMap<Double, Node> activitiesStore;

    @FXML // fx:id="framePane"
    private GridPane framePane; // Value injected by FXMLLoader

    @FXML // fx:id="pdfContainer"
    private StackPane pdfContainer; // Value injected by FXMLLoader

    @FXML // fx:id="pdfStack"
    private Pane pdfStack; // Value injected by FXMLLoader

    @FXML // fx:id="pdfView"
    private ImageView pdfView; // Value injected by FXMLLoader

    @FXML // fx:id="Toolbar"
    private HBox Toolbar; // Value injected by FXMLLoader

    @FXML // fx:id="previousButton"
    private Button previousButton; // Value injected by FXMLLoader

    @FXML // fx:id="pageJump"
    private TextField pageJump; // Value injected by FXMLLoader

    @FXML // fx:id="totalPages"
    private Text totalPages; // Value injected by FXMLLoader

    @FXML // fx:id="nextButton"
    private Button nextButton; // Value injected by FXMLLoader

    public void setMainController(MainController mc) {
        this.mainController = mc;
    }

    public void setPdfView(Image pdfImage) {
        pdfView.setImage(pdfImage);
        pdfView.setOpacity(20.0);
    }

    public void setTotalPages(Integer pages) {
        totalPages.setText("/ " + pages.toString());
        intTotalPages = pages;
    }

    public void setPageNoJump(String text) {
        pageJump.setText(text);
    }

    public void setCurrentPage(Integer page) {
        currentPage = page;
        // Disable page change buttons if necessary
        if (currentPage == 1) {
            previousButton.setDisable(true);
        } else if (currentPage.equals(intTotalPages)) {
            nextButton.setDisable(true);
        }
    }

    public void setPageHeight(Double height) {
        pdfView.setFitHeight(height);
        pdfView.setPreserveRatio(true);
    }

    public void setPageWidth(Double width) {
        pdfView.setFitWidth(width);
        pdfView.setPreserveRatio(true);
    }

    public void setStackHeight(Double height) {
        pdfStack.setMinHeight(height);
        pdfStack.setPrefHeight(height);
        pdfStack.setMaxHeight(height);
    }

    public void setStackWidth(Double width) {
        pdfStack.setMinWidth(width);
        pdfStack.setPrefWidth(width);
        pdfStack.setMaxWidth(width);
    }

    public void setToolbarVisibility(Boolean visibility) {
        Toolbar.setVisible(!visibility);
    }


    public Double getContainerHeight() {
        return pdfStack.getPrefHeight();
    }

    public Double getContainerWidth() {
        return pdfStack.getPrefWidth();
    }

    public Double getCurrentPageHeight() {
        return pdfView.getImage().getHeight();
    }

    public Double getCurrentPageWidth() {
        return pdfView.getImage().getWidth();
    }

    public GridPane getPane() {
        return framePane;
    }


    private Background getBackgroundByColor(Color color) {
        return new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY));
    }

    public void clearActivities() {
        activitiesStore.clear();
        pdfStack.getChildren().clear();
    }

    public void addActivity(Double x1, Double y1, Double x2, Double y2, String type, String id, String value) {
        // Get the ratio between the dimensions of the PDF page vs. the dimensions of the PDF page shown on view
        Bounds pdfViewBounds = pdfView.getLayoutBounds();
        Double pageToViewRatio = pdfViewBounds.getMaxX() / getCurrentPageWidth();

        x1 = x1 * pageToViewRatio;
        x2 = x2 * pageToViewRatio;
        y1 = y1 * pageToViewRatio;
        y2 = y2 * pageToViewRatio;
        double pageHeightCorrected;
        pageHeightCorrected = getCurrentPageHeight() * pageToViewRatio;

        // Convert the coordinates
        Double minX = x1;
        double width = x2 - x1;
        double minY = pageHeightCorrected - y2;
        double height = y2 - y1;

        // The activities are stored using id
        switch (type) {
            case "Blank":
                Rectangle activity = new Rectangle(minX, minY, width, height);
                activity.setFill(Color.TRANSPARENT);
                activity.setStroke(Color.BLACK);
                activity.setStrokeWidth(2.0);
                System.out.println(activity.toString());
                pdfStack.getChildren().add(activity);
                break;
        }
    }

    public void initSize() {
        initY = pdfContainer.getHeight();
        double pdfImageRatio = getCurrentPageHeight() / getCurrentPageWidth();

        // Set initial fit height
        setPageHeight(initY);
        setPageWidth(initY / pdfImageRatio);
        setStackHeight(pdfView.getFitHeight());
        setStackWidth(pdfView.getFitWidth());

        // Set the matching property
        pdfView.fitHeightProperty().bind(mainController.getCurrentStage().heightProperty().subtract(150.0));
        pdfView.fitWidthProperty().bind(mainController.getCurrentStage().widthProperty().subtract(270.0 + mainController.getRightPaneWidth()));
        pdfStack.scaleYProperty().bind(pdfContainer.heightProperty().divide(initY));
        // TODO: Fix very tall pages
        pdfStack.scaleXProperty().bind(pdfContainer.heightProperty().divide(initY));
        /*pdfStack.scaleXProperty().bind(Bindings.when(
            pdfView.fitHeightProperty().greaterThanOrEqualTo(getContainerWidth().longValue()))
            .then(pdfContainer.prefWidthProperty())
            .otherwise(pdfContainer.heightProperty().divide(initY))
            );*/
    }


    @FXML
    void nextButtonPressed(ActionEvent event) {
        // Clear the disabled previous button if necessary
        previousButton.setDisable(false);

        // Check if next page is valid and set the page in main controller
        if (currentPage == intTotalPages - 1) {
            nextButton.setDisable(true);
        }
        currentPage++;
        setPageNoJump(currentPage.toString());
        mainController.onPageChange(currentPage);
    }

    @FXML
    void pageJumpEntered(ActionEvent event) {
        // Only allow integers
        String input = pageJump.getText();
        Integer jumpTo = currentPage;
        try {
            jumpTo = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            // Revert the blank if non integer is entered
            pageJump.setText(currentPage.toString());
            return;
        }

        // Clear button disability
        previousButton.setDisable(false);
        nextButton.setDisable(false);

        // Check for int validity
        if (jumpTo > intTotalPages) {
            jumpTo = intTotalPages;
        } else if (jumpTo < 1) {
            jumpTo = 1;
        } else if (jumpTo == 1) {
            previousButton.setDisable(true);
        } else if (jumpTo == intTotalPages) {
            nextButton.setDisable(true);
        }

        // Page change
        currentPage = jumpTo;
        setPageNoJump(currentPage.toString());
        mainController.onPageChange(currentPage);
    }

    @FXML
    void previousButtonPressed(ActionEvent event) {
        // Clear the disabled next button if necessary
        nextButton.setDisable(false);

        // Check if previous page is valid
        if (currentPage == 2) {
            previousButton.setDisable(true);
        }
        currentPage--;
        setPageNoJump(currentPage.toString());
        mainController.onPageChange(currentPage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.locale = resourceBundle;

        activitiesStore = new HashMap<>();

        // Set button graphics
        SVGLibrary svgLibrary = new SVGLibrary();
        previousButton.setGraphic(svgLibrary.getSVG(SVGImage.PREVIOUS, Color.DARKGRAY, 1.0, 1.0));
        nextButton.setGraphic(svgLibrary.getSVG(SVGImage.NEXT, Color.DARKGRAY, 1.0, 1.0));
    }
}
