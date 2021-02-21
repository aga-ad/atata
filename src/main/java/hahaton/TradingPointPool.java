package hahaton;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class TradingPointPool {
    final Map<Integer, TradingPointId> tradingPointCode2name;
    private final Map<TradingPointId, Integer> name2tradingPointCode;
    private final Map<Integer, int[]> tradingPointCode2schedule;
    private final Map<Integer, Integer> tradingPointCode2stayingTimeInSeconds;

    public TradingPointPool() {
        tradingPointCode2name = new HashMap<>();
        name2tradingPointCode = new HashMap<>();
        tradingPointCode2schedule = new HashMap<>();
        tradingPointCode2stayingTimeInSeconds = new HashMap<>();
    }

    public void setTradingPointCode(int tradingPointCode, String clientName, String clientAddress) {
        TradingPointId id = new TradingPointId(clientName, clientAddress);
        name2tradingPointCode.put(id, tradingPointCode);
        tradingPointCode2name.put(tradingPointCode, id);
    }

    public int getTradingPointCode(String clientName, String clientAddress) {
        return name2tradingPointCode.get(new TradingPointId(clientName, clientAddress));
    }

    public TradingPointId getName(int tradingPointCode) {
        return tradingPointCode2name.get(tradingPointCode);
    }

    public void setSchedule(int tradingPointCode, int[] schedule) {
        tradingPointCode2schedule.put(tradingPointCode, schedule);
    }

    public void setStayingTimeInSeconds(int tradingPointCode, int stayingTimeInSeconds) {
        tradingPointCode2stayingTimeInSeconds.put(tradingPointCode, stayingTimeInSeconds);
    }

    public int[] getSchedule(int tradingPointCode) {
        return tradingPointCode2schedule.get(tradingPointCode);
    }

    public int getStayingTimeInSeconds(int tradingPointCode) {
        return tradingPointCode2stayingTimeInSeconds.get(tradingPointCode);
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
