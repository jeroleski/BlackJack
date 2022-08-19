public class SplitGuest extends Guest{
    private final Guest superGuest;

    public SplitGuest(String name, CardDeck cardDeck, int bet, PlayingCard card, Guest superGuest) {
        super(name, cardDeck, bet);
        setBet(bet);

        cardsOnHand.add(card);
        card = cardDeck.drawCard();
        cardsOnHand.add(card);

        this.superGuest = superGuest;
    }

    public String getInfo() {
        String value = valuesToString();
        String bet = "" + this.bet;
        return "On hand: " + value + "\nBet: " + bet;
    }

    public boolean canDouble() {
        return cardsOnHand.size() == 2 && bet <= superGuest.money;
    }
    public void doubleUp() {
        if (canDouble()) {
            superGuest.money -= bet;
            bet = bet * 2;
            play();
            setStand();
        }
    }

    public void splitIt() {
        if (canSplit()) {
            PlayingCard card = cardsOnHand.get(1);
            superGuest.money -= bet;
            SplitGuest sg = new SplitGuest(name, cardDeck, bet, card, superGuest);
            superGuest.splitGuests.add(sg);
            cardsOnHand.remove(1);
            cardsOnHand.add(cardDeck.drawCard());
        }
    }

    public void reset(double winningRelation) {
        superGuest.money += (int) (winningRelation * bet);
    }
}
