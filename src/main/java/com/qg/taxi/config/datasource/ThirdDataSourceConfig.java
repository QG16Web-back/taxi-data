package com.qg.taxi.config.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * @author Wilder Gao
 * time:2018/7/16
 * description：
 * motto: All efforts are not in vain
 */
//@Configuration
@MapperScan(basePackages = ThirdDataSourceConfig.PACKAGE, sqlSessionFactoryRef = "thirdSqlSessionFactory")
public class ThirdDataSourceConfig {

    static final String PACKAGE = "com.qg.taxi.dao.mysql3";
    private static final String MAPPER_LOCATION = "classpath:mapper/mysql3/*.xml";

    @Value("${spring.datasource.third.driverClassName}")
    private String driverClassName;
    @Value("${spring.datasource.third.url}")
    private String url;
    @Value("${spring.datasource.third.username}")
    private String username;
    @Value("${spring.datasource.third.password}")
    private String password;

    @Bean(name = "thirdDataSource")
    public DataSource thirdDataSource(){
        return OracleDataSourceConfig.getDateSource(driverClassName, url, username, password);
    }

    @Bean(name = "thirdDataSourceTransactionManager")
    public DataSourceTransactionManager thirdDataSourceTransactionManager(){
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(thirdDataSource());
        return dataSourceTransactionManager;
    }

    @Bean(name = "thirdSqlSessionFactory")

    public SqlSessionFactory secondarySqlSessionFactory(@Qualifier("thirdDataSource") DataSource thirdDataSource) throws Exception {
        final SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(thirdDataSource);
        sqlSessionFactoryBean.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(ThirdDataSourceConfig.MAPPER_LOCATION));
        return sqlSessionFactoryBean.getObject();
    }
}
