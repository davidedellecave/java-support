package ddc.support.jack;

public class JsonHelper {
    private static JsonTools tools;
    static {
        tools = new JsonTools();
    }

    public static JsonTools j() {
        return tools;
    }
}
