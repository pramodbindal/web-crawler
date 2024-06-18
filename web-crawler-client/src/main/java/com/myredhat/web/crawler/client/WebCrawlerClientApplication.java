package com.myredhat.web.crawler.client;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class WebCrawlerClientApplication {


	public static void main(String[] args) {
		ConfigurableApplicationContext x = SpringApplication.run(WebCrawlerClientApplication.class, args);
		x.getBean(WebCrawlerClient.class).crawlWeb(args[0]);
	}



}
