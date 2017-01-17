package team.study.mq.activemq.util;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消费消息工具类
 * Created by gyfeng on 17-1-17.
 */
public class MessageProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageProcessor.class);

    private MessageProcessor() {
    }

    /**
     * 消费消息，使用普通打印
     *
     * @param message 消息对象
     */
    public static void print(Message message) {
        if (message instanceof TextMessage) {
            try {
                LOGGER.info("message:{}", ((TextMessage) message).getText());
            } catch (JMSException e) {
                LOGGER.error("消费数据出错", e);
            }
        } else {
            LOGGER.warn("message type not is TextMessage", message.getClass());
        }
    }
}
