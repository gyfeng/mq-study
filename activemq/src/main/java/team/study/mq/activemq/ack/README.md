## AMQ 的 ACK机制

## 问题
- 生产者是不是没有ACK机制，但如果没有的话为何在创建Session的时候还需要转递ACK参数？（这点还未确认，但是看代码也没有发现问题）

## 学习记录
- Session和Consumer接口中没有相应的ack方法，只有对应的AMQ实现类中有
- Message对象中有ack方法，不过注意的是该ack方法会确认session中所有未确认的消息，而不是单条消息。
- CLIENT_ACK模式需要Consumer端手工进行消息的ACK，在不进行ACK的情况下。下次再连接上AMQ服务器的时候会再次收到消息
- AUTO_ACK模式下，不需要Consumer手工进行消息的ACK，当在Listener中抛出异常时，会以送ACK进行确认，否没有异常发生则判断为处理成功

## 参考资料
- [ActiveMQ讯息传送机制以及ACK机制](http://blog.csdn.net/lulongzhou_llz/article/details/42270113)
