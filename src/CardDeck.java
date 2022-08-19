import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class CardDeck {
    private static PlayingCard cardBackside;
    private final ArrayList<PlayingCard> fullDeckOfCards;
    private final int noCardDecks;
    private final ArrayList<PlayingCard> gameCardDeck;

    public CardDeck(int noCardDecks, String themeColor) {
        cardBackside = new PlayingCard("0", themeColor + "_back");


        fullDeckOfCards = new ArrayList<>();
        String[] suits = new String[] {"S", "D", "C", "H"};
        String[] ranks = new String[] {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        //String[] ranks = new String[] {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"};
        for (String s : suits) {
            for (String r : ranks) {
                PlayingCard c = new PlayingCard(r, s);
                fullDeckOfCards.add(c);
            }
        }

        this.noCardDecks = noCardDecks;
        gameCardDeck = new ArrayList<>();
        reset();
    }

    public static PlayingCard getCardBackside() {
        return cardBackside;
    }

    public void reset() {
        gameCardDeck.clear();
        for (int d = 0; d < noCardDecks; d++) {
            ArrayList<PlayingCard> deck = (ArrayList<PlayingCard>) fullDeckOfCards.clone();
            gameCardDeck.addAll(deck);
        }
        Collections.shuffle(gameCardDeck);
    }

    public PlayingCard drawCard() {
        //return new PlayingCard("A", "H");
        int rn = new Random().nextInt(gameCardDeck.size());
        PlayingCard card = gameCardDeck.get(rn);
        gameCardDeck.remove(rn);
        return card;
    }

    public int cardDeckSize() {
        return gameCardDeck.size();
    }
}
