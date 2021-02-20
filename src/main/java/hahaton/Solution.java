package hahaton;

import java.util.ArrayList;
import java.util.List;

/*
Дата посещения,День недели,Торговый представитель,Номер точки маршрута,Название точки,Адрес точки,Время прибытия,Продолжительность посещения,Частота посещения ( раз в неделю),Дистанция до следующей
18.05.2020,Monday,Мерчендайзер-13,1,Карусель,"г.Казань, Ямашева пр-кт, 93",09:00,3:57,2,"4,4"
 */
public class Solution {

    private ArrayList<Entry> path;
    private final AgentPool agentPool;
    private final TradingPointPool tradingPointPool;


    public Solution() {
        path = new ArrayList<>();
        tradingPointPool = new TradingPointPool();
        agentPool = new AgentPool();
    }

    public final class Entry {
        public final String date;
        public final VisitDay day;
        public final int agentId;
        public final int tradingPointId;
        public final String tradingPointName;
        public final String tradingPointAddress;
        public final int arrivalTimeInMinutes;
        public final int stayingTimeInMinutes;
        public final int visitCount;
        public final double nextPointDistance;

        public Entry(String date, VisitDay day, int agentId, int tradingPointId, String tradingPointName, String tradingPointAddress, int arrivalTimeInMinutes, int stayingTimeInMinutes, int visitCount, double nextPointDistance) {
            this.date = date;
            this.day = day;
            this.agentId = agentId;
            this.tradingPointId = tradingPointId;
            this.tradingPointName = tradingPointName;
            this.tradingPointAddress = tradingPointAddress;
            this.arrivalTimeInMinutes = arrivalTimeInMinutes;
            this.stayingTimeInMinutes = stayingTimeInMinutes;
            this.visitCount = visitCount;
            this.nextPointDistance = nextPointDistance;
        }
        
        
    }

    public Entry parse(String[] line) {
        String date = line[0];
        VisitDay day = VisitDay.parse(line[1]);
        int agentId = agentPool.getId(line[2]);
        int tradingPointId = tradingPointPool.getId(line[3]);
        String tradingPointName = line[4];
        String tradingPointAddress = line[5];
        int arrivalTimeInMinutes = (int) InputUtils.parseMinutes(line[6]);
        int stayingTimeInMinutes = (int) InputUtils.parseMinutes(line[7]);
        int visitCount = Integer.parseInt(line[8]);
        double nextPointDistance = Double.parseDouble(line[9].replaceAll(",", "."));
        return new Entry(date,
                day,
                agentId,
                tradingPointId,
                tradingPointName,
                tradingPointAddress,
                arrivalTimeInMinutes,
                stayingTimeInMinutes,
                visitCount,
                nextPointDistance);
    }

    public static Solution parse(List<String[]> lines) {
        Solution solution = new Solution();
        for (String[] line : lines) {
            if ("Дата посещения".equals(line[0]))
                continue;
            solution.path.add(solution.parse(line));
        }
        return solution;
    }

    @Override
    public String toString() {
        return "Solution{" +
                "path=" + path +
                ", agentPool=" + agentPool +
                ", tradingPointPool=" + tradingPointPool +
                '}';
    }
}
