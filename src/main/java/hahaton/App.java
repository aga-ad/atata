package hahaton;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        ArrayList<ArrayList<Double>> distances = InputUtils.getDistances();
        for (ArrayList<Double> row : distances) {
            for (Double ij : row) {
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
