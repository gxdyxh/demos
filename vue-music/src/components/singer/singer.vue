<template>
    <div class="singer_list">
       <cube-index-list ref="scroll" :data="singers" v-if="singers.length">
        <cube-index-list-group
            v-for="(singer, index) in singers"
            :key="index"
            :group="singer">
            <cube-index-list-item
            v-for="(item, index) in singer.items"
            :key="index"
            :item="item"
            @select="selectItem">
                <img class="avatar" v-lazy="item.avatar">
                <span class="name">{{item.name}}</span>
            </cube-index-list-item>
        </cube-index-list-group>
        </cube-index-list>
        <div class="loading-container" v-show="!singers.length">
            <cube-loading></cube-loading>
            <span>加载中...</span>
        </div>
    </div>
</template>
<script>
import { getSingerList } from 'api/singer'
import { ERR_OK } from 'api/config'

import Singer from 'common/js/singer'
import { mapMutations } from 'vuex'

import { playlistMixin } from 'common/js/mixin'

const HOT_NAME = '热门'
const HOT_LEN = 10

export default {
    mixins: [playlistMixin],
    name: 'search',
     data() {
    return {
      singers: []
    }
  },
  methods: {
    handlePlaylist(playList) {
        const bottom = playList.length > 0 ? 60 : 0
        if (this.$refs.scroll) {
            // this.$refs.scroll.$el.style.bottom = `${bottom}px`
            this.$refs.scroll.$el.style.height = `calc(100% - ${bottom}px)`
            this.$refs.scroll.refresh()
        }
    },
    selectItem(singer) {
       console.log(singer)
      this.setSinger(singer)
      this.$router.push({
          path: `/singer/${singer.id}`
      })
    },
    clickTitle(title) {
      console.log(title)
    },
     _getSingerList() {
        getSingerList().then((res) => {
            if (res.code === ERR_OK) {
                this.singers = this._normalizeSinger(res.data.list)
            }
        })
    },
    _normalizeSinger(list) {
        const map = {
            hot: {
                name: HOT_NAME,
                items: []
            }
        }
        list.forEach((item, index) => {
           const singerTmp = new Singer({ id: item.Fsinger_mid, name: item.Fsinger_name })
            if (index < HOT_LEN) {
                map.hot.items.push(singerTmp)
            }
            const key = item.Findex
            if (!map[key]) {
                 map[key] = { items: [], name: key }
            }
            map[key].items.push(singerTmp)
        })
         // 处理map 得到有序列表
         const hot = []
         const ret = []
         const other = []
         for (const key in map) {
             const val = map[key]
            if (val.name === HOT_NAME) {
                hot.push(val)
            } else if (val.name.match(/[a-zA-Z]/)) {
                ret.push(val)
            } else {
                other.push(val)
            }
         }
         ret.sort((a, b) => {
             return a.name.charCodeAt(0) - b.name.charCodeAt(0)
         })
         const tmp = other.map((item) => {
             item.name = '#'
             return item
         })
         return hot.concat(ret).concat(tmp)
    },
    fetch() {
        if (!this.fetched) {
            this.fetched = true
            this._getSingerList()
        }
    },
    ...mapMutations({
        setSinger: 'SET_SINGER'
    })
  }
}
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/stylus/variable'
.singer_list
    height: 100%
    >>>.cube-index-list-nav
            right: 5px;
            background: $color-background
            opacity: 0.8;
            border-radius: 12px;
            padding: 5px 0 5px;
            li
                padding: 4px 4px 4px 4px;
                &.active
                    color: $color-theme
    >>>.cube-index-list-content
           background: $color-background-d
    >>>.cube-index-list-anchor
           background: $color-highlight-background
           padding: 7px 16px;
           font-size: 12px;
    >>>.cube-index-list-group
            padding-bottom: 30px;
    >>>.cube-index-list-item
            display: flex;
            align-items: center;
            padding: 20px 0 0 30px;
            .avatar
                width: 50px;
                height: 50px;
                border-radius: 50%;
            .name
                margin-left: 20px;
                color: hsla(0,0%,100%,.5);
                font-size: 14px;
</style>