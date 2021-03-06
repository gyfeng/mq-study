package team.study.mq.activemq.ack;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.util.MQCloseUtils;
import team.study.mq.activemq.util.MessageProcessor;
import team.study.mq.activemq.util.SendMsgUtils;

/**
 * 发送消息用于对ACK机制的学习的测验
 * Created by gyfeng on 17-1-17.
 */
public class HelloWorldClientAckConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldClientAckConsumer.class);
    /** 队列名称 */
    private static final String CLIENT_ACK_QUEUE_NAME = "hello-world-clientAck";


    private HelloWorldClientAckConsumer() {
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
            session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
            // 接收消息的队列
            Queue helloWorldQueue = session.createQueue(CLIENT_ACK_QUEUE_NAME);
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            consumer.setMessageListener((Message message) -> {
                MessageProcessor.print(message);
                try {
                    message.acknowledge();
                } catch (JMSException e) {
                    LOGGER.error("ACK消息发生错误", e);
                }
            });
            Thread.sleep(10000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }

    private static void sendMsg() throws JMSException, InterruptedException {
        SendMsgUtils.sendQueueMessage(CLIENT_ACK_QUEUE_NAME, 1000, 0);
    }

}
