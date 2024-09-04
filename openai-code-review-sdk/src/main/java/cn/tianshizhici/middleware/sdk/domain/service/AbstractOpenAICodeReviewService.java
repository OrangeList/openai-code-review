package cn.tianshizhici.middleware.sdk.domain.service;

import cn.tianshizhici.middleware.sdk.infrastructure.git.GitCommand;
import cn.tianshizhici.middleware.sdk.infrastructure.openai.ChatGPT;
import cn.tianshizhici.middleware.sdk.infrastructure.weixin.WeiXin;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public abstract class AbstractOpenAICodeReviewService implements IOpenAICodeReviewService {
    private Logger logger = LoggerFactory.getLogger(AbstractOpenAICodeReviewService.class);

    protected final GitCommand gitCommand;
    protected final ChatGPT chatGPT;
    protected final WeiXin weiXin;

    public AbstractOpenAICodeReviewService(GitCommand gitCommand, ChatGPT chatGPT, WeiXin weiXin) {
        this.gitCommand = gitCommand;
        this.chatGPT = chatGPT;
        this.weiXin = weiXin;
    }

    @Override
    public void exec() {
        try {
            // 1. 获取提交代码
            String diffCode = getDiffCode();
            // 2. 开始评审代码
            String recommend = codeReview(diffCode);
            // 3. 记录评审结果；返回日志地址
            String logUrl = recordCodeReview(recommend);
            // 4. 发送消息通知；日志地址、通知的内容
            pushMessage(logUrl);
        } catch (Exception e) {
            logger.error("openai-code-review error", e);
        }
    }

    protected abstract String recordCodeReview(String recommend) throws GitAPIException, IOException;

    protected abstract void pushMessage(String logUrl) throws IOException;

    protected abstract String codeReview(String diffCode);

    protected abstract String getDiffCode() throws IOException, InterruptedException;
}
