package com.example.rabbitmq.producer;

import com.example.rabbitmq.utils.ConnectionUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @author fwx
 * @date 2022/3/18
 * 测试发布订阅模式生产者
 */
public class Send1 {

    private final static String EXCHANGE_NAME = "test_fanout_exchange";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = ConnectionUtil.getConnection();

            channel = connection.createChannel();
            //声明exchange，指定类型为fanout
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);

            //消息内容
            String msg = "注册成功";
            //发布消息到exchange
            channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
            System.out.println("[生产者] Sent '"+msg+"'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }finally {
            ConnectionUtil.closeChannel(channel);
            ConnectionUtil.closeConn(connection);
        }

    }
}
