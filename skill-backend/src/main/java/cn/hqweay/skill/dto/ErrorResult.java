
package cn.hqweay.skill.dto;

import cn.hqweay.skill.enums.SkillStatusEnum;
import cn.hqweay.skill.exception.SkillException;

/***
 *
 * 错误处理返回数据类
 */
public class ErrorResult {
  // 自定义错误码
  private int code;
  // 错误信息
  private String message;

  public ErrorResult() {}

  public ErrorResult(SkillException e) {
    this.code = e.getStatusEnum().getCode();
    this.message = e.getStatusEnum().getMessage();
  }

  public ErrorResult(SkillStatusEnum statusEnum) {
    this.code = statusEnum.getCode();
    this.message = statusEnum.getMessage();
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
