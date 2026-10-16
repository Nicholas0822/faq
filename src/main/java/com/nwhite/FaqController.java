package com.nwhite;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaqController {
    private final ChatClient chatClient;

    public FaqController(ChatClient.Builder builder, VectorStore vectorStore) {
        var documentRetriever = VectorStoreDocumentRetriever.builder()
                .vectorStore(vectorStore)
                .build();
        var ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .documentRetriever(documentRetriever)
                .build();
        this.chatClient = builder
                .defaultAdvisors(ragAdvisor)
                .build();
    }

    @GetMapping("/faq")
    public String faq(@RequestParam(value = "message", defaultValue = "How many athletes compete in the Olympic Games Paris 2024") String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
