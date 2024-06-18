package com.myredhat.web.crawler.service;

import java.util.Set;

import com.myredhat.web.crawler.vo.SiteMapNode;

public interface WebCrawlerService {


    Set<SiteMapNode> crawl(String url);

}
