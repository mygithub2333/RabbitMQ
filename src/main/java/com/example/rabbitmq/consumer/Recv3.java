package com.example.rabbitmq.consumer;

import com.example.rabbitmq.utils.ConnectionUtil;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author fwx
 * @date 2022/3/18
 */
public class Recv3 {

    private final static String QUEUE_NAME = "fanout_exchange_queue_sms";//短信队列

    private final static String EXCHANGE_NAME = "test_fanout_exchange";

    public static void main(String[] args) {
        try {
            Connection connection =ConnectionUtil.getConnection();
            Channel channel = connection.createChannel();
            //声明队列
            channel.queueDeclare(QUEUE_NAME,false,false,false,null);
            //绑定队列到交换机
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
            //定义队列的消费者
            DefaultConsumer consumer = new DefaultConsumer(channel){
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    //body即消息体
                    String msg = new String(body);
                    System.out.println(" [短信服务] received : " + msg + "!");
                }
            };
            //监听队列，自动返回完成
            channel.basicConsume(QUEUE_NAME,true,consumer);
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }
}
