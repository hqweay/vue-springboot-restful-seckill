<template>
  <div class="page">
    <div class="welcome" v-if="phone">
      <span>
        Hello My Friend!!
      </span>
      <span>
        <button @click="logout">logout</button>
      </span>
    </div>
    <div class="phone" v-else>
      <button type="button" class="btn" @click="showInfo" v-show="!isClicked">Sign in</button>
      <div class="field info" v-show="isClicked">
        <label for="name_field">Your phone number</label>
        <input type="text" id="name_field" class="input" placeholder="Enter to post" @keyup.enter="postPhone" v-model="isPhone">
      </div>
    </div>

  </div>
</template>

<script type="text/ecmascript-6">
export default {
  components: {},
  data() {
    return {
      isClicked: false,
      isPhone: "",
      phone: ""
    };
  },
  created: function() {
    if (this.$cookieStore.getCookie("phone")) {
      //
      this.phone = this.$cookieStore.delCookie("phone");
      // console.log(this.phone);
    } else {
      // console.log("no phone");
      //没有手机号啊
    }
  },
  methods: {
    logout: function() {
      this.$cookieStore.getCookie("phone");
      this.phone = "";
      this.isPhone = "";
      this.isClicked = false;
    },
    showInfo: function() {
      if (this.isClicked) {
        this.isClicked = false;
      } else {
        this.isClicked = true;
      }
    },
    postPhone: function() {
      // console.log(this.isPhone);
      //验证手机号
      var reg = /^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/;
      if (this.isPhone == "") {
        alert("请输入手机号码");
      } else if (!reg.test(this.isPhone)) {
        alert("手机格式不正确");
      } else {
        // alert("ojbk");
        this.phone = this.isPhone;
        //存入 cookie
        let expireDays = 1000 * 60 * 60;
        this.$cookieStore.setCookie("phone", this.phone, expireDays);
      }
    }
  }
};
</script>

<style scoped>
.page {
  display: flex;
  justify-content: center;
  /* flex-direction: column; */
}
.phone {
  justify-content: center;
}
</style>