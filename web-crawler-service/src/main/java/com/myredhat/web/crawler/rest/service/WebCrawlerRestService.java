package com.myredhat.web.crawler.rest.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.Objects;
import java.util.Set;

import javax.inject.Inject;

import com.myredhat.web.crawler.service.WebCrawlerService;
import com.myredhat.web.crawler.vo.SiteMapNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebCrawlerRestService {

    @Inject
    private WebCrawlerService service;
    private static Logger logger = LoggerFactory.getLogger(WebCrawlerRestService.class);

    @GetMapping("/crawl")
    @ResponseBody
    public ResponseEntity<?> crawl(@RequestParam(value = "url") String url) {
        if (!isValid(url)) {
            logger.error("Invalid URL");
            return new ResponseEntity<>("Invalid URL : "+url, HttpStatus.BAD_REQUEST);
        }
        logger.info("Crawling URL : {}", url);
        Set<SiteMapNode> nodes = service.crawl(url);
        logger.info("Returning {} Nodes", nodes.size());
        return new ResponseEntity<>(nodes, HttpStatus.OK);
    }

    private boolean isValid(String url) {
        boolean valid = false;
        try {
            URL uri = new URL(url);
            valid = true;
        } catch (Exception e) {
            logger.error("Invalid URL provided", e);
        }
        return valid;
    }

}
