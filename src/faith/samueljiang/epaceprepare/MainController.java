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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private ResourceBundle localeResource;
    private PrepareSdk prepareSdk;
    private FileHandler fileHandler;
    private Stage currentStage;

    // Project Related Variables
    private ePACEProject project;
    private String currentProjectPath;
    private int currentPage;
    private PDFModel pdfModel;
    private PdfViewPaginationCallback callback;

    // Tools Selection; this shows the tools selected
    // 0 means non selected
    private int toolSelect;

    @FXML // fx:id="mainPane"
    private BorderPane mainPane; // Value injected by FXMLLoader

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

    @FXML // fx:id="leftLabel"
    private Label leftLabel; // Value injected by FXMLLoader

    @FXML // fx:id="rightLabel"
    private Label rightLabel; // Value injected by FXMLLoader

    @FXML // fx:id="leftPane"
    private VBox leftPane; // Value injected by FXMLLoader

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

    @FXML // fx:id="pdfView"
    private Pagination pdfView; // Value injected by FXMLLoader

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


    @FXML
    void hideRightPane(ActionEvent event) {
        rightPane.setVisible(false);
        rightPane.setManaged(false);
        projectTree.getSelectionModel().clearSelection();
        callback.setPdfFit(currentStage.getHeight(), leftPane.getWidth(), rightPane.getWidth(), currentStage.getWidth());
    }


    @FXML
    void newProject(ActionEvent event) {
        // TODO: Handle open new project
        // Close the PDF if there is an opened document
        if (pdfModel != null) {
            pdfModel.closePDF();
        }

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
        // Close the PDF if there is an opened document
        if (pdfModel != null) {
            pdfModel.closePDF();
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OpenProjectDialog.fxml"));
            loader.setResources(localeResource);
            Parent parent = (Parent) loader.load();

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
                    // TODO: Fix loading not showing
                    // Set right label to loading project
                    rightLabel.setText(localeResource.getString("ui.loading.project"));
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

        // Add listener for height change (only on resize complete)
        currentStage.heightProperty().addListener((obs, oldVal, newVal) -> fixHeightChange(newVal.doubleValue()));

        // Set bottom left label to path
        leftLabel.setText("(" + project.getProjectName() + ") " + project.getProjectPath());

        // Reset bottom right label to done
        rightLabel.setText(localeResource.getString("ui.done"));

        // Set window title to include the project name
        currentStage.setTitle(localeResource.getString("program.name") + " " + ePacePrepare.version + " | " + project.getProjectName());
    }


    private void populateConfigWithInfo() {
        File pdfFile = project.getPdfFile();
        pdfModel = new PDFModel(pdfFile);
        File config = project.getConfigFile();
        Integer numberOfPages = pdfModel.pdf.getNumberOfPages();

        // Loop through the
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
        File config = project.getConfigFile();

        // Get Info
        Node pageContent = fileHandler.getPageElements(config, String.valueOf(currentPage));
        NodeList pageChildren = pageContent.getChildNodes();

        // Transform info from Node to TreeView
        TreeItem rootItem = new TreeItem("Page " + pageContent.getAttributes().getNamedItem("id").getNodeValue());
        rootItem.setExpanded(true);
        projectTree.setRoot(rootItem);
        /*for (int i = 0; i < pageChildren.getLength(); i++) {
            rootItem.getChildren().add(pageChildren.item(i).getNodeName());
        }*/

        // Set Event Handler
        projectTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {   // This fixes the exception occurred when the minimize button is clicked on the side pane
                if (newVal.getParent() == null) {   // selected value is a root (i.e. the page node)
                    rightPane.setVisible(true);
                    rightPane.setManaged(true);
                    propTitle.setText(localeResource.getString("ui.page.properties"));
                    ppPageType.setManaged(true);
                }
            }
        });
    }


    private void displayPDFPage() {
        File pdfFile = project.getPdfFile();
        this.pdfModel = new PDFModel(pdfFile);

        // Initialize PDF pagination
        this.callback = new PdfViewPaginationCallback(pdfModel, currentStage.getHeight());
        pdfView.setManaged(true);
        pdfView.setPageCount(pdfModel.numPages());
        pdfView.setPageFactory(index -> this.callback.call(index));
        pdfView.setCurrentPageIndex(currentPage - 1);
        pdfView.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> pageControl(newIndex.intValue()));
        pageControl(currentPage - 1);
    }


    private void pageControl(Integer index) {
        long start = System.nanoTime();
        fileHandler.setProperty(project.getProjectPath(), "working_page", String.valueOf(index + 1));
        this.currentPage = index + 1;

        // Make sure the new height is valid
        fixHeightChange(currentStage.getHeight());

        // Populate the tree view
        populateTreeView();
        long end = System.nanoTime();
        System.out.println("PageControl Time: " + ((end - start) / 1000000000.00));
    }


    private void fixHeightChange(Double height) {
        if (hasOpenProject()) {
            if (project.getPdfFile() != null) {
                callback.setPdfFit(height, leftPane.getWidth(), rightPane.getWidth(), currentStage.getWidth());
            }
        }
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
    }

    @FXML
    void AutoGenAnnotPace(ActionEvent event) {
        // TODO: Add warning messages.
        try {
            prepareSdk.autoPreparePace();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
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
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.localeResource = resources;
        this.fileHandler = new FileHandler();
        this.project = new ePACEProject();

        // Fill the images to the tool buttons
        Image toolsBlank = new Image("/asset/tools-blank.png", 30, 30, false, false);
        ImageView toolsBlankView = new ImageView(toolsBlank);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCBlank.setGraphic(toolsBlankView);

        Image toolsMulti = new Image("/asset/tools-multi.png", 30, 30, false, false);
        ImageView toolsMultiView = new ImageView(toolsMulti);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCMultiple.setGraphic(toolsMultiView);

        Image toolsConnect = new Image("/asset/tools-connect.png", 30, 30, false, false);
        ImageView toolsConnectView = new ImageView(toolsConnect);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCConnect.setGraphic(toolsConnectView);

        Image toolsCalli= new Image("/asset/tools-calli.png", 30, 30, false, false);
        ImageView toolsCalliView = new ImageView(toolsCalli);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCCalli.setGraphic(toolsCalliView);

        Image toolsStrip = new Image("/asset/tools-strip.png", 30, 30, false, false);
        ImageView toolsStripView = new ImageView(toolsStrip);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCStrip.setGraphic(toolsStripView);

        Image toolsDict = new Image("/asset/tools-dict.png", 30, 30, false, false);
        ImageView toolsDictView = new ImageView(toolsDict);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCDict.setGraphic(toolsDictView);

        Image toolsEraser = new Image("/asset/tools-eraser.png", 30, 30, false, false);
        ImageView toolsEraserView = new ImageView(toolsEraser);
        toolsBlankView.setFitHeight(30);
        toolsBlankView.setPreserveRatio(true);
        toolsCErase.setGraphic(toolsEraserView);

        // Tools set
        toolSelect = 0;

        // Set right pane width
        rightPane.setMinWidth(190);

        // Hide pagination on start
        pdfView.setManaged(false);

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