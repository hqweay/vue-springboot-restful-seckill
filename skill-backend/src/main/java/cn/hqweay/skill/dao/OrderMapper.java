package cn.hqweay.skill.dao;

import cn.hqweay.skill.entity.OrderKey;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

  @Select("select * from user_buy_product where phone = #{phone} and id = #{skillId}")
  OrderKey queryExistOrder(@Param("phone") String phone, @Param("skillId") Long skillId);

  @Insert("insert into user_buy_product (phone, id) values(#{phone}, #{skillId})")
  int insertSuccessKilled(@Param("phone") String phone, @Param("skillId") Long skillId);

  int insertSelective(OrderKey record);
}