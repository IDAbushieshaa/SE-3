package liverpool.dissertation.SE3.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@PropertySource({"classpath:application.properties"})
@EnableJpaRepositories(
		basePackages = "liverpool.dissertation.SE3.repository.remote",
		entityManagerFactoryRef = "remoteDBEntityManager",
		transactionManagerRef = "remoteDBTransactionManager")
public class RemoteDBConfiguration {
	
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	public DriverManagerDataSource remoteDBDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
		ds.setUrl(env.getProperty("spring.datasource.url"));
		ds.setUsername(env.getProperty("spring.datasource.username"));
		ds.setPassword(env.getProperty("spring.datasource.password"));
		return ds;
	}
	
	@Autowired
	private Environment env;
	

	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean remoteDBEntityManager() {
    	LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    	em.setDataSource(remoteDBDataSource());
    	em.setPackagesToScan(new String[] { "liverpool.dissertation.SE3.entity.remote" });
    	HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    	em.setJpaVendorAdapter(vendorAdapter);
    	HashMap<String, Object> properties = new HashMap<>();
    	properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    	properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
    	em.setJpaPropertyMap(properties);
    	return em;
	}

	@Primary
	@Bean
	public PlatformTransactionManager remoteDBTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(remoteDBEntityManager().getObject());
		return transactionManager;
	}
	
}
