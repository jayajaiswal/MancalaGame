package org.game.mancala.service;

import org.game.mancala.model.Game;
import org.game.mancala.model.Movement;


public interface GameService {

    Game getGame(String gameId);

    Game playMove(Movement movement);
}
