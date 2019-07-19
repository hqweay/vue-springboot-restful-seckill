package cn.hqweay.skill.mq;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @description:  发送指定队列和接受指定队列的名称要一致
 *                至于这里设置队列的名称,应该是高级使用中才需要用到
 *
 *                SpringBoot 整合 RabbitMQ : https://blog.csdn.net/lizc_lizc/article/details/80722090
 *                1.Direct模式 : 消息中的路由键（routing key）如果和 Binding 中的 binding key 一致， 交换器就将消息发到对应的队列中。路由键与队列名完全匹配
 *
 * Created by hqweay on 19-5-23 上午11:20
 */
@Configuration
public class MQConfig {

  @Bean
  public Queue mailQueue(){
    // 第一个参数是队列名字， 第二个参数是指是否持久化
    // return new Queue("hello", true);
    // 这个名字其实
    return new Queue("skillList");
  }

  @Bean
  public Queue helloQU(){
    return new Queue("hello");
  }


  @Bean("customContainerFactory")
  public SimpleRabbitListenerContainerFactory containerFactory(SimpleRabbitListenerContainerFactoryConfigurer configurer, ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConcurrentConsumers(10);  //设置线程数
    factory.setMaxConcurrentConsumers(10); //最大线程数
    configurer.configure(factory, connectionFactory);
    return factory;
  }

}
