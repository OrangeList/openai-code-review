package cn.tianshizhici.middleware.sdk;

import cn.tianshizhici.middleware.sdk.types.utils.WXAccessTokenUtils;
import com.alibaba.fastjson2.JSON;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApiTest {

    public void test() {
        //国内需要代理
        //Proxy proxy = Proxys.http("127.0.0.1", 7890);
        //socks5 代理
        // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-oEd2wR28WEEmcuwd4d740a48Fa534fBa9bFdEfF0A2Fb903b")
                .apiHost("https://xiaoai.plus") //反向代理地址
                .build()
                .init();

        String role = "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审，只给出结果不需要额外的声明。代码为:";
        String res = chatGPT.chat(
                role +
                        "int a = 99;"
        );
        System.out.println(res);
    }

    public void test_wx() {
        String accessToken = WXAccessTokenUtils.getAccessToken();
        System.out.println(accessToken);

        Message message = new Message();
        message.put("project", "big-market");
        message.put("review", "feat: 新加功能");

        String url = String.format("https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s", accessToken);
        sendPostRequest(url, JSON.toJSONString(message));
    }


    private static void sendPostRequest(String urlString, String jsonBody) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setRequestProperty("Accept", "application/json");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            try (Scanner scanner = new Scanner(conn.getInputStream(), StandardCharsets.UTF_8.name())) {
                String response = scanner.useDelimiter("\\A").next();
                System.out.println(response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static class Message {
        private String touser = "o9KUB64bgNLE_Xy1hHFZscvkWUk4";
        private String template_id = "cPLLnV1vAjih9C298FHAerlRs9Z_idcl9TOxwY3wI5Q";
        private String url = "https://github.com/OrangeList/openai-code-review-log/blob/main/2024-09-03/6iHc3al4whyq.md";
        private Map<String, Map<String, String>> data = new HashMap<>();

        public void put(String key, String value) {
            data.put(key, new HashMap<String, String>() {
                {
                    put("value", value);
                }
            });
        }

        public String getTouser() {
            return touser;
        }

        public void setTouser(String touser) {
            this.touser = touser;
        }

        public String getTemplate_id() {
            return template_id;
        }

        public void setTemplate_id(String template_id) {
            this.template_id = template_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Map<String, Map<String, String>> getData() {
            return data;
        }

        public void setData(Map<String, Map<String, String>> data) {
            this.data = data;
        }
    }


}
