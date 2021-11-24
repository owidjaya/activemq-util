package com.util.oscar;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;

import javax.jms.ConnectionFactory;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;

@Configuration
public class ToolConfiguration{  
    // From Broker
    @Value("${transfer.from.broker-url}")
    public String fromBrokerUrl;
    @Value("${transfer.from.username}")
    public String fromUsername;
    @Value("${transfer.from.password}")
    public String fromPassword;

    // To Broker
    @Value("${transfer.to.broker-url}")
    public String toBrokerUrl;
    @Value("${transfer.to.username}")
    public String toUsername;
    @Value("${transfer.to.password}")
    public String toPassword;

    @Bean
    @Primary
    public ConnectionFactory fromJmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(fromBrokerUrl);
        connectionFactory.setUserName(fromUsername);
        connectionFactory.setPassword(fromPassword);
        return connectionFactory;
    }

    @Bean
    public QueueConnectionFactory toJmsConnectionFactory() {
        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(toBrokerUrl);
        connectionFactory.setUserName(toUsername);
        connectionFactory.setPassword(toPassword);
        return connectionFactory;
    }

    @Bean
    public JmsComponent fromJmsComponent() {
        var jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(fromJmsConnectionFactory());
        jmsComponent.setAcknowledgementMode(Session.AUTO_ACKNOWLEDGE);
        return jmsComponent;
    }

    @Bean
    public JmsComponent toJmsTemplate() {
        var jmsComponent = new JmsComponent();
        jmsComponent.setConnectionFactory(toJmsConnectionFactory());
        jmsComponent.setAcknowledgementMode(Session.AUTO_ACKNOWLEDGE);
        return jmsComponent;
    }

    @Bean
    public JmsListenerContainerFactory<?> fromJmsListenerContainerFactory(
            @Qualifier("fromJmsConnectionFactory") ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> toJmsListenerContainerFactory(
            @Qualifier("toJmsConnectionFactory") ConnectionFactory connectionFactory,
            DefaultJmsListenerContainerFactoryConfigurer configurer) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory);
        return factory;
    }
}