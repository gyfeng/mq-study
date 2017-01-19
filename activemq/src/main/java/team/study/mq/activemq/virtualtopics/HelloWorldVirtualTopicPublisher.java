package team.study.mq.activemq.virtualtopics;

import javax.jms.JMSException;

import team.study.mq.activemq.util.SendMsgUtils;

/**
 * AMQ虚拟主题
 * Created by gyfeng on 17-1-19.
 */
public class HelloWorldVirtualTopicPublisher {
    private HelloWorldVirtualTopicPublisher() {
    }

    public static void main(String[] args) throws InterruptedException, JMSException {
        SendMsgUtils.sendTopicMessage("VirtualTopic.HELLO_WORLD", 10, 10);
    }
}
