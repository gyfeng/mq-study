package team.study.mq.activemq.ack;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import team.study.mq.activemq.util.MQCloseUtils;
import team.study.mq.activemq.util.MessageProcessor;
import team.study.mq.activemq.util.SendMsgUtils;

/**
 * 发送消息用于对ACK机制的学习的测验
 * Created by gyfeng on 17-1-17.
 */
public class HelloWorldClientAckConsumer {

    private HelloWorldClientAckConsumer() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
        String queueName = "hello-world-ack";
        int sleepMillis = 0;
        SendMsgUtils.sendQueueMessage(queueName, sleepMillis);

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
            // 创建发送的目的MQ
            Queue helloWorldQueue = session.createQueue("hello-world");
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            consumer.setMessageListener(MessageProcessor::print);
            Thread.sleep(150000);
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }

}
