package org.game.mancala.service;

import org.game.mancala.model.Game;
import org.game.mancala.model.Movement;
import org.game.mancala.model.Player;
import org.game.mancala.model.PlayerEnum;
import org.game.mancala.service.impl.GameServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @InjectMocks
    GameServiceImpl gameService;

    @Test
    public void whenGameIdNotExist_shouldReturnNewGameId() {
        assertNotEquals(gameService.getGame("random").getGameId(), "random");
    }


    @Test
    public void whenGameIdExists_shouldReturnExistOne() throws Exception {
        Game game = gameService.getGame("random");
        assertEquals(gameService.getGame(game.getGameId()).getGameId(), game.getGameId());
    }


    @Test
    public void whenMovementExists_shouldFillTreasuryAndOpponentPits() throws Exception {
        Movement movement = Movement.builder().gameId("random").player(PlayerEnum.player1).pitNumber(4).build();
        Game game = gameService.playMove(movement);
        assertEquals(game.getCurrentPlayer(), PlayerEnum.player2);
        assertFalse(game.isWinnerExist);
        assertEquals(game.getPlayer1().getTreasury(), 1);
        assertEquals(game.getPlayer2().getPits()[1], 7);

    }

    @Test
    public void whenMovementExists_shouldGetAnotherChance() throws Exception {
        Movement movement = Movement.builder().gameId("random").player(PlayerEnum.player1).pitNumber(0).build();
        Game game = gameService.playMove(movement);
        assertEquals(game.getCurrentPlayer(), PlayerEnum.player1);
        assertFalse(game.isWinnerExist);
        assertEquals(game.getPlayer1().getTreasury(), 1);
    }

    @Test
    public void whenStoneReachesOpponentTreasury_shouldSkipIt() throws Exception {

        int[] pits = {6, 6, 6, 6, 6, 8};

        Game game = gameService.getGame("random");

        int initialTreasuryForPlayer2 = game.getPlayer2().getTreasury();

        Player player1 = game.getPlayer1();
        player1.setPits(pits);
        player1.setTreasury(0);
        String gameId = game.gameId;

        Movement movement = Movement.builder().gameId(gameId).player(PlayerEnum.player1).pitNumber(5).build();

        Game newGame = gameService.playMove(movement);
        assertEquals(newGame.getCurrentPlayer(), PlayerEnum.player2);
        assertEquals(newGame.getPlayer2().getTreasury(), initialTreasuryForPlayer2);
        assertEquals(newGame.getPlayer1().getPits()[0], 7);
    }

    @Test
    public void whenStoneReachesEmptyPit_shouldCollectStonesFromBoth() throws Exception {

        int[] pits1 = {6, 1, 0, 6, 6, 0}; // { 0, 6, 6, 0, 1, 6 }
        int[] pits2 = {2, 2, 3, 6, 9, 9}; // { 2, 2, 3, 6, 9, 9 }

        Game game = gameService.getGame("random");

        Player player1 = game.getPlayer1();
        player1.setPits(pits1);
        player1.setTreasury(10);
        String gameId = game.gameId;

        Player player2 = game.getPlayer2();
        player2.setPits(pits2);
        player2.setTreasury(12);

        Movement movement = Movement.builder().gameId(gameId).player(PlayerEnum.player1).pitNumber(1).build();
        Game newGame = gameService.playMove(movement);
        assertEquals(newGame.getCurrentPlayer(), PlayerEnum.player2);
        assertEquals(newGame.getPlayer1().getTreasury(), 17);
        assertEquals(newGame.getPlayer2().getPits()[3], 0);

    }

    @Test
    public void whenStonesAre9_shouldContinueToOpponentPits() throws Exception {

        int[] pits1 = {6, 2, 2, 4, 0, 11}; // { 11, 0, 4, 2, 2, 6 }
        int[] pits2 = {2, 2, 3, 6, 6, 6}; //  { 2, 2, 3, 6, 6, 6 }

        Game game = gameService.getGame("random");

        Player player1 = game.getPlayer1();
        player1.setPits(pits1);
        player1.setTreasury(13);
        String gameId = game.gameId;

        Player player2 = game.getPlayer2();
        player2.setPits(pits2);
        player2.setTreasury(9);

        Movement movement = Movement.builder().gameId(gameId).player(PlayerEnum.player1).pitNumber(5).build();

        Game newGame = gameService.playMove(movement);
        assertEquals(newGame.getCurrentPlayer(), PlayerEnum.player2);
        assertEquals(newGame.getPlayer1().getTreasury(), 14);
        assertEquals(newGame.getPlayer2().getPits()[0], 3);
        assertEquals(newGame.getPlayer1().getPits()[2], 3);
    }

}
