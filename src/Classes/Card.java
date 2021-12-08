package Classes;

public class Card {
    private String card;// String for store the card suit-rank (palo-numero)
    private int rank;//integer to store the rank (numero)
    private String suit;//String to store the suit (palo)
    private int score;//to store the score of the card

    //constructor for most the cards only the card string params needed
    public Card(String card) {
        this.card = card;
        String[] splitedCard = card.split("-");
        this.suit = splitedCard[0];
        switch (splitedCard[1].toLowerCase()) {
            case "j":
                this.rank = 11;
                break;
            case "q":
                this.rank = 12;
                break;
            case "k":
                this.rank = 13;
                break;
            case "a":
                this.rank = 14;
                break;
            default:
                this.rank = Integer.parseInt(splitedCard[1]);
                break;
        }
        if (this.suit.toLowerCase().equals("c")) {
            this.score = 1;
        } else if (this.suit.toLowerCase().equals("p") && this.rank == 12) {
            this.score = 13;
        } else {
            this.score = 0;
        }
    }

    //constructor using all params for a card
    public Card(String card, int rank, String suit) {
        this.card = card;
        this.rank = rank;
        this.suit = suit;
    }

    //getters
    public String getCard() {
        return card;
    }

    public int getRank() {
        return rank;
    }

    public int getScore() {
        return score;
    }

    public String getType() {
        return suit;
    }
}
