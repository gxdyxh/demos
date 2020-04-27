<template>
  <cube-scroll ref="suggest"
          class="suggest"
          :data="result"
          :options="options"
           @pulling-up="onPullingUp"
  >
    <ul class="suggest-list">
      <li @click.stop="selectItem(item)" class="suggest-item" v-for="(item,index) in result" :key="index" >
        <div class="icon">
          <i :class="getIconCls(item)"></i>
        </div>
        <div class="name">
          <p class="text" v-html="getDisplayName(item)"></p>
        </div>
      </li>
    </ul>
    <div v-show="!hasMore && !result.length" class="no-result-wrapper">
      <no-result title="抱歉，暂无搜索结果"></no-result>
    </div>
  </cube-scroll>
</template>

<script type="text/ecmascript-6">
  import { search } from 'api/search'
  import { ERR_OK } from 'api/config'
  import { createSong } from 'common/js/song'
  import { mapMutations, mapActions } from 'vuex'
  import Singer from 'common/js/singer'
  import NoResult from 'components/no-result/no-result'

  const TYPE_SINGER = 'singer'
  const perpage = 20

  export default {
    props: {
      showSinger: {
        type: Boolean,
        default: true
      },
      query: {
        type: String,
        default: ''
      },
      click: {
        type: Boolean,
        default: true
      }
    },
    data() {
      return {
        bottom: 0,
        page: 1,
        pullup: true,
        beforeScroll: true,
        hasMore: true,
        result: [],
        options: {
          click: this.click,
          pullUpLoad: {
            threshold: 0,
            txt: {
              more: '',
              noMore: '没有更多数据'
            }
          }
        }
      }
    },
    methods: {
      setBottom(bottom) {
        this.bottom = bottom
      },
      refresh() {
        this.$refs.suggest.$el.style.height = `calc(100% - ${this.bottom}px)`
        this.$refs.suggest.refresh()
      },
      _setCanPullLoad() {
        this.options.pullUpLoad = {
          threshold: 0,
          txt: {
            more: '',
            noMore: '没有更多数据'
          }
        }
      },
      search() {
        this.page = 1
        this.hasMore = true
        this._setCanPullLoad()
        this.$refs.suggest.scrollTo(0, 0)
        search(this.query, this.page, this.showSinger, perpage).then((res) => {
          if (res.code === ERR_OK) {
            this.result = this._genResult(res.data)
            this._checkMore(res.data)
          }
        })
      },
      onPullingUp() {
        if (!this.hasMore) {
          return
        }
        this.page++
        search(this.query, this.page, false, perpage).then((res) => {
          if (res.code === ERR_OK) {
            this.result = this.result.concat(this._genResult(res.data))
            this._checkMore(res.data)
          }
        })
      },
      selectItem(item) {
        // debugger
        if (item.type === TYPE_SINGER) {
          const singer = new Singer({
            id: item.singermid,
            name: item.singername
          })
          this.setSinger(singer)
          this.$router.push({
            path: `/singer/${singer.id}`
          })
          item.name = item.singername
        } else {
          this.insertSong(item)
        }
        this.$emit('select', item)
      },
      getDisplayName(item) {
        if (item.type === TYPE_SINGER) {
          return item.singername
        } else {
          return `${item.name}-${item.singer}`
        }
      },
      getIconCls(item) {
        if (item.type === TYPE_SINGER) {
          return 'icon-mine'
        } else {
          return 'icon-music'
        }
      },
      _genResult(data) {
        let ret = []
        if (data.zhida && data.zhida.singerid) {
          ret.push({ ...data.zhida, ...{ type: TYPE_SINGER } })
        }
        if (data.song) {
          ret = ret.concat(this._normalizeSongs(data.song.list))
        }
        return ret
      },
      _normalizeSongs(list) {
        const ret = []
        list.forEach((musicData) => {
          if (musicData.songid && musicData.albummid) {
            ret.push(createSong(musicData))
          }
        })
        return ret
      },
      _checkMore(data) {
        const song = data.song
        if (!song.list.length || (song.curnum + song.curpage * perpage) > song.totalnum) {
          this.hasMore = false
          this.$refs.suggest.forceUpdate(false, true)
          this.options.pullUpLoad = false
        }
      },
      ...mapMutations({
        setSinger: 'SET_SINGER'
      }),
      ...mapActions([
        'insertSong'
      ])
    },
    watch: {
      query(newQuery) {
        this.search(newQuery)
      }
    },
    components: {
      NoResult
    }
  }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  @import "~common/stylus/mixin"

  .suggest
    width: 100%
    height: 100%
    overflow: hidden
    .suggest-list
      padding: 0 30px
      .suggest-item
        display: flex
        align-items: center
        padding-bottom: 20px
      .icon
        flex: 0 0 30px
        width: 30px
        [class^="icon-"]
          font-size: 14px
          color: $color-text-d
      .name
        flex: 1
        font-size: $font-size-medium
        color: $color-text-d
        overflow: hidden
        .text
          no-wrap()
    .no-result-wrapper
      color: gray
      font-size: 14px
</style>