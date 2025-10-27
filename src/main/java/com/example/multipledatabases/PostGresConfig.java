package com.example.multipledatabases;


import jakarta.persistence.EntityManagerFactory;
import org.hibernate.dialect.PostgreSQLDialect;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableJpaRepositories(
        basePackages = "com.example.multipledatabases",
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "postgresTransactionManager"

)
public class PostGresConfig {

    @Bean(name = "postgresDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.postgres")
    public DataSource postgresDataSource(){
        return DataSourceBuilder.create().build();
    }

    @Bean(name="postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean postgresEntityManagerFactory(EntityManagerFactoryBuilder entityManagerFactoryBuilder, @Qualifier("postgresDataSource") DataSource dataSource){
        Map<String,Object> jpaproperties=new HashMap<String,Object>();
        jpaproperties.put("hibernate.dialect", PostgreSQLDialect.class.getName());
        return  entityManagerFactoryBuilder.dataSource(dataSource)
                .properties(jpaproperties)
                .persistenceUnit("postgresPU")
                .packages("com.example.multipledatabases")
                .build();


    }

    @Bean("postgresTransactionManager")
    public PlatformTransactionManager postgresTransactionManager(@Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory){

        return new JpaTransactionManager(entityManagerFactory);
    }
}
