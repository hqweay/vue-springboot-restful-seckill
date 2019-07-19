package cn.hqweay.skill.dto;

import org.springframework.stereotype.Component;

//这个类没有使用..
//要使之能够被注入，需要加这个注解
@Component
public class Result {

  private boolean success = true;

  private String title = "这是一个 api";

  private Object data;

  private String error = "no error";


  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
