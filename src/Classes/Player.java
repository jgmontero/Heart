package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Player {
    private String name;
    private Card[] hand;
    private int score;
    private int roundScore;
    private String[] badCard;

    public Player(String name) {
        this.name = name;
        this.score = 0;
        this.badCard = new String[0];
        this.roundScore = 0;
    }

    public Player(String name, Card[] hand) {
        this.name = name;
        this.hand = hand;
        this.badCard = new String[0];
        this.score = 0;
        this.roundScore = 0;
    }

    public String getName() {
        return this.name;
    }

    public Card[] getHand() {
        return this.hand;
    }

    public void setHand(Card[] hand) {
        this.hand = hand;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return this.score;
    }

    public boolean isStarterPlayer() {
//cicling the in order to find the 2 of trebols
        for (int i = 0; i < this.hand.length; i++) {
            //if has it
            if (this.hand[i].getCard().toLowerCase().equals(("t-2"))) {
                return true;
            }
        }
        return false;
    }

    //to find the index of card by his name
    public int FindCard(String card) {
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].getCard().toLowerCase().equals(card.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }

    public void removeAllCards(List<Card> cardsToRmv) {
        //creating list of cards to be removed
        List<Card> newhand = new ArrayList<Card>(Arrays.asList(this.hand));
        newhand.removeAll(cardsToRmv);
        this.hand = newhand.toArray(Card[]::new);
    }

    public void addAllCards(List<Card> cards) {
      /* cards.addAll(this.hand);
       this.hand = cards;
*/
        List<Card> newhand = new ArrayList<Card>(Arrays.asList(this.hand));
        newhand.addAll(cards);
        this.hand = newhand.toArray(Card[]::new);
    }

    public void removeCard(Card card) {
        //creating list of cards to be removed
        List<Card> newhand = new ArrayList<Card>(Arrays.asList(this.hand));
        newhand.remove(card);
        this.hand = newhand.toArray(Card[]::new);
    }

    public void updateScore(int tableScore) {
        this.score += tableScore;
    }

    public boolean hasValidCardToPlay(String suit) {
        for (int i = 0; i < this.hand.length; i++) {
            if (this.hand[i].getType().toLowerCase().equals(suit.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    public String getBadCard() {
        if(this.badCard.length==0){
            return "";
        }else{
            String bcstring ="";
            for (int i = 0; i < this.badCard.length; i++) {
                if(i>0){
                    bcstring+=", ";
                }
                bcstring+=this.badCard[i];
            }
            return bcstring;
        }

    }

    public void setBadCard(String[] badCard) {
        this.badCard = badCard;
    }

    public void resetBadCards() {
        this.badCard = new String[0];
    }

    public int getRoundScore() {
        return roundScore;
    }

    public void setRoundScore(int roundScore) {
        this.roundScore = roundScore;
    }
}

