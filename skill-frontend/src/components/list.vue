<template>
  <div class="products">
    <div class="product" v-for="product in products" :key="product.id">
      <div class="info">
        Just {{ product.number}} !!
        <span class="name"> {{ product.name}}</span>
        {{ product.info}}
      </div>
      <div class="time" v-if="isKillCount[product.id] === 'countDown'">
        {{timeCounts[product.id]}}
        <p>Please waiting...</p>
      </div>
      <div class="skill" v-else-if="isKillCount[product.id] === 'kill' ">
        <!-- <a class="hhh" :href="skillUrls[product.id]"> -->
        <button type="button " class="btn is-primary" @click="skill(skillUrls[product.id])">Seckill</button>
        <!-- </a> -->
      </div>
      <div class="time-out" v-else-if="isKillCount[product.id] === 'timeOut' ">
        The Seckill is over.
      </div>
    </div>
    <!-- <button @click="test">test</button> -->
  </div>
</template>

<script type="text/ecmascript-6">
export default {
  data() {
    return {
      products: [],
      timeNow: "",
      timeCounts: [],
      //三种形式，未到 countDown，之间 kill timeOut
      isKillCount: {}, // 作 Map 使用设置为 {},若是 []， 会被当数组处理
      // isKillCount: new Map(),
      skillUrls: {} // 作 Map 使用
    };
  },
  created: function() {
    //获取秒杀商品信息列表
    this.$ajax({
      url: "/api/seckill/list",
      method: "get"
      // params: {}
    }).then(res => {
      if (res.status === 200) {
        this.products = res.data;
      } else {
        //报错
      }
    });

    //说明第一次进 与服务器交互
    //获取当前时间
    // console.log("服务器交互吗");
    this.$ajax({
      url: "/api/time/now",
      method: "get"
    }).then(res => {
      // this.timeNow = res.data;
      if (res.status === 200) {
        this.timeNow = new Date(res.data);
      } else {
        //没有时间
      }
    });
    //注意，由于回调没有完成，所以在这里获取不到 this.products 等 data 中的值
    //要操作，需要使用 watch
    //console.log(this.products);
    setInterval(this.countDown, 1000);
  },
  watch: {
    // timeNow: function() {
    // this.countDown();
    // }
    isKillCount: function() {
      // 请求 秒杀 url
      for (let key in this.isKillCount) {
        // console.log(this.isKillCount[key]);
        if (this.isKillCount[key] == "kill") {
          //请求
          this.$ajax({
            url: "/api/seckill/exposer",
            method: "get",
            params: {
              skillId: key
            }
          }).then(res => {
            // console.log(res);
            if (res.data.code == undefined) {
              //请求成功
              this.$set(this.skillUrls, key, res.data);
            } else {
              alert(res.data.message);
            }
          });
        }
      }
    }
  },
  methods: {
    skill: function(skill_url) {
      if (this.$cookieStore.getCookie("phone") == undefined) {
        alert("请登录");
        return;
      }
      // console.log(skill_url);
      this.$ajax({
        url: skill_url,
        method: "get",
        params: {
          phone: this.$cookieStore.getCookie("phone")
        }
      }).then(res => {
        // console.log(res);
        if (res.data == "ok") {
          alert("成功啦");
        } else if (res.data.code != undefined) {
          alert(res.data.message);
        }
      });
    },
    // test: function() {
    //   this.$ajax({
    //     url: "/api/seckill/test",
    //     method: "get"
    //   }).then(res => {
    //     console.log(res);
    //     if (res.status == 200) {
    //       //请求成功
    //       if (res.data.code != undefined) {
    //         console.log("也挺好");
    //       }
    //     }
    //   });
    // },
    countDown: function() {
      //执行 + ，被识别为字符串...
      var dateNowSeconds = this.timeNow - -1000;
      var dateNow = new Date(dateNowSeconds);
      this.timeNow = dateNow;
      this.timeCounts = [];

      this.products.forEach(product => {
        var dateStart = new Date(product.start_time);
        var dateEnd = new Date(product.end_time);
        //过了秒杀时段  --- 提示已经结束
        //秒杀时段内    --- 秒杀按钮 秒杀链接请求
        //没到秒杀时间  --- 倒计时
        if (dateNow > dateEnd) {
          //秒杀结束
          if (this.isKillCount[product.id] != "timeOut") {
            this.$set(this.isKillCount, product.id, "timeOut");
            // this.isKillCount.set(product.id, "countDown");
          }
          return;
        }
        if (dateNow > dateStart) {
          //已经是秒杀时间了
          if (this.isKillCount[product.id] != "kill") {
            this.$set(this.isKillCount, product.id, "kill");

            // this.isKillCount.set(product.id, "countDown");
          }
        } else {
          //          alert("没到");
          // 倒计时
          var ts = dateStart - dateNow;
          if (ts <= 0) {
            //倒计时完毕 显示秒杀按钮
            //再请求 秒杀链接吧！！
            if (this.isKillCount[product.id] != "kill") {
              this.$set(this.isKillCount, product.id, "kill");

              // this.isKillCount.set(product.id, "countDown");
            }

            //请求秒杀链接
          } else {
            if (this.isKillCount[product.id] != "countDown") {
              this.$set(this.isKillCount, product.id, "countDown");
              // this.isKillCount.set(product.id, "countDown");
            }

            var dd = parseInt(ts / 1000 / 60 / 60 / 24, 10); // 计算剩余天数
            var hh = parseInt((ts / 1000 / 60 / 60) % 24, 10); // 计算剩余小时
            var mm = parseInt((ts / 1000 / 60) % 60, 10); // 计算剩余分钟
            var ss = parseInt((ts / 1000) % 60, 10); // 秒数

            // 以数组的形式传入
            // this.timeCounts.push(
            //   dd + " d " + hh + " h " + mm + " m " + ss + " s"
            // );

            //以键值对的形式传入
            this.$set(
              this.timeCounts,
              product.id,
              dd + " d " + hh + " h " + mm + " m " + ss + " s"
            );
          }
        }
      });

      // console.log(this.timeCounts);
    }
  }
};
</script>

<style scoped>
ul,
li {
  list-style: none;
}

.products {
  display: flex;
  flex-direction: column;
  justify-content: center;
}
.product {
  line-height: 200%;
}
.name {
  color: blueviolet;
}
.info {
  margin-top: 5%;
}
.skill {
  margin-top: 5px;
}
</style>
