package hahaton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TradingPointPool {
    final Map<Integer, TradingPointId> id2name;
    private final Map<TradingPointId, Integer> name2id;
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

    public int getId(String clientName, String clientAddress) {
        TradingPointId id = new TradingPointId(clientName, clientAddress);
        if (!name2id.containsKey(id)) {
            name2id.put(id, nf);
            id2name.put(nf, id);
            nf++;
        }
        return name2id.get(id);
    }

    public TradingPointId getName(int id) {
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


    public static final class TradingPointId {
        public final String clientName;
        public final String clientAddress;

        public TradingPointId(String clientName, String clientAddress) {
            this.clientName = clientName;
            this.clientAddress = clientAddress;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TradingPointId that = (TradingPointId) o;
            return Objects.equals(clientName, that.clientName) && Objects.equals(clientAddress, that.clientAddress);
        }

        @Override
        public int hashCode() {
            return Objects.hash(clientName, clientAddress);
        }

        @Override
        public String toString() {
            return "TradingPointId{" +
                    "clientName='" + clientName + '\'' +
                    ", clientAddress='" + clientAddress + '\'' +
                    '}';
        }
    }
}
