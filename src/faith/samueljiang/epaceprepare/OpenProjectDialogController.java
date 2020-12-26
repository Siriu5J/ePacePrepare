package faith.samueljiang.epaceprepare;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class OpenProjectDialogController implements Initializable {

    private FileHandler fileHandler;
    private ResourceBundle localeResource;
    private File projectFilePath;
    private ePACEProject project;

    @FXML // fx:id="projectOpenProject"
    private AnchorPane projectOpenProject; // Value injected by FXMLLoader

    @FXML // fx:id="projectPath"
    private TextField projectPath; // Value injected by FXMLLoader

    @FXML // fx:id="projectPathButton"
    private Button projectPathButton; // Value injected by FXMLLoader

    @FXML // fx:id="projectCancel"
    private Button projectCancel; // Value injected by FXMLLoader

    @FXML // fx:id="projectOpen"
    private Button projectOpen; // Value injected by FXMLLoader

    @FXML
    void cancelButton(ActionEvent event) {
        Stage stage = (Stage) projectCancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    void openButton(ActionEvent event) {
        // TODO: Check project integrity

        this.project.clone(fileHandler.openProject(this.projectFilePath));
        Stage stage = (Stage) projectOpen.getScene().getWindow();
        stage.close();
    }

    @FXML
    void pickPath(ActionEvent event) {
        String path = fileHandler.selectDirectoryWithGUI(localeResource.getString("open.proj.select.path")).getAbsolutePath();
        projectPath.setText(path);
        projectFilePath = new File(path);
    }

    public void setProject(ePACEProject project) {
        this.project = project;
    }

    public void setFileHandler(FileHandler fileHandler) {
        this.fileHandler = fileHandler;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.localeResource = resourceBundle;
    }
}