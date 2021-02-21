package hahaton;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {

    /**
     * dayid -> agentid -> tradingpoints[]
     */
    List<Integer>[][] visits;
    ArrayList<Long> agentOfPoint;
    ArrayList<Long> totalTime;
    ArrayList<Long> working;

    public Solution(List<Integer>[][] visits) {
        this.visits = visits;

    }

    public Solution(SolutionDTO solutionDTO) {
        visits = new List[7][solutionDTO.getAgentsCount()];
        for (int day = 0; day < 7; day++) {
            for (int agentId = 0; agentId < solutionDTO.getAgentsCount(); agentId++) {
                visits[day][agentId] = new ArrayList<>();
            }
        }
        for (SolutionDTO.Entry entry : solutionDTO.getPaths()) {
            visits[entry.day.ordinal()][entry.agentId].add(entry.tradingPointCode);
        }
        totalTime = new ArrayList<>();
        for (int agentId = 0; agentId < solutionDTO.getAgentsCount(); agentId++) {

        }
    }

    public List<Integer> getTradingPoints(int dayId, int agentId) {
        return visits[dayId][agentId];
    }

    public int days() {
        return visits.length;
    }

    public int agents() {
        return visits[0].length;
    }

    public static ArrayList<Integer> TSP(Distances distances, List<Integer> points) {
        long[][] dp = new long[1 << points.size()][points.size()];
        int[][] prev = new int[1 << points.size()][points.size()];
        for (int i = 0; i < (1 << points.size()); i++) {
            for (int j = 0; j < points.size(); j++) {
                dp[i][j] = Long.MAX_VALUE;
            }
        }
        for (int j = 0; j < points.size(); j++) {
            dp[1 << j][j] = 0;
        }
        for (int mask = 0; mask < (1 << points.size()); mask++) {
            for (int last = 0; last < points.size(); last++) {
                if ((mask & (1 << last)) == 0) {
                    continue;
                }
                for (int next = 0; next < points.size(); next++) {
                    if ((mask & (1 << next)) > 0) {
                        continue;
                    }
                    long newDist = dp[mask][last] + distances.get(points.get(last), points.get(next));
                    if (dp[mask | (1 << next)][next] > newDist) {
                        dp[mask | (1 << next)][next] = newDist;
                        prev[mask | (1 << next)][next] = last;
                    }
                }
            }
        }
        int mask = (1 << points.size()) - 1;
        int last = 0;
        for (int i = 1; i < points.size(); i++) {
            if (dp[mask][last] > dp[mask][i]) {
                last = i;
            }
        }
        ArrayList<Integer> res = new ArrayList<Integer>(points);
        for (int i = points.size() - 1; i >= 0; i--) {
            res.set(i, last);
            int newMask = mask - (1 << last);
            last = prev[mask][last];
            mask = newMask;
        }
        return res;
    }


}
