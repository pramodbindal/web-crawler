package com.myredhat.web.crawler.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WebCrawlerClient {

    private final RestClient restClient = RestClient.create();


    @Value("${web.crawler.service.Url:http://localhost:8080/crawl?url={url}}")
    private String serviceUrl;

    private static final Logger logger = LoggerFactory.getLogger(WebCrawlerClient.class);

    public void crawlWeb(String url) {
        String response = restClient.get()
                .uri(serviceUrl, url)
                .retrieve()
                .body(String.class);

        logger.info("Service Response is : {}", response);

    }
}
