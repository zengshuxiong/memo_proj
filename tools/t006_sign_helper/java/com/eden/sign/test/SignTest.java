package com.eden.util.sign.test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class SignTest {
    public static void main(String[] args) throws Exception {
        doSign();
    }

    public static void doSign() throws Exception {
        String timestamp = DateHelper.getDateString(new Date(),SysEnum.DateTimeFormat.yyyyMMddHHmmss);
        String json = "{\"QueryType\":\"01:楼盘信息\",\"PageIndex\":\"0\",\"PageSize\":\"1000\"}";

        String app_id = "1e76fdc99fe54de9ac6d5ac4d27da743";

        sig_result sigResult = Sender.doSign(app_id, json);
        System.out.println(sigResult);

        Map<String, String> map = new HashMap<>();
        map.put(ApiHelper.REQUEST_ROUTE_CODE, "demo.api");
        map.put(ApiHelper.REQUEST_BIZ_CODE, "bzcc");
        map.put(ApiHelper.REQUEST_CALLER, "api_test_caller");
        map.put(ApiHelper.REQUEST_TIMESTAMP, timestamp);
        map.put(ApiHelper.REQUEST_DATA, sigResult.source);
        map.put(ApiHelper.REQUEST_SIG_DATA, sigResult.sig_data);
        map.put(ApiHelper.REQUEST_APP_ID, app_id);

        String responseStr = HttpHelper.sendPost("http://localhost:8000/gateway", map);
        System.out.println("[responseStr]" + responseStr);
    }
}
