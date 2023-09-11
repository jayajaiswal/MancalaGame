package org.game.mancala.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Game {
    public String gameId;
    public PlayerEnum currentPlayer;
    public boolean isWinnerExist;
    public PlayerEnum winner;
    public Player player1;
    public Player player2;
}
