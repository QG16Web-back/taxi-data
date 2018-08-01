package com.qg.taxi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;

/**
 * @author ming
 */
@SpringBootApplication(exclude = {SolrAutoConfiguration.class, GsonAutoConfiguration.class})
public class TaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiApplication.class, args);
    }
}
