package org.game.mancala.service.impl;

import org.game.mancala.model.Game;
import org.game.mancala.model.Movement;
import org.game.mancala.model.Player;
import org.game.mancala.model.PlayerEnum;
import org.game.mancala.service.GameService;
import org.game.mancala.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

@Service
@Slf4j
public class GameServiceImpl implements GameService {
    private HashMap<String, Game> games = new HashMap<>();

    @Override
    public Game getGame(String gameId) {
        gameId = games.containsKey(gameId) ? gameId : createNewGame();
        return games.get(gameId);
    }

    @Override
    public Game playMove(Movement movement) {
        Game game = this.getGame(movement.getGameId());
        if (!isPlayerValid(game.getCurrentPlayer(), movement.getPlayer())) return game;

        int selectedPitNumber = movement.getPitNumber();

        boolean isCurrentsTurn = calculateMovements(game, selectedPitNumber);
        if (!isCurrentsTurn) {
            game.setCurrentPlayer(switchPlayers(game.getCurrentPlayer()));
        }

        boolean isWinnerExists = isWinnerExists(game);
        if (isWinnerExists) {
            PlayerEnum winner = decideWinner(game);
            game.setWinnerExist(true);
            game.setWinner(winner);
        }
        return game;
    }

    private PlayerEnum switchPlayers(PlayerEnum currentPlayer) {
        return currentPlayer == PlayerEnum.player1 ? PlayerEnum.player2 : PlayerEnum.player1;
    }

    private boolean isPlayerValid(PlayerEnum gamePlayer, PlayerEnum movementPlayer) {
        return (gamePlayer.equals(movementPlayer)) ? true : false;
    }

    private String createNewGame() {
        String gameId = UUID.randomUUID().toString();
        games.put(gameId, this.initializeGame(gameId));
        log.info("New Game is created. Game ID:" + gameId);
        return gameId;
    }


    private boolean calculateMovements(Game game, int pitNumber) {
        Player currentPlayer = game.getCurrentPlayer() == PlayerEnum.player1 ? game.getPlayer1() : game.getPlayer2();
        Player counterPlayer = game.getCurrentPlayer() == PlayerEnum.player1 ? game.getPlayer2() : game.getPlayer1();

        int[] ownPits = currentPlayer.getPits();
        int stones = ownPits[pitNumber];
        boolean isMyTurn = false;


        ownPits[pitNumber] = Constants.BLANK;
        //If by mistake pressed on pit with 0 stones
        if (stones == Constants.BLANK) return true;
        int startingPit = pitNumber + 1;

        while (stones != Constants.BLANK) {
            int lastPitFilledIndex = 0;
            //filling own pits
            for (int i = startingPit; i < Constants.PIT; i++) {
                if (stones == Constants.BLANK) break;
                ownPits[i]++;
                stones--;
                lastPitFilledIndex = i;
            }

            //if last stone ends on empty pit
            if (ownPits[lastPitFilledIndex] == 1 && stones == 0 && counterPlayer.pits[Constants.PIT - lastPitFilledIndex - 1] > 0) {
                currentPlayer.treasury += counterPlayer.pits[Constants.PIT - lastPitFilledIndex - 1] + 1;
                ownPits[lastPitFilledIndex] = 0;
                counterPlayer.pits[Constants.PIT - lastPitFilledIndex - 1] = 0;
            }

            //if stone comes to treasury
            if (stones != Constants.BLANK) {
                isMyTurn = true;
                currentPlayer.treasury++;
                stones--;
            }

            //if stones continue to counter player's pits
            if (stones != Constants.BLANK) {
                isMyTurn = false;
                int[] counterPits = counterPlayer.getPits();
                for (int i = 0; i < Constants.PIT; i++) {
                    if (stones == Constants.BLANK) break;
                    counterPits[i]++;
                    stones--;
                }
                counterPlayer.setPits(counterPits);
            }
            currentPlayer.setPits(ownPits);
            startingPit = Constants.BLANK;
        }

        if (game.currentPlayer == PlayerEnum.player1) {
            game.setPlayer1(currentPlayer);
            game.setPlayer2(counterPlayer);
        }
        else {
            game.setPlayer2(currentPlayer);
            game.setPlayer1(counterPlayer);
        }
        return isMyTurn;
    }

    private Game initializeGame(String gameId) {
        int[] pits1 = new int[Constants.PIT];
        int[] pits2 = new int[Constants.PIT];

        Arrays.fill(pits1, Constants.STONE);
        Arrays.fill(pits2, Constants.STONE);

        return Game.builder().
                gameId(gameId).
                currentPlayer(PlayerEnum.player1).
                player1(Player.builder().pits(pits1).build()).
                player2(Player.builder().pits(pits2).build()).
                build();
    }

    private boolean isWinnerExists(Game game) {
        return checkStones(game.getPlayer1().getPits())
                || checkStones(game.getPlayer2().getPits());
    }

    private PlayerEnum decideWinner(Game game) {
        int player1Treasury = game.getPlayer1().treasury +
                Arrays.stream(game.getPlayer1().getPits()).sum();
        int player2Treasury = game.getPlayer2().treasury +
                Arrays.stream(game.getPlayer2().getPits()).sum();

        if (player1Treasury > player2Treasury) {
            log.info("Winner of " + game.getGameId() + " is " + Constants.PLAYER1_KEY);
            return PlayerEnum.player1;
        }

        log.info("Winner of " + game.getGameId() + " is " + Constants.PLAYER2_KEY);
        return PlayerEnum.player2;

    }

    private static boolean checkStones(int[] pits) {
        for (int pit : pits) if (pit != 0) return false;
        return true;
    }
}
