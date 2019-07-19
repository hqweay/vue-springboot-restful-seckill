package cn.hqweay.skill.dao.cache;

import cn.hqweay.skill.entity.OrderKey;
import cn.hqweay.skill.entity.Product;
import cn.hqweay.skill.exception.SkillException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static cn.hqweay.skill.enums.SkillStatusEnum.REDIS_GET_ERROR;

/**
 * @description: TODO
 * Created by hqweay on 1/1/19 2:28 PM
 */
@Component
public class RedisDao {

  @Autowired
  private RedisTemplate redisTemplate;

  public boolean putProductList(ArrayList<Product> products) {
    if (redisTemplate.hasKey("productList")) {
      return false;
    } else {
      redisTemplate.opsForValue().set("productList", products);
    }
    return true;
  }

  public ArrayList<Product> getProductList() throws SkillException {
    try {
      return (ArrayList<Product>) redisTemplate.opsForValue().get("productList");
    } catch (SkillException e) {
      e.setStatusEnum(REDIS_GET_ERROR);
      throw e;
    }
  }



  public void test(){
    OrderKey orderKey = new OrderKey();
    orderKey.setPhone("1155555");
    redisTemplate.opsForValue().set("11",orderKey);
    redisTemplate.opsForValue().set("myse","mys");
    System.out.println(redisTemplate.opsForValue().get("33"));
  }
}
