/**
 * Sample Skeleton for 'Main.fxml' Controller Class
 */

package faith.samueljiang.epaceprepare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private ResourceBundle localeResource;
    private PrepareSdk prepareSdk;
    private FileHandler fileHandler;
    private Stage currentStage;
    private SVGLibrary svgLib;

    // Project Related Variables
    private ePACEProject project;
    private String currentProjectPath;
    private Integer currentPage;
    private PDFControl control;
    private Element openedElement;

    // Tools Selection; this shows the tools selected
    // 0 means non selected
    private int toolSelect;

    @FXML // fx:id="mainPane"
    private BorderPane mainPane; // Value injected by FXMLLoader

    @FXML // fx:id="topPane"
    private MenuBar topPane; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarFile"
    private Menu menuBarFile; // Value injected by FXMLLoader

    @FXML // fx:id="menuNewProject"
    private MenuItem menuNewProject; // Value injected by FXMLLoader

    @FXML // fx:id="menuOpenProject"
    private MenuItem menuOpenProject; // Value injected by FXMLLoader

    @FXML // fx:id="menuSettings"
    private MenuItem menuSettings; // Value injected by FXMLLoader

    @FXML // fx:id="menuExit"
    private MenuItem menuExit; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarProject"
    private Menu menuBarProject; // Value injected by FXMLLoader

    @FXML // fx:id="menuAddPdf"
    private MenuItem menuAddPdf; // Value injected by FXMLLoader

    @FXML // fx:id="menuAddTest"
    private MenuItem menuAddTest; // Value injected by FXMLLoader

    @FXML // fx:id="menuAddAnnot"
    private MenuItem menuAddAnnot; // Value injected by FXMLLoader

    @FXML // fx:id="menuAutoGen"
    private Menu menuAutoGen; // Value injected by FXMLLoader

    @FXML // fx:id="menuGenPage"
    private MenuItem menuGenPage; // Value injected by FXMLLoader

    @FXML // fx:id="menuGenPace"
    private MenuItem menuGenPace; // Value injected by FXMLLoader

    @FXML // fx:id="menuGenPackage"
    private Menu menuGenPackage; // Value injected by FXMLLoader

    @FXML // fx:id="menuGenDebug"
    private MenuItem menuGenDebug; // Value injected by FXMLLoader

    @FXML // fx:id="menuGenRelease"
    private MenuItem menuGenRelease; // Value injected by FXMLLoader

    @FXML // fx:id="menuGenReleaseResource"
    private MenuItem menuGenReleaseResource; // Value injected by FXMLLoader

    @FXML // fx:id="menuProjProperties"
    private MenuItem menuProjProperties; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarEdit"
    private Menu menuBarEdit; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarAbout"
    private Menu menuBarAbout; // Value injected by FXMLLoader

    @FXML // fx:id="menuAbout"
    private MenuItem menuAbout; // Value injected by FXMLLoader

    @FXML // fx:id="bottomPane"
    private HBox bottomPane; // Value injected by FXMLLoader

    @FXML // fx:id="leftLabel"
    private Label leftLabel; // Value injected by FXMLLoader

    @FXML // fx:id="rightLabel"
    private Label rightLabel; // Value injected by FXMLLoader

    @FXML // fx:id="leftPane"
    private VBox leftPane; // Value injected by FXMLLoader

    @FXML // fx:id="deleteActivityButton"
    private Button deleteActivityButton; // Value injected by FXMLLoader

    @FXML // fx:id="projectTree"
    private TreeView<?> projectTree; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCErase"
    private Button toolsCErase; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCBlank"
    private Button toolsCBlank; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCMultiple"
    private Button toolsCMultiple; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCConnect"
    private Button toolsCConnect; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCCalli"
    private Button toolsCCalli; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCStrip"
    private Button toolsCStrip; // Value injected by FXMLLoader

    @FXML // fx:id="toolsCDict"
    private Button toolsCDict; // Value injected by FXMLLoader

    @FXML // fx:id="rightPane"
    private AnchorPane rightPane; // Value injected by FXMLLoader

    @FXML // fx:id="rightPaneVBox"
    private VBox rightPaneVBox; // Value injected by FXMLLoader

    @FXML // fx:id="propTitle"
    private Text propTitle; // Value injected by FXMLLoader

    @FXML // fx:id="ppPageType"
    private HBox ppPageType; // Value injected by FXMLLoader

    @FXML // fx:id="ppPageTypeBox"
    private MenuButton ppPageTypeBox; // Value injected by FXMLLoader

    @FXML // fx:id="ppPageNormal"
    private MenuItem ppPageNormal; // Value injected by FXMLLoader

    @FXML // fx:id="ppPageCU"
    private MenuItem ppPageCU; // Value injected by FXMLLoader

    @FXML // fx:id="ppPageST"
    private MenuItem ppPageST; // Value injected by FXMLLoader

    @FXML // fx:id="ppPagePT"
    private MenuItem ppPagePT; // Value injected by FXMLLoader

    @FXML // fx:id="centerPane"
    private GridPane centerPane; // Value injected by FXMLLoader

    @FXML // fx:id="PDFToolBar"
    private HBox PDFToolBar; // Value injected by FXMLLoader

    @FXML // fx:id="PDFPreviousButton"
    private Button PDFPreviousButton; // Value injected by FXMLLoader

    @FXML // fx:id="PDFPageJump"
    private TextField PDFPageJump; // Value injected by FXMLLoader

    @FXML // fx:id="PDFTotalPage"
    private Text PDFTotalPage; // Value injected by FXMLLoader

    @FXML // fx:id="PDFNextButton"
    private Button PDFNextButton; // Value injected by FXMLLoader

    @FXML // fx:id="PDFAnchor"
    private AnchorPane PDFAnchor; // Value injected by FXMLLoader

    @FXML // fx:id="PDFStack"
    private StackPane PDFStack; // Value injected by FXMLLoader

    @FXML // fx:id="PDFView"
    private ImageView PDFView; // Value injected by FXMLLoader


    @FXML
    void hideRightPane(ActionEvent event) {
        rightPane.setVisible(false);
        rightPane.setManaged(false);
        projectTree.getSelectionModel().clearSelection();
    }


    @FXML
    void newProject(ActionEvent event) {
        // TODO: Handle open new project

        // Open create project window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("NewProjectDialog.fxml"));
            loader.setResources(localeResource);
            Parent parent = (Parent) loader.load();

            // Pass file handler to new window controller
            NewProjectDialogController npdc = loader.getController();
            npdc.setFileHandler(fileHandler);
            npdc.setProject(project);

            Scene scene = new Scene(parent, 620, 255);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set window properties
            stage.setTitle(localeResource.getString("ui.title.newproj"));
            stage.setMinWidth(620);
            stage.setMinHeight(275);

            // Show window
            stage.setScene(scene);
            stage.showAndWait();

            if (!isSameProject(project.getProjectPath())) {
                if (hasOpenProject()) {
                    initPane();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    @FXML
    void openProject(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OpenProjectDialog.fxml"));
            loader.setResources(localeResource);
            Parent parent = loader.load();

            // Pass project object to window controller
            OpenProjectDialogController opdc = loader.getController();
            opdc.setFileHandler(fileHandler);
            opdc.setProject(project);

            Scene scene = new Scene(parent, 620, 100);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);

            // Set window properties
            stage.setTitle(localeResource.getString("ui.title.openproj"));
            stage.setMinWidth(620);
            stage.setMinHeight(100);

            // Show window
            stage.setScene(scene);
            stage.showAndWait();

            if (!isSameProject(project.getProjectPath())) {
                if (hasOpenProject()) {
                    initPane();
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    private boolean hasOpenProject() {
        return this.project.getProjectPath() != null;
    }


    private boolean isSameProject(String newPath) {return newPath.equals(currentProjectPath);}


    @FXML
    void PDFNextButtonClick(ActionEvent event) {
        currentPage = (currentPage < control.getNumPages()) ? currentPage + 1 : control.getNumPages();
        fileHandler.setProperty(project.getProjectPath(), "working_page", currentPage.toString());

        PDFPageJump.setText(currentPage.toString());

        PDFView.setImage(control.getPage(currentPage));
        control.bufferImage(currentPage);
        fixPDFPaneSize(true, currentStage.getHeight());
        populateTreeView();
    }

    @FXML
    void PDFPageJumpAction(ActionEvent event) {
        Integer toPage = currentPage;
        try {
            toPage = Integer.parseInt(PDFPageJump.getText());
            if (toPage < 1) {
                toPage = 1;
                PDFPageJump.setText(toPage.toString());
            } else if (toPage > control.getNumPages()) {
                toPage = control.getNumPages();
                PDFPageJump.setText(toPage.toString());
            }
        } catch (Exception e) {
            // User did not type in a number
            PDFPageJump.setText(currentPage.toString());
        }

        this.currentPage = toPage;
        fileHandler.setProperty(project.getProjectPath(), "working_page", currentPage.toString());

        PDFView.setImage(control.getPage(currentPage));
        control.bufferImage(currentPage);
        fixPDFPaneSize(true, currentStage.getHeight());
        populateTreeView();
    }

    @FXML
    void PDFPreviousButtonClick(ActionEvent event) {
        currentPage = (currentPage > 1) ? currentPage - 1 : 1;
        fileHandler.setProperty(project.getProjectPath(), "working_page", currentPage.toString());

        PDFPageJump.setText(currentPage.toString());

        PDFView.setImage(control.getPage(currentPage));
        control.bufferImage(currentPage);
        fixPDFPaneSize(true, currentStage.getHeight());
        populateTreeView();
    }


    private void initPane() {
        // Create the page info in config if necessary
        if (fileHandler.getProperty(project.getProjectPath(), "config_populated").equals("0")) {
            populateConfigWithInfo();
        }

        // Init the PrepareSdk
        this.prepareSdk = new PrepareSdk(project);

        // Enable menu items
        setDisabledMenuItems();

        // Set the current project path
        currentProjectPath = project.getProjectPath();

        // Get current page
        this.currentPage = Integer.parseInt(fileHandler.getProperty(project.getProjectPath(), "working_page"));

        displayPDFPage();

        // Add listener for height and width change on the PDFStack
        currentStage.heightProperty().addListener((obs, oldVal, newVal) -> fixPDFPaneSize(true, newVal.doubleValue()));
        currentStage.widthProperty().addListener((obs, oldVal, newVal) -> fixPDFPaneSize(false, newVal.doubleValue()));

        // Set bottom left label to path
        leftLabel.setText("(" + project.getProjectName() + ") " + project.getProjectPath());

        // Set window title to include the project name
        currentStage.setTitle(localeResource.getString("program.name") + " " + ePacePrepare.version + " | " + project.getProjectName());
    }


    private void populateConfigWithInfo() {
        File pdfFile = project.getPdfFile();
        this.control = new PDFControl(pdfFile, localeResource, rightLabel);
        File config = project.getConfigFile();
        Integer numberOfPages = control.getNumPages();

        fileHandler.initConfigFilePages(config, numberOfPages);
        try {
            fileHandler.updateXMLNode(config, "TotalPages", "", numberOfPages.toString());
        } catch (Exception e) {
            System.err.println("Error while recording Total Pages.\n" + e.toString());
        }
        // Update the property
        fileHandler.setProperty(project.getProjectPath(), "config_populated", "1");
    }


    private void populateTreeView() {
        try {
            // Get info from prepareSdk
            Element pageElement = prepareSdk.getPageFromConfig(currentPage);
            NodeList activities = pageElement.getChildNodes();

            // Transform element to TreeView
            TreeItem rootItem = new TreeItem("Page " + pageElement.getAttributes().getNamedItem("id").getNodeValue());
            rootItem.setExpanded(true);

            projectTree.setRoot(rootItem);

            for (int i = 0; i < activities.getLength(); i++) {
                // Skip the non-element nodes
                if (activities.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

                // Skip things that aren't activity
                if (!activities.item(i).getNodeName().equals("Activity")) continue;

                // Each activity becomes a leaf node
                Element activity = (Element) activities.item(i);
                String blank = activity.getAttribute("type");
                String value = activity.getAttribute("value");
                String id = activity.getAttribute("id");
                TreeItem childItem = new TreeItem("(id=\"" + id + "\") " + localeResource.getString(blank) + ": " + value);

                rootItem.getChildren().add(childItem);
            }

            // Set Event Handler
            projectTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {   // This fixes the exception occurred when the minimize button is clicked on the side pane
                    if (newVal.getParent() == null) {   // selected value is a root (i.e. the page node)
                        rightPane.setVisible(true);
                        rightPane.setManaged(true);
                        propTitle.setText(localeResource.getString("ui.page.properties"));
                        ppPageType.setManaged(true);
                    } else {    // Those are leaves
                        // Set the right pane visible
                        rightPane.setVisible(true);
                        rightPane.setManaged(true);
                        propTitle.setText(localeResource.getString("ui.activity.properties"));
                        ppPageType.setManaged(false);
                        ppPageType.setVisible(false);
                    }
                }
            });
        } catch (Exception e) {
            e.toString();
        }

    }


    private void displayPDFPage() {
        // Initialize PDFControl if necessary
        if (this.control == null) {
            File pdfFile = project.getPdfFile();
            this.control = new PDFControl(pdfFile, localeResource, rightLabel);
        }

        PDFView.setImage(control.getPage(currentPage));
        control.bufferImage(currentPage);

        // Set the total pages on the control field
        PDFTotalPage.setText("/ " + control.getNumPages());
        PDFPageJump.setText(Integer.toString(currentPage));

        // Set the height
        fixPDFPaneSize(true, currentStage.getHeight());

        // Populate the tree view
        populateTreeView();
    }


    private void fixPDFPaneSize(boolean isHeight, double value) {
        // Enlarge the PDF to it's max allowed size
        double stackHeight, stackWidth, lPaneWidth, rPaneWidth, tPaneHeight, bPaneHeight;
        if (isHeight) {
            stackHeight = value - 150;
            stackWidth = PDFStack.getWidth();
        } else {
            lPaneWidth = leftPane.getWidth();
            rPaneWidth = rightPane.getWidth();
            stackHeight = PDFStack.getHeight();
            stackWidth = value - (lPaneWidth + rPaneWidth + 20);
        }

        double pdfImageRatio = PDFView.getImage().getHeight() / PDFView.getImage().getWidth();

        // Choose to anchor to stackHeight or stackWidth while prioritizing anchoring to stackHeight
        if (stackHeight / pdfImageRatio < stackWidth) {
            PDFStack.setPrefHeight(stackHeight);
            PDFView.setFitHeight(stackHeight);
        } else if (stackWidth * pdfImageRatio < stackHeight) {
            PDFStack.setPrefWidth(stackWidth);
            PDFView.setFitWidth(stackWidth);
        }

        // Preserve ratio
        PDFView.setPreserveRatio(true);
    }


    @FXML
    void safeExit(ActionEvent event) {
        System.exit(0);
    }



    @FXML
    void showAddAnnot(ActionEvent event) {

    }

    @FXML
    void showAddPdf(ActionEvent event) {

    }

    @FXML
    void AutoGenAnnotPage(ActionEvent event) {
        // TODO: Add warning messages.
        try {
            prepareSdk.autoPreparePage(this.currentPage, true);
        } catch (NoSuchFieldException e) {
            System.err.println(e.toString());
        }

        populateTreeView();
    }

    @FXML
    void AutoGenAnnotPace(ActionEvent event) {
        // TODO: Add warning messages.
        try {
            prepareSdk.autoPreparePace();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        populateTreeView();
    }

    @FXML
    void showAbout(ActionEvent event) {

    }

    @FXML
    void toolsCBlankClick(ActionEvent event) {
        if (!(toolSelect == 1)) {
            highlight(1);
            toolSelect = 1;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }

    @FXML
    void toolsCMultpleClick(ActionEvent event) {
        if (!(toolSelect == 2)) {
            highlight(2);
            toolSelect = 2;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }

    @FXML
    void toolsCConnectClick(ActionEvent event) {
        if (!(toolSelect == 3)) {
            highlight(3);
            toolSelect = 3;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }

    @FXML
    void toolsCCalliClick(ActionEvent event) {
        if (!(toolSelect == 4)) {
            highlight(4);
            toolSelect = 4;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }

    @FXML
    void toolsCStripClick(ActionEvent event) {
        if (!(toolSelect == 5)) {
            highlight(5);
            toolSelect = 5;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }

    @FXML
    void toolsCDictClick(ActionEvent event) {
        if (!(toolSelect == 6)) {
            highlight(6);
            toolSelect = 6;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }

    @FXML
    void toolsCEraseClick(ActionEvent event) {
        if (!(toolSelect == 7)) {
            highlight(7);
            toolSelect = 7;
        } else {
            highlight(0);
            toolSelect = 0;
        }
    }


    private void highlight(int tool) {

        // Fill everything with white
        toolsCBlank.setStyle("-fx-background-color: white");
        toolsCMultiple.setStyle("-fx-background-color: white");
        toolsCConnect.setStyle("-fx-background-color: white");
        toolsCCalli.setStyle("-fx-background-color: white");
        toolsCStrip.setStyle("-fx-background-color: white");
        toolsCDict.setStyle("-fx-background-color: white");
        toolsCErase.setStyle("-fx-background-color: white");

        switch (tool) {
            case 1:
                toolsCBlank.setStyle("-fx-background-color: lightgray");
                break;
            case 2:
                toolsCMultiple.setStyle("-fx-background-color: lightgray");
                break;
            case 3:
                toolsCConnect.setStyle("-fx-background-color: lightgray");
                break;
            case 4:
                toolsCCalli.setStyle("-fx-background-color: lightgray");
                break;
            case 5:
                toolsCStrip.setStyle("-fx-background-color: lightgray");
                break;
            case 6:
                toolsCDict.setStyle("-fx-background-color: lightgray");
                break;
            case 7:
                toolsCErase.setStyle("-fx-background-color: lightgray");
                break;
        }
    }


    @FXML
    void ppPageCUSelect(ActionEvent event) {
        //Set Text to menu
        ppPageTypeBox.setText(ppPageCU.getText());
    }

    @FXML
    void ppPageNormalSelect(ActionEvent event) {
        //Set Text to menu
        ppPageTypeBox.setText(ppPageNormal.getText());
    }

    @FXML
    void ppPagePTSelect(ActionEvent event) {
        //Set Text to menu
        ppPageTypeBox.setText(ppPagePT.getText());
    }

    @FXML
    void ppPageSTSelect(ActionEvent event) {
        //Set Text to menu
        ppPageTypeBox.setText(ppPageST.getText());
    }


    public void setMainStage(Stage stage) {
        this.currentStage = stage;
    }


    private void setDisabledMenuItems() {
        if (!hasOpenProject()) {
            menuAddPdf.setDisable(true);
            menuAddAnnot.setDisable(true);
            menuGenPace.setDisable(true);
            menuGenPage.setDisable(true);
            menuGenDebug.setDisable(true);
            menuGenRelease.setDisable(true);
            menuGenReleaseResource.setDisable(true);
            menuProjProperties.setDisable(true);
            
            toolsCBlank.setDisable(true);
            toolsCErase.setDisable(true);
            toolsCDict.setDisable(true);
            toolsCStrip.setDisable(true);
            toolsCCalli.setDisable(true);
            toolsCMultiple.setDisable(true);
            toolsCConnect.setDisable(true);

            deleteActivityButton.setDisable(true);

            PDFToolBar.setDisable(true);
        } else {
            menuAddPdf.setDisable(false);
            menuAddAnnot.setDisable(false);
            menuGenPace.setDisable(false);
            menuGenPage.setDisable(false);
            menuGenDebug.setDisable(false);
            menuGenRelease.setDisable(false);
            menuGenReleaseResource.setDisable(false);
            menuProjProperties.setDisable(false);

            toolsCBlank.setDisable(false);
            toolsCErase.setDisable(false);
            toolsCDict.setDisable(false);
            toolsCStrip.setDisable(false);
            toolsCCalli.setDisable(false);
            toolsCMultiple.setDisable(false);
            toolsCConnect.setDisable(false);

            deleteActivityButton.setDisable(false);

            PDFToolBar.setDisable(false);
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.localeResource = resources;
        this.fileHandler = new FileHandler();
        this.project = new ePACEProject();
        this.svgLib = new SVGLibrary();

        // Fill the images to the tool buttons
        toolsCBlank.setGraphic(svgLib.getSVG(SVGImage.BLANK, Color.BLACK, 1.0, 1.0));
        toolsCMultiple.setGraphic(svgLib.getSVG(SVGImage.MULTISELECT, Color.BLACK, 1.0, 1.0));
        toolsCConnect.setGraphic(svgLib.getSVG(SVGImage.CONNECT, Color.BLACK, 1.0, 1.0));
        toolsCCalli.setGraphic(svgLib.getSVG(SVGImage.CALLIGRAPHY, Color.BLACK, 1.0, 1.0));
        toolsCStrip.setGraphic(svgLib.getSVG(SVGImage.SCORESTRIP, Color.BLACK, 1.0, 1.0));
        toolsCDict.setGraphic(svgLib.getSVG(SVGImage.DICTATION, Color.BLACK, 1.0, 1.0));
        toolsCErase.setGraphic(svgLib.getSVG(SVGImage.ERASER, Color.BLACK, 1.1, 1.1));

        // Fill image to the delete button
        deleteActivityButton.setGraphic(svgLib.getSVG(SVGImage.DELETE, Color.DARKGRAY, 0.83, 0.83));
        
        // Fill the PDF toolbar icons
        PDFPreviousButton.setGraphic(svgLib.getSVG(SVGImage.PREVIOUS, Color.DARKGRAY, 1.0, 1.0));
        PDFNextButton.setGraphic(svgLib.getSVG(SVGImage.NEXT, Color.DARKGRAY, 1.0, 1.0));

        // Tools set
        toolSelect = 0;

        // Set right pane width
        rightPane.setPrefWidth(200);

        // Default project set to ""
        currentProjectPath = "";

        // Disable necessary menu items
        setDisabledMenuItems();

        // Set hide properties menu
        rightPane.setVisible(false);
        rightPane.setManaged(false);

        // Initialize the two labels at the bottom
        leftLabel.setText(localeResource.getString("ui.no.open.project"));
        rightLabel.setText(localeResource.getString("ui.done"));

        // TODO: Implement those features in future
        menuSettings.setVisible(false);
        menuBarEdit.setVisible(false);
    }
}