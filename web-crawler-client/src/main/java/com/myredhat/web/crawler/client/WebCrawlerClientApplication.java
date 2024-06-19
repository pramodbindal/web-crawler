package com.myredhat.web.crawler.client;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class WebCrawlerClientApplication {


	public static void main(String[] args) {
		if(args.length<1){
			throw new RuntimeException("Please provide URL to Crawl");
		}
		ConfigurableApplicationContext applicationContext = SpringApplication.run(WebCrawlerClientApplication.class, args);
		WebCrawlerClient webCrawlerClient = applicationContext.getBean(WebCrawlerClient.class);
		webCrawlerClient.crawlWeb(args[0]);
	}



}
