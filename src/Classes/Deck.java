package Classes;

import Classes.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private List<Card> deck = new ArrayList<Card>();

    public Deck() {
        String suit[] = {"C", "D", "P", "T"};
        String rank[] = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "K", "Q", "A"};

//iterating suit array
        Card obj;
        for (int i = 0; i < suit.length; i++) {
            //iterating rank array
            for (int j = 0; j < rank.length; j++) {

                obj = new Card(suit[i] + "-" + rank[j]);

                //after the obj Object was initialized within the conditions, the card is inserted in the generic card list
                this.deck.add(obj);
            }
        }
        //shuffling the deck
        Collections.shuffle(deck);
    }

    public List<Card> getDeck() {
        return deck;
    }
}
