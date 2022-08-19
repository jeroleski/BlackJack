import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class ErrorGUI extends Application {
    private static String message;
    private static Stage playGUI;

    public static void setMessage(String m) {
        message = m;
    }
    public static void setPlayGUI(Stage pg) {
        playGUI = pg;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        HBox mainLayout = new HBox();

        Label textMessage = new Label(message);
        mainLayout.getChildren().add(textMessage);

        primaryStage.setOnCloseRequest(e -> {
            primaryStage.close();
            if (playGUI != null) {
                playGUI.close();
                playGUI = null;
                new PlayGUI().start(new Stage());
            }
        });

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage .setTitle("ERRUDUMELLAHVAD");
        primaryStage.show();
    }
}
