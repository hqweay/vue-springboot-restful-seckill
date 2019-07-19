package cn.hqweay.skill.mq;


import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;

/**
 * @description: TODO
 * Created by hqweay on 19-5-23 上午11:19
 */
@Component
public class Sender {

  //注入 AmqpTemplate，然后利用AmqpTemplate向一个名为hello的消息队列中发送消息。
  @Autowired
  private AmqpTemplate rabbitTemplate;

  /**
   *
   * @description: 消息队列发送注册邮件
   * @param: [email, password]
   * @return: void
   */
  public void sendSkillMessage(ArrayList<String> arrayList){
    // 第一个参数指定发送到哪个队列
    this.rabbitTemplate.convertAndSend("skillList", arrayList);
  }

  public void send(){
    // 第一个参数指定发送到哪个队列
    // 创建 多个 消息
    for(int i = 0; i < 1000; i++){
      this.rabbitTemplate.convertAndSend("hello", "ss");
    }
  }
}
