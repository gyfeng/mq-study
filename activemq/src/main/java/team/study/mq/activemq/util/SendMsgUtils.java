package team.study.mq.activemq.util;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 发送消息的工具类
 * Created by gyfeng on 17-1-17.
 */
public class SendMsgUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMsgUtils.class);

    private SendMsgUtils() {
    }

    /**
     * 发送消息到MQ
     *
     * @param queueName   队列名称
     * @param msgCount    要发送的消息条数
     * @param sleepMillis 每条消息的间隔时间
     * @throws JMSException         发送消息异常
     * @throws InterruptedException 异常
     */
    public static void sendQueueMessage(String queueName, int msgCount, long sleepMillis) throws JMSException, InterruptedException {
        // 构造 ConnectionFactory 对象，连接本地的activeMQ，默认打开61616端口
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        // 通过 ConnectionFactory 对象创建连接
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        try {
            connection = connectionFactory.createConnection();
            // 开始连接
            connection.start();
            // 创建会话，该会话中的消息都使用自动确认机制
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            // 消息发送者
            Queue helloWorldQueue = session.createQueue(queueName);
            producer = session.createProducer(helloWorldQueue);

            // 循环发送消息
            for (int i = 0; i < msgCount; i++) {
                LOGGER.info("send message:[hello-world:{}]", i);
                producer.send(session.createTextMessage("hello-world:" + i));
                if (sleepMillis > 0) {
                    Thread.sleep(sleepMillis);
                }
            }
        } finally {
            // 关闭连接
            MQCloseUtils.closeConnection(connection, session, producer);
        }
    }
}
