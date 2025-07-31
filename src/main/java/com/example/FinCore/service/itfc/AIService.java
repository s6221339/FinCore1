package com.example.FinCore.service.itfc;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public final class AIService 
{
	
	private final ChatClient chatClient;
	
	@Value("classpath::code.st")
	private Resource templateResourceFromCodest;
	
	public AIService(ChatClient.Builder builder)
	{
		this.chatClient = builder.build();
	}
	
	public String templateFromFileCode(String language, String methodName) {
		PromptTemplate promptTemplate = new PromptTemplate(templateResourceFromCodest);
		Prompt prompt = promptTemplate.create(Map.of("language", language, "methodName", methodName));
		ChatResponse response = chatClient.prompt(prompt).call().chatResponse();
		return response.getResult().getOutput().getText();
	}

	
}
