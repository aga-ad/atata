package hahaton;

import hahaton.solutionserializer.SolutionsSaver;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Hello world!
 */
public class App {



    public static SolutionDTO getBaseLine() {
        TradingPointPool tradingPointPool = new TradingPointPool();
        AgentPool agentPool = new AgentPool();
        InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);

        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);

        var schedulesMap = new HashMap<Integer, TradingPointSchedule>();
        schedules.forEach(x -> schedulesMap.put(x.tradingPointCode, x));
        return InputUtils.getSolution(tradingPointPool);
    }

    public static Solution getBaseLineSolution() {
        TradingPointPool tradingPointPool = new TradingPointPool();
        AgentPool agentPool = new AgentPool();
        InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);

        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);
        var schedulesMap = new HashMap<Integer, TradingPointSchedule>();
        schedules.forEach(x -> schedulesMap.put(x.tradingPointCode, x));


        SolutionDTO sdto = InputUtils.getSolution(tradingPointPool);
        Distances distances = InputUtils.getDistances();

        InputUtils.getTradingPoints(tradingPointPool);
        //System.out.println(TradingPointPool.);

        return new Solution(sdto, distances, schedulesMap, tradingPointPool);
    }

    static long runTSP(Solution solution) {
        for (int agent = 0; agent < 14; agent++) {
            solution.findAgentPaths(agent);
        }
        return solution.getScore();
    }

    static long bruteAgent(Solution solution, long oldScore) {
        long best = oldScore;
        for (int i = 1; i <= solution.agentOfPoint.size(); i++) {
            for (int agent = 0; agent < 14; agent++) {
                int old_agent = solution.getAgent(i);
                solution.changeAgent(i, agent);
                long score = solution.getScore();
                if (best <= score) {
                    solution.changeAgent(i, old_agent);
                } else {
                    best = score;
                    System.out.println("score: " + best + "   meters " + solution.getMeters());
                }
            }
        }
        return best;
    }

    static long brutePairs(Solution solution, long oldScore) {
        long best = oldScore;
        for (int i = 1; i <= solution.agentOfPoint.size(); i++) {
            for (int j = i + 1; j <= solution.agentOfPoint.size(); j++) {
                int old_agent_i = solution.getAgent(i);
                int old_agent_j = solution.getAgent(j);
                solution.changeAgent(i, old_agent_j);
                solution.changeAgent(j, old_agent_i);
                long score = solution.getScore();
                if (best <= score) {
                    solution.changeAgent(i, old_agent_i);
                    solution.changeAgent(j, old_agent_j);
                } else {
                    best = score;
                    System.out.println("score: " + best + "   meters " + solution.getMeters());
                }
            }
        }
        return best;
    }

    public static void optimize(Solution solution) {
        long best = solution.getScore();
        System.out.println("score: " + best + "   meters " + solution.getMeters());
        best = runTSP(solution);
        System.out.println("score: " + best + "   meters " + solution.getMeters());
        while (true) {
            long old = best;
            while (true) {
                long newScore = bruteAgent(solution, best);
                if (newScore >= best) {
                    break;
                } else {
                    best = newScore;
                }
            }
            while (true) {
                long newScore = brutePairs(solution, best);
                if (newScore >= best) {
                    break;
                } else {
                    best = newScore;
                }
            }
            if (old == best) {
                break;
            }
        }


    }

    public static void main(String[] args) {

        Solution solution = getBaseLineSolution();
        optimize(solution);
        SolutionsSaver.save(solution);



    }
}
