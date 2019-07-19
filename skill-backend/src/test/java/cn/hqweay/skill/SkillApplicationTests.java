package cn.hqweay.skill;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;


//MockMvc 对 controller 层 做测试
//每个测试类都需要这些注解，所以写在主测试类上，其余测试类继承它就好了

//引用 SpringRunner 与 SpringJUnit4ClassRunner 没有区别 “ SpringRunner is an alias for the SpringJUnit4ClassRunner.”
@RunWith(SpringRunner.class)
//网上有些教程让 配置 SpringApplicationConfiguration，现在的版本用 SpringBootTest 代替 SpringApplicationConfiguration
@SpringBootTest(classes = SkillApplication.class)
@WebAppConfiguration
public class SkillApplicationTests {

  @Test
  public void contextLoads() {
  }

}
