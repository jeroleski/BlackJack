import java.util.ArrayList;

public class Dealer extends Player{

    public Dealer(CardDeck cardDeck) {
        super("$$ DEALER $$", cardDeck);
    }

    public String getInfo() {
        return "On hand: " + valuesToString();
    }
    private String valuesToString() {
        String values;
        //if (! playing && getBestValueOnHand() == 21) throw new Exception("Dealer wins");
        if (!playing) values = "" + cardsOnHand.get(1).getCardValue();
        else values = "" + getBestValueOnHand();

        return values;
    }

    public ArrayList<PlayingCard> getCardsOnHand() {
        if (!playing) {
            ArrayList<PlayingCard> cards = new ArrayList<>();
            cards.add(CardDeck.getCardBackside());
            cards.add(cardsOnHand.get(1));
            return cards;
        }
        return cardsOnHand;
    }

    public void play() {
        playing = true;
        while (getBestValueOnHand() != 21 && getPossibleValuesOnHand().get(0) < 17) {
            super.play();
        }
    }

    public void reset() {
        super.reset();
    }
}
