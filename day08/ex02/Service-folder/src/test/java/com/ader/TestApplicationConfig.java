package test.java.com.ader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = { "com.ader.repositories", "com.ader.services" })
public class TestApplicationConfig {

    @Bean(name = "hikariDataSource")
    public HikariDataSource dataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setJdbcUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }

    @Bean(name = "driverManagerDataSource")
    public DriverManagerDataSource driverManagerDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:testdb");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        return dataSource;
    }
    
    @Bean
    public JdbcTemplate jdbcTemplate(HikariDataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    // This bean is used for managing database transactions in the test environment
    // It allows Spring to handle transactions automatically, ensuring data
    // consistency
    // during test execution, especially when using @Transactional annotation

    // We use DataSourceTransactionManager because we're working with a JDBC
    // DataSource
    // It coordinates transactions for a single JDBC DataSource
    @Bean
    public PlatformTransactionManager transactionManager(HikariDataSource dataSource) {

        return new DataSourceTransactionManager(dataSource);
    }
}
