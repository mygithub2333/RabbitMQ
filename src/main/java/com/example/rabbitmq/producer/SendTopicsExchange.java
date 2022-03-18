package com.example.rabbitmq.producer;

import com.example.rabbitmq.utils.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author fwx
 * @date 2022/3/18
 * Topics 通配符模式（交换机类型：topics）
 */
public class SendTopicsExchange {
    private final static String EXCHANGE_NAME = "test_topic_exchange";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        // 声明exchange，指定类型为topic
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        // 消息内容
        //String message = "这是一只行动迅速的橙色的兔子";
        // 发送消息，并且指定routing key为：quick.orange.rabbit
        //channel.basicPublish(EXCHANGE_NAME, "quick.orange.rabbit", null, message.getBytes());
        //String message = "这是一只懶惰的橙色的大象";
        // 发送消息，并且指定routing key为：lazy.orange.elephant
        //channel.basicPublish(EXCHANGE_NAME, "lazy.orange.elephant", null, message.getBytes());
        //String message = "这是一只行动迅速的橙色的狐狸";
        // 发送消息，并且指定routing key为：lazy.orange.elephant
        //channel.basicPublish(EXCHANGE_NAME, "quick.orange.fox", null, message.getBytes());
        String message = "这是一只懶惰的粉色的兔子";
        // 发送消息，并且指定routing key为：lazy.orange.elephant
        channel.basicPublish(EXCHANGE_NAME, "lazy.pink.rabbit", null, message.getBytes());
        System.out.println(" [动物描述：] Sent '" + message + "'");

        channel.close();
        connection.close();
    }
}
