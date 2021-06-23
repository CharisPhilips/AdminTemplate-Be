package com.kilcote.configuration;

import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.kilcote.common.Global;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.kilcote.configuration" })
@PropertySource("classpath:persistent.properties")
@EnableJpaRepositories(basePackages = {"com.kilcote.dao"})
public class PersistentConfig {

	@Autowired
	private Environment environment;

	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(environment.getProperty("jdbc.driverClassName"));
		dataSource.setUrl(environment.getProperty("jdbc.url"));
		dataSource.setUsername(environment.getProperty("jdbc.user"));
		dataSource.setPassword(environment.getProperty("jdbc.pass"));
		return dataSource;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setDataSource(dataSource());
		em.setPackagesToScan(new String[] { "com.kilcote.entity" });
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setJpaProperties(getHibernateProperties());
		return em;
	}

	@Bean
	public JavaMailSender getMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

		mailSender.setHost(environment.getProperty("spring.mail.host"));
		mailSender.setPort(Integer.valueOf(environment.getProperty("spring.mail.port")));
		Global.g_mailForVerification = environment.getProperty("spring.mail.username");
		mailSender.setUsername(Global.g_mailForVerification);
		String decodedPassword = new String(Base64.decodeBase64(environment.getProperty("spring.mail.password")));
//		String decodedPassword = environment.getProperty("spring.mail.password");
		mailSender.setPassword(decodedPassword);

		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.starttls.enable", "true");
		javaMailProperties.put("mail.smtp.auth", "true");
		javaMailProperties.put("mail.transport.protocol", "smtp");

		mailSender.setJavaMailProperties(javaMailProperties);
		return mailSender;
	}

	private Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
		properties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
		properties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
		setAdditionalProperties(properties);
		return properties;        
	}
	
	private void setAdditionalProperties(Properties properties) {
		Global.FRONTEND_DOMAIN_URL = environment.getProperty("frontend.domain.url");
		Global.BACKEND_DOMAIN_URL = environment.getProperty("backend.domain.url");
		Global.PATH_AVATAR_ROOT = environment.getProperty("avatar.downloadpath");
//        Global.STRIPE_SECRET_KEY = environment.getProperty("stripe.secretkey");
//        Global.STRIPE_PUBLIC_KEY = environment.getProperty("stripe_publickey");
//        Global.PATH_PDF_ROOT = environment.getProperty("pdf.downloadpath");
	}

	@Bean
	public JpaTransactionManager transactionManager(final EntityManagerFactory entityManagerFactory) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory);
		return transactionManager;
	}
}