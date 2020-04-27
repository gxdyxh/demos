<template>
  <transition name="slide">
    <div class="user-center">
      <div class="back" @click="back">
        <i class="icon-back"></i>
      </div>
      <div class="switches-wrapper">
        <cube-tab-bar v-model="selectedLabel"
                      :show-slider=false
                      :use-transition=false
                      ref="tabNav"
                      :data="tabLabels">
        </cube-tab-bar>
      </div>
      <div ref="playBtn" class="play-btn" @click="random">
        <i class="icon-play"></i>
        <span class="text">随机播放全部</span>
      </div>
      <div class="list-box" ref="listWrapper">
        <cube-scroll ref="searchScrollItem" :data="favoriteList" :options="scrollOptions" v-if="initialIndex==0">
          <ul class="list-wrapper">
            <li v-for="(item, index) in favoriteList" class="list-item" :key="index">
              <song-list @click="selectSong" :song="item" :index="index"></song-list>
            </li>
          </ul>
        </cube-scroll>
        <cube-scroll ref="playScrollItem" :data="playHistory" :options="scrollOptions" v-if="initialIndex==1">
          <ul class="list-wrapper">
            <li v-for="(item, index) in playHistory" class="list-item" :key="index">
              <song-list @click="selectSong" :song="item" :index="index"></song-list>
            </li>
          </ul>
        </cube-scroll>
      </div>
      <div class="no-result-wrapper" v-show="noResult">
        <no-result :title="noResultDesc"></no-result>
      </div>
    </div>
  </transition>
</template>

<script type="text/ecmascript-6">
  import SongList from 'components/song-list/song-list'
  import NoResult from 'components/no-result/no-result'
  import Song from 'common/js/song'
  import { mapGetters, mapActions } from 'vuex'
  import { playlistMixin } from 'common/js/mixin'

  export default {
    name: 'user-center',
    mixins: [playlistMixin],
    data() {
      return {
        currentIndex: 0,
        selectedLabel: '我喜欢的',
        tabLabels: [{
          label: '我喜欢的'
          }, {
            label: '最近听的'
          }],
        slideOptions: {
          listenScroll: true,
          probeType: 3,
          /* lock y-direction when scrolling horizontally and  vertically at the same time */
          directionLockThreshold: 0
        },
        scrollOptions: {
          click: true,
          /* lock x-direction when scrolling horizontally and  vertically at the same time */
          directionLockThreshold: 0
        }
      }
    },
    activated() {
      if (this.initialIndex === 0) {
        this.$refs.searchScrollItem.refresh()
      } else {
        this.$refs.playScrollItem.refresh()
      }
    },
    computed: {
      initialIndex () {
        let index = 0
        index = this.tabLabels.findIndex((item) => { return item.label === this.selectedLabel })
        return index
      },
      noResult() {
        if (this.initialIndex === 0) {
          return !this.favoriteList.length
        } else {
          return !this.playHistory.length
        }
      },
      noResultDesc() {
        if (this.initialIndex === 0) {
          return '暂无收藏歌曲'
        } else {
          return '你还没有听过歌曲'
        }
      },
      ...mapGetters([
        'favoriteList',
        'playHistory'
      ])
    },
    methods: {
      handlePlaylist(playlist) {
        const bottom = playlist.length > 0 ? '60px' : ''
        this.$refs.listWrapper.style.bottom = bottom
        this.$refs.searchScrollItem && this.$refs.searchScrollItem.refresh()
        this.$refs.playScrollItem && this.$refs.playScrollItem.refresh()
      },
      switchItem(index) {
        this.currentIndex = index
      },
      selectSong(song) {
        this.insertSong(new Song(song))
      },
      back() {
        this.$router.back()
      },
      random() {
        let list = this.initialIndex === 0 ? this.favoriteList : this.playHistory
        if (list.length === 0) {
          return
        }
        list = list.map((song) => {
          return new Song(song)
        })
        this.randPlay({
          list
        })
      },
      ...mapActions([
        'insertSong',
        'randPlay'
      ])
    },
    components: {
      SongList,
      NoResult
    }
  }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  >>>.cube-tab-bar
        width: 240px;
        margin: 0 auto;
        border: 1px solid #333;
        border-radius: 5px;
        .cube-tab
          padding: 8px;
          font-size: 14px;
          color: hsla(0,0%,100%,.3);
          &.cube-tab_active
            background: #333;
            color: #fff;
  .user-center
    position: fixed
    top: 0
    bottom: 0
    z-index: 100
    width: 100%
    background: $color-background
    &.slide-enter-active, &.slide-leave-active
      transition: all 0.3s
    &.slide-enter, &.slide-leave-to
      transform: translate3d(100%, 0, 0)
    .back
      position absolute
      top: 0
      left: 6px
      z-index: 50
      .icon-back
        display: block
        padding: 10px
        font-size: $font-size-large-x
        color: $color-theme
    .switches-wrapper
      margin: 10px 0 30px 0
    .play-btn
      box-sizing: border-box
      width: 135px
      padding: 7px 0
      margin: 0 auto
      text-align: center
      border: 1px solid  $color-text-l
      color: $color-text-l
      border-radius: 100px
      font-size: 0
      .icon-play
        display: inline-block
        vertical-align: middle
        margin-right: 6px
        font-size: $font-size-medium-x
      .text
        display: inline-block
        vertical-align: middle
        font-size: $font-size-small
    .list-box
      position: absolute
      top: 110px
      bottom: 0
      width: 100%
      padding:5px 20px 15px
      .list-scroll
        height: 100%
        overflow: hidden
        .list-inner
          padding: 20px 30px
    .no-result-wrapper
      position: absolute
      width: 100%
      top: 50%
      transform: translateY(-50%)
</style>