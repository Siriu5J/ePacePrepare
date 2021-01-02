package faith.samueljiang.epaceprepare;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;

import java.util.ResourceBundle;

public class BufferPdfTask extends Task<Void> {
    private PDFControl control;
    private int page;

    BufferPdfTask(PDFControl control, int page) {
        this.control = control;
        this.page = page;
    }

    @Override
    protected Void call() {
        final int labelInt = this.page;
        Platform.runLater(
                () -> {
                    control.getRightLabel().setText(control.getLocaleResource().getString("ui.buffering.page") + "（" + (labelInt + 1) + "）");
                }
        );
        this.control.renderPage(page);
        Platform.runLater(
                () -> {
                    control.getRightLabel().setText(control.getLocaleResource().getString("ui.done"));
                }
        );
        return null;
    }
}
