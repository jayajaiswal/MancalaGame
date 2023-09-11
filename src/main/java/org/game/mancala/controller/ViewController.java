package org.game.mancala.controller;

import org.game.mancala.util.Constants;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {

    @GetMapping("/")
    public String main(Model model) {
        return Constants.HOME_TEMPLATE;
    }
}
