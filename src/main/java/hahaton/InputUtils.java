package hahaton;

import com.opencsv.CSVReader;

import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InputUtils {

    private static SimpleDateFormat sdf = new SimpleDateFormat("H:m");
    private static SimpleDateFormat sdfs = new SimpleDateFormat("H:m:s");

    private InputUtils() {
    }

    /**
     * @return distances matrix in meters
     */
    public static Distances getDistances() {
        ArrayList<ArrayList<Long>> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("distances.csv"))) {
            List<String[]> r = reader.readAll();
            for (String[] line : r) {
                ArrayList<Long> lineDistances = new ArrayList<>();
                for (String adjStr : line) {
                    lineDistances.add(Long.parseLong(adjStr));
                }
                result.add(lineDistances);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Distances(result);
    }

    public static ArrayList<TradingPointSchedule> getTradingPointSchedules(AgentPool agentPool, TradingPointPool pool) {
        ArrayList<TradingPointSchedule> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("schedule.csv"))) {
            List<String[]> r = reader.readAll();
            for (String[] line : r) {
                if ("Код ТТ".equals(line[0]))
                    continue;
                result.add(TradingPointSchedule.parseLine(line, agentPool, pool));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static SolutionDTO getSolution(TradingPointPool tradingPointPool) {
        try (CSVReader reader = new CSVReader(new FileReader("baseline.csv"))) {
            List<String[]> r = reader.readAll();
            return SolutionDTO.parse(r, tradingPointPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<TradingPoint> getTradingPoints(TradingPointPool tradingPointPool) {
        ArrayList<TradingPoint> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("spotsToVisit.csv"))) {
            List<String[]> r = reader.readAll();
            for (String[] line : r) {
                if ("Мерчендайзер (ФИО)".equals(line[0]))
                    continue;
                result.add(TradingPoint.parse(line, tradingPointPool));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long parseMinutes(String minutes) {
        try {
            return sdf.parse(minutes).getTime() / 1000L / 60L;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static long parseSeconds(String minutes) {
        try {
            return sdfs.parse(minutes).getTime() / 1000L;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}
