package org.game.mancala.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Movement {
    @NotBlank
    public String gameId;

    @NotNull
    public PlayerEnum player;

    @Min(0)
    @Max(5)
    public int pitNumber;
}
