/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ca.sheridancollege.project;

import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author ashle
 */
public class WarCardGame extends Game {

    private GroupOfCards deck;
    
    private LivePlayer player1;
    private ArrayList<PlayersPile> deckPlayer1; 
    private PlayersPile player1Card;
    int player1Score;
    int player1WarScore;

    private LivePlayer player2;
    private ArrayList<PlayersPile> deckPlayer2; 
    private PlayersPile player2Card;
    int player2Score;
    int player2WarScore;

    public WarCardGame(String givenName) {
        super(givenName);
    }

    @Override
    public void play() {

//      Asking for player names and number of cards they want to play with
        Scanner input = new Scanner(System.in);
        System.out.println("Number of cards playing with (must be even):");
        String deckSizeString = input.nextLine();
        int deckSize = Integer.parseInt(deckSizeString); // this stores the number of cards in the deck being used
        if (deckSize > 52 || deckSize < 0) {
            System.out.println("Re-enter Number of cards playing with as it must be even and no more than 52:");
            String deckSizeStringRE = input.nextLine();
            deckSize = Integer.parseInt(deckSizeStringRE);
        }
        System.out.println("What is player 1 name: ");
        String player1Name = input.nextLine();
        player1 = new LivePlayer(player1Name);
        System.out.println("What is player 2 name: ");
        String player2Name = input.nextLine();
        player2 = new LivePlayer(player2Name);

        String response = "";

//      Shuffleing the deck of cards
        deck = new GroupOfCards(deckSize);
        deck.shuffle();

        // Distrubing the cards between the players
        deckPlayer1 = new ArrayList();
        deckPlayer2 = new ArrayList();

        for (int i = 1; i <= deckSize; i++) {
            if (i % 2 == 0) {
                deckPlayer1.add((PlayersPile) deck.removeFirstCard());
            } else {
                deckPlayer2.add((PlayersPile) deck.removeFirstCard());
            }
        }
//        Battling
        do {
            player1Card = deckPlayer1.remove(0);
            player2Card = deckPlayer2.remove(0);

            displayRound(player1, player1Card, player2, player2Card);

            if (deck.indexOf(player1Card.getRank()) > deck.indexOf(player2Card.getRank())) {
                player1Score++;
                deckPlayer1.add(player2Card);
                deckPlayer1.add(player1Card);
                displayRoundWinner(player1, player2, player1Score, player2Score);
                response = nextTurn();
            } else if (deck.indexOf(player2Card.getRank()) > deck.indexOf(player1Card.getRank())) {
                player2Score++;
                deckPlayer2.add(player1Card);
                deckPlayer2.add(player2Card);
                displayRoundWinner(player2, player1, player2Score, player1Score);
                response = nextTurn();
            } else {
                if (deckPlayer1.size() > 2 && deckPlayer2.size() > 2) {
                    war();
                } else {
                    response = "N";
                }
            }
        } while (response.equalsIgnoreCase("Y") && (deckPlayer1.size() > 0 && deckPlayer2.size() > 0));
        declareWinner();
    }

    public String nextTurn() {
        Scanner input = new Scanner(System.in);
        System.out.println("\nContinue Y/N?");
        String response = input.nextLine();
        return response;
    }

    public void displayRound(LivePlayer player1, PlayersPile player1Card,
            LivePlayer player2, PlayersPile player2Card) {
        System.out.println("\nROUND START");
        System.out.println("Player 1: " + player1.getPlayerID() + "\n" + "Draws: "
                + player1Card.getRank() + " of " + player1Card.getSuit());
        System.out.println("VS");
        System.out.println("Player 2: " + player2.getPlayerID() + "\n" + "Draws: "
                + player2Card.getRank() + " of " + player2Card.getSuit());
    }

    public void displayRoundWinner(LivePlayer winner, LivePlayer loser, int winnerScore, int loserScore) {
        System.out.println("\nWINNER: " + winner.getPlayerID() + "\nWinner scores 1 point");
        System.out.println("\nTotal Score");
        System.out.println(winner.getPlayerID() + ": " + winnerScore + " Points");
        System.out.println(loser.getPlayerID() + ": " + loserScore + " Points");
    }

    @Override
    public void declareWinner() {
        if (player1Score > player2Score) {
            System.out.println("\n" + player1.getPlayerID() + " WON THE GAME!!!");
            System.out.println(player2.getPlayerID() + " has no more cards");
            System.out.println("\nTotal Score");
            System.out.println(player1.getPlayerID() + ": " + player1Score + " Points");
            System.out.println(player2.getPlayerID() + ": " + player2Score + " Points");
        } else if (player2Score > player1Score) {
            System.out.println("\n" + player2.getPlayerID() + " WON THE GAME!!!");
            System.out.println(player1.getPlayerID() + " has no more cards");
            System.out.println("\nTotal Score");
            System.out.println(player1.getPlayerID() + ": " + player1Score + " Points");
            System.out.println(player2.getPlayerID() + ": " + player2Score + " Points");
        } else if (player1Score == player2Score) {
            System.out.println("\nIT IS A TIE GAME!!!");
            System.out.println("\nTotal Score");
            System.out.println(player1.getPlayerID() + ": " + player1Score + " Points");
            System.out.println(player2.getPlayerID() + ": " + player2Score + " Points");
        }
    }
//    }

