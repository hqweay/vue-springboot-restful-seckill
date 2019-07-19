# skill-backend 后端说明

## 参考

Github 仓库: [Sunybyjava/seckill](https://github.com/Sunybyjava/seckill)

Mooc 视频: [Java 高并发秒杀](https://www.imooc.com/learn/587)

## 文档

[Spring Boot 2.x 中文文档](https://github.com/DocsHome/springboot)

[MyBatis 中文文档 3.4](https://www.kancloud.cn/wizardforcel/java-opensource-doc/112545)

[MyBatis Generator 用户手册](https://www.kancloud.cn/wizardforcel/java-opensource-doc/152983)

[Spring Data Redis- Version 2.1.3.RELEASE](https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/)

[spring-boot-starter-data-redis 翻译官方文档 5.3 - 5.6](https://blog.csdn.net/michaelehome/article/details/79485661)

## 使用工具

核心: spring boot + mybatis

版本控制: gradle

插件: mybatis-generator 工具

## 说明

基于上面的两个项目,准备实现一下秒杀.

抽取了最核心的部分,一切从简...

打算前后端分离,此仓库是后端实现,对应的前端项目 [hqweay/skill-frontend](https://github.com/hqweay/skill-frontend) 采用 Vue CLI3.

开发过程:从交互分析,提取 api,再层层分解,一步一步实现.

按照上面的参考,从 dao 层到 service 层,再到 controller 层,这样自底向上的流程容易让人迷糊...每一步都不知道为了啥.应该从上到下,我缺少什么,然后再去做什么...这样理解起来会比较容易.

见 **开发步骤** .

## 使用

使用记得将项目中的两个配置文件 `/skill/src/main/resources/mybatis/config.properties_back`, `/skill/src/main/resources/application.yml_back` 更名为
`config.properties` 以及 `application.yml`.

# 开发步骤

1. 搭建后端项目基本框架.放在 [MyBatisGenerator-Tool](https://github.com/hqweay/MyBatisGenerator-Tool) .完成 mybatis 的相关繁琐配置.
2. 分析项目,设计数据结构,数据库,项目结构.考虑 **数据交互,异常处理** 等细节.
3. 分析交互,设计 api.
4. 对每一个 api 进行实现.
5. 优化.

讨论一下第三步:

比如,首先用户进入网页,首先肯定会需要一个获取秒杀商品信息的 api.

商品需要在秒杀时段内才能暴露秒杀按钮,同时总不可能让用户不断刷新网页吧,所以前端需要做倒计时,并且动态暴露秒杀按钮.

秒杀操作,实际上还是执行了一个 url (也是 api),这个秒杀 url 不能在倒计时的阶段暴露,只有到了秒杀阶段才能暴露,我们怎么获取它呢?

可以通过写一个获取秒杀 api 的 api.

此时需要考虑到对秒杀 api 加密.(甚至随时间变化,能实现吗?能吧?按时间混淆?)

用户开始秒杀,这里异常情况最多.

此时应该考虑到**事务** , **存储过程** ...(SQL 怎么实现,框架怎么实现...)

同时这里也是并发点,后面进行优化也主要是围绕这一块.

# api 列表

| api                                        | 参数                                               | 返回值         | 说明                                   |
| ------------------------------------------ | -------------------------------------------------- | -------------- | -------------------------------------- |
| /api/seckill/list                          | 无                                                 | 秒杀商品列表   |                                        |
| /api/seckill/exposer                       | skillId(商品 id)                                   | 商品的秒杀 url |                                        |
| /api/seckill/execute/{seckillId}/{md5Code} | phone(手机号), seckillId(商品 id), md5Code(加密码) | 返回秒杀结果   | 手机号从 cookie 获得,后两者从 url 获得 |
| /api/time/now                              |                                                    |                | 获取服务器端时间                       |

api 是根据前端来的,前端要啥,后端写啥...

这一块不是重点,列在这可以和其他块内容参考理解.比如下面的优化块.

# 重点

## RESTful API 设计

### 参考资料

Github 仓库 : [restful-api-design-references](https://github.com/aisuhua/restful-api-design-references)

主要这三篇:

1. [理解 RESTful 架构](http://www.ruanyifeng.com/blog/2011/09/restful.html) - 阮一峰 简单了解什么是 RESTFul
2. [RESTful API 设计指南](http://www.ruanyifeng.com/blog/2014/05/restful_api.html) - 阮一峰
3. [Restful API 的设计规范](http://novoland.github.io/%E8%AE%BE%E8%AE%A1/2015/08/17/Restful%20API%20%E7%9A%84%E8%AE%BE%E8%AE%A1%E8%A7%84%E8%8C%83.html) 实战经验的总结，具有较强的启发意义

### 执行情况

各种规范(即便是自己总结的)都自有道理,我在 [Restful API 的设计规范](http://novoland.github.io/%E8%AE%BE%E8%AE%A1/2015/08/17/Restful%20API%20%E7%9A%84%E8%AE%BE%E8%AE%A1%E8%A7%84%E8%8C%83.html) 这篇文章看到 **不要包装** 的建议,所以选择在开发过程中统一利用注解 `RestController` 直接返回对象(框架帮我们把对象解析为 json 数据).

在我参考的仓库中,封装了一个 dto 的 ajax 交互类.

不包装的话,对异常怎么处理呢?

可以采用 `ResponseEntity`.

```java
@RequestMapping(value = "/exposer", method = RequestMethod.GET)
  public ResponseEntity<?> exposer(Long skillId) {
    return ResponseEntity.ok("ok");
    // return ResponseEntity.ok(new Object());
    // return ResponseEntity.status(422).body("bad");
  }
```

如上,ResponseEntity 可以携带各种数据返回,而且可以设置返回的 http 的状态码.

**也有人建议通过设置状态码来区分异常.**

我采用的方式是:

**http 的状态码始终返回 200.**

**自定义错误信息,返回自定义的状态码与错误信息**

先建立:运行时异常类,维护错误信息的枚举类,异常信息交互类(ErrorResult.class).

先看这三个类.

异常:

```
public class SkillException extends RuntimeException {

  private SkillStatusEnum statusEnum;
  ...
}
```

真正的错误信息被枚举类维护:

```
public enum SkillStatusEnum {
  PRODUCT_NOT_EXIST(1000, "this is in not in products."),
  SKILL_DATE_NOT_START(1001, "the skill is not started."),
  SKILL_DATE_END(1002, "the skill is over."),
  SKILL_URL_ERROR(2000, "秒杀地址被篡改。"),
  SKILL_REPEAT(2001, "秒杀成功！无需再次秒杀。"),
  PRODUCT_COUNT_OVER(2002, "库存不足。"),
  SKILL_ERROR(3000, "其他异常");

  private int code;
  private String message;

  SkillStatusEnum() {
  }
  SkillStatusEnum(int state, String info) {
    this.code = state;
    this.message = info;
  }
  public int getCode() {return code;}
  public String getMessage() {return message;}
}
```

`ErrorResult` 简单地封装了 code,message 两条属性.

```
public class ErrorResult {
  // 自定义错误码
  private int code;
  // 错误信息
  private String message;
  ...
}
```

如果遇到了运行时异常,就把对应的错误信息(在枚举类中)传至异常类,**然后抛出**.

最后在 controller 层捕获,把异常信息取出,封装至交互类 ErrorResult.class,再通过 `ResponseEntity` 携带错误信息返回给前端.

回头看上面 controller 层的代码!!我使用了多态(`ResponseEntity<?>`),这样可以保证我在正常情况下可以向前端传送某些对象,遇到异常时也可以返回 `ErrorResult` 的实例!

------

做成这个鬼样也不是我一开始想到的.

比如本来没打算做一个异常类,但是在做 **事务** 的时候,需要抛出 runtimeexcetpion,才会回滚,为了统一处理异常,就不如自己封装了一下.

PS:有更优雅的方式吗?

这样处理还不如创建个交互类直观...

------

前端就这样处理:

```js
import axios from 'axios'
axios.get('/api/test')
  .then(function (res) {
    if (res.data.code != undefined) {
        console.log(response.data)
    } else {
        // 由后端抛出的错误
        alert(res.data.message)
    }
  }).catch(function (error) {
       // 由网络或者服务器抛出的错误
     alert(error.toString())
  })
```

也挺好...

# 优化

优化是重点,单独提出来.

在这里,优化集中在两个点,看 api 列表,一个是获取商品信息,一个是秒杀.

为什么在这些地方?主要是这里涉及到与数据库的交互,比如获取商品信息,商品信息的变动比较小,就不必每次查询就建立数据库连接.

在视频教程里,分别采用:

使用 redis 缓存商品信息.

对于秒杀,采用存储过程.

对于前者,redis 的作用就不赘叙.对于后者,什么是存储过程也不赘叙了.

因为秒杀涉及到修改两个表,那么至少会执行两条 sql,在之前,我们是在 Service 层来执行这两个操作,并且通过 Spring 的声明式事务来管理的.我们可以把这些过程封装为 SQL 层面的一个存储过程,然后只需要在 Service 层调用这个存储过程就行了.

这里优化的是事务行级锁持有的时间.

不过视频教程不建议过多依赖存储过程,这里的逻辑很简单,可以一用.

网络上查询,秒杀的操作几乎也都是通过 redis 来优化的.

## 其他

对 `/api/seckill/list` 使用了缓存,其他同理,先不做了...

# 其他优化

进一步的优化,就是分布式之类的了...接下来稍微学习一下.

# redis 安装与使用

安装不提,缺少某些依赖安装即可...

注意:

再不进行任何配置的情况下,使用的是默认配置.执行 `redis-server` 后,该程序会在前台执行.而我们实际对 redis 进行操作需要打开 `redis-cli`,在这种情况下只能另开一个窗口打开 `redis-cli`.

如果在当前窗口按 `ctrl +z` 或 `ctrl + c` 关闭,再执行 `redis-cli`,是没有效果的.

如果已经整合到 spring,进行数据操作时会报错`NOAUTH Authentication required.`

另外:执行 `redis-server` 如果遇到某些错误信息,其实当前窗口已经给出了解决办法.看不明白可以搜索引擎查看.

我们需要后台运行 redis.

修改 `/redis/redis.conf` 中的 `daemonize no` 为 `daemonize yes`.

进入 `/redis/src`

执行 `./redis-server ../redis.conf` 意思就是以自己的配置(后台执行)来运行 `redis-server`.

ok.

命令不是死的,灵活变通.比如使用默认值执行了 `make install`,会在 `/usr/local/bin` 生成安装目录,可全局执行 `redis` 相关命令...

`ps -ef|grep redis` 检查是否后台启动.

# springboot2.x 整合 redis

配置文件 /config/RedisConfiguration.java

配置 application.yml

redis 是提供了常见的数据结构的存储你,如 String,List...

在本项目中,redis 使用 jackjson 把对象转换为 json,以 String 来储存,所以只用了 k/v 的方式. json 也是序列化的一种嘛.

`redisTemplate.opsForValue()`

直接序列化和 json 怎么选型呢?

可以:只读取用 json,涉及到修改用普通序列化.看场景嘛...

# update

2019-05-23

增加了 RabbitMQ 来异步处理秒杀逻辑.之前执行秒杀会阻塞直到返回秒杀结果,现在会直接返回"正在排队执行秒杀"的信息,不会阻塞.

不过这样的话,前端怎么获取是否是否秒杀成功呢?

可以通过轮询,判断秒杀表是否存在 用户-商品 的对应.

不过现在,秒杀有三种状态.秒杀成功,秒杀失败,秒杀排队中...

怎么做?首先,根据秒杀表可以获取秒杀成功,秒杀尚未成功这两种状态.然后怎么判断秒杀失败和秒杀排队呢?

可以这样,查询商品表,如果还有商品,就返回正在排队,如果没有商品了,就返回秒杀失败.

真是机智...

# skill-frontend 前端说明

![](https://raw.githubusercontent.com/hqweay/skill-frontend/master/screenshot/index.jpg)

## 说明

秒杀练习的前端，配套的后端项目 [skill-back-end](https://github.com/hqweay/skill-backend)。

## 遇到的问题

### 数据库存时间类型为 timestamp

前后端数据交换，解析。

### 倒计时

与服务器交互了一次,获取服务器时间,然后靠轮循倒计时..

可以从请求头获取时间...

## Project setup
```
npm install
```

### Compiles and hot-reloads for development
```
npm run serve
```

### Compiles and minifies for production
```
npm run build
```

### Run your tests
```
npm run test
```

### Lints and fixes files
```
npm run lint
```

### Customize configuration
See [Configuration Reference](https://cli.vuejs.org/config/).
