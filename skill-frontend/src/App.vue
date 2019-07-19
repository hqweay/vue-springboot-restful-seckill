<template>
  <div id="app">
    <!-- <img alt="Vue logo" src="./assets/logo.png"> -->
    <!-- <HelloWorld msg="Welcome to Your Vue.js App"/> -->
    <index/>
  </div>
</template>

<script>
import index from "./components/index.vue";

export default {
  name: "app",
  components: {
    index
  }
};
</script>

<style>
#app {
  display: flex;
  justify-content: center;
  /* align-items: center; */
  margin-top: 5%;
}
</style>
