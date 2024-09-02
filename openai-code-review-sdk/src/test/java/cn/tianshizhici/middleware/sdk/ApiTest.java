package cn.tianshizhici.middleware.sdk;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.Proxy;

public class ApiTest {

    @Test
    public void test() {
        //国内需要代理
        Proxy proxy = Proxys.http("127.0.0.1", 7890);
        //socks5 代理
        // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-oEd2wR28WEEmcuwd4d740a48Fa534fBa9bFdEfF0A2Fb903b")
                .proxy(proxy)
                .apiHost("https://xiaoai.plus") //反向代理地址
                .build()
                .init();

        String role = "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言请，请您根据git diff记录，对代码做出评审，只给出结果不需要额外的声明。代码为:";
        String res = chatGPT.chat(
                role +
                        "int a;"
        );
        System.out.println(res);
    }


}
