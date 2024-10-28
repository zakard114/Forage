package com.wellsfargo.counselor.config;

import com.wellsfargo.counselor.dto.ClientDTO;
import com.wellsfargo.counselor.dto.PortfolioDTO;
import com.wellsfargo.counselor.dto.SecurityDTO;
import com.wellsfargo.counselor.entity.Client;
import com.wellsfargo.counselor.entity.Portfolio;
import com.wellsfargo.counselor.entity.Security;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public ModelMapper getMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config
                        .Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.STRICT);

        // Mapping from Client to ClientDTO in createClient method of ClientService.java
        // * Due to the mismatch between the advisorId field in the DTO and the entity, explicitly mapping advisorId
        //   from Advisor in Client -> ClientDTO mapping.
        // * The typeMap method returns a TypeMap object that allows defining mapping rules between two classes
        modelMapper.typeMap(Client.class, ClientDTO.class).addMappings(mapper -> {
            mapper.map(client -> client.getAdvisor().getAdvisorId(), ClientDTO::setAdvisorId);
        });

        // Mapping from Portfolio to PortfolioDTO
        modelMapper.typeMap(Portfolio.class, PortfolioDTO.class).addMappings(mapper -> {
            mapper.map(portfolio -> portfolio.getClient().getClientId(), PortfolioDTO::setClientId);
        });

        // Mapping from Security to SecurityDTO
        modelMapper.typeMap(Security.class, SecurityDTO.class).addMappings(mapper -> {
            mapper.map(security -> security.getPortfolio().getPortfolioId(), SecurityDTO::setPortfolioId);
        });

        return modelMapper;
    }

//    @Bean
//    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
//        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
//        em.setDataSource(dataSource);
//        em.setPackagesToScan("com.wellsfargo.counselor.entity"); // 엔티티 패키지 경로
//
//        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
//        em.setJpaVendorAdapter(vendorAdapter);
//        em.setJpaProperties(hibernateProperties());
//
//        return em;
//    }

//    @Bean
//    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
//        return new JpaTransactionManager(entityManagerFactory);
//    }
//
//    private Properties hibernateProperties() {
//        Properties properties = new Properties();
//        properties.put("hibernate.hbm2ddl.auto", "update");
//        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
//        return properties;
//    }


    // Activate when necessary: @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
//    @Bean
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName("org.h2.Driver"); // Driver class
//        dataSource.setUrl("jdbc:h2:mem:testdb"); // H2 memory database URL
//        dataSource.setUsername("sa");
//        dataSource.setPassword("");
//        return dataSource;
//    }
}
