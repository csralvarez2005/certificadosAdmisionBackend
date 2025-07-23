package certificadosAdmisionBackend.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        basePackages = "certificadosAdmisionBackend.repository.postgres",
        entityManagerFactoryRef = "postgresEntityManagerFactory",
        transactionManagerRef = "transactionManager" // <- Aquí usamos el nombre por defecto
)
public class PostgresConfig {

    private final Environment env;

    public PostgresConfig(Environment env) {
        this.env = env;
    }

    @Bean(name = "postgresDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(env.getProperty("spring.datasource.postgres.driver-class-name"))
                .url(env.getProperty("spring.datasource.postgres.url"))
                .username(env.getProperty("spring.datasource.postgres.username"))
                .password(env.getProperty("spring.datasource.postgres.password"))
                .build();
    }

    @Bean(name = "postgresEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Qualifier("postgresDataSource") DataSource dataSource) {

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setDatabase(Database.POSTGRESQL);
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
        factory.setDataSource(dataSource);
        factory.setPackagesToScan("certificadosAdmisionBackend.model.postgres");
        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setJpaPropertyMap(properties);
        return factory;
    }

    // Renombrado a transactionManager y marcado como Primary
    @Bean(name = "transactionManager")
    @Primary
    public PlatformTransactionManager transactionManager(
            @Qualifier("postgresEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}


