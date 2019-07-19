package cn.hqweay.skill.enums;

/**
 * @description: TODO
 * Created by hqweay on 12/17/18 6:59 PM
 */
public enum SkillStatusEnum {
  PRODUCT_NOT_EXIST(1000, "this is in not in products."),
  SKILL_DATE_NOT_START(1001, "the skill is not started."),
  SKILL_DATE_END(1002, "the skill is over."),
  SKILL_URL_ERROR(2000, "秒杀地址被篡改。"),
  SKILL_REPEAT(2001, "秒杀成功！无需再次秒杀。"),
  PRODUCT_COUNT_OVER(2002, "库存不足。"),
  SKILL_ERROR(3000, "其他异常"),
  REDIS_GET_ERROR(4000, "redis 获取失败");


  private int code;
  private String message;

  SkillStatusEnum() {
  }

  SkillStatusEnum(int state, String info) {
    this.code = state;
    this.message = info;
  }


  public int getCode() {
    return code;
  }

  public String getMessage() {
    return message;
  }

//  public static SkillStatusEnum stateOf(int index) {
//    for (SkillStatusEnum statEnum : values()) {
//      if (statEnum.getState() == index) {
//        return statEnum;
//      }
//    }
//    return null;
//  }
}
