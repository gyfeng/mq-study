package team.study.mq.activemq.ack;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import team.study.mq.activemq.util.MQCloseUtils;
import team.study.mq.activemq.util.MessageProcessor;
import team.study.mq.activemq.util.SendMsgUtils;

/**
 * AUTO_ACK预加载
 * Created by gyfeng on 17-1-18.
 */
public class HelloWorldAutoAckPrefetchConsumer {

    /** 队列名称 */
    private static final String AUTO_ACK_QUEUE_NAME = "hello-world-optimizeAck";

    private HelloWorldAutoAckPrefetchConsumer() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        sendMsg();
        // 构造 ConnectionFactory 对象，连接本地的activeMQ，默认打开61616端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616?jms.optimizeAcknowledge=true&jms.optimizeAcknowledgeTimeOut=15000");
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
            consumer.setMessageListener(MessageProcessor::print);
            Thread.sleep(1000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }

    private static void sendMsg() throws JMSException, InterruptedException {
        SendMsgUtils.sendQueueMessage(AUTO_ACK_QUEUE_NAME, 100, 10);
    }
}
