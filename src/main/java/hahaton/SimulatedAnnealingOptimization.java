package hahaton;

import java.util.HashMap;
import java.util.Random;

import static hahaton.PenaltyCalculator.maxWorkingDayLengthInSeconds;
import static hahaton.PenaltyCalculator.minWorkingTimeInSeconds;

/**
 * Solution -> Solution+
 */
public class SimulatedAnnealingOptimization {

    private Solution buffer = new Solution();
    protected Random rn;
    protected Distances distances;
    protected HashMap<Integer, TradingPointSchedule> schedules;

    public SimulatedAnnealingOptimization(int seed, Distances distances, HashMap<Integer, TradingPointSchedule> schedules) {
        rn = new Random(seed);
        this.distances = distances;
        this.schedules = schedules;
    }

    protected double getTransitionProbability(double dE, double temperature) {
        return Math.exp(-(dE + 1) / temperature) / 5;
    }

    protected double decreaseTemperature(double temperature, int iteration) {
        return getInitialTemperature() * Math.exp(-iteration / 390.0);
    }

    protected Solution getInitialSolution() {
        return new Solution(App.getBaseLine());
    }

    protected double getInitialTemperature() {
        return 8;
    }

    protected long getMaxWorkingPenalty() {
        return 190000L;
    }

    protected long getMinWorkingPenalty() {
        return 190000L;
    }

    protected double getEnergy(Solution solution) {
        var penalty = 0L;
        for (int day = 0; day < solution.days(); day++) {
            for (int agent = 0; agent < solution.agents(); agent++) {
                var tradingPoints = solution.getTradingPoints(day, agent);
                if (tradingPoints.isEmpty())
                    continue;
                var workedTimeInSeconds = 0;
                var allTimeInSeconds = 0;
                for (int tradingPointNumber = 0; tradingPointNumber < tradingPoints.size(); tradingPointNumber++) {
                    var tradingPointCode = tradingPoints.get(tradingPointNumber);
                    var schedule = schedules.get(tradingPointCode);
                    var distToNext = tradingPointNumber == tradingPoints.size() - 1
                            ? 0
                            : distances.get(tradingPointCode, tradingPoints.get(tradingPointNumber + 1));
                    workedTimeInSeconds += schedule.stayingTimeInMinutes * 60;
                    allTimeInSeconds += schedule.stayingTimeInMinutes * 60;
                    allTimeInSeconds += (long) ((double) distToNext / 1000 / 60 * 60 * 60);
                    penalty += distToNext;
                }
                if (allTimeInSeconds > maxWorkingDayLengthInSeconds) {
                    penalty += getMaxWorkingPenalty();
                }
                if (workedTimeInSeconds < minWorkingTimeInSeconds) {
                    penalty += getMinWorkingPenalty();
                }
            }
        }
        return penalty;
    }

    protected int getMaxIterations() {
        return 100000000;
    }

    protected int getMaxOperations() {
        return 200;
    }

    protected int getOperationsCount(double temperature) {
        return (int)(Math.log(Math.pow(temperature, 40) + 3.0));
    }

    protected Solution generateSolutionCandidate(Solution solution, double temperature) {
        Solution candidate = null;
        int n, day=0, agent1, agent2;
        Integer tp;
        boolean goodday;
        for (int i = 0; i < getOperationsCount(temperature); i++) {
            candidate = new Solution(solution);
            n = candidate.visits[0].length;
            goodday = false;
            while (!goodday) {
                day = rn.nextInt(7);
                for (int j = 0; j < n; j++) {
                    goodday |= (candidate.visits[day][j].size() > 0);
                }
            }
            agent1 = rn.nextInt(n);
            while (candidate.visits[day][agent1].size() == 0)
                agent1 = rn.nextInt(n);

            tp = rn.nextInt(candidate.visits[day][agent1].size()) + 1;
            agent2 = rn.nextInt(n);
            if (agent2 == agent1) {
                for (int dayI = 0; dayI < 7; dayI++) {
                    candidate.visits[dayI][agent2] = Solution.TSP(distances, candidate.visits[dayI][agent2]);
                }
            } else {
                for (int dayI = 0; dayI < 7; dayI++) {
                    if (candidate.visits[dayI][agent1].remove(tp)) {
                        candidate.visits[dayI][agent2].add(tp);
                        candidate.visits[dayI][agent2] = Solution.TSP(distances, candidate.visits[dayI][agent2]);
                        candidate.visits[dayI][agent1] = Solution.TSP(distances, candidate.visits[dayI][agent1]);
                    }
                }
            }
        }

        return candidate;
    }

    protected double getEndTemperature() {
        return 1E-200;
    }

    public Solution simulatedAnnealing() {

        double temperature = getInitialTemperature();
        Solution solution = getInitialSolution();
        double energy = getEnergy(solution);
        Solution solutionCandidate;
        double energyCandidate;
        double p;
        System.out.println("initial energy" + energy);
        for (int iteration = 0; iteration < getMaxIterations(); iteration++) {
            solutionCandidate = generateSolutionCandidate(solution, temperature);
            energyCandidate = getEnergy(solutionCandidate);
            p = getTransitionProbability(energyCandidate - energy, temperature);
            if (energyCandidate < energy) {
                solution = solutionCandidate;
                energy = energyCandidate;
                System.out.println("cand iteration=" + iteration + " operCount=" + getOperationsCount(temperature) + " prob=" + p + " energy=" + energy + " temperature=" + temperature);
            } else {
                if (rn.nextDouble() < p) {
                    System.out.println(" prob iteration=" + iteration + " operCount=" + getOperationsCount(temperature) + " prob=" + p + " energy=" + energy + " temperature=" + temperature);
                    solution = solutionCandidate;
                    energy = energyCandidate;
                }
            }

            temperature = decreaseTemperature(temperature, iteration);

            if (temperature < getEndTemperature()) {
                break;
            }
        }

        return getInitialSolution();
    }

}
