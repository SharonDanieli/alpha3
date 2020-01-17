package com.example.alpha3;

import java.util.ArrayList;
import java.util.List;

public class TeamResults {
    public List<Integer> playing;
    public List<String> swaps;
    public List<String> timeouts;
    public List<String> punishments;

    public TeamResults() { }

    public TeamResults(List<Integer> playing, ArrayList<String> swaps, ArrayList<String> timeouts, ArrayList<String> punishments) {
        this.playing = playing;
        this.swaps = swaps;
        this.timeouts = timeouts;
        this.punishments = punishments;
    }
}