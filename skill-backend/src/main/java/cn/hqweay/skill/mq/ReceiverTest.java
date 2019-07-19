package cn.hqweay.skill.mq;

import cn.hqweay.skill.dto.ErrorResult;
import cn.hqweay.skill.exception.SkillException;
import cn.hqweay.skill.service.interfaces.ProductService;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: RabbitListener 设置监听的队列
 * Created by hqweay on 19-5-23 上午11:21
 */

// 使用 @RabbitListener 注解标记方法，当监听到队列中有消息时则会进行接收并处理
// @RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，
// 具体使用哪个方法处理，根据 MessageConverter 转换后的参数类型
  // , containerFactory = "customContainerFactory"
@Component
@RabbitListener(queues = "hello")
public class ReceiverTest {

  ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 200, 3100,
          TimeUnit.SECONDS,
          new ArrayBlockingQueue<Runnable>(100),
          new ThreadPoolExecutor.DiscardOldestPolicy());

  private AtomicInteger ss = new AtomicInteger(0);
  private int temp = 0;

  @RabbitHandler
  public void process(String msg) {
    System.out.println(++temp);
//    System.out.println(ss.addAndGet(1));
  }

}
