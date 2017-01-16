package team.study.mq.activemq.transaction;

import javax.jms.*;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.study.mq.activemq.util.MQCloseUtils;

/**
 * 测试事务型消费者，事务提交之后才真正把消息从服务器中删除
 * Created by gyfeng on 17-1-16.
 */
public class TransactionCommitConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransactionCommitConsumer.class);

    private TransactionCommitConsumer() {
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
            session = connection.createSession(true, Session.AUTO_ACKNOWLEDGE);
            // 创建发送的目的MQ
            Queue helloWorldQueue = session.createQueue("transaction-test-queue");
            // 消息接收者
            consumer = session.createConsumer(helloWorldQueue);
            while (true) {
                Message message = consumer.receive(500);
                if (message == null) {
                    break;
                }
                if (message instanceof TextMessage) {
                    LOGGER.info("receive message:{}", ((TextMessage) message).getText());
                } else {
                    LOGGER.warn("receive message type not is TextMessage", message.getClass());
                }
            }
            // 消息还存在于AMQ服务器中，若不进行commit，则不会删除消息。再次打开Session进行消费时，还会重复消费
            session.commit();
        } finally {
            MQCloseUtils.closeConnection(connection, session, consumer);
        }
    }

}
