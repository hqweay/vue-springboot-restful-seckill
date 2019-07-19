package cn.hqweay.skill.service;

import cn.hqweay.skill.dao.OrderMapper;
import cn.hqweay.skill.dao.ProductMapper;
import cn.hqweay.skill.dao.cache.RedisDao;
import cn.hqweay.skill.entity.OrderKey;
import cn.hqweay.skill.entity.Product;
import cn.hqweay.skill.enums.SkillStatusEnum;
import cn.hqweay.skill.exception.SkillException;
import cn.hqweay.skill.service.interfaces.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @description: TODO
 * Created by hqweay on 12/17/18 2:00 PM
 */
@Service
public class ProductServiceImpl implements ProductService {

  private final String salt = "This is a salt.";

  @Autowired
  private ProductMapper productMapper;

  @Autowired
  private OrderMapper orderMapper;

  @Autowired
  private RedisDao redisDao;


  /**
   * @description: 获取秒杀产品列表
   * @param: []
   * @return: java.util.ArrayList<cn.hqweay.skill.entity.Product>
   * Created by hqweay on 12/17/18 8:41 PM
   */
  @Override
  public ArrayList<Product> getProduces() {
    ArrayList<Product> products = null;
    try {
      products = redisDao.getProductList();
    } catch (SkillException e) {
//    redis 出错  if (e.getStatusEnum().getCode() == 4000)
    }
    //若缓存中无
    if (products == null) {
      products = productMapper.queryAll();
      redisDao.putProductList(products);
    }
    return products;
  }

  /**
   * @description: 暴露秒杀 url
   * @param: [skillId]
   * @return: org.springframework.http.ResponseEntity<?>
   * Created by hqweay on 12/17/18 8:41 PM
   */
  @Override
  public ResponseEntity<?> exportSkillUrl(Long skillId) {
    //Redis 优化一下

    Product product = productMapper.queryById(skillId);
    try {
//商品不存在
      if (null == product) {
        throw new SkillException(SkillStatusEnum.PRODUCT_NOT_EXIST);
      }
//验证秒杀时间
      validSkillTime(skillId);
    } catch (SkillException e) {
//统一处理异常
      throw e;
//其他未知错误
    } catch (Exception e) {
//      throw new SkillException(SkillStatusEnum.SKILL_ERROR);
      throw e;
    }
//返回秒杀地址
    return ResponseEntity.ok(getSkillUrl(skillId));
  }

  /**
   * @description: 秒杀 添加事务
   * @param: [phone, skillId, md5Code]
   * @return: org.springframework.http.ResponseEntity<?>
   * Created by hqweay on 12/17/18 8:41 PM
   */
  @Override
  @Transactional
  public ResponseEntity<?> executeSkill(String phone, Long skillId, String md5Code) throws SkillException {
//todo        处理数据库锁，redis 缓存优化

    try {

//    检查手机号 防止 恶意 post
      Pattern p = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$");
      Matcher m = p.matcher(phone);
      if (!m.matches()) {
        throw new SkillException(SkillStatusEnum.SKILL_URL_ERROR);
      }
//      检查 url 是否被篡改
      if (!md5Code.equals(getMd5(skillId))) {
        throw new SkillException(SkillStatusEnum.SKILL_URL_ERROR);
      }
//检查是否重复秒杀
      if (null != orderMapper.queryExistOrder(phone, skillId)) {
        throw new SkillException(SkillStatusEnum.SKILL_REPEAT);
      }

//检查库存
      if (productMapper.queryStockProduct(skillId) == 0) {
        throw new SkillException(SkillStatusEnum.PRODUCT_COUNT_OVER);
      }
//验证秒杀时间
      validSkillTime(skillId);

//库存减少
      int reduceNumber = productMapper.reduceNumber(skillId);
      int insertSuccessKilled = orderMapper.insertSuccessKilled(phone, skillId);

      if (reduceNumber < 0 || insertSuccessKilled < 0) {
        throw new SkillException(SkillStatusEnum.SKILL_ERROR);
      }
//订单表插入数据
    } catch (SkillException e) {
      throw e;
//其他未知错误
    } catch (Exception e) {
//      throw new SkillException(SkillStatusEnum.SKILL_ERROR);
      throw e;
    }
    return ResponseEntity.ok("ok");

  }

//   util class

  private String getSkillUrl(Long skillId) {
    String skillUrlBase = "api/seckill/execute/";
    return skillUrlBase + skillId + "/" + getMd5(skillId);
  }

  private String getMd5(Long skillId) {
    String base = skillId + "/" + salt;
    return DigestUtils.md5DigestAsHex(base.getBytes());
  }

  /**
   * @description: 效验秒杀时间 可能抛出异常
   * @param: [skillId]
   * @return: void
   * Created by hqweay on 12/17/18 8:36 PM
   */
  private void validSkillTime(Long skillId) throws SkillException {
    Product product = productMapper.queryById(skillId);
    Date startTime = product.getStart_time();
    Date endTime = product.getEnd_time();
    Date nowTime = new Date();
    try {
//若秒杀未开始
      if (nowTime.before(startTime)) {
        throw new SkillException(SkillStatusEnum.SKILL_DATE_NOT_START);
      }
//若秒杀已结束
      if (nowTime.after(endTime)) {
        throw new SkillException(SkillStatusEnum.SKILL_DATE_END);
      }
    } catch (SkillException e) {
//      交给调用者处理
      throw e;
    }
  }


  @Override
  public ResponseEntity<?> isSkill(String phone, Long skillId) {
    OrderKey orderKey = orderMapper.queryExistOrder(phone, skillId);
    if (orderKey != null){
      // 查询成功
      return ResponseEntity.ok("秒杀成功啦已经");
    }else {
      if(productMapper.queryStockProduct(skillId) > 0){
        return ResponseEntity.ok("秒杀还在排队");
      }else{
        return ResponseEntity.ok("秒杀失败了老兄");
      }
    }
  }
}

//    Date startTime = product.getStart_time();
//    Date endTime = product.getEnd_time();
//    Date nowTime = new Date();
////若秒杀未开始
//    if (nowTime.before(startTime)) {
//      ErrorResult errorResult = setError(SkillStatusEnum.SKILL_DATE_NOT_START);
//      return ResponseEntity.ok(errorResult);
//
//    }
////若秒杀已结束
//    if (nowTime.after(endTime)) {
//      ErrorResult errorResult = setError(SkillStatusEnum.SKILL_DATE_OVER);
//      return ResponseEntity.ok(errorResult);
//    }



