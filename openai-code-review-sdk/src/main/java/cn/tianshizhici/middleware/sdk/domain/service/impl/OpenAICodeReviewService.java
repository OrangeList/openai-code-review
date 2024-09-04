package cn.tianshizhici.middleware.sdk.domain.service.impl;

import cn.tianshizhici.middleware.sdk.domain.service.AbstractOpenAICodeReviewService;
import cn.tianshizhici.middleware.sdk.infrastructure.git.GitCommand;

import cn.tianshizhici.middleware.sdk.infrastructure.openai.ChatGPT;
import cn.tianshizhici.middleware.sdk.infrastructure.weixin.WeiXin;
import cn.tianshizhici.middleware.sdk.infrastructure.weixin.dto.TemplateMessageDTO;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OpenAICodeReviewService extends AbstractOpenAICodeReviewService {
    public OpenAICodeReviewService(GitCommand gitCommand, ChatGPT chatGPT, WeiXin weiXin) {
        super(gitCommand, chatGPT, weiXin);
    }

    @Override
    protected String recordCodeReview(String recommend) throws GitAPIException, IOException {
        return gitCommand.commitAndPush(recommend);
    }

    @Override
    protected void pushMessage(String logUrl) throws IOException {
        Map<String, Map<String, String>> data = new HashMap<>();
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.REPO_NAME, gitCommand.getProject());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.BRANCH_NAME, gitCommand.getBranch());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_AUTHOR, gitCommand.getAuthor());
        TemplateMessageDTO.put(data, TemplateMessageDTO.TemplateKey.COMMIT_MESSAGE, gitCommand.getMessage());
        weiXin.sendTemplateMessage(logUrl, data);
    }

    @Override
    protected String codeReview(String diffCode) {
        return chatGPT.ask(diffCode);
    }

    @Override
    protected String getDiffCode() throws IOException, InterruptedException {
        return gitCommand.diff();
    }
}
