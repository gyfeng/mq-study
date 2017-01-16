package team.study.mq.activemq.simple;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的测试avtiveMQ，使用队列发送hello world字符串
 * Created by gyfeng on 17-1-16.
 */
public class HelloWordPublisher {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWordPublisher.class);

    private HelloWordPublisher() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        // 构造 ConnectionFactory 对象，连接本地的activeMQ，默认打开61616端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 通过 ConnectionFactory 对象创建连接
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            // 开始连接
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue helloWorldQueue = session.createQueue("hello-world");
            producer = session.createProducer(helloWorldQueue);

            for (int i = 0; i < 100; i++) {
                ActiveMQTextMessage message = new ActiveMQTextMessage();
                message.setText("hello-world:" + i);
                LOGGER.info("send message:[hello-world:{}]", i);
                producer.send(message);
                Thread.sleep(1000);
            }
        } finally {
            try {
                if (producer != null) {
                    producer.close();
                }
            } catch (JMSException e) {
                LOGGER.warn("close producer error", e);
            }
            try {
                if (session != null) {
                    session.close();
                }
            } catch (JMSException e) {
                LOGGER.warn("close session error", e);
            }
            try {
                if (connection != null) {
                    connection.close();
                }
            } catch (JMSException e) {
                LOGGER.warn("close connection error", e);
            }
        }
    }
}
