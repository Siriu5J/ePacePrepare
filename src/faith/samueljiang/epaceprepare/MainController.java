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
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    private ResourceBundle localeResource;
    private PrepareSdk blankExtractor;
    private FileHandler fileHandler;

    // Project Related Variables
    private ePACEProject project;

    // Tools Selection; this shows the tools selected
    // 0 means non selected
    private int toolSelect;


    @FXML // fx:id="menuBarFile"
    private Menu menuBarFile; // Value injected by FXMLLoader

    @FXML // fx:id="menuOpenProject"
    private MenuItem menuOpenProject; // Value injected by FXMLLoader

    @FXML // fx:id="menuNewProject"
    private MenuItem menuNewProject; // Value injected by FXMLLoader

    @FXML // fx:id="menuExit"
    private MenuItem menuExit; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarProject"
    private Menu menuBarProject; // Value injected by FXMLLoader

    @FXML // fx:id="menuAddPdf"
    private MenuItem menuAddPdf; // Value injected by FXMLLoader

    @FXML // fx:id="menuAddAnnot"
    private MenuItem menuAddAnnot; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarEdit"
    private Menu menuBarEdit; // Value injected by FXMLLoader

    @FXML // fx:id="menuBarAbout"
    private Menu menuBarAbout; // Value injected by FXMLLoader

    @FXML // fx:id="menuAbout"
    private MenuItem menuAbout; // Value injected by FXMLLoader

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

    @FXML
    void newProject(ActionEvent event) {
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

            if (hasOpenProject()) {
                initPane();
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

            if (hasOpenProject()) {
                initPane();
            }
        } catch (Exception e) {
            System.err.println(e);
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


    private boolean hasOpenProject() {
        return this.project.getProjectPath() != null;
    }


    private void initPane() {
        // Create the page info in config if necessary
        if (fileHandler.getProperty(project.getProjectPath(), "config_populated").equals("0")) {
            populateConfigWithInfo();
        }

        //populateTreeView();
    }


    private void populateConfigWithInfo() {
        File pdfFile = project.getPdfFile();
        File config = project.getConfigFile();

        // Loop through the
        try (PDDocument pdf = PDDocument.load(pdfFile)) {
            fileHandler.initConfigFilePages(config, pdf.getNumberOfPages());
            // Update the property
            fileHandler.updateProperty(project.getProjectPath(), "config_populated", "1");
        } catch (Exception e) {
            System.err.println(e);
        }
    }


    private void populateTreeView() {
        File properties = new File(project.getProjectPath() + "/.properties");
        File config = project.getConfigFile();

        // Populate the tree for current page

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.localeResource = resources;
        this.fileHandler = new FileHandler();
        this.blankExtractor = new PrepareSdk();
        this.project = new ePACEProject();

        // Fill the images to the tool buttons
        Image toolsBlank = new Image("/asset/tools-blank.png");
        ImageView toolsBlankView = new ImageView(toolsBlank);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCBlank.setGraphic(toolsBlankView);

        Image toolsMulti = new Image("/asset/tools-multi.png", 45, 45, false, false);
        ImageView toolsMultiView = new ImageView(toolsMulti);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCMultiple.setGraphic(toolsMultiView);

        Image toolsConnect = new Image("/asset/tools-connect.png", 45, 45, false, false);
        ImageView toolsConnectView = new ImageView(toolsConnect);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCConnect.setGraphic(toolsConnectView);

        Image toolsCalli= new Image("/asset/tools-calli.png", 45, 45, false, false);
        ImageView toolsCalliView = new ImageView(toolsCalli);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCCalli.setGraphic(toolsCalliView);

        Image toolsStrip = new Image("/asset/tools-strip.png", 45, 45, false, false);
        ImageView toolsStripView = new ImageView(toolsStrip);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCStrip.setGraphic(toolsStripView);

        Image toolsDict = new Image("/asset/tools-dict.png", 45, 45, false, false);
        ImageView toolsDictView = new ImageView(toolsDict);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCDict.setGraphic(toolsDictView);

        Image toolsEraser = new Image("/asset/tools-eraser.png", 45, 45, false, false);
        ImageView toolsEraserView = new ImageView(toolsEraser);
        toolsBlankView.setFitHeight(45);
        toolsBlankView.setPreserveRatio(true);
        toolsCErase.setGraphic(toolsEraserView);

        // Tools set
        toolSelect = 0;

    }
}