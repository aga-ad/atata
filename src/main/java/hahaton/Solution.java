package hahaton;

import java.util.ArrayList;
import java.util.List;

public class Solution {

    /**
     * dayid -> agentid -> tradingpoints[]
     */
    List<Integer>[][] visits;

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
            visits[entry.day.ordinal()][entry.agentId].add(entry.tradingPointId);
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

    public boolean isValid(TradingPointPool tradingPointPool) {
        //9:30
        //only one agent for tradingpoint

        return true;
    }
}
