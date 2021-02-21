package hahaton;

import java.io.IOException;

/*
Код ТТ,Активность,Название/адрес,Наименование клиента,Код сети,Адрес клиента,Широта,Долгота,Количество посещений,День посещения,Закрепленный за клиентом ТП,Продолжительность посещения,Начало посещения,Конец посещения
00001,1,Лента,Лента,,"г.Казань, Рихарда Зорге ул., 11б",,,4,,Мерчендайзер-1,259,,
 */
public class TradingPointSchedule {
    public final int tradingPointCode;
    public final String name;
    public final String clientName;
    public final String networkCode;
    public final String clientAddress;
    public final String latitude;
    public final String longitude;
    public final int visitsCount;
    public final VisitDay visitDay;
    public final int agentId;
    public final int stayingTimeInMinutes;

    public TradingPointSchedule(int tradingPointCode, String name, String clientName, String networkCode, String clientAddress, String latitude, String longitude, int visitsCount, VisitDay visitDay, int agentId, int stayingTimeInMinutes) {
        this.tradingPointCode = tradingPointCode;
        this.name = name;
        this.clientName = clientName;
        this.networkCode = networkCode;
        this.clientAddress = clientAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.visitsCount = visitsCount;
        this.visitDay = visitDay;
        this.agentId = agentId;
        this.stayingTimeInMinutes = stayingTimeInMinutes;
    }

    public static TradingPointSchedule parseLine(String[] line, AgentPool agentPool, TradingPointPool pool) throws IOException {
        int tradingPointCode = Integer.parseInt(line[0]);
        String name = line[2];
        String clientName = line[3];
        String networkCode = line[4];
        String clientAddress = line[5];
        String latitude = line[6];
        String longitude = line[7];
        int visitsCount = Integer.parseInt("0" + line[8]);
        VisitDay visitDay = VisitDay.parse(line[9]);
        int agentId = agentPool.getId(line[10]);
        int stayingTimeInMinutes = Integer.parseInt(line[11]);
        pool.setTradingPointCode(tradingPointCode, clientName, clientAddress);
        pool.setStayingTimeInSeconds(tradingPointCode, stayingTimeInMinutes * 1000);
        return new TradingPointSchedule(tradingPointCode,
                name,
                clientName,
                networkCode,
                clientAddress,
                latitude,
                longitude,
                visitsCount,
                visitDay,
                agentId,
                stayingTimeInMinutes);
    }

    @Override
    public String toString() {
        return "TradingPointSchedule{" +
                "tradingPointCode=" + tradingPointCode +
                ", name='" + name + '\'' +
                ", clientName='" + clientName + '\'' +
                ", networkCode='" + networkCode + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", visitsCount=" + visitsCount +
                ", visitDay=" + visitDay +
                ", agentId=" + agentId +
                ", stayingTime=" + stayingTimeInMinutes +
                '}';
    }
}
