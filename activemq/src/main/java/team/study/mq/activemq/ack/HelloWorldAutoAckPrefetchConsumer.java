package team.study.mq.activemq.ack;

import java.util.concurrent.CountDownLatch;
import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.util.MQCloseUtils;
import team.study.mq.activemq.util.MessageProcessor;
import team.study.mq.activemq.util.SendMsgUtils;

/**
 * AUTO_ACK预加载
 * Created by gyfeng on 17-1-18.
 */
public class HelloWorldAutoAckPrefetchConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldAutoAckPrefetchConsumer.class);

    /** 队列名称 */
    private static final String AUTO_ACK_QUEUE_NAME = "hello-world-optimizeAck";
    public static final int MSG_COUNT = 100;

    private HelloWorldAutoAckPrefetchConsumer() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        sendMsg();
        // 构造 ConnectionFactory 对象，连接本地的activeMQ，默认打开61616端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.optimizeAcknowledge=true&jms.optimizeAcknowledgeTimeOut=2000");
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
            Queue helloWorldQueue = session.createQueue(AUTO_ACK_QUEUE_NAME + "?consumer.prefetchSize=15");
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            CountDownLatch countDownLatch = new CountDownLatch(MSG_COUNT);
            consumer.setMessageListener((Message message) -> {
                MessageProcessor.print(message);
                countDownLatch.countDown();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.error("发生异常", e);
                }
            });
            countDownLatch.await();
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }

    private static void sendMsg() throws JMSException, InterruptedException {
        SendMsgUtils.sendQueueMessage(AUTO_ACK_QUEUE_NAME, MSG_COUNT, 10);
    }
}
