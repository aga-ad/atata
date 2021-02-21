package hahaton;

import java.util.ArrayList;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Distances distances = InputUtils.getDistances();
        for (int i = 1; i <= distances.size(); i++) {
            System.out.println(distances.row(i)); //ok
        }
        System.out.println(distances.get(72, 68));

//        TradingPointPool tradingPointPool = new TradingPointPool();
//        AgentPool agentPool = new AgentPool();
//        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);
//        for (TradingPointSchedule schedule : schedules) {
//            System.out.println(schedule);
//        }
//
//        System.out.println(InputUtils.getTradingPoints(tradingPointPool));
//
//        //System.out.println(InputUtils.getSolution(tradingPointPool));
//        Solution baseline = new Solution(InputUtils.getSolution(tradingPointPool));
//        for (int day = 0; day < baseline.days(); day++) {
//            for (int agent = 0; agent < baseline.agents(); agent++) {
//                if (baseline.getTradingPoints(day, agent).size() == 0) {
//                    continue;
//                }
//                System.out.println(day + " " + agent + "(" + agentPool.getName(agent) + "):");
//                for (int point: baseline.getTradingPoints(day, agent)) {
//                    System.out.println(point + " " + tradingPointPool.getName(point));
//                }
//            }
//        }

    }
}
