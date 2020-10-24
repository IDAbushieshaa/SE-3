package liverpool.dissertation.SE3.config;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
		basePackages = "liverpool.dissertation.SE3.repository.local",
		entityManagerFactoryRef = "localDBEntityManager",
		transactionManagerRef = "localDBTransactionManager")
public class LocalDBConfiguration {
	
	@Bean("SecondDS")
	@ConfigurationProperties(prefix = "spring.second-datasource")
	public DriverManagerDataSource localDBDataSource() {
		DriverManagerDataSource ds = new DriverManagerDataSource();
		ds.setDriverClassName(env.getProperty("spring.second-datasource.driverClassName"));
		ds.setUrl(env.getProperty("spring.second-datasource.jdbcUrl"));
		ds.setUsername(env.getProperty("spring.second-datasource.username"));
		ds.setPassword(env.getProperty("spring.second-datasource.password"));
		return ds;
	}
	
	@Autowired
	private Environment env;


	@Bean
	public LocalContainerEntityManagerFactoryBean localDBEntityManager() {
    	LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
    	em.setDataSource(localDBDataSource());
    	em.setPackagesToScan(new String[] { "liverpool.dissertation.SE3.entity.local" });
    	HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    	em.setJpaVendorAdapter(vendorAdapter);
    	HashMap<String, Object> properties = new HashMap<>();
    	properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
    	properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
    	em.setJpaPropertyMap(properties);
    	return em;
	}

	@Bean
	public PlatformTransactionManager localDBTransactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(localDBEntityManager().getObject());
		return transactionManager;
	}

}
