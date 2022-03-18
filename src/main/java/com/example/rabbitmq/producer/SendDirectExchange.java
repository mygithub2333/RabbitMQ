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
 * Routing 路由模型（交换机类型：direct）
 */
public class SendDirectExchange {

    private final static String EXCHANGE_NAME = "test_direct_exchange";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = ConnectionUtil.getConnection();
            channel = connection.createChannel();
            //声明exchange,指定类型为direct
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            //消息内容
            String msg = "注册成功！请短信回复【T】退订";
            //发送消息，并且指定routing key为：sms,只有短信服务才能接收到消息
            channel.basicPublish(EXCHANGE_NAME,"sms",null,msg.getBytes());
            System.out.println("[x] Sent '"+msg+"'");
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }finally {
            ConnectionUtil.closeChannel(channel);
            ConnectionUtil.closeConn(connection);
        }
    }
}
