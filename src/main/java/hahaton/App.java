package hahaton;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ArrayList<ArrayList<Long>> distances = InputUtils.getDistances();
        for (ArrayList<Long> row : distances) {
            for (Long ij : row) {
                System.out.println(ij);
            }
        }

        TradingPointPool tradingPointPool = new TradingPointPool();
        AgentPool pool = new AgentPool();
        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(pool, tradingPointPool);
        for (TradingPointSchedule schedule : schedules) {
            System.out.println(schedule);
        }
        
        System.out.println(InputUtils.getTradingPoints(tradingPointPool));

        System.out.println(InputUtils.getSolution(tradingPointPool));

    }
}
