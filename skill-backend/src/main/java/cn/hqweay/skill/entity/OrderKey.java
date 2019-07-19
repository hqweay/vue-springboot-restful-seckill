/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package cn.hqweay.skill.entity;

import java.io.Serializable;

public class OrderKey implements Serializable {
  private static final long serialVersionUID = 1L;
  private String phone;
  private Long id;

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone == null ? null : phone.trim();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}