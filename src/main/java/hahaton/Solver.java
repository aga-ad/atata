package hahaton;

import hahaton.solutionserializer.SolutionsSaver;

import java.util.HashMap;
import java.util.List;

public class Solver {
    public static void main(String[] args) {
        Distances distances = InputUtils.getDistances();
        HashMap<Integer, TradingPointSchedule> schedules = new HashMap<>();
        TradingPointPool pointPool = new TradingPointPool();
        List<TradingPointSchedule> tradingPointSchedules = InputUtils.getTradingPointSchedules(new AgentPool(), pointPool);
        for (TradingPointSchedule schedule : tradingPointSchedules) {
            schedules.put(schedule.tradingPointCode, schedule);
        }
        SimulatedAnnealingOptimization optimization = new SimulatedAnnealingOptimization(0, distances, schedules);
        Solution solution = optimization.simulatedAnnealing();
        System.out.println(solution);
        SolutionsSaver.save(solution);
    }
}
