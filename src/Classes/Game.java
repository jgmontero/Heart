package Classes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Game {
    //list of players
    private Player[] players;
    //actual round type (1,2,3,4)
    private String round;
    //the player which should play
    private Player turnPlayer;
    //array to control which player has passed cards in the actual round
    private boolean[] CardsPassed;
    //accumulated  points in the actual game
    private int[] PointOnGame;
    //cards on the table
    private Card[] CardsOnTable;
    //controlling the order of player when they played
    private Player[] PlayerOrderplay;
    //is the table dirty
    private boolean dirty;

    public Game(Player[] players) {
        this.players = players;
        this.CardsOnTable = new Card[0];
        this.PlayerOrderplay = new Player[0];
        this.setTurnPlayer(players[0]);
        this.round = "1";
        this.CardsPassed = new boolean[4];
        Arrays.fill(this.CardsPassed, false);
        this.dirty = false;
        this.dealCards();
    }

    public void resetGame() {

        this.CardsOnTable = new Card[0];
        this.PlayerOrderplay = new Player[0];
        this.setTurnPlayer(players[0]);
        this.round = "1";
        this.CardsPassed = new boolean[4];
        Arrays.fill(this.CardsPassed, false);
        this.dirty = false;
        this.dealCards();
    }

    public void setCardsPassed(boolean[] cardsPassed) {
        CardsPassed = cardsPassed;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayer(Player[] players) {
        this.players = players;
    }

    public String getRound() {
        return round;
    }

    public void AddCardsOnTable(Card card) {
        List<Card> cardList = new ArrayList<>(Arrays.asList(this.CardsOnTable));
        cardList.add(card);
        this.CardsOnTable = cardList.toArray(Card[]::new);
       /* for (int i = 0; i < this.CardsOnTable.length; i++) {
            if (this.CardsOnTable[i] != null)
                System.out.println(this.CardsOnTable[i].getCard());
        }*/
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public void NextRound() {
        switch (this.round) {
            case "1": {
                this.round = "2";
            }
            ;
            break;
            case "2": {
                this.round = "3";
            }
            ;
            break;
            case "3": {
                this.round = "4";
            }
            ;
            break;
            case "4": {
                this.round = "1";
            }
            ;
            break;
        }
        this.round = round;
        this.resetCardsPassed();
        this.CardsOnTable = new Card[0];
        this.setTurnPlayer(players[0]);
        this.dirty = false;
        this.dealCards();
    }

    public Player getTurnPlayer() {
        return turnPlayer;
    }

    public void setTurnPlayer(Player turnPlayer) {
        this.turnPlayer = turnPlayer;
    }

    //to obtain the starter player, the one which has 2 of trebols
    public Player getStarterPlayer() {

        for (Player player : this.players) {

            if (player.isStarterPlayer()) {
                this.turnPlayer = player;
                return player;
            }
        }
        return null;
    }

    //to obtain the next player to play
    public Player getNextPlayer(Player player) {
        List<Player> playerList = new ArrayList<>(Arrays.asList(this.players));
        int nextPIndex = playerList.indexOf(player) + 1;
        if (nextPIndex == this.players.length) {
            return this.players[0];
        } else {
            return this.players[nextPIndex];
        }
    }

    //to obtain the previous player to play
    public Player getPrevioustPlayer(Player player) {
        List<Player> playerList = new ArrayList<>(Arrays.asList(this.players));
        int prevPIndex = playerList.indexOf(player) - 1;
        if (prevPIndex < 0) {
            return this.players[this.players.length - 1];
        } else {
            return this.players[prevPIndex];
        }
    }

    //to obtein the front player
    public Player getFrontPlayer(Player player) {
        //basically, the table game is a cycle, and there are 4 players,
        // hence the front player is the next of the next player
        return this.getNextPlayer(this.getNextPlayer(player));
    }

    //pass the cards from the turn player to the target player
    public void PassCard(List<String[]> cardsAddRmv) {

        //cycling the player list
        for (int i = 0; i < this.players.length; i++) {
            //scoping the player
            Player player = this.players[i];

            //creating the lst of cards to remove
            List<Card> cardsToRmv = new ArrayList<Card>();

            //string array with cards
            String[] cardsNames = cardsAddRmv.get(i);

            cardsToRmv.add(player.getHand()[player.FindCard(cardsNames[0])]);
            cardsToRmv.add(player.getHand()[player.FindCard(cardsNames[1])]);
            cardsToRmv.add(player.getHand()[player.FindCard(cardsNames[2])]);

            //creating the target player
            Player targetPlayer = null;

            //choosing target player
            switch (this.round) {
                case "1": {
                    //target player(left) is the next player
                    targetPlayer = this.getNextPlayer(this.getNextPlayer(player));
                }
                ;
                break;
                case "2": {
                    //target player(right) is the previous
                    targetPlayer = this.getPrevioustPlayer(this.getPrevioustPlayer(player));
                }
                ;
                break;
                case "3": {
                    //target player(font) is the next one of the turn player
                    targetPlayer = this.getFrontPlayer(player);
                }
                ;
                break;
            }

            player.removeAllCards(cardsToRmv);
            //adding to the trget player the cards given
            targetPlayer.addAllCards(cardsToRmv);
        }


    }

    //calculates the winner card and return the winner player
    public Player winnerPlayer() {
        Player winner = null;
        //index of winner card
        int index = 0;
        //geting the suit of first card played on table
        Card card = this.CardsOnTable[0];
        String firstCardSuit = card.getType();
        int biggerrank = card.getRank();

        //cycling cards on the table
        for (int i = 1; i < this.CardsOnTable.length; i++) {
            //if the cards has the same suit and his rank superior
            if (this.CardsOnTable[i].getType().equals(firstCardSuit) && this.CardsOnTable[i].getRank() > biggerrank) {
                //updating the winner card data
                biggerrank = this.CardsOnTable[i].getRank();
                //storing the index
                index = i;
            }
        }
        // using the winner card index, the winner player can be identified,
        // because both arrays has the same order, and a correspondence.
        // the index of winner card is the same of the winner player
        winner = this.PlayerOrderplay[index];
        //reseting the order of the players
        this.PlayerOrderplay = new Player[0];

        //setting as turn player, the winner
        this.turnPlayer = winner;

        //creating a badCards List
        List<String> bC = new ArrayList<>();

        //storing badCards of the player
        for (int i = 0; i < this.CardsOnTable.length; i++) {
            if (this.CardsOnTable[i].getScore() > 0) {
                bC.add(this.CardsOnTable[i].getCard());
            }
        }
        //set player's bad cards
        winner.setBadCard(bC.toArray(String[]::new));

        //updating the player score
        winner.updateScore(this.getPointOnTable());

        //reseting cardsotable
        this.CardsOnTable = new Card[0];

        return winner;
    }

    //calculates the point of the cards on table
    public int getPointOnTable() {
        int points = this.CardsOnTable[0].getScore() +
                this.CardsOnTable[1].getScore() +
                this.CardsOnTable[2].getScore() +
                this.CardsOnTable[3].getScore();
        return points;
    }

    public void setPlayersCardsSelected(Player player) {
        List<Player> playerList = new ArrayList<>(Arrays.asList(this.players));
        this.CardsPassed[playerList.indexOf(player)] = true;
    }

    public void setPointOnGame(int[] pointOnGame) {
        PointOnGame = pointOnGame;
    }

    //checks if all players already passed his 3 cards
    public boolean AllPlayersHasPassedCards() {
        for (int i = 0; i < this.CardsPassed.length; i++) {
            if (this.CardsPassed[i] == false)
                return false;
        }
        return true;
    }

    //checks if the turn player already passed his 3 cards
    public boolean TurnPlayerHasPassedCards() {
        List<Player> playerList = new ArrayList<>(Arrays.asList(this.players));
        return this.CardsPassed[playerList.indexOf(this.turnPlayer)];
    }

    public Player[] getPlayerOrderplay() {
        return PlayerOrderplay;
    }

    public void PlayCard(Card card) {
        //the card is removed from the hand of the player
        this.turnPlayer.removeCard(card);
        //adding the card to the table
        this.AddCardsOnTable(card);
        //converting to a list the playersOrderPLay array, to dynamically add a card to it
        //adding the turnplayer
        //updating the array, by converting to one, the previous list
        List<Player> orderP = new ArrayList<>(Arrays.asList(this.PlayerOrderplay));
        orderP.add(this.turnPlayer);
        this.PlayerOrderplay = orderP.toArray(Player[]::new);
        this.turnPlayer = this.getNextPlayer(this.turnPlayer);
    }

    //check if all players played they card
    public boolean AllPlayersPlayed() {
        return this.CardsOnTable.length == 4;
    }

    //cards on table
    public Card[] getCardsOnTable() {
        return CardsOnTable;
    }

    //reset the array to control the cards passed on a round
    public void resetCardsPassed() {
        Arrays.fill(this.CardsPassed, false);
    }

    //creates a new deck, shufle an deals the cards
    public void dealCards() {
        List<Card> p1deck;
        List<Card> p2deck;
        List<Card> p3deck;
        List<Card> p4deck;
        try {

            System.out.println("Creando mazo de cartas");
            Thread.sleep(1000);
            //creation and getting the deck
            List<Card> deck = (new Deck()).getDeck();


            System.out.println("Barajando mazo de cartas");
            Thread.sleep(1000);

            //shuffling deck
            Collections.shuffle(deck);

            System.out.println("Repartiendo cartas entre jugadores");
            Thread.sleep(2000);

            //creating 4 hands, 1 for every players
            p1deck = deck.subList(0, 13);
            p2deck = deck.subList(13, 26);
            p3deck = deck.subList(26, 39);
            p4deck = deck.subList(39, 52);

            //dealind the hand to the 4 players
            this.getPlayers()[0].setHand(p1deck.toArray(Card[]::new));
            this.getPlayers()[1].setHand(p2deck.toArray(Card[]::new));
            this.getPlayers()[2].setHand(p3deck.toArray(Card[]::new));
            this.getPlayers()[3].setHand(p4deck.toArray(Card[]::new));

            this.getPlayers()[0].setRoundScore(0);
            this.getPlayers()[1].setRoundScore(0);
            this.getPlayers()[2].setRoundScore(0);
            this.getPlayers()[3].setRoundScore(0);


            System.out.println("Los jugadores se sientan en la mesa");
            Thread.sleep(1000);


            System.out.println("Table player distribution");
            System.out.println("              player3");
            System.out.println("         ▓▓--------------▓▓");
            System.out.println("         |                | ");
            System.out.println(" player2 |                | player4");
            System.out.println("         |                |  ");
            System.out.println("         ▓▓--------------▓▓");
            System.out.println("              player1");
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    public int isRoundEnd() {
        if (this.AllPlayersPlayed()) {
            if (this.PlayersHasnoCardsToPlay()) {
                return 4;
            } else if (this.ShotTheMoon() != 1) {
                //if there is shot o the moon, ("pleno de dama")
                for (int i = 0; i < this.players.length; i++) {
                    if (this.players[i].getRoundScore() == 26) {
                        //the player who did it decreases his points by 26
                        this.players[i].setScore(this.players[i].getScore()-26);
                    }else{
                        //the other players increases his points by 26
                        this.players[i].setScore(this.players[i].getScore()+26);
                    }
                }

                return this.ShotTheMoon();
            }
        }
        return -1;
    }

    //check if a player has 26 poins in a round
    public int ShotTheMoon() {
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getRoundScore() == 26) {
                return i;
            }
        }
        return -1;
    }

    public boolean PlayersHasnoCardsToPlay() {

        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getHand().length > 0) {
                return false;
            }
        }
        return true;
    }

    public Player isGameEnded() {
        Player gameWinner = null;
        int lowerS=100;
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getScore() >= 100) {

                for (int j = 0; j < this.players.length; j++) {
                    if (this.players[j].getScore()<lowerS){
                        lowerS = this.players[j].getScore();
                        gameWinner=this.players[j];
                    }
                }
            }
        }
        return gameWinner;
    }

    public void showGameScore() {
        System.out.println("      ▓▓   Players  ▓▓   Round Score  ▓▓");
        System.out.println("      ▓▓------------▓▓-----------▓▓");
        System.out.println("      Name: " + this.players[0].getName() + "        " + this.players[0].getScore());
        System.out.println("      Name: " + this.players[1].getName() + "        " + this.players[1].getScore());
        System.out.println("      Name: " + this.players[2].getName() + "        " + this.players[2].getScore());
        System.out.println("      Name: " + this.players[3].getName() + "        " + this.players[3].getScore());
        System.out.println("      ▓▓------------▓▓-----------▓▓");
    }
    //round winner is the player with less score in the round
    public Player getRoundWinner(){
        Player roundWinner = null;
        int lowerRS = 26;
        for (int i = 0; i < this.players.length; i++) {
            if (this.players[i].getRoundScore()< lowerRS){
                lowerRS = this.players[i].getScore();
                roundWinner = this.players[i];
            }
        }
        return roundWinner;
    }
}
