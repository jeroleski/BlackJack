import java.util.ArrayList;
import java.util.InputMismatchException;

public class Guest extends Player {
    protected int money;
    protected int bet;
    protected boolean stand;
    protected ArrayList<SplitGuest> splitGuests;

    public Guest(String name, CardDeck cardDeck, int money) {
        super(name, cardDeck);
        this.money = money;
        bet = 100;
        stand = false;
        splitGuests = new ArrayList<>();
    }

    public String getInfo() {
        String value;
        String bet;
        if (!playing) {
            bet = "";
            value = "?";
        } else {
            value = valuesToString();
            bet = "" + this.bet;
        }
        return "On hand: " + value + "\nMoney: " + money + " $\nBet: " + bet;
    }
    protected String valuesToString() {
        String values = "";
        ArrayList<Integer> valueOnHand = getPossibleValuesOnHand();
        if (getBestValueOnHand() == 21) values = "21";
        else if (stand) values = "" + getBestValueOnHand();
        else {
            for (int i = 0; i < valueOnHand.size(); i++) {
                values = values.concat("" + valueOnHand.get(i));
                if (i + 1 != valueOnHand.size()) values += " - ";
            }
        }
        return values;
    }

    public ArrayList<PlayingCard> getCardsOnHand() {
        if (playing) return cardsOnHand;
        else {
            ArrayList<PlayingCard> cards = new ArrayList<>();
            cards.add(CardDeck.getCardBackside());
            cards.add(CardDeck.getCardBackside());
            return cards;
        }
    }

    public void setBet(int amount) {
        if (amount > money) throw new InputMismatchException("Not enough money\nGET A JOB!");
        money -= amount;
        bet = amount;

        if (!playing && getBestValueOnHand() == 21) {
            playing = true;
            throw new IndexOutOfBoundsException(name + " GOT A BJ!!! (bj = black jack)");
        }

        playing = true;
        if (getBestValueOnHand() == 21) setStand();
    }
    public int getBet() {
        return bet;
    }

    public void setStand() {
        stand = true;
    }
    public boolean getStand() {
        return stand;
    }

    public void play() {
        super.play();
        if (getBestValueOnHand() == 21) setStand();
        if (getPossibleValuesOnHand().get(0) > 21) setStand();
    }

    public boolean canDouble() {
        return cardsOnHand.size() == 2 && bet <= money;
    }
    public void doubleUp() {
        if (canDouble()) {
            int beforeBet = bet;
            setBet(bet * 2);
            money += beforeBet;
            play();
            setStand();
        }
    }

    public boolean canSplit() {
        if (canDouble()) {
            String rank1 = cardsOnHand.get(0).getCardName().split("")[0];
            String rank2 = cardsOnHand.get(1).getCardName().split("")[0];
            if (rank1.equals(rank2)) return true;
        }
        return false;
    }
    public void splitIt() {
        if (canSplit()) {
            PlayingCard card = cardsOnHand.get(1);
            money -= bet;
            SplitGuest sg = new SplitGuest(name + "-split", cardDeck, bet, card, this);
            splitGuests.add(sg);
            cardsOnHand.remove(1);
            cardsOnHand.add(cardDeck.drawCard());
        }
    }
    public ArrayList<SplitGuest> getSplitGuests() {
        return splitGuests;
    }

    public void reset(double winningRelation) {
        money += (int) (winningRelation * bet);
        stand = false;
        splitGuests.clear();
        if (money < bet) bet = money;
        super.reset();
    }
}
