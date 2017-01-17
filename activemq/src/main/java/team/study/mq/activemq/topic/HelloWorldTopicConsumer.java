package team.study.mq.activemq.topic;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import team.study.mq.activemq.util.MQCloseUtils;
import team.study.mq.activemq.util.MessageProcessor;

/**
 * AMQ主题Topic测试，消息订阅者
 * Created by gyfeng on 17-1-17.
 */
public class HelloWorldTopicConsumer {

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
            consumer.setMessageListener(MessageProcessor::print);
            Thread.sleep(15000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }
}
