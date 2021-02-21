package hahaton;

import java.util.Random;

/**
 * Solution -> Solution+
 */
public class SimulatedAnnealingOptimization {

    private Solution buffer = new Solution();
    protected Random rn;
    protected Distances distances;

    public SimulatedAnnealingOptimization(int seed, Distances distances) {
        rn = new Random(seed);
        this.distances = distances;
    }

    protected double getTransitionProbability(double dE, double temperature) {
        return Math.exp(-dE / temperature);
    }

    protected double decreaseTemperature(double temperature, int iteration) {
        return temperature * 0.1 / ((double) iteration);
    }

    protected Solution getInitialSolution() {
        return new Solution(App.getBaseLine());
    }

    protected double getInitialTemperature() {
        return 10000.0;
    }

    protected double getEnergy(Solution solution) {
        return 0.1;
    }

    protected int getMaxIterations() {
        return 10000;
    }

    protected int getMaxOperations() {
        return 20;
    }

    protected int getOperationsCount(double temperature) {
        return getMaxOperations() * (int)(((getInitialTemperature() - getEndTemperature()) /  (temperature - getEndTemperature()))) + 1;
    }

    protected Solution generateSolutionCandidate(Solution solution, double temperature) {
        Solution candidate = null;
        int n, day, agent1, agent2;
        Integer tp;

        for (int i = 0; i < getOperationsCount(temperature); i++) {
            candidate = new Solution(solution);
            n = candidate.visits[0].length;
            day = rn.nextInt(7);
            agent1 = rn.nextInt(n);
            tp = rn.nextInt(candidate.visits[day][agent1].size());
            agent2 = rn.nextInt(n);
            for (int dayI = 0; dayI < 7; dayI++) {
                if (candidate.visits[dayI][agent1].remove(tp)) {
                    candidate.visits[dayI][agent2].add(tp);
                    candidate.visits[dayI][agent2] = Solution.TSP(distances, candidate.visits[dayI][agent2]);
                    candidate.visits[dayI][agent1] = Solution.TSP(distances, candidate.visits[dayI][agent1]);
                }
            }
        }

        return candidate;
    }

    protected double getEndTemperature() {
        return 0.0000000001;
    }

    public Solution simulatedAnnealing() {

        double temperature = getInitialTemperature();
        Solution solution = getInitialSolution();
        double energy = getEnergy(solution);
        Solution solutionCandidate;
        double energyCandidate;
        double p;
        for (int iteration = 0; iteration < getMaxIterations(); iteration++) {
            solutionCandidate = generateSolutionCandidate(solution, temperature);
            energyCandidate = getEnergy(solutionCandidate);

            if (energyCandidate < energy) {
                solution = solutionCandidate;
                energy = energyCandidate;
            } else {
                p = getTransitionProbability(energyCandidate - energy, temperature);
                if (rn.nextDouble() < p) {
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
