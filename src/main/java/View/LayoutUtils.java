package View;

import javafx.scene.text.Font;
import javafx.stage.Screen;

public final class LayoutUtils {

    public static final double WINDOW_WIDTH = (Screen.getPrimary().getVisualBounds().getWidth()/12)* 10;
    public static final double WINDOW_HEIGHT = (Screen.getPrimary().getVisualBounds().getHeight()/12) * 11;

    public static final double CARD_WIDTH = WINDOW_HEIGHT/10;
    public static final double CARD_HEIGHT = (CARD_WIDTH/3) * 4;

    public static final double MINIMUM_BET = 50.0; // Dollars

    public static final double BANK_START_Y = WINDOW_HEIGHT/10;

    public static final double MARGIN_5 = 5.0;
    public static final double MARGIN_10 = 10.0;
    public static final double MARGIN_15 = 15.0;
    public static final double MARGIN_20 = 20.0;
    public static final double MARGIN_30 = 30.0;
    public static final double MARGIN_40 = 40.0;
    public static final double MARGIN_50 = 50.0;

    public static final double COMBO_BOX_MARGIN = 150.0;

    public static final double BET_TEXT_MARGIN = 200.0;

    public static final double HIT_BUTTON_Y_MARGIN = 60.0;

    public static Font getPreferredFont(double size) {
        return Font.font("Verdana", size);
    }

    private LayoutUtils() {
        throw new RuntimeException("You cannot allocate an instance of this class. Sorry.");
    }
}
