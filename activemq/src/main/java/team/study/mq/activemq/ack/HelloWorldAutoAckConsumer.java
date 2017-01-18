package team.study.mq.activemq.ack;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import team.study.mq.activemq.util.MQCloseUtils;
import team.study.mq.activemq.util.MessageProcessor;
import team.study.mq.activemq.util.SendMsgUtils;

/**
 * 自动确认，AUTO_ACK Consumer
 * Created by gyfeng on 17-1-18.
 */
public class HelloWorldAutoAckConsumer {
    /** 队列名称 */
    private static final String AUTO_ACK_QUEUE_NAME = "hello-world-autoAck";

    private HelloWorldAutoAckConsumer() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        sendMsg();
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
            Queue helloWorldQueue = session.createQueue(AUTO_ACK_QUEUE_NAME);
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            consumer.setMessageListener((Message message) -> {
                MessageProcessor.print(message);
                // 模拟发生异常情况，看AMQ是否会重复发消息
                if (Math.random() > 0.5) {
                    throw new RuntimeException();
                }
            });
            Thread.sleep(10000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }

    private static void sendMsg() throws JMSException, InterruptedException {
        SendMsgUtils.sendQueueMessage(AUTO_ACK_QUEUE_NAME, 10, 0);
    }
}
