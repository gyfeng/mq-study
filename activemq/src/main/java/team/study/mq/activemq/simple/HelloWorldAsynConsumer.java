package team.study.mq.activemq.simple;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.util.MQCloseUtils;

/**
 * 异步地消费队列
 * Created by gyfeng on 17-1-16.
 */
public class HelloWorldAsynConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldConsumer.class);

    private HelloWorldAsynConsumer() {
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
            // 开始连接
            connection.start();
            // 创建会话，该会话中的消息都使用自动确认机制
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            Queue helloWorldQueue = session.createQueue("hello-world");
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            consumer.setMessageListener(message -> {
                if (message instanceof TextMessage) {
                    try {
                        LOGGER.info("receive message:{}", ((TextMessage) message).getText());
                    } catch (JMSException e) {
                        LOGGER.error("消费数据出错", e);
                    }
                } else {
                    LOGGER.warn("receive message type not is TextMessage", message.getClass());
                }
            });
            Thread.sleep(150000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }
}
