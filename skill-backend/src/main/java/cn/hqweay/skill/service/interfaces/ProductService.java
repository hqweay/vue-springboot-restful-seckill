package cn.hqweay.skill.service.interfaces;

import cn.hqweay.skill.entity.Product;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;


public interface ProductService {


  /**
   * 获取秒杀商品列表，没考虑分页
   *
   * @return list of products
   */
  ArrayList<Product> getProduces();

  /**
   * 暴露秒杀 url
   *
   * @param SkillId
   * @return
   */
  ResponseEntity<?> exportSkillUrl(Long SkillId);

  ResponseEntity<?> executeSkill(String phone, Long skillId, String md5Code);


  // 判断是否秒杀成功
  ResponseEntity<?> isSkill(String phone, Long skillId);
}
