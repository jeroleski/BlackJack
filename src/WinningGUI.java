import javafx.application.Application;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class WinningGUI extends Application {
    private static BlackJack blackJack;
    private static Stage playGUI;

    public static void setBlackJack(BlackJack bj) {
        blackJack = bj;
    }
    public static void setPlayGUI(Stage pg) {
        playGUI = pg;
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        VBox mainLayout = new VBox();

        HBox boxAllPlayers = new HBox();
        boxAllPlayers.setSpacing(30);
        Guest[] allGuests = blackJack.getGuests();

        for (Guest g : allGuests) {
            VBox playerBox = createPlayerBox(g, blackJack.whoWins(g));
            boxAllPlayers.getChildren().add(playerBox);

            for (SplitGuest sg : g.getSplitGuests()) {
                VBox splitPlayerBox = createPlayerBox(sg, blackJack.whoWins(sg));
                boxAllPlayers.getChildren().add(splitPlayerBox);
            }
        }
        mainLayout.getChildren().add(boxAllPlayers);

        Button newGameButton = new Button("Start a new game");
        newGameButton.setOnAction(e -> {
            primaryStage.close();
            PlayGUI.newGame();
            playGUI.close();
            new PlayGUI().start(new Stage());
        });
        mainLayout.getChildren().add(newGameButton);

        primaryStage.setOnCloseRequest(e -> {
            primaryStage.close();
            playGUI.close();
        });

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage .setTitle("$$   Black Jack   $$");
        primaryStage.show();
    }

    private VBox createPlayerBox(Player player, double relation) {
        VBox playerBox = new VBox();

        Label nameLabel = new Label(player.getName());
        Label valueLabel = new Label("Value: " + player.getBestValueOnHand());
        playerBox.getChildren().addAll(nameLabel, valueLabel);

        Label winnerLabel = new Label();
        playerBox.getChildren().add(winnerLabel);

        if (relation == 0) winnerLabel.setText("--LOST--");
        else {
            if (relation == 1) winnerLabel.setText("--TIE--");
            else if (relation == 2) winnerLabel.setText("--WIN--");

            Label betLabel = new Label("Prize: " + (int) (relation * ((Guest) player).getBet()) + " $");
            playerBox.getChildren().add(betLabel);
        }

        return  playerBox;
    }
}
