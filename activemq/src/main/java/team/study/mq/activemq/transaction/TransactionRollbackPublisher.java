package team.study.mq.activemq.transaction;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.simple.HelloWorldPublisher;
import team.study.mq.activemq.util.MQCloseUtils;

/**
 * 测试事务型生产者，事务回滚后消息不会发送到AMQ服务器
 * Created by gyfeng on 17-1-16.
 */
public class TransactionRollbackPublisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldPublisher.class);

    private TransactionRollbackPublisher() {
    }

    public static void main(String[] args) throws JMSException, InterruptedException {
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
            // 创建会话，该会话中的消息都使用自动确认机制，使用事务型MQ
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            Queue helloWorldQueue = session.createQueue("transaction-test-queue");
            // 消息发送者
            producer = session.createProducer(helloWorldQueue);

            // 循环发送消息
            for (int i = 0; i < 100; i++) {
                LOGGER.info("send message:[transaction-test-msg:{}]", i);
                producer.send(session.createTextMessage("transaction-test-msg:" + i));
                Thread.sleep(100);
            }
            // 当这里回滚之后，消息不会发送到AMQ
            session.rollback();
        } finally {
            // 关闭连接
            MQCloseUtils.closeConnection(connection, session, producer);
        }
    }
}
