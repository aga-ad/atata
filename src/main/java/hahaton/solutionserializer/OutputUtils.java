package hahaton.solutionserializer;

import com.opencsv.CSVWriter;
import hahaton.*;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class OutputUtils {

    public static void main(String[] args) {
        Distances distances = InputUtils.getDistances();
        for (int i = 1; i <= distances.size(); i++) {
            System.out.println(distances.row(i)); //ok
        }
        System.out.println(distances.get(72, 68));


        TradingPointPool tradingPointPool = new TradingPointPool();
        AgentPool agentPool = new AgentPool();
        ArrayList<TradingPointSchedule> schedules = InputUtils.getTradingPointSchedules(agentPool, tradingPointPool);
        for (TradingPointSchedule schedule : schedules) {
            System.out.println(schedule);
        }

        System.out.println(InputUtils.getTradingPoints(tradingPointPool));


        var solution = InputUtils.getSolution(tradingPointPool);

        var minsInTravel = 0L;
        for (SolutionDTO.Entry x : solution.getPaths()) {
            for (SolutionDTO.Entry y : solution.getPaths()) {
                if (x.agentId == y.agentId && x.day == y.day && x.visitNumberInsideDay + 1 == y.visitNumberInsideDay) {
                    minsInTravel += y.arrivalTimeInMinutes - (x.arrivalTimeInMinutes + x.stayingTimeInMinutes);

                    System.out.println("--------------------------------");
                    System.out.println("x" + x.agentId + "|" + x.day + "|" + x.visitNumberInsideDay);
                    System.out.println("x" + x.arrivalTimeInMinutes + "|" + x.stayingTimeInMinutes);
                    System.out.println("y" + y.agentId + "|" + y.day + "|" + y.visitNumberInsideDay);
                    System.out.println("y" + y.arrivalTimeInMinutes + "|" + y.stayingTimeInMinutes);
                    System.out.println(y.arrivalTimeInMinutes - (x.arrivalTimeInMinutes + x.stayingTimeInMinutes));

                    System.out.println("--------------------------------");
                    break;
                }
            }
        }
        System.out.println("!!! " + minsInTravel);

    }

    private static String genFileName() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return "solution-" + now.toString().replace(":", "-") + ".csv";
    }

    private static final String[] initLine = {
            "Дата посещения",
            "День недели",
            "Торговый представитель",
            "Номер точки маршрута",
            "Название точки",
            "Адрес точки",
            "Время прибытия",
            "Продолжительность посещения",
            "Частота посещения ( раз в неделю)",
            "Дистанция до следующей"
    };

    public static void saveSolution(
            Solution solution,
            AgentPool agentPool,
            ArrayList<TradingPointSchedule> schedules,
            Distances distances
    ) {
        String fileName = genFileName();
        try (FileWriter fw = new FileWriter(fileName)) {
            System.out.println("Saving solution to " + fileName);
            CSVWriter csvFw = new CSVWriter(fw);
            csvFw.writeNext(initLine);
            var schedulesMap = new HashMap<Integer, TradingPointSchedule>();
            schedules.forEach(x -> schedulesMap.put(x.tradingPointCode, x));
            for (int day = 0; day < solution.days(); day++) {
                for (int agent = 0; agent < solution.agents(); agent++) {
                    var tradingPoints = solution.getTradingPoints(day, agent);
                    var startTime = LocalTime.of(9, 0, 0);
                    for (int tradingPointNumber = 0; tradingPointNumber < tradingPoints.size(); tradingPointNumber++) {
                        var tradingPointCode = tradingPoints.get(tradingPointNumber);
                        var schedule = schedulesMap.get(tradingPointCode);
                        var distToNext = tradingPointNumber == tradingPoints.size() - 1
                                ? 0
                                : distances.get(tradingPointCode, tradingPoints.get(tradingPointNumber + 1));
                        String[] line = {
                                getWeekOfDay(day).date,
                                getWeekOfDay(day).name,
                                agentPool.getName(agent),
                                "" + (tradingPointNumber + 1),
                                schedule.clientName,
                                schedule.clientAddress,
                                startTime.toString(),
                                LocalTime.of(0, 0).plusMinutes(schedule.stayingTimeInMinutes).toString(),
                                "" + schedule.visitsCount,
                                "" + ((double) distToNext / 1000)
                        };
                        csvFw.writeNext(line);
                        var travelTimeInSeconds = (long) ((double) distToNext / 1000 / 60 * 60 * 60);
                        startTime = startTime.plusMinutes(schedule.stayingTimeInMinutes).plusSeconds(travelTimeInSeconds);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static VisitDay getWeekOfDay(int dayNumber) {
        return VisitDay.values()[dayNumber];
    }

}
