package hahaton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {

    /**
     * dayid -> agentid -> tradingpoints[]
     */
    public List<Integer>[][] visits;
    ArrayList<Integer> agentOfPoint;
    ArrayList<Long[]> totalTime;
    ArrayList<Long[]> workingTime;
    Long time;

    Distances distances;
    HashMap<Integer, TradingPointSchedule> schedules;
    TradingPointPool tradingPointPool;

    /*public Solution() {

    }*/

    /*public Solution(Solution solution) {
        visits = new List[7][solution.visits[0].length];
        for (int day = 0; day < 7; day++) {
            for (int agentId = 0; agentId < solution.visits[0].length; agentId++) {
                visits[day][agentId] = new ArrayList<>(solution.getTradingPoints(day, agentId));
            }
        }
    }*/

    public Solution(List<Integer>[][] visits) {
        this.visits = visits;

    }

    public Solution(int agentCount, Distances distances, HashMap<Integer, TradingPointSchedule> schedules, TradingPointPool tradingPointPool) {
        this.distances = distances;
        this.schedules = schedules;
        this.tradingPointPool = tradingPointPool;
        visits = new List[7][agentCount];
        for (int day = 0; day < 7; day++) {
            for (int agentId = 0; agentId < agentCount; agentId++) {
                visits[day][agentId] = new ArrayList<>();
            }
        }
        agentOfPoint = new ArrayList<>();
        for (int point = 1; point <= distances.size(); point++) {
            agentOfPoint.add(-1);
        }
        time = 0L;
        totalTime = new ArrayList<>();
        for (int agentId = 0; agentId < agentCount; agentId++) {
            var tt = new Long[7];
            var wt = new Long[7];
            for (int i = 0; i < 7; i++) {
                tt[i] = 0L;
                wt[i] = 0L;
            }
            totalTime.add(tt);
            workingTime.add(wt);
        }
    }

    public Solution(SolutionDTO solutionDTO, Distances distances, HashMap<Integer, TradingPointSchedule> schedules, TradingPointPool tradingPointPool) {
        this.distances = distances;
        this.schedules = schedules;
        this.tradingPointPool = tradingPointPool;
        visits = new List[7][solutionDTO.getAgentsCount()];
        for (int day = 0; day < 7; day++) {
            for (int agentId = 0; agentId < solutionDTO.getAgentsCount(); agentId++) {
                visits[day][agentId] = new ArrayList<>();
            }
        }
        int maxTradingPointCode = 0;
        for (SolutionDTO.Entry entry : solutionDTO.getPaths()) {
            if (maxTradingPointCode < entry.tradingPointCode) {
                maxTradingPointCode = entry.tradingPointCode;
            }
        }
        agentOfPoint = new ArrayList<>();
        for (int point = 1; point <= maxTradingPointCode; point++) {
            agentOfPoint.add(-1);
        }
        for (SolutionDTO.Entry entry : solutionDTO.getPaths()) {
            visits[entry.day.ordinal()][entry.agentId].add(entry.tradingPointCode);
            if (!agentOfPoint.get(entry.tradingPointCode - 1).equals(-1) && !agentOfPoint.get(entry.tradingPointCode - 1).equals(entry.agentId)) {
                System.err.println("Point " + entry.tradingPointCode + " has two agents " + agentOfPoint.get(entry.tradingPointCode - 1) + " and " + entry.agentId);
            }
            agentOfPoint.set(entry.tradingPointCode - 1, entry.agentId);
        }
        time = 0L;
        totalTime = new ArrayList<>();
        workingTime = new ArrayList<>();
        for (int agentId = 0; agentId < solutionDTO.getAgentsCount(); agentId++) {
            var curTotalTime = calculateTotalTime(agentId);
            totalTime.add(curTotalTime);
            workingTime.add(calculateWorkingTime(agentId));
            for (int i = 0; i < 7; i++) {
                time += curTotalTime[i];
            }
        }
    }

    public Long[] calculateWorkingTime(int agentId) {
        Long[] res = new Long[7];
        for (int i = 0; i < 7; i++) {
            res[i] = 0L;
        }
        for (int day = 0; day < 7; day++) {
            for (int point: visits[day][agentId]) {

                res[day] += (long)schedules.get(point).stayingTimeInMinutes * 60;

            }
        }
        return res;
    }

    public Long[] calculateTotalTime(int agentId) {
        Long[] res = calculateWorkingTime(agentId);
        for (int day = 0; day < 7; day++) {
            for (int i = 1; i < visits[day][agentId].size(); i++) {
                int from = visits[day][agentId].get(i - 1);
                int to = visits[day][agentId].get(i);
                res[day] += distances.get(from, to) * 6 / 100;
            }
        }
        return res;
    }

    public List<Integer> getTradingPoints(int dayId, int agentId) {
        return visits[dayId][agentId];
    }

    public int days() {
        return visits.length;
    }

    public int agents() {
        return visits[0].length;
    }

    public static ArrayList<Integer> TSP(Distances distances, List<Integer> points) {
        long[][] dp = new long[1 << points.size()][points.size()];
        int[][] prev = new int[1 << points.size()][points.size()];
        for (int i = 0; i < (1 << points.size()); i++) {
            for (int j = 0; j < points.size(); j++) {
                dp[i][j] = Long.MAX_VALUE;
            }
        }
        for (int j = 0; j < points.size(); j++) {
            dp[1 << j][j] = 0;
        }
        for (int mask = 0; mask < (1 << points.size()); mask++) {
            for (int last = 0; last < points.size(); last++) {
                if ((mask & (1 << last)) == 0) {
                    continue;
                }
                for (int next = 0; next < points.size(); next++) {
                    if ((mask & (1 << next)) > 0) {
                        continue;
                    }
                    long newDist = dp[mask][last] + distances.get(points.get(last), points.get(next));
                    if (dp[mask | (1 << next)][next] > newDist) {
                        dp[mask | (1 << next)][next] = newDist;
                        prev[mask | (1 << next)][next] = last;
                    }
                }
            }
        }
        int mask = (1 << points.size()) - 1;
        int last = 0;
        for (int i = 1; i < points.size(); i++) {
            if (dp[mask][last] > dp[mask][i]) {
                last = i;
            }
        }
        ArrayList<Integer> res = new ArrayList<Integer>(points);
        for (int i = points.size() - 1; i >= 0; i--) {
            res.set(i, points.get(last));
            int newMask = mask - (1 << last);
            last = prev[mask][last];
            mask = newMask;
        }
        return res;
    }

    public int emptyPoints() {
        int res = 0;
        for (int point = 0; point < agentOfPoint.size(); point++) {
            if (agentOfPoint.get(point) < 0) {
                res++;
            }
        }
        return res;
    }

    public int underWorkingAgents() {
        int res = 0;
        for (var time: workingTime) {
            for (int i = 0; i < 7; i++) {
                if (time[i] < 5 * 60 * 60 && time[i] > 0) {
                    res++;
                    //System.out.println(i + " " + time[i]);
                    break;
                }
            }
        }
        return res;
    }

    public int overWorkingAgents() {
        int res = 0;
        for (var time: totalTime) {
            for (int i = 0; i < 7; i++) {
                if (time[i] > (9 * 60 + 30) * 60) {
                    res++;
                    //System.out.println(i + " " + time[i]);
                    break;
                }
            }
        }
        return res;
    }

    public Boolean checkSchedule() {
        for (int point = 1; point <= distances.size(); point++) {
            var sh = tradingPointPool.getSchedule(point);
            int agent = getAgent(point);
            for (int day = 0; day < 7; day++) {
                if (sh[day] == 1) {
                    if (!visits[day][agent].contains(point)) {
                        return false;
                    }
                } else {
                    if (visits[day][agent].contains(point)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Boolean isValid() {
        return emptyPoints() == 0 && underWorkingAgents() == 0 && overWorkingAgents() == 0 && checkSchedule();
    }

    public void changeAgent(int pointId, int newAgent) {
        pointId--;
        int oldAgent = agentOfPoint.get(pointId);
        agentOfPoint.set(pointId, newAgent);


//        for (int day = 0; day < 7; day++) {
//            for (var x: visits[day][oldAgent]) {
//                System.out.print(x + " ");
//            }
//            System.out.println();
//        }
//        System.out.println("zhhh");

        var sh = tradingPointPool.getSchedule(pointId + 1);
//        for (var x: sh) {
//            System.out.print(x + " ");
//        }
//        System.out.println(pointId + 1);
        for (int day = 0; day < 7; day++) {
            if (sh[day] == 1) {
                if (oldAgent >= 0) {
                    visits[day][oldAgent].remove(new Integer(pointId + 1));
                }
                if (newAgent >= 0) {
                    visits[day][newAgent].add(pointId + 1);
                }
            }
        }

//        for (int day = 0; day < 7; day++) {
//            for (var x: visits[day][oldAgent]) {
//                System.out.print(x + " ");
//            }
//            System.out.println();
//        }

        if (oldAgent >= 0) {
            findAgentPaths(oldAgent);
        }
        if (newAgent >= 0) {
            findAgentPaths(newAgent);
        }


//        for (int day = 0; day < 7; day++) {
//            for (var x: visits[day][oldAgent]) {
//                System.out.print(x + " ");
//            }
//            System.out.println();
//        }
    }

    public void findAgentPaths(int agentId) {
        for (int day = 0; day < 7; day++) {
            ArrayList<Integer> newPath = Solution.TSP(distances, visits[day][agentId]);

            visits[day][agentId] = newPath;
        }
        Long[] newWorkingTime = calculateWorkingTime(agentId);
        Long[] newTotalTime = calculateTotalTime(agentId);
        for (int i = 0; i < 7; i++) {
            time += newTotalTime[i] - totalTime.get(agentId)[i];
        }
        totalTime.set(agentId, newTotalTime);
        workingTime.set(agentId, newWorkingTime);
    }

    public long getTotalTime() {
        return time;
    }

    public long getWorkingTime() {
        long res = 0;
        for (var time: workingTime) {
            for (int i = 0; i < 7; i++) {
                res += time[i];
            }
        }
        return res;
    }

    public long getRoadTime() {
        return getTotalTime() - getWorkingTime();
    }

    public double getMeters() {
        return (double)50 * getRoadTime() / 3;
    }

    public int getAgent(int pointId) {
        return agentOfPoint.get(pointId - 1);
    }

    public long getScore() {
        long res = 0;
        if (!checkSchedule()) {
            res += 100000000000l;
        }
        return (long)getMeters() + 10000000000L * emptyPoints() + 1000000000L * overWorkingAgents() + 1000000000L * underWorkingAgents() + res;
    }




}
