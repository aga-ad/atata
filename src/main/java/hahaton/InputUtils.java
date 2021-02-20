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
     * @return distances matrix in minutes
     */
    public static ArrayList<ArrayList<Double>> getDistances() {
        ArrayList<ArrayList<Double>> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("distances.csv"))) {
            List<String[]> r = reader.readAll();
            for (String[] line : r) {
                ArrayList<Double> lineDistances = new ArrayList<>();
                for (String adjStr : line) {
                    lineDistances.add(Double.parseDouble(adjStr) / 1_000_000.0);
                }
                result.add(lineDistances);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static ArrayList<TradingPointSchedule> getTradingPointSchedules(AgentPool pool) {
        ArrayList<TradingPointSchedule> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("schedule.csv"))) {
            List<String[]> r = reader.readAll();
            for (String[] line : r) {
                if ("Код ТТ".equals(line[0]))
                    continue;
                result.add(TradingPointSchedule.parseLine(line, pool));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Solution getSolution() {
        try (CSVReader reader = new CSVReader(new FileReader("baseline.csv"))) {
            List<String[]> r = reader.readAll();
            return Solution.parse(r);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<TradingPoint> getTradingPoints() {
        ArrayList<TradingPoint> result = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new FileReader("spotsToVisit.csv"))) {
            List<String[]> r = reader.readAll();
            for (String[] line : r) {
                if ("Мерчендайзер (ФИО)".equals(line[0]))
                    continue;
                result.add(TradingPoint.parse(line));
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
