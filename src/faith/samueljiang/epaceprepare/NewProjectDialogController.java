package faith.samueljiang.epaceprepare;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewProjectDialogController implements Initializable {

    private FileHandler fileHandler;
    private ResourceBundle localeResources;
    private File projectFilePath;
    private File projectPdfFile;
    private File projectAnnotFile;
    private ePACEProject project;

    @FXML // fx:id="projectNewProject"
    private AnchorPane projectNewProject; // Value injected by FXMLLoader

    @FXML // fx:id="projectNameText"
    private Label projectNameText; // Value injected by FXMLLoader

    @FXML // fx:id="projectName"
    private TextField projectName; // Value injected by FXMLLoader

    @FXML // fx:id="projectPathText"
    private Label projectPathText; // Value injected by FXMLLoader

    @FXML // fx:id="projectPath"
    private TextField projectPath; // Value injected by FXMLLoader

    @FXML // fx:id="projectPathButton"
    private Button projectPathButton; // Value injected by FXMLLoader

    @FXML // fx:id="projectCourseText"
    private Label projectCourseText; // Value injected by FXMLLoader

    @FXML // fx:id="projectCourse"
    private TextField projectCourse; // Value injected by FXMLLoader

    @FXML // fx:id="projectPaceNumText"
    private Label projectPaceNumText; // Value injected by FXMLLoader

    @FXML // fx:id="projectPaceNum"
    private TextField projectPaceNum; // Value injected by FXMLLoader

    @FXML // fx:id="projectImportPdfText"
    private Label projectImportPdfText; // Value injected by FXMLLoader

    @FXML // fx:id="projectImportPdfSwitch"
    private CheckBox projectImportPdfSwitch; // Value injected by FXMLLoader

    @FXML // fx:id="PdfPathHBox"
    private HBox PdfPathHBox; // Value injected by FXMLLoader

    @FXML // fx:id="projectPdfPathText"
    private Label projectPdfPathText; // Value injected by FXMLLoader

    @FXML // fx:id="projectPdfPath"
    private TextField projectPdfPath; // Value injected by FXMLLoader

    @FXML // fx:id="projectPdfPathButton"
    private Button projectPdfPathButton; // Value injected by FXMLLoader

    @FXML // fx:id="AnnotPathHBox"
    private HBox AnnotPathHBox; // Value injected by FXMLLoader

    @FXML // fx:id="projectAnnotPathText"
    private Label projectAnnotPathText; // Value injected by FXMLLoader

    @FXML // fx:id="projectAnnotPath"
    private TextField projectAnnotPath; // Value injected by FXMLLoader

    @FXML // fx:id="projectAnnotPathButton"
    private Button projectAnnotPathButton; // Value injected by FXMLLoader

    @FXML // fx:id="projectCancel"
    private Button projectCancel; // Value injected by FXMLLoader

    @FXML // fx:id="projectComplete"
    private Button projectComplete; // Value injected by FXMLLoader


    @FXML
    void cancelButton(ActionEvent event) {
        Stage stage = (Stage) projectCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void completeButton(ActionEvent event) {
        //TODO: Verify input data

        // Create necessary files and dir
        fileHandler.createDirIfNotExist(projectFilePath.getPath());
        File configFile = fileHandler.createConfigFile(projectFilePath.getPath());

        this.project.clone(fileHandler.createProject(projectName.getText(),
                projectFilePath.getPath(),
                projectPdfFile,
                projectAnnotFile,
                configFile,
                projectCourse.getText(),
                Integer.parseInt(projectPaceNum.getText())));

        Stage stage = (Stage) projectComplete.getScene().getWindow();
        stage.close();
    }

    @FXML
    void intCheck(KeyEvent event) {
        //TODO: Only allow integer input
    }

    @FXML
    void importPdfCheckbox(ActionEvent event) {
        // Get current scene
        Node node = (Node) event.getSource();
        Stage currentStage = (Stage) node.getScene().getWindow();

        if (projectImportPdfSwitch.isSelected()) {
            PdfPathHBox.setManaged(true);
            PdfPathHBox.setVisible(true);
            AnnotPathHBox.setManaged(true);
            AnnotPathHBox.setVisible(true);

            // Set Proper height
            currentStage.setMinHeight(375);
            currentStage.setHeight(375);
        } else {
            PdfPathHBox.setManaged(false);
            PdfPathHBox.setVisible(false);
            AnnotPathHBox.setManaged(false);
            AnnotPathHBox.setVisible(false);

            // Clear the paths
            projectPdfPath.setText("");
            projectAnnotPath.setText("");

            // Set Proper height
            currentStage.setMinHeight(275);
            currentStage.setHeight(275);
        }
    }

    @FXML
    void pickAnnotFile(ActionEvent event) {
        FileChooser.ExtensionFilter[] filters = {new FileChooser.ExtensionFilter("Adobe Acrobat XFDF Document", "*.xfdf")};
        projectAnnotFile = fileHandler.selectFileWithGUI(localeResources.getString("new.proj.pick.annot"), filters);
        if (projectAnnotFile != null) {
            projectAnnotPath.setText(projectAnnotFile.getPath());
        }
    }

    @FXML
    void pickPdfFile(ActionEvent event) {
        FileChooser.ExtensionFilter[] filters = {new FileChooser.ExtensionFilter("PDF File", "*.pdf")};
        projectPdfFile = fileHandler.selectFileWithGUI(localeResources.getString("new.proj.select.pdf"), filters);
        if (projectPdfFile != null) {
            projectPdfPath.setText(projectPdfFile.getPath());
        }
    }

    /**
     * This class handles the onClick event of the path picker
     * @param event The ActionEvent
     */
    @FXML
    void pickPath(ActionEvent event) {
        String path = fileHandler.selectDirectoryWithGUI(localeResources.getString("new.proj.select.path")).getAbsolutePath();
        if (!path.isEmpty()) {
            if (!projectName.getText().isEmpty()) {
                projectFilePath = new File(path + "/" + projectName.getText());
            } else {
                projectFilePath = new File(path);
            }

            projectPath.setText(projectFilePath.getPath());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.localeResources = resources;
        this.projectFilePath = null;
        this.projectPdfFile = null;

        // Hide import files first
        projectImportPdfSwitch.setSelected(false);
        PdfPathHBox.setManaged(false);
        PdfPathHBox.setVisible(false);
        AnnotPathHBox.setManaged(false);
        AnnotPathHBox.setVisible(false);

        // Set Height
        projectNewProject.setPrefHeight(255);
    }


    public void setFileHandler(FileHandler f) {
        this.fileHandler = f;
    }

    public void setProject(ePACEProject ep) {
        this.project = ep;
    }
}
