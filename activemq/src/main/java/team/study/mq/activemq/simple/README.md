## 简单的MQ测试

使用简单的MQ API进行MQ生产者和消费者的测试。

## 学习记录
- 在MQ中，需要先创建连接，再通过连接创建会话Session，然后再通过Session创建具体的生产者和消费者；
- 在创建生产者 OR 消费者时需要指定 Destination，但是在发送/消费的时候也可指定具体的 Destination，猜想在创建的时候传递的 Destination 是默认值，若在发送/消费时不指定则使用默认的Destination (这在看源码时得到确认)；
- 在创建会话Session的时候需要指定是否是事务型Session和ACK机制，在该Demo中使用非事务型，自动ACK机制；
- MQ消息费者可以使用同步消费和异步消息两种模式，本例使用同步消费模式。
- 在使用 consumer#receive 方法时，可指定一个超时时间，若在超时时间内没有数据返回，则超时时返回null，这点在编程的时候需要注意；
- 消息类型有TextMessage，StreamMessage，MapMessage，ObjectMessage，ByteMessage众多类型可供选择。

