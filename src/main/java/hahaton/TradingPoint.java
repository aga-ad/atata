package hahaton;

import java.util.Arrays;

/*
Мерчендайзер (ФИО),Сеть,Адрес ТТ,Время в ТТ,Рын. формат,Понедельник,Вторник,Среда,Четверг,Пятница,Суббота,Воскресенье,Кол-во визитов в неделю
Мерчендайзер-1,Лента,"г.Казань, Рихарда Зорге ул., 11б",4:19:46,Дискаунтер,1,1,,,1,1,,4
 */
public class TradingPoint {
    public final String clientName;
    public final String clientAddress;
    public final long stayingTimeInSeconds;
    public final String marketFormat;
    public final int[] schedule;
    public final int visitCount;

    public TradingPoint(String clientName, String clientAddress, long stayingTimeInSeconds, String marketFormat, int[] schedule, int visitCount) {
        this.clientName = clientName;
        this.clientAddress = clientAddress;
        this.stayingTimeInSeconds = stayingTimeInSeconds;
        this.marketFormat = marketFormat;
        this.schedule = schedule;
        this.visitCount = visitCount;
    }

    public static TradingPoint parse(String[] line) {
        int[] schedule = new int[7];
        for (int i = 0; i < 7; i++) {
            if ("".equals(line[i + 5]))
                schedule[i] = 1;
        }
        return new TradingPoint(line[1],
                line[2],
                InputUtils.parseSeconds(line[3]),
                line[4],
                schedule,
                Integer.parseInt(line[11]));
    }


    @Override
    public String toString() {
        return "TradingPoint{" +
                "clientName='" + clientName + '\'' +
                ", clientAddress='" + clientAddress + '\'' +
                ", stayingTimeInSeconds='" + stayingTimeInSeconds + '\'' +
                ", marketFormat='" + marketFormat + '\'' +
                ", schedule=" + Arrays.toString(schedule) +
                ", visitCount=" + visitCount +
                '}';
    }
}