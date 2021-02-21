package hahaton;

import java.util.HashMap;

public class PenaltyCalculator {

    /**
     * @param schedules
     * @param distances
     * @return Integer.MAX_VALUE if solution is not correct, sum of traveled distance otherwise
     */
    public static long calculatePenalty(
            Solution solution,
            HashMap<Integer, TradingPointSchedule> schedules,
            Distances distances,
            boolean verbose
    ) {
        var failed = false;
        var penalty = 0L;
        for (int day = 0; day < solution.days(); day++) {
            for (int agent = 0; agent < solution.agents(); agent++) {
                var tradingPoints = solution.getTradingPoints(day, agent);
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
                if (allTimeInSeconds > maxWorkingDayLengthInSeconds || workedTimeInSeconds < minWorkingTimeInSeconds) {
                    failed = true;
                    if (verbose) {
                        System.out.println("Solution is not correct");
                        System.out.println("agent = " + agent);
                        System.out.println("day = " + day);
                        System.out.println("tradingPoints = " + tradingPoints.toString());
                        System.out.println("allTimeInSeconds = " + allTimeInSeconds + " should be less than " + maxWorkingDayLengthInSeconds);
                        System.out.println("workedTimeInSeconds = " + workedTimeInSeconds + " should be more than " + minWorkingTimeInSeconds);
                    }
                }
                if (failed)
                    break;
            }
            if (failed)
                break;
        }
        return failed ? Integer.MAX_VALUE : penalty;
    }

    public static final long maxWorkingDayLengthInSeconds = (9 * 60 + 30) * 60;

    public static final long minWorkingTimeInSeconds = 5 * 60 * 60;

}
