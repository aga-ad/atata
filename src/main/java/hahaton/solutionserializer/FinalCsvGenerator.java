package hahaton.solutionserializer;

import hahaton.*;

import java.util.ArrayList;
import java.util.HashMap;

public class FinalCsvGenerator {

    public static void main(String[] args) {
        var fileName = "annealing_solution.csv";

        Distances distances = InputUtils.getDistances();

        TradingPointPool tradingPointPool = new TradingPointPool();
        AgentPool agentPool = new AgentPool();
        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);
        var schedulesMap = new HashMap<Integer, TradingPointSchedule>();
        schedules.forEach(x -> schedulesMap.put(x.tradingPointCode, x));

        var solution = SolutionsSaver.load(fileName);
        OutputUtils.saveSolution(solution, agentPool, schedules, distances);
    }

}
