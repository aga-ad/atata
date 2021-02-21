package hahaton.solutionserializer;

import hahaton.*;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

public class OutputUtils {

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
