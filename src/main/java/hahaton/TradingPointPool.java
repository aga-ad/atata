package hahaton;

import java.util.HashMap;
import java.util.Map;

public class TradingPointPool {
    private final Map<Integer, String> id2name;
    private final Map<String, Integer> name2id;
    private final Map<Integer, int[]> id2schedule;
    private final Map<Integer, Integer> id2stayingTimeInSeconds;
    private int nf;

    public TradingPointPool() {
        id2name = new HashMap<>();
        name2id = new HashMap<>();
        id2schedule = new HashMap<>();
        id2stayingTimeInSeconds = new HashMap<>();
        nf = 0;
    }

    public int getId(String str) {
        if (!name2id.containsKey(str)) {
            name2id.put(str, nf);
            id2name.put(nf, str);
            nf++;
        }
        return name2id.get(str);
    }

    public String getName(int id) {
        return id2name.get(id);
    }

    public void setSchedule(int agentId, int[] schedule) {
        id2schedule.put(agentId, schedule);
    }

    public void setStayingTimeInSeconds(int agentId, int stayingTimeInSeconds) {
        id2stayingTimeInSeconds.put(agentId, stayingTimeInSeconds);
    }

    public int[] getSchedule(int agentId) {
        return id2schedule.get(agentId);
    }

    public int getStayingTimeInSeconds(int agentId) {
        return id2stayingTimeInSeconds.get(agentId);
    }
}
