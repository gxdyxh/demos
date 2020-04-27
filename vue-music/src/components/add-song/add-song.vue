<template>
  <transition name="slide">
    <div class="add-song" v-show="showFlag" @click.stop>
      <div class="header">
        <h1 class="title">添加歌曲到列表</h1>
        <div class="close" @click="hide">
          <i class="icon-close"></i>
        </div>
      </div>
      <div class="search-box-wrapper">
        <search-box ref="searchBox" @query="onQueryChange" placeholder="搜索歌曲"></search-box>
      </div>
      <div class="shortcut" v-show="!query">
          <cube-tab-bar v-model="selectedLabel"
                  :show-slider=false
                  :use-transition=false
                  ref="tabNav"
                  :data="tabLabels">
          </cube-tab-bar>
        <div class="tab-slide-container">
              <cube-scroll ref="playScrollItem" :data="playHistory" :options="scrollOptions" v-if="initialIndex==0">
                <ul class="list-wrapper">
                  <li v-for="(item, index) in playHistory" class="list-item" :key="index">
                    <song-list @click="selectSong" :song="item" :index="index"></song-list>
                  </li>
                </ul>
              </cube-scroll>
          <cube-scroll ref="searchScrollItem" :data="searchHistoryList" :options="scrollOptions" v-if="initialIndex==1">
              <search-list @select="addQuery" @delete="delSearchHisroty"  :searches="searchHistoryList"></search-list>
          </cube-scroll>
        </div>
      </div>
      <div class="search-result" v-show="query">
        <suggest :query="query" :showSinger="showSinger" @select="onSelect"></suggest>
      </div>
    </div>
  </transition>
</template>

<script type="text/ecmascript-6">
  import SearchBox from 'components/search-box/search-box'
  import Suggest from 'components/suggest/suggest'
  import { mapGetters, mapActions } from 'vuex'
  import Song from 'common/js/song'
  import { searchMixin } from 'common/js/mixin'
  import SongList from 'components/song-list/song-list'
  import SearchList from 'components/search-list/search-list'

  export default {
    name: 'add-song',
    mixins: [searchMixin],
    data() {
      return {
        showFlag: false,
        showSinger: false,
        currentIndex: 0,
        songs: [],
        selectedLabel: '最近播放',
        tabLabels: [{
          label: '最近播放'
        }, {
          label: '搜索历史'
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
    computed: {
      initialIndex () {
        let index = 0
        index = this.tabLabels.findIndex((item) => { return item.label === this.selectedLabel })
        return index
      },
      ...mapGetters([
        'playHistory'
      ])
    },
    methods: {
      changePage (current) {
        this.selectedLabel = this.tabLabels[current].label
      },
      show() {
        this.showFlag = true
        setTimeout(() => {
          if (this.initialIndex === 0) {
            this.$refs.playScrollItem.refresh()
          } else {
            this.$refs.searchScrollItem.refresh()
          }
        }, 20)
      },
      hide() {
        this.showFlag = false
      },
      selectSong(song, index) {
        console.log(song)
        if (index !== 0) {
          this.insertSong(new Song(song))
          this.showTip()
        }
      },
      onSelect(item) {
        // console.log('onSelect', item)
        this.saveSearchHisroty(item)
        this.showTip()
      },
      switchItem(index) {
        this.currentIndex = index
      },
      ...mapActions([
        'insertSong'
      ])
    },
    components: {
      SearchBox,
      Suggest,
      SongList,
      SearchList
    }
  }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  @import "~common/stylus/mixin"
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
  .add-song
    position: fixed
    top: 0
    bottom: 0
    width: 100%
    z-index: 200
    background: $color-background
    &.slide-enter-active, &.slide-leave-active
      transition: all 0.3s
    &.slide-enter, &.slide-leave-to
      transform: translate3d(100%, 0, 0)
    .header
      position: relative
      height: 44px
      text-align: center
      .title
        line-height: 44px
        font-size: $font-size-large
        color: $color-text
      .close
        position: absolute
        top: 0
        right: 8px
        .icon-close
          display: block
          padding: 12px
          font-size: 20px
          color: $color-theme

    .search-box-wrapper
      margin: 20px
    .shortcut
      position: fixed;
      top: 124px;
      bottom: 0;
      width: 100%;
      .tab-slide-container
        height: calc(100% - 40px)
      .list-wrapper
        padding:20px
        box-sizing: content-box;
      .search-list
        padding:20px
        box-sizing: content-box;
    .search-result
      position: fixed
      top: 124px
      bottom: 0
      width: 100%
</style>