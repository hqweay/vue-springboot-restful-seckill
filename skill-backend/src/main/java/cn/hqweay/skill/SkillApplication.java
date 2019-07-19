package cn.hqweay.skill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//开启支持事务
@EnableTransactionManagement
@SpringBootApplication
public class SkillApplication {

  public static void main(String[] args) {
    SpringApplication.run(SkillApplication.class, args);
  }

}
