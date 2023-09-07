package org.game.mancala.controller;

import org.game.mancala.service.GameService;
import org.game.mancala.model.Movement;
import org.game.mancala.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/start")
    @ResponseBody
    public Game getStart(@RequestParam String gameId) {
        return gameService.getGame(gameId);
    }

    @PostMapping("/move")
    public Game getMovement(@Valid @RequestBody Movement movement) {
        return gameService.playMove(movement);
    }


}
