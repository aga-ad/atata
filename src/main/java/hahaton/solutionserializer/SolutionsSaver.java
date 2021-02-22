package hahaton.solutionserializer;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import hahaton.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class SolutionsSaver {

    private static String genFileName() {
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        return "solution-raw-" + now.toString().replace(":", "-") + ".csv";
    }

    public static String save(Solution solution) {
        String fileName = genFileName();
        try (FileWriter fw = new FileWriter(fileName)) {
            System.out.println("Saving solution to " + fileName);
            CSVWriter csvFw = new CSVWriter(fw);
            for (int day = 0; day < solution.days(); day++) {
                var agentsVisits = new ArrayList<String>();
                for (int agent = 0; agent < solution.agents(); agent++) {
                    var list = solution.visits[day][agent];
                    var str = String.join(",", list.stream().map(Object::toString).toArray(String[]::new));
                    agentsVisits.add(str);
                }
                csvFw.writeNext(agentsVisits.toArray(String[]::new));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }

    public static Solution load(String fileName) {
        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            List<String[]> r = reader.readAll();
            List<Integer>[][] visits = new ArrayList[r.size()][r.get(0).length];
            for (int i = 0; i < r.size(); i++) {
                for (int j = 0; j < r.get(i).length; j++) {
                    visits[i][j] = r.get(i)[j].isEmpty()
                            ? new ArrayList<>()
                            : Arrays.stream(r.get(i)[j].split(",")).map(Integer::valueOf).collect(Collectors.toList());
                }
            }
            return new Solution(visits);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