    public void displayWarRoundWinner(LivePlayer winner, LivePlayer loser, int winnerScore, int loserScore) {
        System.out.println("\nWINNER: " + winner.getPlayerID() + "\nWinner scores 1 WAR point");
        System.out.println("\nWAR Total Score");
        System.out.println(winner.getPlayerID() + ": " + winnerScore + " Points");
        System.out.println(loser.getPlayerID() + ": " + loserScore + " Points");
    }

    public void war() {
        System.out.println("\nA WAR HAS STARTED!!! \nBoth Players drew the same value cards.");
        System.out.println("Each player will now face off in best of 3 battles!");
        System.out.println("\nWAR TURN 1!!");
        player1WarScore = 0;
        player2WarScore = 0;
        PlayersPile player1WarCard1 = deckPlayer1.remove(0);
        PlayersPile player2WarCard1 = deckPlayer2.remove(0);

        displayRound(player1, player1WarCard1, player2, player2WarCard1);

        if (deck.indexOf(player1WarCard1.getRank()) > deck.indexOf(player2WarCard1.getRank())) {
            player1WarScore++;
            displayWarRoundWinner(player1, player2, player1WarScore, player2WarScore);
        } else if (deck.indexOf(player1WarCard1.getRank()) < deck.indexOf(player2WarCard1.getRank())) {
            player2WarScore++;
            displayWarRoundWinner(player2, player1, player2WarScore, player1WarScore);
        } else {
            System.out.println("War TURN 1 is TIE");
        }

        System.out.println("\nWAR TURN 2");
        PlayersPile player1WarCard2 = deckPlayer1.remove(0);
        PlayersPile player2WarCard2 = deckPlayer2.remove(0);

        displayRound(player1, player1WarCard2, player2, player2WarCard2);

        if (deck.indexOf(player1WarCard1.getRank()) > deck.indexOf(player2WarCard1.getRank())) {
            player1WarScore++;
            displayWarRoundWinner(player1, player2, player1WarScore, player2WarScore);
        } else if (deck.indexOf(player1WarCard1.getRank()) < deck.indexOf(player2WarCard1.getRank())) {
            player2WarScore++;
            displayWarRoundWinner(player2, player1, player2WarScore, player1WarScore);
        } else {
            System.out.println("War TURN 2 is TIE");
        }

        System.out.println("\nWAR TURN 3");
        PlayersPile player1WarCard3 = deckPlayer1.remove(0);
        PlayersPile player2WarCard3 = deckPlayer2.remove(0);

        displayRound(player1, player1WarCard3, player2, player2WarCard3);

        if (deck.indexOf(player1WarCard1.getRank()) > deck.indexOf(player2WarCard1.getRank())) {
            player1WarScore++;
            displayWarRoundWinner(player1, player2, player1WarScore, player2WarScore);
        } else if (deck.indexOf(player1WarCard1.getRank()) < deck.indexOf(player2WarCard1.getRank())) {
            player2WarScore++;
            displayWarRoundWinner(player2, player1, player2WarScore, player1WarScore);
        } else {
            System.out.println("War TURN 3 is TIE");
        }

        if (player1WarScore > player2WarScore) {
            player1Score++;
            deckPlayer1.add(player1WarCard1);
            deckPlayer1.add(player2WarCard1);
            deckPlayer1.add(player1WarCard2);
            deckPlayer1.add(player2WarCard2);
            deckPlayer1.add(player1WarCard3);
            deckPlayer1.add(player2WarCard3);
            deckPlayer1.add(player1Card);
            deckPlayer1.add(player2Card);
            displayRoundWinner(player1, player2, player1Score, player2Score);
        } else if (player2WarScore > player1WarScore) {
            player2Score++;
            deckPlayer2.add(player1WarCard1);
            deckPlayer2.add(player2WarCard1);
            deckPlayer2.add(player1WarCard2);
            deckPlayer2.add(player2WarCard2);
            deckPlayer2.add(player1WarCard3);
            deckPlayer2.add(player2WarCard3);
            deckPlayer2.add(player1Card);
            deckPlayer2.add(player2Card);
            displayRoundWinner(player2, player1, player2Score, player1Score);
        } else {
            war();
        }
    }
}
