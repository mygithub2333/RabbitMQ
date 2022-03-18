package com.example.rabbitmq.producer;

import com.example.rabbitmq.utils.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

/**
 * @author fwx
 * @date 2022/3/15
 * 生产者
 */
public class Producer {

    //队列名称
    private final static String QUEUE_NAME = "simple_queue";
    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            //1、获取连接
            connection = ConnectionUtil.getConnection();
            //2、从连接中创建通道，使用通道才能完成消息相关的操作
            channel = connection.createChannel();
            //3、声明（创建）队列
            //参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /**
             * 参数明细
             * 1、queue 队列名称
             * 2、durable 是否持久化，如果持久化，mq重启后队列还在
             * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
             * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
             * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             */
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            //4、消息内容
            String message ="Hello World!";
            //6、向指定的队列中发送消息
            //参数：String exchange, String routingKey, BasicProperties props, byte[] body
            /**
             * 参数明细：
             * 1、exchange，交换机，如果不指定将使用mq的默认交换机（设置为""）
             * 2、routingKey，路由key，交换机根据路由key来将消息转发到指定的队列，如果使用默认交换机，routingKey设置为队列的名称
             * 3、props，消息的属性
             * 4、body，消息内容
            */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("[x] Sent '"+message+"'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }finally {
            //关闭通道和连接(资源关闭最好用try-catch-finally语句处理)
            ConnectionUtil.closeChannel(channel);
            ConnectionUtil.closeConn(connection);
        }

        new Thread(() -> {
            Connection connection1 = null;
            try {
                Thread.sleep(10000);
                connection1 = ConnectionUtil.getConnection();
                Channel channel1 = connection1.createChannel();
                channel1.queueDeclare(QUEUE_NAME,false,false,false,null);
                DefaultConsumer consumer =  new DefaultConsumer(channel1){
                    @Override
                    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                        String exchange = envelope.getExchange();
                        long deliveryTag = envelope.getDeliveryTag();
                        String s = new String(body, StandardCharsets.UTF_8);
                        System.out.println("[x] received："+s+"!");
                        channel1.basicAck(envelope.getDeliveryTag(), false);
                    }
                };
                channel1.basicConsume(QUEUE_NAME,false,consumer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}
