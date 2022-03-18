package com.example.rabbitmq.utils;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

/**
 * @author fwx
 * @date 2022/3/15
 */
public class ConnectionUtil {

    /**
     * 建立与RabbitMQ的连接
     * @return Connection
     * @throws IOException
     * @throws TimeoutException
     */
    public static Connection getConnection() throws IOException, TimeoutException {
        //定义连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置服务地址
        factory.setHost("localhost");
        //client端通信端口
        factory.setPort(5672);
        //设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
        factory.setVirtualHost("fwx");
        //设置账号信息，用户名、密码
        factory.setUsername("fwx");
        factory.setPassword("123456");
        // 通过工厂获取连接
        return  factory.newConnection();
    }

    public static void closeConn(Connection connection){
        if (Objects.nonNull(connection)){
            try {
                connection.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeChannel(Channel channel){
        if (Objects.nonNull(channel)){
            try {
                channel.close();
            } catch (IOException | TimeoutException e) {
                e.printStackTrace();
            }
        }
    }

}
