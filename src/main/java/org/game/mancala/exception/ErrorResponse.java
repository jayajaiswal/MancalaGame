package org.game.mancala.exception;

import java.util.ArrayList;
import java.util.List;

/**
 * Error response with messages and (when available) more info
 */
public class ErrorResponse {
    private List<Violation> violations = new ArrayList<>();

    public List<Violation> getViolations() {
        return violations;
    }
}
