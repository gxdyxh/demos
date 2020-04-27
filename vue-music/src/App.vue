<template>
  <div id="app">
    <keep-alive>
      <router-view/>
    </keep-alive>
    <player></player>
  </div>
</template>

<script type="text/ecmascript-6">
  import Player from 'components/player/player'
  export default {
    components: {
      Player
    }
  }
</script>

<style lang="stylus">
  #app
    height: 100%
</style>
