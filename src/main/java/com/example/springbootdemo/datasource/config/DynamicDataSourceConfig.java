package com.example.springbootdemo.datasource.config;


import com.alibaba.druid.pool.DruidDataSource;
import com.example.springbootdemo.datasource.DynamicDataSource;
import com.example.springbootdemo.datasource.enums.DataSourceTypeEnum;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.context.properties.bind.BindHandler;
import org.springframework.boot.context.properties.bind.BindResult;
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
public class DynamicDataSourceConfig implements BeanDefinitionRegistryPostProcessor, ApplicationContextAware, EnvironmentAware {
    private static final String DB_CONFIG_PREFIX = "spring.db.";

    private ApplicationContext applicationContext;

    private Environment environment;


    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>();

        DataSourceTypeEnum.getDataSourceTypes().forEach(x -> {
            DruidDataSource dataSource = applicationContext.getBean(DB_CONFIG_PREFIX + x.getName(), DruidDataSource.class);
            //设置默认数据库
            if (x == DataSourceTypeEnum.getDefaultDataSourceType()) {
                dynamicDataSource.setDefaultTargetDataSource(dataSource);
            }
            targetDataSources.put(x.getName(), dataSource);
        });

        dynamicDataSource.setTargetDataSources(targetDataSources);
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
            BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(DruidDataSource.class);
            if (dataSourceTypeEnum == DataSourceTypeEnum.getDefaultDataSourceType()) {
                builder.getBeanDefinition().setPrimary(true);
            }
            String dsName = DB_CONFIG_PREFIX + dataSourceTypeEnum.getName();
            beanDefinitionRegistry.registerBeanDefinition(dsName, builder.getBeanDefinition());

            DruidDataSource dataSource = applicationContext.getBean(dsName, DruidDataSource.class);


        });
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private Map<String, PropertyValues> dataSourceMap = new HashMap<>();

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
