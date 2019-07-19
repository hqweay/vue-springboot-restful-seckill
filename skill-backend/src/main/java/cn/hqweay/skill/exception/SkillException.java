package cn.hqweay.skill.exception;

import cn.hqweay.skill.enums.SkillStatusEnum;

/**
 * @description: TODO
 * Created by hqweay on 12/17/18 7:48 PM
 */
public class SkillException extends RuntimeException {

  private SkillStatusEnum statusEnum;

  public SkillException() {}

  public SkillException(SkillStatusEnum statusEnum) {
    this.statusEnum = statusEnum;
  }

  public SkillStatusEnum getStatusEnum() {
    return statusEnum;
  }

  public void setStatusEnum(SkillStatusEnum statusEnum) {
    this.statusEnum = statusEnum;
  }
}
