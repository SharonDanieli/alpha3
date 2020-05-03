package com.example.alpha3;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates set information of the teams
 */

public class TeamResults {
    public List<Integer> playing;
    public List<String> swaps;
    public List<String> timeouts;
    public List<String> punishments;

    public TeamResults() { }

    /**
     * Creates set information of the teams object
     * @param playing Six players starting the game
     * @param swaps Player exchanges made during the game
     * @param timeouts Timeouts taken
     * @param punishments Penalties given
     */

    public TeamResults(List<Integer> playing, ArrayList<String> swaps, ArrayList<String> timeouts, ArrayList<String> punishments) {
        this.playing = playing;
        this.swaps = swaps;
        this.timeouts = timeouts;
        this.punishments = punishments;
    }
}