package com.amazing.company.nodes;

import com.amazing.company.nodes.entities.Node;
import com.amazing.company.nodes.repositories.NodeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@SpringBootApplication
public class AmazingCompanyApplication {

  public static void main(String[] args) {
    SpringApplication.run(AmazingCompanyApplication.class, args);
  }

  @Bean
  CommandLineRunner initialize(NodeRepository nodeRepository) {
    return args -> {
      Node rootNode = new Node();
      rootNode.setRoot(rootNode);
      rootNode.setParent(null);
      rootNode.setHeight(0);
      nodeRepository.save(rootNode);
    };
  }

  @Bean
  public CommonsRequestLoggingFilter requestLoggingFilter() {
    CommonsRequestLoggingFilter loggingFilter = new CommonsRequestLoggingFilter();
    loggingFilter.setIncludeClientInfo(true);
    loggingFilter.setIncludeQueryString(true);
    loggingFilter.setIncludePayload(true);
    loggingFilter.setMaxPayloadLength(64000);
    return loggingFilter;
  }
}
