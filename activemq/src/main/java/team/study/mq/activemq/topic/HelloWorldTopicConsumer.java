package team.study.mq.activemq.topic;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.simple.HelloWorldConsumer;
import team.study.mq.activemq.util.MQCloseUtils;

/**
 * AMQ主题Topic测试，消息订阅者
 * Created by gyfeng on 17-1-17.
 */
public class HelloWorldTopicConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldConsumer.class);

    private HelloWorldTopicConsumer() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        // 构造 ConnectionFactory 对象，连接本地的activeMQ，默认打开61616端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 通过 ConnectionFactory 对象创建连接
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            connection = connectionFactory.createConnection();
            connection.setClientID("client1");
            // 开始连接
            connection.start();
            // 创建会话，该会话中的消息都使用自动确认机制
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            Topic helloWorldTopic = session.createTopic("hello-world-topic");
            // 消息接收者
            consumer = session.createDurableSubscriber(helloWorldTopic, "local-subscriber");
            consumer.setMessageListener((Message message) -> {
                if (message instanceof TextMessage) {
                    try {
                        LOGGER.info("receive topic message:{}", ((TextMessage) message).getText());
                    } catch (JMSException e) {
                        LOGGER.error("处理消息错误", e);
                    }
                } else {
                    LOGGER.warn("receive topic message type not is TextMessage", message.getClass());
                }
            });
            Thread.sleep(15000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }
}
