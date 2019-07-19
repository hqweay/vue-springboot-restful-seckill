<template>
  <div class="page">
    <user id="user" />
    <list id="list" />
  </div>
</template>

<script>
import list from "../components/list.vue";
import user from "../components/user.vue";
export default {
  components: {
    list,
    user
  },
  data() {
    return {};
  }
};
</script>

<style scoped lang="stylus">
</style>
