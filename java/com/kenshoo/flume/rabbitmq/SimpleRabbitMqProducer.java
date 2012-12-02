/*
* Copyright 2011 Kenshoo.com
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/  
package com.kenshoo.flume.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: sagyr
 * Date: 7/5/11
 * Time: 7:12 PM
 */
public class SimpleRabbitMqProducer implements QueuePublisher {
    private ConnectionFactory factory;
    private Connection conn;
    private Channel channel;
    public static final String EXCHANGE_NAME = "app.tracking.exchange";
    private static final Logger logger = LoggerFactory.getLogger(SimpleRabbitMqProducer.class);

    public SimpleRabbitMqProducer(String host, String userName, String password, String virtualHost) {
        factory = new ConnectionFactory();
        factory.setUsername(userName);
        factory.setPassword(password);
        factory.setVirtualHost(virtualHost);
        factory.setHost(host);
        factory.setPort(5672);
    }

    public void open() throws IOException {
        conn = factory.newConnection();
        channel = conn.createChannel();
        logger.debug("channel created successfully");
    }

    public void close() throws IOException {
        channel.close();
        conn.close();
    }

    public void publish(String routingKey, byte[] msg) throws IOException {
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true);
        channel.basicPublish(EXCHANGE_NAME, routingKey, MessageProperties.TEXT_PLAIN, msg);
        logger.debug("published successfully: {} with key: {}",new String(msg), routingKey );
    }
}
