import java.util.ArrayList;

public abstract class Player {
    protected CardDeck cardDeck;
    protected String name;
    protected ArrayList<PlayingCard> cardsOnHand;
    protected boolean playing;

    public Player(String name, CardDeck cardDeck) {
        this.cardDeck = cardDeck;
        this.name = name;
        cardsOnHand = new ArrayList<>();
        playing = false;
    }

    public String getName() {
        return name;
    }

    protected ArrayList<Integer> getPossibleValuesOnHand() {
        int valueOnHand = 0;
        int aces = 0;
        for (PlayingCard c : cardsOnHand) {
            if (c.getCardName().split("")[0].equals("A")) aces++; //To NOT include bAckSide
            valueOnHand += c.getCardValue();
        }

        ArrayList<Integer> possibleValues = new ArrayList<>();
        possibleValues.add(valueOnHand);
        for (int a = 1; a <= aces; a++) {
            int value = valueOnHand + 10 * a;
            if (value <= 21) possibleValues.add(value);
        }
        return possibleValues;
    }

    public int getBestValueOnHand() {
        int highValue = getPossibleValuesOnHand().get(0);
        for (int v : getPossibleValuesOnHand()) {
            highValue = v > 21 ? highValue : v;
        }
        return highValue;
    }

    public void play() {
        PlayingCard card = cardDeck.drawCard();
        cardsOnHand.add(card);
    }

    public boolean getPlaying() {
        return playing;
    }

    protected void reset() {
        cardsOnHand.clear();
        play();
        play();
        playing = false;
    }

    public abstract String getInfo();

    public abstract ArrayList<PlayingCard> getCardsOnHand();
}
