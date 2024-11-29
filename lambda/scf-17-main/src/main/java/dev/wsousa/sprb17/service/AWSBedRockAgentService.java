package dev.wsousa.sprb17.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.bedrockagentruntime.BedrockAgentRuntimeClient;
import software.amazon.awssdk.services.bedrockagentruntime.model.*;
import software.amazon.awssdk.services.bedrockruntime.model.ContentBlock;
import software.amazon.awssdk.services.bedrockruntime.model.ConversationRole;
import software.amazon.awssdk.services.bedrockruntime.model.Message;

@Slf4j
@Service
public class AWSBedRockAgentService {
    private final static String KNOWLEDGE_BASE_ID = "YW5JIBEETC";
    ObjectMapper requestMapper = new ObjectMapper();

    // Initialize the knowledgebase configuration
    KnowledgeBaseVectorSearchConfiguration knowledgeBaseVectorSearchConfiguration = KnowledgeBaseVectorSearchConfiguration.builder()
            .numberOfResults(5)
            .build();

    KnowledgeBaseRetrievalConfiguration knowledgeBaseRetrievalConfiguration = KnowledgeBaseRetrievalConfiguration.builder()
            .vectorSearchConfiguration(knowledgeBaseVectorSearchConfiguration)
            .build();

    public String converse(String prompt) {
        // Create a Bedrock Runtime client in the AWS Region you want to use.
        // Replace the DefaultCredentialsProvider with your preferred credentials provider.
        var client = BedrockAgentRuntimeClient.builder()
                .credentialsProvider(DefaultCredentialsProvider.create())
                .region(Region.US_EAST_1)
                .build();

        // Form the request for bedrock knowledgebase
        KnowledgeBaseQuery knowledgeBaseQuery = KnowledgeBaseQuery.builder()
                .text(prompt)
                .build();

        RetrieveRequest retrieveRequest = RetrieveRequest.builder()
                .knowledgeBaseId(KNOWLEDGE_BASE_ID)
                .retrievalQuery(knowledgeBaseQuery)
                .retrievalConfiguration(knowledgeBaseRetrievalConfiguration)
                .build();

        // Invoke the bedrock knowledgebase
        RetrieveResponse retrieveResponse = client.retrieve(retrieveRequest);

        // Extract the bedrock results and return the results
        try {
            if (retrieveResponse.hasRetrievalResults()) {
                ArrayNode responseNode = requestMapper.createArrayNode();
                for (KnowledgeBaseRetrievalResult result : retrieveResponse.retrievalResults()) {
                    responseNode.add(result.content().text());
                }
                log.info("OUTPUT RESPONSE: %s".formatted(responseNode.toString()));
                return requestMapper.writeValueAsString(responseNode);
            }
        } catch (JsonProcessingException e) {
            log.info(e.getMessage());
        }
        return  "";
    }

}