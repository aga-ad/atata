package hahaton;

import java.util.ArrayList;

public class Distances {
    ArrayList<ArrayList<Long>> distances;

    public Distances(ArrayList<ArrayList<Long>> distances) {
        this.distances = distances;
    }

    public long get(int from, int to) {
        return distances.get(from - 1).get(to - 1);
    }

    public ArrayList<Long> row(int from) {
        return distances.get(from - 1);
    }

    public long size() {
        return distances.size();
    }
}
