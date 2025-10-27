package com.example.multipledatabases;


import jakarta.persistence.EntityManagerFactory;

import org.hibernate.dialect.MySQLDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(basePackages = "com.example.multipledatabases"
        ,entityManagerFactoryRef = "mysqlEntityMangerFactory"
,transactionManagerRef = "mysqlTransactionManager")
@EnableTransactionManagement
public class MysqlConfig {

    //@Autowired


    @Primary
    @Bean(name = "mysqlDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.msql")
    public DataSource mysqlDataSource(){

        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean(name = "mysqlEntityMangerFactory")
    public LocalContainerEntityManagerFactoryBean mysqlEntityMangerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("mysqlDataSource") DataSource dataSource){
        Map<String, Object> jpaProperties = new HashMap<>();
        jpaProperties.put("hibernate.dialect", MySQLDialect.class.getName());
        return entityManagerFactoryBuilder.dataSource(dataSource)
                .packages("com.example.multipledatabases")
                .persistenceUnit("mysqlPU")
                .properties(jpaProperties)
                .build();
    }

    @Bean(name = "mysqlTransactionManager")
    public PlatformTransactionManager mysqlTransactionManager(@Qualifier("mysqlEntityMangerFactory") EntityManagerFactory entityManagerFactory){

        return new JpaTransactionManager(entityManagerFactory);
    }

}
