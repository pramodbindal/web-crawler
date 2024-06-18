package com.myredhat.web.crawler.client;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class WebCrawlerClient {

    private final RestClient restClient = RestClient.create();

    public void crawlWeb(String url) {
        String response = restClient.get()
                .uri("http://localhost:8080/crawl?url={url}", url)
                .retrieve()
                .body(String.class);

        System.out.println("response = " + response);

    }
}
