package team.study.mq.activemq.simple;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 简单的测试avtiveMQ，使用队列接收hello world字符串
 * Created by gyfeng on 17-1-16.
 */
public class HelloWorldConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldConsumer.class);

    private HelloWorldConsumer() {
    }

    public static void main(String[] args) throws JMSException {
        // 构造 ConnectionFactory 对象，连接本地的activeMQ，默认打开61616端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 通过 ConnectionFactory 对象创建连接
        Connection connection = null;
        Session session = null;
        MessageConsumer consumer = null;
        try {
            connection = connectionFactory.createConnection();
            // 开始连接
            connection.start();
            // 创建会话，该会话中的消息都使用自动确认机制
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            Queue helloWorldQueue = session.createQueue("hello-world");
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            while (true) {
                Message message = consumer.receive(2000);
                if (message == null) {
                    break;
                }
                if (message instanceof TextMessage) {
                    LOGGER.info("receive message:{}", ((TextMessage) message).getText());
                } else {
                    LOGGER.warn("receive message type not is TextMessage", message.getClass());
                }
            }
        } finally {
            closeConnection(connection, session, consumer);
        }
    }

    /**
     * 关闭MQ连接，若关闭连接失败，不会抛出异常。会在日志中打印warn级别的日志
     *
     * @param connection MQ连接对象
     * @param session    MQ Session对象
     * @param consumer   MQ消息消费者
     */
    private static void closeConnection(Connection connection, Session session, MessageConsumer consumer) {
        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close consumer error", e);
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
