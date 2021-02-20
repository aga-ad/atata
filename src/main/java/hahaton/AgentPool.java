package hahaton;

import java.util.HashMap;
import java.util.Map;

public class AgentPool {
    private final Map<Integer, String> id2name;
    private final Map<String, Integer> name2id;
    private int nf;

    public AgentPool() {
        id2name = new HashMap<>();
        name2id = new HashMap<>();
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
}
