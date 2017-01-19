package team.study.mq.activemq.virtualtopics;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.util.MQCloseUtils;

/**
 * 虚拟主题消费者测试
 * Created by gyfeng on 17-1-19.
 */
public class HelloWorldVirtualTopicConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldVirtualTopicConsumer.class);

    private HelloWorldVirtualTopicConsumer() {
    }

    public static void main(String[] args) throws InterruptedException, JMSException {
        Thread thread1 = new Thread(() -> listenerQueue("A.VirtualTopic.HELLO_WORLD"));
        Thread thread2 = new Thread(() -> listenerQueue("A.VirtualTopic.HELLO_WORLD"));
        Thread thread3 = new Thread(() -> listenerQueue("B.VirtualTopic.HELLO_WORLD"));
        Thread thread4 = new Thread(() -> listenerQueue("B.VirtualTopic.HELLO_WORLD"));
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
    }

    private static void listenerQueue(String queueName) {
        try {
            listener(queueName);
        } catch (Exception e) {
            LOGGER.error("监听消息失败", e);
        }
    }

    private static void listener(String queueName) throws JMSException, InterruptedException {
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
            // 接收消息的队列
            Queue helloWorldQueue = session.createQueue(queueName);
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            String listenerName = Thread.currentThread().getName();
            consumer.setMessageListener((message) -> {
                try {
                    LOGGER.info("{} 从 {} 接收到消息 {}", listenerName, queueName, ((TextMessage) message).getText());
                } catch (JMSException e) {
                    LOGGER.error("处理消息失败", e);
                }
            });
            Thread.sleep(20000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }
}
