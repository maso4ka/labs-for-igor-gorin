package ru.nsu.ineverovich.task112.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class RoundTest {

    @Test
    void storesRoundNumber() {
        Round round = new Round(3);

        assertEquals(3, round.getRoundNumber());
    }

    @Test
    void newRoundIsNotFinished() {
        Round round = new Round(1);

        assertFalse(round.isFinished());
    }

    @Test
    void finishesRound() {
        Round round = new Round(1);

        assertFalse(round.isFinished());

        round.finish();

        assertTrue(round.isFinished());
    }
}