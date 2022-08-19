import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.InputMismatchException;

public class PlayGUI extends Application {
    private Stage primaryStage;
    private static BlackJack blackJack;

    public static void setBlackJack(BlackJack bj) {
        blackJack = bj;
    }
    public static void newGame() {
        blackJack.reset();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        VBox mainLayout = new VBox();
        mainLayout.setSpacing(30);

        Label roundLabel = new Label(blackJack.getInfo());
        mainLayout.getChildren().add(roundLabel);

        Dealer dealer = blackJack.getDealer();
        createPlayerBox(dealer, mainLayout);

        HBox boxAllPlayers = new HBox();
        boxAllPlayers.setSpacing(30);
        Guest[] allGuests = blackJack.getGuests();
        for (Guest g : allGuests) {
            createPlayerBox(g, boxAllPlayers);
        }
        mainLayout.getChildren().add(boxAllPlayers);

        Scene scene = new Scene(mainLayout);
        primaryStage.setScene(scene);
        primaryStage .setTitle("$$   Black Jack   $$");
        primaryStage.show();
    }

    private void createPlayerBox(Player player, Pane boxToAddOn) {
        VBox boxPlayer = new VBox();

        Label playerName = new Label(player.getName());
        boxPlayer.getChildren().add(playerName);

        HBox boxCards = new HBox();
        ArrayList<PlayingCard> playerCards = player.getCardsOnHand();
        for (PlayingCard c : playerCards) {
            ImageView view = new ImageView(c.getCardImage());
            view.setFitHeight(141);
            view.setFitWidth(100);
            boxCards.getChildren().add(view);
        }
        boxPlayer.getChildren().add(boxCards);
        boxToAddOn.getChildren().add(boxPlayer);

        if (player instanceof Dealer) boxPlayer.getChildren().add(createDealer((Dealer) player));
        if (player instanceof Guest) {
            Guest guest = (Guest) player;
            boxPlayer.getChildren().add(createGuest(guest));
            for (SplitGuest sg : guest.getSplitGuests()) {
                createPlayerBox(sg, boxToAddOn);
            }
        }
    }

    private HBox createDealer(Dealer dealer) {
        HBox boxInfo = new HBox();

        Label playerInfo = new Label(dealer.getInfo());
        boxInfo.getChildren().add(playerInfo);

        if (blackJack.getAllHasPass()) {
            Button playButton = new Button("Start dealers turn");
            playButton.setOnAction(e -> {
                dealer.play();
                primaryStage.close();
                Stage futureStage = new Stage();
                new PlayGUI().start(futureStage);
                WinningGUI.setPlayGUI(futureStage);
                new WinningGUI().start(new Stage());
            });
            boxInfo.getChildren().addAll(playButton);
        }

        return boxInfo;
    }

    private HBox createGuest(Guest guest) {
        HBox boxInfo = new HBox();

        Label playerInfo = new Label(guest.getInfo());
        boxInfo.getChildren().add(playerInfo);

        VBox boxMenu = new VBox();
        if (! guest.getPlaying()) {
            Button betButton = new Button("Set Bet");
            TextField amountToBet = new TextField();
            betButton.setOnAction(e -> {
                try {
                    guest.setBet(Integer.parseInt(amountToBet.getText()));
                    primaryStage.close();
                    new PlayGUI().start(new Stage());
                } catch (NumberFormatException x) {
                    ErrorGUI.setMessage("NO DOLLAR SIGNS OR DECIMAL NUMBERS!");
                    new ErrorGUI().start(new Stage());
                } catch (InputMismatchException x) {
                    ErrorGUI.setMessage(x.getMessage());
                    new ErrorGUI().start(new Stage());
                } catch (IndexOutOfBoundsException x) {
                    primaryStage.close();
                    Stage futureStage = new Stage();
                    new PlayGUI().start(futureStage);
                    guest.reset(1.5);
                    ErrorGUI.setPlayGUI(futureStage);
                    ErrorGUI.setMessage(x.getMessage());
                    new ErrorGUI().start(new Stage());
                }
            });
            boxMenu.getChildren().add(betButton);

            amountToBet.setPromptText("How lucky do you feel?");
            amountToBet.setText("" + guest.getBet());
            boxMenu.getChildren().add(amountToBet);
        } else if (! guest.getStand()) {
            Button playButton = new Button("Hit");
            playButton.setOnAction(e -> {
                guest.play();
                primaryStage.close();
                new PlayGUI().start(new Stage());
            });
            boxMenu.getChildren().add(playButton);

            Button holdButton = new Button("Stand");
            holdButton.setOnAction(e -> {
                guest.setStand();
                primaryStage.close();
                new PlayGUI().start(new Stage());
            });
            boxMenu.getChildren().add(holdButton);

            if (blackJack.getDoubleAllowed() && guest.canDouble()) {
                Button doubleButton = new Button("Double");
                doubleButton.setOnAction(e -> {
                    try {
                        guest.doubleUp();
                        primaryStage.close();
                        new PlayGUI().start(new Stage());
                    } catch (InputMismatchException x) {
                        ErrorGUI.setMessage(x.getMessage());
                        new ErrorGUI().start(new Stage());
                    }
                });
                boxMenu.getChildren().add(doubleButton);
            }

            if (blackJack.getSplitAllowed() && guest.canSplit()) {
                Button splitButton = new Button("Split");
                splitButton.setOnAction(e -> {
                    guest.splitIt();
                    primaryStage.close();
                    new PlayGUI().start(new Stage());
                });
                boxMenu.getChildren().add(splitButton);
            }
        }

        boxInfo.getChildren().add(boxMenu);
        return boxInfo;
    }
}
