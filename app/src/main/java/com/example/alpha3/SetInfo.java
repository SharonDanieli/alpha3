package com.example.alpha3;

import java.util.ArrayList;
import java.util.List;

public class SetInfo {
    public List<String> points;
    public List<Boolean> distribution;
    public List<TeamResults> resultsA;
    public List<TeamResults> resultsB;

    public SetInfo() { }

    public SetInfo(List<Boolean> distribution, List<String> points, List<TeamResults> resultsA, List<TeamResults>  resultsB) {
        this.distribution = distribution;
        this.resultsA = resultsA;
        this.resultsB = resultsB;

        this.points = points;
    }
}
