public class BlackJack {
    private final boolean doubleAllowed;
    private final boolean splitAllowed;
    private final boolean shuffle;
    private int round;
    private final CardDeck cardDeck;
    private final Dealer dealer;
    private final Guest[] guests;

    public BlackJack(String rules, String themeColor, String[] playerNames, int noMoney, int noCardDecks) {
        doubleAllowed = rules.contains("Double");
        splitAllowed = rules.contains("Split");
        shuffle = rules.contains("Shuffle");

        round = 1;
        cardDeck = new CardDeck(noCardDecks, themeColor);

        dealer = new Dealer(cardDeck);

        guests = new Guest[playerNames.length];
        for (int p = 0; p < guests.length; p++) {
            guests[p] = new Guest(playerNames[p], cardDeck, noMoney);
        }

        for (Guest g : guests) {
            g.reset(0);
        }
        dealer.reset();
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Guest[] getGuests() {
        return guests;
    }

    public String getInfo() {
        String roundInfo = "Round: " + round + "\n";
        String cardDeckInfo = "Cards in deck: " + cardDeck.cardDeckSize();
        cardDeckInfo += shuffle ? " - constant shuffle" : "";
        return roundInfo + cardDeckInfo;
    }

    public void reset() {
        for (Guest g : guests) {
            for (SplitGuest sg : g.getSplitGuests()) {
                int dealerValue = dealer.getBestValueOnHand();
                if (dealerValue > 21) dealerValue = 0;
                int guestValue = sg.getBestValueOnHand();
                if (guestValue > 21) guestValue = 0;

                int winningRelation;
                if (guestValue > dealerValue) winningRelation = 2;
                else if (guestValue == dealerValue) winningRelation = 1;
                else winningRelation = 0;
                sg.reset(winningRelation);
            }

            g.reset(whoWins(g));
        }

        dealer.reset();
        if (shuffle || cardDeck.cardDeckSize() < 52) cardDeck.reset();
        round++;
    }

    public double whoWins(Guest g) {
        int dealerValue = dealer.getBestValueOnHand();
        if (dealerValue > 21) dealerValue = 0;
        int guestValue = g.getBestValueOnHand();
        if (guestValue > 21) guestValue = 0;

        double winningRelation;

        if (guestValue > dealerValue) winningRelation = 2;
        else if (guestValue == dealerValue) winningRelation = 1;
        else winningRelation = 0;

        return winningRelation;
    }

    public boolean getAllHasPass() {
        for (Guest g : guests) {
            if (! g.getStand()) {
                return false;
            }
            for (SplitGuest sg : g.getSplitGuests()) {
                if (! sg.getStand()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean getDoubleAllowed() {
        return doubleAllowed;
    }
    public boolean getSplitAllowed() {
        return splitAllowed;
    }
}
