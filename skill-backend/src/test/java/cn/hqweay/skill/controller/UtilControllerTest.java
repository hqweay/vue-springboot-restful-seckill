/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package cn.hqweay.skill.controller;

import cn.hqweay.skill.SkillApplicationTests;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.concurrent.ThreadPoolExecutor;

public class UtilControllerTest extends SkillApplicationTests {
  @Autowired
  private UtilController utilController;


  private MockMvc mvc;

  @Before
  public void setUp() {
    //注意，indexController 需要通过自动注入的方式传入，不能用 new，否则 IndexController 中用到的依赖注入就注入不成功
    mvc = MockMvcBuilders.standaloneSetup(utilController).build();
  }

  @Test
  public void testUserController() throws Exception {
    // 1.查询列表
    mvc.perform(MockMvcRequestBuilders.get("/api/time/now"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print());// 打印结果  .andDo(MockMvcResultHandlers.print());// 打印结果;// 判断响应状态是否成功
  }

  @Test
  public void testgetTime() throws Exception {
    // 1.查询列表
//    mvc.perform(MockMvcRequestBuilders.get("/api/test"))
//            .andExpect(MockMvcResultMatchers.status().isOk())
//            .andDo(MockMvcResultHandlers.print());// 打印结果  .andDo(MockMvcResultHandlers.print());// 打印结果;// 判断响应状态是否成功
//

        mvc.perform(MockMvcRequestBuilders.get("/api/test"))
                .andExpect(MockMvcResultMatchers.status().isOk());

  }

}
