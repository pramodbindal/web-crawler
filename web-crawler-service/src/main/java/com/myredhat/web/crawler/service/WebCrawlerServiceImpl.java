package com.myredhat.web.crawler.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Named;

import com.myredhat.web.crawler.vo.SiteMapNode;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Named
public class WebCrawlerServiceImpl implements WebCrawlerService {

    private static final Logger logger = LoggerFactory.getLogger(WebCrawlerServiceImpl.class);

    ThreadPoolExecutor executorService = new ThreadPoolExecutor(10, 100, 0L,
            TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(10));


    private final ThreadLocal<Set<String>> visited = new ThreadLocal<>();


    @Override
    public Set<SiteMapNode> crawl(String url) {
        if (visited.get() == null) {
            visited.set(Collections.synchronizedSet(new HashSet<>()));
        }
        Set<SiteMapNode> siteMap = Collections.emptySet();
        try {
            Set<String> visitedUrls = visited.get();
            siteMap = crawlNode(new SiteMapNode(url), visitedUrls);
            logger.info("visitedUrls: {}", visitedUrls);

        } catch (Exception e) {
            logger.error("Error while crawling URL: " + url, e);
        } finally {
            visited.remove();
        }
        return siteMap;
    }


    private Set<SiteMapNode> crawlNode(SiteMapNode node, Set<String> visitedUrls) {
        Set<SiteMapNode> childNodes = Collections.emptySet();

        try {
            childNodes = this.childLinks(node, visitedUrls);
            if (childNodes != null) {
                List<Future<Set<SiteMapNode>>> tasks = new ArrayList<>();
                for (SiteMapNode childNode : childNodes) {
                    if (childNode.getNodeLevel() < 5 && !childNode.isExternal()) { // Limiting to 5 Levels only. This can be customized.
                        Callable<Set<SiteMapNode>> callableTask = () -> this.crawlNode(childNode, visitedUrls);
                        if (executorService.getActiveCount() < executorService.getMaximumPoolSize()) {
                            Future<Set<SiteMapNode>> task = executorService.submit(callableTask);
                            tasks.add(task);
                        } else {
                            callableTask.call();
                        }

                    }
                }
                if (!tasks.isEmpty()) {
                    for (Future<Set<SiteMapNode>> task : tasks) {
                        task.get();
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while waiting for Future", e);
//            throw new RuntimeException(e);
        }
        return childNodes;

    }


    private Set<SiteMapNode> childLinks(SiteMapNode parentNode, Set<String> visitedUrls) {
        String url = parentNode.getUrl();
        if (visitedUrls.contains(url)) {
            logger.debug("URL already visited. Skipping :" + url);
            return null;
        }
        logger.info("Crawling: {}", url);
        visitedUrls.add(url);
        String domain = getDomain(url);
        String scheme = getScheme(url);
        Set<SiteMapNode> nodes = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url).timeout(30000).get();
            Elements elements = doc.select("a[href]");
            for (Element element : elements) {
                String link = element.attr("href");
                String childDomain = getDomain(link);
                String domainLink = link;
                boolean external = false;
                if (childDomain.isEmpty()) {
                    if (link.startsWith("/") || domain.endsWith("/"))
                        domainLink = domain + link;
                    else
                        domainLink = domain + "/" + link;
                    domainLink = scheme + "://" + domainLink;
                } else if (childDomain.equals(domain)) {
                    domainLink = link;
                } else {
                    logger.debug("External Link - Child Domain : {}, Domain: {}", childDomain, domain);
                    external = true;
                }
                SiteMapNode node = new SiteMapNode(domainLink, parentNode, external);
                nodes.add(node);
                if (nodes.size() >= 20) // Limit to 20  nodes only. This can be customized.
                    break;
            }
        } catch (Exception e) {
            logger.error("Error while getting links : " + url, e);
        }
        parentNode.setChildNodes(nodes);
        return nodes;
    }


    private String getDomain(String url) {
        String domain = "";
        if (url.startsWith("http")) {
            try {
                URI uri = new URI(url);
                domain = uri.getHost();
            } catch (Exception e) {
//                throw new RuntimeException(e);
            }

        }
        return domain;

    }

    private String getScheme(String url) {
        String scheme = "http";
        if (url.startsWith("http")) {
            try {
                URI uri = new URI(url);
                scheme = uri.getScheme();
            } catch (Exception e) {
//                throw new RuntimeException(e);
            }

        }
        return scheme;

    }

}

