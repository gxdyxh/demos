<template>
  <div class="body">
    <m-header></m-header>
    <tab :tabs="tabs" ></tab>
  </div>
</template>

<script>
// @ is an alias to /src
import MHeader from 'components/m-header/m-header'
import Tab from 'components/tab/tab'

import Recommend from 'components/recommend/recommend'
import Rank from 'components/rank/rank'
import Singer from 'components/singer/singer'
import Search from 'components/search/search'

export default {
  name: 'Home',
  computed: {
    tabs() {
      return [
        {
          label: '推荐',
          // component: () => import('components/recommend/recommend'),
          component: Recommend,
          data: { }
        },
        {
          label: '歌手',
          // component: () => import('components/singer/singer'),
          component: Singer,
          data: { }
        },
        {
          label: '排行',
          // component: () => import('components/rand/rand'),
          component: Rank,
          data: { }
        },
        {
          label: '搜索',
          // component: () => import('components/search/search'),
          component: Search,
          data: { }
        }
      ]
    }
  },
  components: {
    MHeader,
    Tab
  }
}
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
.body
  height: 100%
  display: flex;
  flex-direction: column;
  .m-header
    height: 44px
    flex: 0;
  >>>.navtab
        height: calc(100% - 44px);
</style>
