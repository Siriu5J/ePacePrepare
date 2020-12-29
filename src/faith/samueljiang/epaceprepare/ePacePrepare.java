package faith.samueljiang.epaceprepare;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class ePacePrepare extends Application {
    private static Locale cn_locale;
    private static ResourceBundle cn_bundle;

    public static final String version = "0.0.1";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Add localization initialization
        cn_locale = Locale.CHINA;
        cn_bundle = ResourceBundle.getBundle("faith.samueljiang.epaceprepare.locale", cn_locale);
        FXMLLoader guiLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        guiLoader.setResources(cn_bundle);

        Parent root = (Parent) guiLoader.load();
        Scene mainScene = new Scene(root, 800, 800);
        primaryStage.setTitle(cn_bundle.getString("program.name") + " " + version);
        primaryStage.setMinHeight(860);
        primaryStage.setMinWidth(1050);
        primaryStage.setScene(mainScene);

        // Pass primary stage to MainController
        MainController mc = guiLoader.getController();
        mc.setMainStage(primaryStage);

        primaryStage.show();
    }
}
