package faith.samueljiang.epaceprepare;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;

enum SVGImage {
    DELETE,
    PREVIOUS,
    NEXT,
    BLANK,
    SCORESTRIP,
    MULTISELECT,
    CALLIGRAPHY,
    CONNECT,
    ERASER,
    DICTATION
}

public class SVGLibrary {
    public Node getSVG(SVGImage svgType, Color strokeColor, Double scaleX, Double scaleY) {
        SVGPath svg = new SVGPath();

        switch (svgType) {
            case DELETE:
                svg.setContent("M6 21h12V7H6v14zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z");
                break;
            case PREVIOUS:
                svg.setContent("M20 11H7.83l5.59-5.59L12 4l-8 8 8 8 1.41-1.41L7.83 13H20v-2z");
                break;
            case NEXT:
                svg.setContent("M12 4l-1.41 1.41L16.17 11H4v2h12.17l-5.58 5.59L12 20l8-8z");
                break;
            case BLANK:
                svg.setContent("M4.08,14.08H15.75m0,0h10M18.16,30.75H30.75V17.42Zm-4.08,0h0L15.75,29l15-15.89V.75H.75v30Z");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
            case SCORESTRIP:
                svg.setContent("M.75,30.75V.75h30V13.1L15.75,29l-1.67,1.76H.75Zm25-18.33H4.08v3.33H25.75Zm-15,0v3.33m8.33-3.33v3.33m-1.66,15H30.75V17.42l-6.67,6.66-6.66,6.67Z");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
            case CALLIGRAPHY:
                svg.setContent("M.75,30.75V.75h30V13.1L15.75,29l-1.67,1.76H.75ZM4.08,14.08H27.42m-11.67-10v20m1.67,6.67H30.75V17.42l-6.67,6.66-6.66,6.67Z");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
            case MULTISELECT:
                svg.setContent("M18.16,30.75H30.75V17.42Zm-4.08,0h0L15.75,29l15-15.89V.75H.75v30ZM10.36,14.91h3.33m3.34,0h3.33m3.33,0H27m-2.84-2.14,1.2,1.09.07.08,2.28-2.17M8.57,13.24H5.24v3.34H8.57Z");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
            case CONNECT:
                svg.setContent("M10.75,14.08h8.33m-.92,16.67H30.75V17.42Zm-4.08,0h0L15.75,29l15-15.89V.75H.75v30ZM7.42,12.42H4.08v3.33H7.42Zm18.33,0H22.42v3.33h3.33Z");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
            case ERASER:
                svg.setContent("M3.59,17.29l9.92,7.37,2.55,1.9L13,30.5.5,21.23ZM30.5,13.11,13.53.5l-12,15.28,2,1.51h0l9.92,7.37,5,3.73ZM3.59,17.29l9.92,7.37L3.59,17.29Z");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
            case DICTATION:
                svg.setContent("M1,16a11.59,11.59,0,0,1-.2-2.14C.75,6.6,7.47.75,15.75.75s15,5.85,15,13.07a12.17,12.17,0,0,1-.19,2.11M8.25,16H.75v8.72h7.5Zm22.5,0h-7.5v8.72h7.5ZM16.23,30.75l1.63-1.42H14.6l1.63,1.42M14.6,12.26h3.26V9.42H14.6V29.33h3.26V12.26");
                svg.setStrokeWidth(1.7);
                svg.setStrokeLineCap(StrokeLineCap.SQUARE);
                svg.setFill(Color.TRANSPARENT);
                break;
        }

        svg.setStroke(strokeColor);
        svg.setScaleX(scaleX);
        svg.setScaleY(scaleY);

        return svg;
    }
}
