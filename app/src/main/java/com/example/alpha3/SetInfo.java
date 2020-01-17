package com.example.alpha3;

import java.util.ArrayList;
import java.util.List;

public class SetInfo {
    public List<String> points;
    public List<Boolean> distribution;
    public TeamResults resultsA;
    public TeamResults resultsB;

    public SetInfo() { }

    public SetInfo(List<Boolean> distribution, List<String> points, TeamResults resultsA, TeamResults resultsB) {
        this.distribution = distribution;
        this.resultsA = resultsA;
        this.resultsB = resultsB;

        this.points = points;
    }
}
