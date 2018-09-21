package library.assistant.util;

import net.sf.json.JSONArray;

public class jsonUtil {
    public static JSONArray getJsonArrayFromString(String json){
        JSONArray jsonarray;
        //读取JSONArray，用下标索引获取
        String array = json;
        jsonarray = JSONArray.fromObject(array);
        return jsonarray;
    }
}
