package cn.tianshizhici.middleware.sdk;

import cn.tianshizhici.middleware.sdk.domain.service.impl.OpenAICodeReviewService;
import cn.tianshizhici.middleware.sdk.infrastructure.git.GitCommand;
import cn.tianshizhici.middleware.sdk.infrastructure.openai.ChatGPT;
import cn.tianshizhici.middleware.sdk.infrastructure.weixin.WeiXin;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class OpenAICodeReview {

    public static final Logger logger = LoggerFactory.getLogger(OpenAICodeReview.class);

    // 微信配置
    private static String weixin_appid = "wx0f370e0873d37d06";
    private static String weixin_secret = "b9960e8c28ad066b0d6e6493782c1b7b";
    private static String weixin_touser = "o9KUB64bgNLE_Xy1hHFZscvkWUk4";
    private static String weixin_template_id = "cPLLnV1vAjih9C298FHAerlRs9Z_idcl9TOxwY3wI5Q";

    // ChatGPT配置
    private String chatgpt_apikey = "sk-oEd2wR28WEEmcuwd4d740a48Fa534fBa9bFdEfF0A2Fb903b";

    // Github 配置
    private String github_review_log_uri;
    private String github_token;

    // 工程配置 - 自动获取
    private String github_project;
    private String github_branch;
    private String github_author;

    public static void main(String[] args) throws IOException, InterruptedException, GitAPIException {
        GitCommand gitCommand = new GitCommand(
                getEnv("CODE_REVIEW_LOG_URI"),
                getEnv("CODE_TOKEN"),
                getEnv("COMMIT_PROJECT"),
                getEnv("COMMIT_BRANCH"),
                getEnv("COMMIT_AUTHOR"),
                getEnv("COMMIT_MESSAGE")
        );

        WeiXin weiXin = new WeiXin(
                getEnv("WEIXIN_APPID"),
                getEnv("WEIXIN_SECRET"),
                getEnv("WEIXIN_TOUSER"),
                getEnv("WEIXIN_TEMPLATE_ID")
        );


        ChatGPT chatGPT = new ChatGPT(getEnv("CHATGPT_APIKEY"));

        OpenAICodeReviewService openAICodeReviewService = new OpenAICodeReviewService(gitCommand, chatGPT, weiXin);
        openAICodeReviewService.exec();

        logger.info("openai-code-review done!");
    }

    private static String getEnv(String key) {
        String value = System.getenv(key);
        if (null == value || value.isEmpty()) {
            throw new RuntimeException("value is null");
        }
        return value;
    }
}
