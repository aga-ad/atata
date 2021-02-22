package hahaton.solutionchecker;

import hahaton.*;
import hahaton.solutionserializer.SolutionsSaver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Checker {

    public static void main(String[] args) {
        var fileName = "annealing_solution.csv";

        Distances distances = InputUtils.getDistances();

        TradingPointPool tradingPointPool = new TradingPointPool();
        AgentPool agentPool = new AgentPool();
        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);
        var schedulesMap = new HashMap<Integer, TradingPointSchedule>();
        schedules.forEach(x -> schedulesMap.put(x.tradingPointCode, x));

        var failed = false;

        var solution = SolutionsSaver.load(fileName);

        if (PenaltyCalculator.calculatePenalty(solution, schedulesMap, distances, true) == Integer.MAX_VALUE) {
            failed = true;
            System.out.println("Work time check failed!");
        }
        if (!isAllTradingPointsVisited(solution, tradingPointPool)) {
            failed = true;
            System.out.println("Not all trading points visited!");
        }

        if (!isTradingPointsVisitedBuyOneAgent(solution, tradingPointPool)) {
            failed = true;
            System.out.println("Not all trading points visited by one agent!");
        }

        System.out.println(failed ? fileName + " is incorrect!" : fileName + " is correct!");

    }

    private static boolean isAllTradingPointsVisited(Solution solution, TradingPointPool tradingPointPool) {
        var failed = false;
        var tradingPoints = InputUtils.getTradingPoints(tradingPointPool);
        for (TradingPoint tradingPoint : tradingPoints) {
            var code = tradingPointPool.getTradingPointCode(tradingPoint.clientName, tradingPoint.clientAddress);
            for (int day = 0; day < 7; day++) {
                if (tradingPoint.schedule[day] == 1) {
                    var count = 0;
                    for (List<Integer> list : solution.visits[day]) {
                        for (Integer tradingPointCode : list) {
                            if (code == tradingPointCode)
                                count++;
                        }
                    }
                    if (count != 1) {
                        System.out.println(tradingPoint + " is not visited at day " + day);
                        failed = true;
                    }
                }

            }
        }
        return !failed;
    }

    private static boolean isTradingPointsVisitedBuyOneAgent(Solution solution, TradingPointPool tradingPointPool) {
        var failed = false;
        var tradingPoints = InputUtils.getTradingPoints(tradingPointPool);
        for (TradingPoint tradingPoint : tradingPoints) {
            var code = tradingPointPool.getTradingPointCode(tradingPoint.clientName, tradingPoint.clientAddress);
            var agentId = -1;
            for (var day = 0; day < solution.days(); day++) {
                for (var agent = 0; agent < solution.agents(); agent++) {
                    if (solution.visits[day][agent].contains(code)) {
                        if (agentId == -1 || agentId == agent)
                            agentId = agent;
                        else {
                            failed = true;
                            System.out.println("trading point " + tradingPoint + "is visited by different agents " + agent + " and " + agentId);
                        }
                    }
                }
            }
        }
        return !failed;
    }

}
