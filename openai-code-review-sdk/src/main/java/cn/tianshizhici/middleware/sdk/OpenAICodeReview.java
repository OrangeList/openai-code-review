package cn.tianshizhici.middleware.sdk;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import com.sun.org.apache.bcel.internal.classfile.Code;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;

public class OpenAICodeReview {
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("测试执行");

        // 1. 代码检出
        ProcessBuilder processBuilder = new ProcessBuilder("git", "diff", "HEAD~1", "HEAD");
        processBuilder.directory(new File("."));

        Process process = processBuilder.start();

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        // 读取git diff命令的输出，每读取一行就将其附加到StringBuilder中
        StringBuilder diffCode = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            diffCode.append(line);
        }

        int exitCode = process.waitFor();
        System.out.println("Exited with code:" + exitCode);

        System.out.println("diff code：" + diffCode.toString());

        // chatgpt 代码评审
        String log = codeReview(diffCode.toString());
        System.out.println("code review：" + log);
    }

    private static String codeReview(String diffCode) {

        //国内需要代理
//        Proxy proxy = Proxys.http("127.0.0.1", 7890);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("sk-oEd2wR28WEEmcuwd4d740a48Fa534fBa9bFdEfF0A2Fb903b")
//                .proxy(proxy)
                .proxy(null)
                .apiHost("https://xiaoai.plus") //反向代理地址
                .build()
                .init();

        String role = "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言" +
                "请，请您根据git diff记录，对代码做出评审，包括代码变更分析，代码变更优缺点，改进建议等" +
                "，只给出结果不需要额外的声明，格式为" +
                "-1 代码变更分析" +
                " -1.1 ..." +
                "-2 代码变更优缺点" +
                "...以此类推的分点格式。代码为:";
        String res = chatGPT.chat(role + diffCode);
        return res;
    }
}
