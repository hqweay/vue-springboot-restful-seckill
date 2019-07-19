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

/**
 * @description: RabbitListener 设置监听的队列
 * Created by hqweay on 19-5-23 上午11:21
 */

// 使用 @RabbitListener 注解标记方法，当监听到队列中有消息时则会进行接收并处理
// @RabbitListener 标注在类上面表示当有收到消息的时候，就交给 @RabbitHandler 的方法处理，
// 具体使用哪个方法处理，根据 MessageConverter 转换后的参数类型
@Component
@RabbitListener(queues = "skillList")
public class Receiver {

  @Autowired
  private ProductService productService;

  /**
   *
   * @description:  秒杀逻辑
   * @param: [user]
   * @return: void
   */
  @RabbitHandler
  public void process(ArrayList<String> arrayList){

    // 尝试多线程执行
    // threadPool.executor(new Runnable({})
    // 消费者
    //todo 不过秒杀后 怎么通知呢?
    String phone = arrayList.get(0);
    Long seckillId = Long.valueOf(arrayList.get(1));
    String md5Code = arrayList.get(2);

    ErrorResult errorResult = null;
    try {
      productService.executeSkill(phone, seckillId, md5Code);
    } catch (SkillException e) {
      errorResult = new ErrorResult(e);
    }
//    return ResponseEntity.ok(errorResult);
  }


  private int ss = 100;

  @RabbitHandler
  public void process() {
    System.out.println("zhi");
    new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println(ss--);
      }
    }).start();
  }

}
