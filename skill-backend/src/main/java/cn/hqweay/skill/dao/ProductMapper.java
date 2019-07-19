package cn.hqweay.skill.dao;

import cn.hqweay.skill.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.ArrayList;


@Mapper
public interface ProductMapper {

  /**
   * 查询所有秒杀商品，不考虑分页
   * sql 语句不建议加分号，因为一些分页插件会在 sql 后面接一些参数已到达效果，若加分号会报错哦！
   * <p>
   * 添加时间的判断
   *
   * @return list of products
   */
  @Select("select * from product where start_time < end_time")
  ArrayList<Product> queryAll();

  @Select("select * from product where id = #{skillId} and start_time < end_time")
  Product queryById(@Param("skillId") Long skillId);

  @Select("select number from product where id = #{skillId} and start_time < end_time")
  int queryStockProduct(@Param("skillId") Long skillId);

  @Update("update product set number = number -1 where id = #{skillId} and number > 0 and start_time < end_time")
  int reduceNumber(@Param("skillId") Long skillId);

  int insert(Product record);

  int insertSelective(Product record);

  Product selectByPrimaryKey(Long id);

  int updateByPrimaryKeySelective(Product record);

  int updateByPrimaryKey(Product record);
}