import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.Random;

public class SetupGUI extends Application {
    private static int noPlayersSelected = 2;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(10);

        Label message = new Label("Welcome to Black Jack!");
        mainLayout.getChildren().add(message);

        HBox boxNoPlayers = new HBox();
        Label textNoPlayers = new Label("How many players?");
        ChoiceBox<Integer> selectNoPlayers = new ChoiceBox<>();
        for (int i = 1; i <= 4; i++) {
            selectNoPlayers.getItems().add(i);
        }
        selectNoPlayers.setValue(noPlayersSelected);
        selectNoPlayers.setOnAction(e -> {
            noPlayersSelected = selectNoPlayers.getValue();
            primaryStage.close();
            new SetupGUI().start(new Stage());
        });
        boxNoPlayers.getChildren().addAll(textNoPlayers, selectNoPlayers);
        mainLayout.getChildren().addAll(boxNoPlayers);

        VBox boxAllPlayerNames = new VBox();
        TextField[] arrayAllPlayerNames = new TextField[noPlayersSelected];
        for (int i = 1; i <= noPlayersSelected; i++) {
            HBox boxPlayerName = new HBox();
            Label textPlayerName = new Label("Player " + i + "'s name:");
            TextField selectPlayerName = new TextField();
            selectPlayerName.setPromptText("Write the name of player " + i);
            arrayAllPlayerNames[i - 1] = selectPlayerName;
            boxPlayerName.getChildren().addAll(textPlayerName, selectPlayerName);
            boxAllPlayerNames.getChildren().add(boxPlayerName);
        }
        mainLayout.getChildren().add(boxAllPlayerNames);

        HBox boxMoney = new HBox();
        Label textMoney = new Label("How much money will you insert?");
        TextField selectMoney = new TextField("1000");
        selectMoney.setPromptText("Amount to withdraw");
        Label textAfterMoney = new Label("$");
        boxMoney.getChildren().addAll(textMoney, selectMoney, textAfterMoney);
        mainLayout.getChildren().add(boxMoney);

        HBox boxCards = new HBox();
        Label textCards = new Label("How many decks of Cards should be shuffled?");
        ChoiceBox<Integer> selectCards = new ChoiceBox<>();
        for (int i = 1; i <= 8; i++) {
            selectCards.getItems().add(i);
        }
        selectCards.setValue(6);
        boxCards.getChildren().addAll(textCards, selectCards);
        mainLayout.getChildren().add(boxCards);

        HBox boxShuffle = new HBox();
        Label textShuffle = new Label("Shuffle card deck between rounds?");
        CheckBox selectShuffle = new CheckBox();
        boxShuffle.getChildren().addAll(textShuffle, selectShuffle);
        mainLayout.getChildren().add(boxShuffle);

        HBox boxRules = new HBox();
        Label textRules = new Label("What is allowed:");
        ChoiceBox<String> selectRules = new ChoiceBox<>();
        selectRules.getItems().addAll("Double and Split", "Double only", "Split only", "Neither");
        selectRules.setValue("Double and Split");
        boxRules.getChildren().addAll(textRules, selectRules);
        mainLayout.getChildren().add(boxRules);

        HBox boxColor = new HBox();
        Label textColor = new Label("Select color theme:");
        String[] colors = new String[] {"Blue", "Gray", "Green", "Purple", "Red", "Yellow"};
        ChoiceBox<String> selectColor = new ChoiceBox<>();
        for (String c : colors) {
            selectColor.getItems().add(c);
        }
        Random rn = new Random();
        selectColor.setValue(colors[rn.nextInt(colors.length)]);
        boxColor.getChildren().addAll(textColor, selectColor);
        mainLayout.getChildren().add(boxColor);

        Button startButton = new Button("START");
        startButton.setOnAction(e -> {
            String[] playerNames = new String[noPlayersSelected];
            for (int i = 0; i < noPlayersSelected; i++) {
                String name = arrayAllPlayerNames[i].getText();
                playerNames[i] = name.trim().isEmpty() ? "Player " + (i + 1) : name;
            }
            int noMoney = Integer.parseInt(selectMoney.getText());
            int noCardDecks = selectCards.getValue();
            String rules = selectShuffle.isSelected() ? "Shuffle" : "Stay same";
            rules += " " + selectRules.getValue();
            String color = selectColor.getValue();
            BlackJack blackJack = new BlackJack(rules, color, playerNames, noMoney, noCardDecks);

            PlayGUI.setBlackJack(blackJack);
            WinningGUI.setBlackJack(blackJack);
            primaryStage.close();
            new PlayGUI().start(new Stage());
        });
        mainLayout.getChildren().add(startButton);

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("$$   Black Jack   $$");
        primaryStage.show();
    }
}
