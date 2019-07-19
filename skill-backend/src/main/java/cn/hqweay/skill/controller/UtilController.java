/*
 * Copyright (c) 2018. Lorem ipsum dolor sit amet, consectetur adipiscing elit.
 * Morbi non lorem porttitor neque feugiat blandit. Ut vitae ipsum eget quam lacinia accumsan.
 * Etiam sed turpis ac ipsum condimentum fringilla. Maecenas magna.
 * Proin dapibus sapien vel ante. Aliquam erat volutpat. Pellentesque sagittis ligula eget metus.
 * Vestibulum commodo. Ut rhoncus gravida arcu.
 */

package cn.hqweay.skill.controller;

import cn.hqweay.skill.mq.Sender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api")
public class UtilController {

  @RequestMapping(value = "/time/now")
  public LocalDateTime getTimeNow() {
    LocalDateTime localDateTime = LocalDateTime.now();
    return localDateTime;
  }


  @Autowired
  private Sender sender;

  @RequestMapping(value = "/test")
  public void getTime() {
    sender.send();
  }

}
