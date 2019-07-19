/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package cn.hqweay.skill.controller;


import cn.hqweay.skill.dao.ProductMapper;
import cn.hqweay.skill.dto.ErrorResult;
import cn.hqweay.skill.entity.Product;
import cn.hqweay.skill.exception.SkillException;
import cn.hqweay.skill.dao.cache.RedisDao;
import cn.hqweay.skill.mq.Sender;
import cn.hqweay.skill.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

/**
 *
 */
@RestController
@RequestMapping("/api/seckill")
public class IndexController {

  @Autowired
  private ProductService productService;
  @Autowired
  private RedisDao redisDao;


  @Autowired
  private Sender sender;


  @RequestMapping(value = "/list", method = RequestMethod.GET)
  public ArrayList<Product> getProducts() {
    //这里应该做个异常处理
    //返回 json 到前端，应不应该封装呢？
    //返回多个对象，使用 Map
    return productService.getProduces();
  }


  /***
   *
   * @param skillId
   */

  @RequestMapping(value = "/exposer", method = RequestMethod.GET)
  public ResponseEntity<?> exposer(Long skillId) {
    ErrorResult errorResult = null;
    try {
      return productService.exportSkillUrl(skillId);
    } catch (SkillException e) {
      errorResult = new ErrorResult(e);
    }
    return ResponseEntity.ok(errorResult);
  }


  /**
   * PathVariable 获取路径中的参数
   *
   * @param phone
   * @param seckillId
   * @param md5Code
   * @return
   */
  @RequestMapping(value = "/execute/{seckillId}/{md5Code}", method = RequestMethod.GET)
  public ResponseEntity<?> execute(@CookieValue(value = "phone", required = false) String phone,
                                   @PathVariable("seckillId") Long seckillId,
                                   @PathVariable("md5Code") String md5Code) {
    // 执行秒杀
    // 交给 队列
    ArrayList<String> arrayList = new ArrayList<String>();
    arrayList.add(phone);
    arrayList.add(String.valueOf(seckillId));
    arrayList.add(md5Code);

    sender.sendSkillMessage(arrayList);
    return ResponseEntity.ok("正在排队秒杀,等待通知");
    // 排队后怎么知道秒杀是否成功呢?
    // 可以通过轮询...
  }


  @RequestMapping(value = "/isKill", method = RequestMethod.GET)
  public ResponseEntity<?> isKill(@CookieValue(value = "phone", required = false) String phone,
                                  @RequestParam("skillId") Long skillId) {
    return productService.isSkill(phone, skillId);
  }

}
