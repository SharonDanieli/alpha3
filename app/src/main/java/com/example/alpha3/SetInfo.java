package com.example.alpha3;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates set information
 */

public class SetInfo {
    public List<String> points;
    public List<Boolean> distribution;
    public List<TeamResults> resultsA;
    public List<TeamResults> resultsB;

    public SetInfo() { }

    /**
     * Creates set information object
     * @param distribution Distribution of points
     * @param points result of the game
     * @param resultsA 1st team game results that is of TeamResults type
     * @param resultsB 2nd team game results that is of TeamResults type
     * @see {@link TeamResults}
     */

    public SetInfo(List<Boolean> distribution, List<String> points, List<TeamResults> resultsA, List<TeamResults> resultsB) {
        this.distribution = distribution;
        this.resultsA = resultsA;
        this.resultsB = resultsB;
        this.points = points;
    }
}
