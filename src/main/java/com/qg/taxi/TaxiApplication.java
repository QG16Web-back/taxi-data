package com.qg.taxi;

import com.qg.taxi.cache.YctCache;
import com.qg.taxi.dao.mysql.YctDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration;
import org.springframework.boot.autoconfigure.solr.SolrAutoConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @author ming
 */
@SpringBootApplication(exclude = {SolrAutoConfiguration.class, GsonAutoConfiguration.class})
public class TaxiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaxiApplication.class, args);
    }

    @Bean
    CommandLineRunner init(YctDao yctDao) {
        return (args) -> YctCache.yctList = yctDao.getYctList();
    }
}
