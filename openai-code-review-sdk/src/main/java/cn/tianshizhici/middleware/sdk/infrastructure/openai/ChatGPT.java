package cn.tianshizhici.middleware.sdk.infrastructure.openai;


public class ChatGPT {

    private final String apikey;

    public ChatGPT(String apikey) {
        this.apikey = apikey;
    }

    public String getApikey() {
        return apikey;
    }

    public String ask(String diffCode) {
        com.plexpt.chatgpt.ChatGPT chatGPT = com.plexpt.chatgpt.ChatGPT.builder()
                .apiKey(apikey)
//                .proxy(proxy)
                .proxy(null)
                .apiHost("https://xiaoai.plus") //反向代理地址
                .build()
                .init();

        String role = "你是一个高级编程架构师，精通各类场景方案、架构设计和编程语言" +
                "请，请您根据git diff记录，对代码做出评审，包括改动文件，代码变更分析（原代码，新代码/改动代码），代码变更优缺点，改进建议等" +
                "，尽可能严谨且详细，格式为分点格式。代码为:";
        String res = chatGPT.chat(role + diffCode);
        return res;
    }

}
