package team.study.mq.activemq.topic;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.simple.HelloWorldPublisher;
import team.study.mq.activemq.util.MQCloseUtils;

/**
 * AMQ主题Topic测试，消息发布者
 * Created by gyfeng on 17-1-17.
 */
public class HelloWorldTopicPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldPublisher.class);

    private HelloWorldTopicPublisher() {
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
            // 创建会话，该会话中的消息都使用自动确认机制
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            Topic helloWorldTopic = session.createTopic("hello-world-topic");
            // 消息发送者
            producer = session.createProducer(helloWorldTopic);

            // 循环发送消息
            for (int i = 0; i < 100; i++) {
                ActiveMQTextMessage message = new ActiveMQTextMessage();
                message.setText("hello-world:" + i);
                LOGGER.info("send topic message:[hello-world:{}]", i);
                producer.send(message);
                Thread.sleep(100);
            }
        } finally {
            // 关闭连接
            MQCloseUtils.closeConnection(connection, session, producer);
        }
    }
}
