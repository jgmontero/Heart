import Classes.Card;
import Classes.Deck;
import Classes.Game;
import Classes.Player;


import java.io.BufferedReader;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) {


        //declaring 4 players
        Player Player1;
        Player Player2;
        Player Player3;
        Player Player4;

        //declaring player list
        List<Player> players = null;


        //declaring game object
        Game game = null;

        //initialize round
        String round;

        //initialize starter player
        Player StarterPlayer;

        //Cards to be passed array
        List<String[]> cardsToBePassed = new ArrayList<String[]>();

        System.out.println("Juego de Corazones");

        boolean gameEnded = false;
        String comand = "";
        while (!gameEnded) {
            if (game != null) {
                if (game.AllPlayersHasPassedCards() && !game.getRound().equals("4")) {
                    System.out.println("Las cartas de la ronda fueron pasadas");
                    if (game.getTurnPlayer().FindCard("T-2") != -1) {
                        System.out.println("Es el turno del jugador " + game.getTurnPlayer().getName() + ", el cual tiene el T-2");
                    }

                } else {

                }
                System.out.println("Es el turno del jugador " + game.getTurnPlayer().getName());
                if (!game.AllPlayersHasPassedCards()) {
                    if (game.getRound().equals("1") && !game.TurnPlayerHasPassedCards()) {
                        System.out.println("Debe selecionar 3 cartas para pasar hacia el jugador de la izquierda");
                    } else if (game.getRound().equals("2") && !game.TurnPlayerHasPassedCards()) {
                        System.out.println("Debe selecionar 3 cartas para pasar hacia el jugador de la derecha");
                    } else if (game.getRound().equals("3") && !game.TurnPlayerHasPassedCards()) {
                        System.out.println("Debe selecionar 3 cartas para pasar hacia el jugador enfrente");
                    }
                }


                //cards on table
                System.out.println("Las cartas en mesa son:");
                String table = "";
                if (game.getCardsOnTable().length > 0) {
                    for (int i = 0; i < game.getCardsOnTable().length; i++) {

                        table += (game.getCardsOnTable()[i].getCard() );
                        if (i + 1 < game.getTurnPlayer().getHand().length) {
                            table += ", ";
                        }
                    }
                } else {
                    table = " NO HAY CARTAS EN MESA TODAVIA";
                }

                System.out.println(table);

                // if the round is end
                if (game.isRoundEnd() != -1 ) {
                    System.out.println("Ronda terminada");
                    if ( game.isRoundEnd()==4){
                        System.out.println("El ganador de la ronda es el jugador" + game.getRoundWinner().getName());
                    }else{
                        System.out.println("El ganador de la ronda es el jugador" + game.getPlayers()[game.isRoundEnd()]+" " +
                                "al obtener un \"pleno de dama\"");
                    }


                    System.out.println("      ▓▓   Jugadores  ▓▓ Malas Cartas                  ▓▓");
                    System.out.println("      ▓▓--------------▓▓-------------------------------▓▓");

                    for (int i = 0; i < game.getPlayers().length; i++) {
                        if (!game.getPlayers()[i].getBadCard().equals("")) {
                            System.out.println(game.getPlayers()[i].getName() + "     " + game.getPlayers()[i].getBadCard());
                        }
                    }
                    System.out.println("      ▓▓--------------▓▓-------------------------------▓▓");
                    game.NextRound();
                    if (game.isGameEnded()!=null) {
                        game.showGameScore();
                        gameEnded = true;
                    }
                }


//if the round ends

            } else if (game == null) {
                System.out.println("Deseas empezar el juego?");
            }

            Scanner sc = new Scanner(System.in);
            System.out.println("Por favor inserte un comando");
            comand = sc.nextLine();


            if (comand.equals("")) {
                System.out.println("No se introdujo ningún comando");
            } else if (comand.equals("iniciar")) {
                if (game == null) {

                    //creating 4 players
                    Player1 = new Player("Player1");
                    Player2 = new Player("Player2");
                    Player3 = new Player("Player3");
                    Player4 = new Player("Player4");

                    //creating a list of players
                    players = new ArrayList<Player>();
                    players.add(Player1);
                    players.add(Player2);
                    players.add(Player3);
                    players.add(Player4);

                    //creating the game object
                    game = new Game(players.toArray(Player[]::new));


                } else {
                    System.out.println("Ya existe un juego en curso");
                }
            } else if (comand.equals("reiniciar")) {

                if (game != null) {

                    //creating the game object
                    game.resetGame();

                } else {
                    System.out.println("El juego no se ha iniciado, por tanto no puede ser reiniciado");
                }


            } else if (comand.equals("mostrar puntos")) {
                try {
                    if (game != null) {

                        System.out.println("Mostrando puntos acumulados de todos los jugadores, hasta el momento");
                        Thread.sleep(1000);
                        game.showGameScore();
                        Thread.sleep(1000);
                    } else {
                        System.out.println("No es posible mostrar la tabla de punto. Inicie un juego primero");
                    }

                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } else if (comand.equals("mostrar")) {
                System.out.println("Las cartas en mano son:");
                String hand = "";
                System.out.println("----------------------------");
                for (int i = 0; i < game.getTurnPlayer().getHand().length; i++) {
                    hand += game.getTurnPlayer().getHand()[i].getCard();
                    if (i + 1 < game.getTurnPlayer().getHand().length) {
                        hand += ", ";
                    }
                }
                System.out.println(hand);
                System.out.println("----------------------------");
            } else if ((comand.split(" "))[0].equals("select")) {
                if (!game.TurnPlayerHasPassedCards() && !game.getRound().equals("4") && game.AllPlayersHasPassedCards() == false) {
                    String[] selectCardsComand = comand.split(" ");

                    if (selectCardsComand.length == 4) {

                        String card1 = selectCardsComand[1];
                        String card2 = selectCardsComand[2];
                        String card3 = selectCardsComand[3];

                        //verifiying the player has the cards in order to pass it
                        if (game.getTurnPlayer().FindCard(card1) != -1 && game.getTurnPlayer().FindCard(card2) != -1 && game.getTurnPlayer().FindCard(card3) != -1) {
                            String[] cardsToPass = {card1, card2, card3};
                            cardsToBePassed.add(cardsToPass);
                            game.setPlayersCardsSelected(game.getTurnPlayer());
                            game.setTurnPlayer(game.getNextPlayer(game.getTurnPlayer()));

                        } else {
                            System.out.println("No posees las cartas introducidas o tienen un formato incorrecto");
                        }
                        if (game.AllPlayersHasPassedCards()) {

                            game.PassCard(cardsToBePassed);
                            game.setTurnPlayer(game.getStarterPlayer());
                        }
                    } else {
                        System.out.println("El comando \"select\" tiene un formato no válido");
                    }
                } else {
                    if (game.getRound().equals("4")) {
                        System.out.println("No es necesario pasar cartas en esta ronda");
                    } else if (game.AllPlayersHasPassedCards()) {
                        System.out.println("Ya fueron pasadas las cartas");
                    }
                }

                //validating select 3 cards command format


            } else if ((comand.split(" "))[0].equals("jugar")) {
                if (game.AllPlayersHasPassedCards() && game.AllPlayersPlayed() == false) {
                    //player with T-2

                    if (game.getTurnPlayer().FindCard("T-2") != -1) {
                        System.out.println("Al poseer la carta T-2, esta se jugará automaticamente");
                        Card[] turnPlayerHand = game.getTurnPlayer().getHand();
                        game.PlayCard(turnPlayerHand[game.getTurnPlayer().FindCard("T-2")]);
                    } else {

                        String[] playCardsComand = comand.split(" ");

                        if (playCardsComand.length == 2) {
                            //storing the string with the card
                            String card1 = playCardsComand[1];

                            //verifiying the player has the cards in order to pass it
                            if (game.getTurnPlayer().FindCard(card1) != -1) {
                                //storing the suit of the card
                                String suit = card1.split("-")[0];
                                if (suit.toLowerCase().equals("c") && !game.isDirty() && game.getTurnPlayer().hasValidCardToPlay(game.getCardsOnTable()[0].getType())) {
                                    System.out.println("No puedes jugar una carta de corazon, mientras no se haya jugado con anterioridad, ni teiendo una carta valida para jugar");
                                } else {

                                    //getting the cards index
                                    int cardInxed = game.getTurnPlayer().FindCard(card1);
                                    //getting the cards to play using that index
                                    Card cardToPlay = game.getTurnPlayer().getHand()[cardInxed];
                                    //if the player has a card with the same suit of the first card on table
                                    if (game.getCardsOnTable().length > 0) {
                                        if (game.getTurnPlayer().hasValidCardToPlay(game.getCardsOnTable()[0].getType())
                                                //and the played card has the same suit of first played card on table
                                                && cardToPlay.getType().toLowerCase().equals(game.getCardsOnTable()[0].getType().toLowerCase())) {
                                            //play the card
                                            game.PlayCard(cardToPlay);
                                            //the player does not has a card o the required suit and the
                                        } else if (!game.getTurnPlayer().hasValidCardToPlay(game.getCardsOnTable()[0].getType())) {
                                            game.PlayCard(cardToPlay);
                                            if (cardToPlay.getType().toLowerCase().equals("c")) {
                                                game.setDirty(true);
                                            }
                                        } else {
                                            System.out.println("La carta seleccionada no es un movimiento valido");
                                        }
                                    } else {
                                        game.PlayCard(cardToPlay);
                                    }


                                    if (game.AllPlayersPlayed()) {
                                        System.out.println("Todos los jugadores jugaron su carta");
                                        System.out.println("El jugador que gana la mesa es " + game.winnerPlayer().getName());
                                    }
                                }

                            } else {
                                System.out.println("No tienes la carta introducida");
                            }
                        } else {
                            System.out.println("El comando \"jugar\" tiene un formato no válido");
                        }
                    }


                } else {
                    System.out.println("No puedes jugar hasta q todos los jugadores pasen las cartas");
                }

            } else if ((comand.split(" "))[0].equals("ayuda")) {
                if (comand.split(" ").length == 1) {
                    System.out.println("1.\tiniciar");
                    System.out.println("2.\treiniciar");
                    System.out.println("3.\tmostrar:");
                    System.out.println("4.\tmostrar puntos:");
                    System.out.println("5.\tselect “carta1” “carta2” “carta3”");
                    System.out.println("6.\tjugar “carta” ");
                    System.out.println("7.\tayuda");
                    System.out.println("8.\tayuda “nombre del comando”");
                } else if (comand.split(" ").length > 1) {
                    //spliting the command to analize the parts
                    String[] splitedCommand = comand.split(" ");

                    if (splitedCommand[1].toLowerCase().equals("iniciar")) {
                        System.out.println("1.\tiniciar: Comienza una partida.");
                    } else if (splitedCommand[1].toLowerCase().equals("reiniciar")) {
                        System.out.println("2.\treiniciar: Termina la partida que se está desarrollando y comienza una nueva.");
                    } else if (splitedCommand[1].toLowerCase().equals("mostrar") && splitedCommand.length == 2) {
                        System.out.println("3.\tmostrar: Este comando muestra las cartas del jugador en turno separadas por una coma (,).");
                    } else if (splitedCommand[1].toLowerCase().equals("mostrar") && splitedCommand[2].toLowerCase().equals("puntos")) {
                        System.out.println("4.\tmostrar puntos: Muestra en consola los puntos que tiene cada jugador en todo el juego.");
                    } else if (splitedCommand[1].toLowerCase().equals("select")) {
                        System.out.println("5.\tselect “carta1” “carta2” “carta3”: Al inicio de cada ronda este es " +
                                "el comando que debe utilizar cada jugador para seleccionar las cartas a pasar," +
                                " según la ronda en la que se encuaentre el juego");
                    } else if (splitedCommand[1].toLowerCase().equals("jugar")) {
                        System.out.println("6.\tjugar “carta”: este comando es para que el jugador en turno juegue una carta.");
                    }
                }


            } else {
                System.out.println("Comando no válido, intente escribir \"ayuda\"  para una lista detallada de los comandos");
            }
        }
    }
}