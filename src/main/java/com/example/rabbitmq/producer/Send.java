package com.example.rabbitmq.producer;

import com.example.rabbitmq.utils.ConnectionUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

/**
 * @author fwx
 * @date 2022/3/16
 * 生产者（生产多个消息）
 */
public class Send {
    private final static String QUEUE_NAME = "test_work_queue";

    public static void main(String[] argv) throws Exception {
        // 获取到连接
        Connection connection = ConnectionUtil.getConnection();
        // 获取通道
        Channel channel = connection.createChannel();
        channel.confirmSelect();
        // 声明队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 循环发布任务
        int sum = 0;
        for (int i = 0; i < 50; i++) {
            // 消息内容
            String message = "task .. " + i;
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
            Thread.sleep(i * 2);
            sum=sum+1;
        }
        channel.waitForConfirmsOrDie(); //直到所有信息都发布，只要有一个未确认就会IOException
        System.out.println("全部执行完成");
        System.out.println("总计发的消息数："+sum);
        // 关闭通道和连接
        channel.close();
        connection.close();


    }
}
