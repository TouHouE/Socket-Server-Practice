package network;

import java.util.HashMap;

public class Requests {

    public HashMap<String, Object> map = new HashMap<>();
    public String method;
    public String path[];
    public String version;

    public Requests(String requests) {
        String cutLint[] = requests.split("(\n)|(\r\n)");
        String firstLine[] = cutLint[0].split(" ");
        this.method = firstLine[0];
        this.path = firstLine[1].split("/");
        this.version = firstLine[2];
        map.put("method", method);
        map.put("path", path);
        map.put("version", version);

        for(int i = 1; i < cutLint.length; ++i) {
            String keyValue[] = cutLint[i].split(":");
            map.put(keyValue[0], keyValue[1]);
        }

    }

    public Object get(String key) {
        return map.get(key);
    }

    public String makeString() {
        return "nothing\n";
    }

}
