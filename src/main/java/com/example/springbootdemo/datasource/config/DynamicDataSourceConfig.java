package com.example.springbootdemo.datasource.config;


import com.example.springbootdemo.datasource.DynamicDataSource;
import com.example.springbootdemo.datasource.enums.DataSourceTypeEnum;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.util.HashMap;
import java.util.Map;

@Configuration
@MapperScan(basePackages = "com.example.springbootdemo.dao")
public class DynamicDataSourceConfig implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {//,ApplicationContextAware
    private static final String CUSTOM_CONFIG = "db.config-prefix";

    private String dbConfigPrefix;

    private Environment environment;

    private Map<Object, Object> targetDataSources;


    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Object defaultTargetDataSource = this.targetDataSources.getOrDefault(DataSourceTypeEnum.getDefaultDataSourceType().getFullName(this.dbConfigPrefix), null);
        dynamicDataSource.setDefaultTargetDataSource(defaultTargetDataSource);
        dynamicDataSource.setTargetDataSources(this.targetDataSources);
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource dynamicDataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dynamicDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/**/*.xml"));
        return bean.getObject();
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dynamicDataSource) {
        return new DataSourceTransactionManager(dynamicDataSource);
    }


    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry beanDefinitionRegistry) throws BeansException {
        DataSourceTypeEnum.getDataSourceTypes().forEach(dataSourceTypeEnum -> {
            //创建数据源，并注册到IOC容易中
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class);
            if (dataSourceTypeEnum == DataSourceTypeEnum.getDefaultDataSourceType()) {
                builder.getBeanDefinition().setPrimary(true);
            }
            beanDefinitionRegistry.registerBeanDefinition(dataSourceTypeEnum.getFullName(dbConfigPrefix), builder.getBeanDefinition());
        });

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
        Map<Object, Object> dataSourcesMap = new HashMap<>();
        DataSourceTypeEnum.getDataSourceTypes().forEach(dataSourceTypeEnum -> {
            //绑定配置
            String fullName = dataSourceTypeEnum.getFullName(this.dbConfigPrefix);
            Object dataSource = configurableListableBeanFactory.getBean(fullName);
            Binder.get(environment).bind(fullName, Bindable.ofInstance(dataSource));
            dataSourcesMap.put(dataSourceTypeEnum.getName(), dataSource);
        });
        this.targetDataSources = dataSourcesMap;
    }


    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.dbConfigPrefix = environment.getProperty(CUSTOM_CONFIG);
    }

}
