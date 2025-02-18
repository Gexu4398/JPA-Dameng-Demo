package com.gexu.jpadamengdemo.datasource;

import com.gexu.jpadamengdemo.repository.BaseRepositoryImpl;
import com.zaxxer.hikari.HikariDataSource;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories(
    repositoryBaseClass = BaseRepositoryImpl.class,
    basePackages = "com.gexu.jpadamengdemo.repository",
    entityManagerFactoryRef = "dmEntityManager",
    transactionManagerRef = "dmTransactionManager"
)
public class Dm {

  private final Environment environment;

  @Autowired
  public Dm(Environment environment) {

    this.environment = environment;
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "app.datasource.dm")
  public DataSourceProperties dmDataSourceProperties() {

    return new DataSourceProperties();
  }

  @Bean
  @Primary
  @ConfigurationProperties(prefix = "app.datasource.dm.hikari")
  public DataSource dmDataSource() {

    return dmDataSourceProperties()
        .initializeDataSourceBuilder()
        .type(HikariDataSource.class)
        .build();
  }

  @Bean
  public LocalContainerEntityManagerFactoryBean dmEntityManager() {

    final var showSql = environment.getProperty("app.show-sql", "false");
    LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    em.setDataSource(dmDataSource());
    em.setPackagesToScan("com.gexu.jpadamengdemo.model");
    em.setJpaVendorAdapter(dmHibernateJpaVendorAdapter());
    em.setJpaPropertyMap(Map.of(
        "hibernate.hbm2ddl.auto", "none",
        "hibernate.show_sql", showSql
    ));
    return em;
  }

  @Bean
  public HibernateJpaVendorAdapter dmHibernateJpaVendorAdapter() {

    final var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.setGenerateDdl(true);
    hibernateJpaVendorAdapter.setDatabasePlatform(
        environment.getProperty("app.datasource.dm.dialect"));
    return hibernateJpaVendorAdapter;
  }

  @Bean
  public JpaTransactionManager dmTransactionManager() {

    JpaTransactionManager transactionManager = new JpaTransactionManager();
    transactionManager.setEntityManagerFactory(dmEntityManager().getObject());
    return transactionManager;
  }

  @Bean
  @Primary
  public HibernateJpaVendorAdapter bizHibernateJpaVendorAdapter() {

    final var hibernateJpaVendorAdapter = new HibernateJpaVendorAdapter();
    hibernateJpaVendorAdapter.setShowSql(true);
    hibernateJpaVendorAdapter.setGenerateDdl(true);
    hibernateJpaVendorAdapter.setDatabasePlatform(
        environment.getProperty("app.datasource.dm.dialect"));
    return hibernateJpaVendorAdapter;
  }
}
