package team.study.mq.activemq.util;

import javax.jms.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MQ关闭工具类
 * Created by gyfeng on 17-1-16.
 */
public class MQCloseUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(MQCloseUtils.class);

    private MQCloseUtils() {
    }

    /**
     * 关闭MQ连接，若关闭连接失败，不会抛出异常。会在日志中打印warn级别的日志
     *
     * @param connection MQ连接对象
     * @param session    MQ Session对象
     * @param consumer   MQ消息消费者
     */
    public static void closeConnection(Connection connection, Session session, MessageConsumer consumer) {
        try {
            if (consumer != null) {
                consumer.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close consumer error", e);
        }
        try {
            if (session != null) {
                session.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close session error", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close connection error", e);
        }
    }

    /**
     * 关闭MQ连接，若关闭连接失败，不会抛出异常。会在日志中打印warn级别的日志
     *
     * @param connection MQ连接对象
     * @param session    MQ Session对象
     * @param producer   MQ发布对象
     */
    public static void closeConnection(Connection connection, Session session, MessageProducer producer) {
        try {
            if (producer != null) {
                producer.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close producer error", e);
        }
        try {
            if (session != null) {
                session.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close session error", e);
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (JMSException e) {
            LOGGER.warn("close connection error", e);
        }
    }
}
