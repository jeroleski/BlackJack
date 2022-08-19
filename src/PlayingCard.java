import javafx.scene.image.Image;

public class PlayingCard {
    private final Image cardImage;
    private String cardName;
    private final int cardValue;

    public PlayingCard(String rank, String suit) {
        this.cardName = rank + suit;
        if (suit.contains("back")) cardName = suit;

        String source = "CardPictures/" + cardName + ".jpg";
        cardImage = new Image(getClass().getClassLoader().getResourceAsStream(source));

        if (rank.equals("J") || rank.equals("Q") || rank.equals("K")) {
            this.cardValue = 10;
        } else if (rank.equals("A")) {
            this.cardValue = 1;
        } else {
            this.cardValue = Integer.parseInt(rank);
        }

    }

    public String getCardName() {
        return cardName;
    }

    public Image getCardImage() {
        return cardImage;
    }

    public int getCardValue() {
        return cardValue;
    }
}
