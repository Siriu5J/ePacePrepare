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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainController implements Initializable {

    private ResourceBundle localeResource;
    private PrepareSdk prepareSdk;
    private FileHandler fileHandler;
    private Stage currentStage;
    private SVGLibrary svgLib;
    private PDFDisplayPane pdfDisplayPane;

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
    private TreeView<String> projectTree; // Value injected by FXMLLoader

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

    @FXML // fx:id="ppActivityValue"
    private HBox ppActivityValue; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityValueField"
    private TextField ppActivityValueField; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityCoord"
    private Text ppActivityCoord; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityX1"
    private HBox ppActivityX1; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityX1Spinner"
    private Spinner<Double> ppActivityX1Spinner; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityY1"
    private HBox ppActivityY1; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityY1Spinner"
    private Spinner<Double> ppActivityY1Spinner; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityX2"
    private HBox ppActivityX2; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityX2Spinner"
    private Spinner<Double> ppActivityX2Spinner; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityY2"
    private HBox ppActivityY2; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityY2Spinner"
    private Spinner<Double> ppActivityY2Spinner; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityRotation"
    private HBox ppActivityRotation; // Value injected by FXMLLoader

    @FXML // fx:id="ppActivityRotationSpinner"
    private Spinner<Double> ppActivityRotationSpinner; // Value injected by FXMLLoader

    @FXML // fx:id="pdfViewAnchor"
    private AnchorPane pdfViewAnchor; // Value injected by FXMLLoader


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
        //currentStage.heightProperty().addListener((obs, oldVal, newVal) -> pdfDisplayPane.fixSize(true, rightPane.isVisible(), newVal.doubleValue()));
        //currentStage.widthProperty().addListener((obs, oldVal, newVal) -> pdfDisplayPane.fixSize(false, rightPane.isVisible(), newVal.doubleValue()));

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


    public void onPageChange(Integer page) {
        currentPage = page;
        fileHandler.setProperty(project.getProjectPath(), "working_page", currentPage.toString());

        pdfDisplayPane.setPdfView(control.getPage(currentPage));
        //pdfDisplayPane.fixSize(true,rightPane.isVisible(), currentStage.getHeight());
        populateActivities();
        control.bufferImage(currentPage);
    }


    private void displayPDFPage() {
        // Initialize PDFControl if necessary
        if (this.control == null) {
            File pdfFile = project.getPdfFile();
            this.control = new PDFControl(pdfFile, localeResource, rightLabel);
        }

        pdfDisplayPane.setCurrentPage(currentPage);
        pdfDisplayPane.setPdfView(control.getPage(currentPage));

        // Set the total pages on the control field
        pdfDisplayPane.setTotalPages(control.getNumPages());
        pdfDisplayPane.setPageNoJump(Integer.toString(currentPage));

        // Set the height
        //pdfDisplayPane.fixSize(true, rightPane.isVisible(), currentStage.getHeight());
        pdfDisplayPane.initSize();

        // Populate the tree view
        populateActivities();

        control.bufferImage(currentPage);
    }


    private void populateActivities() {
        try {
            // Get info from prepareSdk
            Element pageElement = prepareSdk.getPageFromConfig(currentPage);
            openedElement = pageElement;
            NodeList activities = pageElement.getChildNodes();

            // Clear the activities list
            pdfDisplayPane.clearActivities();

            // Transform element to TreeView
            String pageNumber = pageElement.getAttributes().getNamedItem("id").getNodeValue();
            String pageType = pageElement.getChildNodes().item(1).getTextContent();
            String pageTypeToLocale = pageType.equals("") ? localeResource.getString("ui.unknown") : localeResource.getString(pageType);
            TreeItem<String> rootItem = new TreeItem<>("Page " + pageNumber + "（" + pageTypeToLocale + "）");
            rootItem.setExpanded(true);

            projectTree.setRoot(rootItem);

            // Add the activities to the TreeView and to the PDFDisplayPane
            for (int i = 0; i < activities.getLength(); i++) {
                // Skip the non-element nodes
                if (activities.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

                // Skip things that aren't activity
                if (!activities.item(i).getNodeName().equals("Activity")) continue;

                // Each activity becomes a leaf node
                Element activity = (Element) activities.item(i);
                String type = activity.getAttribute("type");
                String value = activity.getAttribute("value");
                String id = activity.getAttribute("id");
                TreeItem<String> childItem = new TreeItem<>("(id=\"" + id + "\") " + localeResource.getString(type) + ": " + value);

                // Add to the PDFDisplayPane
                Double x1 = Double.parseDouble(activity.getAttribute("x1"));
                Double y1 = Double.parseDouble(activity.getAttribute("y1"));
                Double x2 = Double.parseDouble(activity.getAttribute("x2"));
                Double y2 = Double.parseDouble(activity.getAttribute("y2"));
                pdfDisplayPane.addActivity(x1, y1, x2, y2, type, id, value);

                // Add to the tree
                rootItem.getChildren().add(childItem);
            }

            // Set Event Handler
            projectTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal != null) {   // This fixes the exception occurred when the minimize button is clicked on the side pane
                    // Recalculate width with right pane open
                    //pdfDisplayPane.fixSize(false, true, currentStage.getWidth());

                    if (newVal.getParent() == null) {   // selected value is a root (i.e. the page node)
                        // Set Right Pane visibility
                        setRightPaneVisibility(true, false);
                        propTitle.setText(localeResource.getString("ui.page.properties"));

                        // Set option defaults
                        if (pageTypeToLocale.equals(localeResource.getString("ui.unknown"))) {
                            ppPageTypeBox.setText(localeResource.getString("ui.page.type.not.selected"));
                        } else {
                            ppPageTypeBox.setText(pageTypeToLocale);
                        }
                    } else {    // Those are leaves
                        // Parse the id
                        String selectedId = newVal.getValue();
                        Pattern p = Pattern.compile("\"([^\"]*)\"");
                        Matcher m = p.matcher(selectedId);
                        while (m.find()) {
                            selectedId = m.group(1);
                        }

                        // Get the activity node
                        Element selectedActivity = null;
                        for (int i = 0; i < activities.getLength(); i++) {
                            // Skip the non-element nodes
                            if (activities.item(i).getNodeType() != Node.ELEMENT_NODE) continue;

                            // Skip things that aren't activity
                            if (!activities.item(i).getNodeName().equals("Activity")) continue;

                            Element activity = (Element) activities.item(i);
                            if (activity.getAttribute("id").equals(selectedId)) {
                                selectedActivity = activity;
                            }
                        }

                        // Set the right pane visible
                        setRightPaneVisibility(false, false);
                        assert selectedActivity != null;
                        String type = selectedActivity.getAttribute("type");
                        propTitle.setText(localeResource.getString("ui.activity.properties") + "（" + localeResource.getString(type)  + "）");

                        // Initialize the pane values
                        ppActivityValueField.setText(selectedActivity.getAttribute("value"));
                        // Set the factory
                        SpinnerValueFactory<Double> X1SpinnerFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, control.getPDFWidth(currentPage - 1), 0.1);
                        SpinnerValueFactory<Double> X2SpinnerFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, control.getPDFWidth(currentPage - 1), 0.1);
                        ppActivityX1Spinner.setValueFactory(X1SpinnerFactory);
                        ppActivityX1Spinner.getValueFactory().setValue(Double.parseDouble(selectedActivity.getAttribute("x1")));
                        ppActivityX2Spinner.setValueFactory(X2SpinnerFactory);
                        ppActivityX2Spinner.getValueFactory().setValue(Double.parseDouble(selectedActivity.getAttribute("x2")));
                        SpinnerValueFactory<Double> Y1SpinnerFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, control.getPDFHeight(currentPage - 1), 0.1);
                        SpinnerValueFactory<Double> Y2SpinnerFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0.0, control.getPDFHeight(currentPage - 1), 0.1);
                        ppActivityY1Spinner.setValueFactory(Y1SpinnerFactory);
                        ppActivityY1Spinner.getValueFactory().setValue(Double.parseDouble(selectedActivity.getAttribute("y1")));
                        ppActivityY2Spinner.setValueFactory(Y2SpinnerFactory);
                        ppActivityY2Spinner.getValueFactory().setValue(Double.parseDouble(selectedActivity.getAttribute("y2")));
                    }
                }
            });
        } catch (Exception e) {
            System.err.println(e.toString());
        }

    }


    @FXML
    void hideRightPane() {
        setRightPaneVisibility(false, true);
        //pdfDisplayPane.fixSize(true, rightPane.isVisible(), currentStage.getHeight());
        populateActivities();
    }


    private void setRightPaneVisibility(boolean isPageProp, boolean isClear) {
        if (isClear) {
            rightPane.setVisible(false);
            rightPane.setManaged(false);
            ppPageType.setVisible(false);
            ppPageType.setManaged(false);
            ppActivityValue.setVisible(false);
            ppActivityValue.setManaged(false);
            ppActivityCoord.setVisible(false);
            ppActivityCoord.setManaged(false);
            ppActivityX1.setVisible(false);
            ppActivityX1.setManaged(false);
            ppActivityY1.setVisible(false);
            ppActivityY1.setManaged(false);
            ppActivityX2.setVisible(false);
            ppActivityX2.setManaged(false);
            ppActivityY2.setVisible(false);
            ppActivityY2.setManaged(false);
            ppActivityRotation.setVisible(false);
            ppActivityRotation.setManaged(false);

            return;
        }

        rightPane.setVisible(true);
        rightPane.setManaged(true);
        if (isPageProp) {
            ppPageType.setVisible(true);
            ppPageType.setManaged(true);
            ppActivityValue.setVisible(false);
            ppActivityValue.setManaged(false);
            ppActivityCoord.setVisible(false);
            ppActivityCoord.setManaged(false);
            ppActivityX1.setVisible(false);
            ppActivityX1.setManaged(false);
            ppActivityY1.setVisible(false);
            ppActivityY1.setManaged(false);
            ppActivityX2.setVisible(false);
            ppActivityX2.setManaged(false);
            ppActivityY2.setVisible(false);
            ppActivityY2.setManaged(false);
            ppActivityRotation.setVisible(false);
            ppActivityRotation.setManaged(false);
        } else {
            ppPageType.setVisible(false);
            ppPageType.setManaged(false);
            ppActivityValue.setVisible(true);
            ppActivityValue.setManaged(true);
            ppActivityCoord.setVisible(true);
            ppActivityCoord.setManaged(true);
            ppActivityX1.setVisible(true);
            ppActivityX1.setManaged(true);
            ppActivityY1.setVisible(true);
            ppActivityY1.setManaged(true);
            ppActivityX2.setVisible(true);
            ppActivityX2.setManaged(true);
            ppActivityY2.setVisible(true);
            ppActivityY2.setManaged(true);
            ppActivityRotation.setVisible(true);
            ppActivityRotation.setManaged(true);
        }
    }


    @FXML
    void ppPageCUSelect(ActionEvent event) {
        /*//
    Set Text to menu
        ppPageTypeBox.setText(ppPageCU.getText());
        openedElement.getChildNodes().item(1).setTextContent("checkup");

        // Refresh tree page title
        String treeTitle = projectTree.getRoot().getValue();
        String firstHalfOfTitle = treeTitle.substring(0, treeTitle.indexOf('（'));
        projectTree.getRoot().setValue(firstHalfOfTitle + "（" + ppPageCU.getText() + "）");

        // Save this node to PrepareSdk
        try {
            prepareSdk.setPageToConfig(currentPage, this.openedElement);
        } catch (Exception e) {
            System.err.println("Error while saving node");
            e.printStackTrace();
        }*/
    }

    @FXML
    void ppPageNormalSelect(ActionEvent event) {
        /*//Set Text to menu
        ppPageTypeBox.setText(ppPageNormal.getText());
        openedElement.getChildNodes().item(1).setTextContent("normal");

        // Refresh tree page title
        String treeTitle = projectTree.getRoot().getValue();
        String firstHalfOfTitle = treeTitle.substring(0, treeTitle.indexOf('（'));
        projectTree.getRoot().setValue(firstHalfOfTitle + "（" + ppPageNormal.getText() + "）");

        // Save this node to PrepareSdk
        try {
            prepareSdk.setPageToConfig(currentPage, this.openedElement);
        } catch (Exception e) {
            System.err.println("Error while saving node");
            e.printStackTrace();
        }*/
    }

    @FXML
    void ppPagePTSelect(ActionEvent event) {
        /*//Set Text to menu
        ppPageTypeBox.setText(ppPagePT.getText());
        openedElement.getChildNodes().item(1).setTextContent("ptest");

        // Refresh tree page title
        String treeTitle = projectTree.getRoot().getValue();
        String firstHalfOfTitle = treeTitle.substring(0, treeTitle.indexOf('（'));
        projectTree.getRoot().setValue(firstHalfOfTitle + "（" + ppPagePT.getText() + "）");

        // Save this node to PrepareSdk
        try {
            prepareSdk.setPageToConfig(currentPage, this.openedElement);
        } catch (Exception e) {
            System.err.println("Error while saving node");
            e.printStackTrace();
        }*/
    }

    @FXML
    void ppPageSTSelect(ActionEvent event) {
        /*//Set Text to menu
        ppPageTypeBox.setText(ppPageST.getText());
        openedElement.getChildNodes().item(1).setTextContent("selftest");

        // Refresh tree page title
        String treeTitle = projectTree.getRoot().getValue();
        String firstHalfOfTitle = treeTitle.substring(0, treeTitle.indexOf('（'));
        projectTree.getRoot().setValue(firstHalfOfTitle + "（" + ppPageST.getText() + "）");

        // Save this node to PrepareSdk
        try {
            prepareSdk.setPageToConfig(currentPage, this.openedElement);
        } catch (Exception e) {
            System.err.println("Error while saving node");
            e.printStackTrace();
        }*/
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

        populateActivities();
    }

    @FXML
    void AutoGenAnnotPace(ActionEvent event) {
        // TODO: Add warning messages.
        try {
            prepareSdk.autoPreparePace();
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        populateActivities();
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

            pdfDisplayPane.setToolbarVisibility(true);
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

            pdfDisplayPane.setToolbarVisibility(false);
        }
    }

    public Stage getCurrentStage() {
        return currentStage;
    }

    public Double getRightPaneWidth() {
        if (rightPane.isVisible()) {
            return 200.0;
        }

        return 0.0;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.localeResource = resources;
        this.fileHandler = new FileHandler();
        this.project = new ePACEProject();
        this.svgLib = new SVGLibrary();

        // Initialize PDF display pane and set it to the center anchor pane
        FXMLLoader loader = new FXMLLoader(getClass().getResource("PDFDisplayPaneFragment.fxml"));
        loader.setResources(localeResource);
        try {
            loader.load();
        } catch (IOException e) {
            System.err.println("Cannot load PDF Display Pane");
            e.printStackTrace();
        }
        this.pdfDisplayPane = loader.getController();
        pdfDisplayPane.setMainController(this);
        pdfViewAnchor.getChildren().add(pdfDisplayPane.getPane());
        AnchorPane.setTopAnchor(pdfDisplayPane.getPane(), 0.0);
        AnchorPane.setBottomAnchor(pdfDisplayPane.getPane(), 0.0);
        AnchorPane.setLeftAnchor(pdfDisplayPane.getPane(), 0.0);
        AnchorPane.setRightAnchor(pdfDisplayPane.getPane(), 0.0);

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