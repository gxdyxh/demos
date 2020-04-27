<template>
  <transition name="list-fade">
    <div class="playlist"  v-show="showFlag" @click="hide">
      <div class="list-wrapper" @click.stop>
        <div class="list-header">
          <h1 class="title">
            <i class="icon" :class="iconModel" @click="changeModel"></i>
            <span class="text">{{modeText}}</span>
            <span class="clear" @click.stop="delAll"><i class="icon-clear" ></i></span>
          </h1>
        </div>
        <div class="list-content">
          <cube-scroll
                  ref="playListScroll"
                  :data="sequenceList"
                  :options="playListScrollOptions">
           <transition-group name="list" tag="ul">
            <li ref="listItem" class="item" v-for="(item, index) in sequenceList" :key="item.id" @click="selectItem(item, index)">
              <i class="current" :class="getCurrentIcon(item)"></i>
              <span class="text">{{item.name}}</span>
              <span @click.stop="toggleFavorite(item)" class="like"><i :class="getFavoriteIcon(item)"></i></span>
              <span class="delete" @click.stop="delItem(item)"><i class="icon-delete"></i></span>
            </li>
            </transition-group>
          </cube-scroll>
        </div>
        <div class="list-operate">
          <div  class="add" @click="showAddSong">
            <i class="icon-add"></i>
            <span class="text">添加歌曲到队列</span>
          </div>
        </div>
        <div  class="list-close" @click="hide">
          <span>关闭</span>
        </div>
      </div>
    </div>
  </transition>
</template>
<script type="text/ecmascript-6">
  import { mapActions } from 'vuex'
  import { playMode } from 'common/js/config'
  import { playerMixin } from 'common/js/mixin'
  export default {
    mixins: [playerMixin],
    data() {
      return {
        showFlag: false,
        refreshDelay: 120,
        playListScrollOptions: {
        }
      }
    },
    methods: {
      showAddSong() {
        this.addSongComp = this.addSongComp || this.$createAddSong({
          $props: {}
        })
          console.log(this.addSongComp)
        this.addSongComp.show()
      },
      show() {
        this.showFlag = true
        setTimeout(() => {
          this.$refs.playListScroll.refresh()
          this.scrollToCurrent(this.currentSong)
        }, 30)
      },
      hide() {
        this.showFlag = false
      },
      selectItem(item, index) {
        if (this.model === playMode.random) {
          index = this.playList.findIndex((song) => {
            return song.id === item.id
          })
        }
        this.setCurrentIndex(index)
      },
      delItem(item) {
        this.delectSong(item)
        if (!this.playList.length) {
          this.hide()
        }
      },
      delAll() {
        this.$createDialog({
          type: 'confirm',
          title: '',
          zIndex: 201,
          content: '是否清空播放列表',
          confirmBtn: {
            text: '清空',
            active: true,
            disabled: false,
            href: 'javascript:;'
          },
          cancelBtn: {
            text: '取消',
            active: false,
            disabled: false,
            href: 'javascript:;'
          },
          onConfirm: () => {
            this.clearSong()
          }
        }).show()
      },
      scrollToCurrent(current) {
        const index = this.sequenceList.findIndex((song) => {
          return current.id === song.id
        })
        // console.log(index)
        // this.$refs.playListScroll.scrollTo(0, index * 40)
        this.$refs.playListScroll.scrollToElement(this.$refs.listItem[index])
      },
      getCurrentIcon(item) {
        return this.currentSong.id === item.id ? 'icon-play' : ''
      },
      ...mapActions([
              'delectSong',
              'clearSong'
      ])
    },
    watch: {
      currentSong(newSong, oldSong) {
        if (!this.showFlag || newSong.id === oldSong.id) {
          return
        }
        this.scrollToCurrent(newSong)
      }
    },
    computed: {
      modeText() {
        return this.model === playMode.sequence ? '顺序播放' : this.model === playMode.random ? '随机播放' : '循环播放'
      }
    }
  }
</script>
<style scoped lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  @import "~common/stylus/mixin"

  .playlist
    position: fixed
    left: 0
    right: 0
    top: 0
    bottom: 0
    z-index: 180
    background-color: $color-background-d
    &.list-fade-enter-active, &.list-fade-leave-active
      transition: opacity 0.3s
      .list-wrapper
        transition: all 0.3s
    &.list-fade-enter, &.list-fade-leave-to
      opacity: 0
      .list-wrapper
        transform: translate3d(0, 100%, 0)
    &.list-fade-enter
    .list-wrapper
      position: absolute
      left: 0
      bottom: 0
      width: 100%
      background-color: $color-highlight-background
      .list-header
        position: relative
        padding: 20px 30px 10px 20px
        .title
          display: flex
          align-items: center
          .icon
            margin-right: 10px
            font-size: 30px
            color: $color-theme-d
          .text
            flex: 1
            font-size: $font-size-medium
            color: $color-text-l
          .clear
            extend-click()
            .icon-clear
              font-size: $font-size-medium
              color: $color-text-d
      .list-content
        height: 240px
        overflow: hidden
        .item
          display: flex
          align-items: center
          height: 40px
          padding: 0 30px 0 20px
          overflow: hidden
          &.list-enter-active, &.list-leave-active
            transition: all 0.1s
          &.list-enter, &.list-leave-to
            height: 0
          .current
            flex: 0 0 20px
            width: 20px
            font-size: $font-size-small
            color: $color-theme-d
          .text
            flex: 1
            no-wrap()
            font-size: $font-size-medium
            color: $color-text-d
          .like
            extend-click()
            margin-right: 15px
            font-size: $font-size-small
            color: $color-theme
            .icon-favorite
              color: $color-sub-theme
          .delete
            extend-click()
            font-size: $font-size-small
            color: $color-theme
      .list-operate
        width: 140px
        margin: 20px auto 30px auto
        .add
          display: flex
          align-items: center
          padding: 8px 16px
          border: 1px solid $color-text-l
          border-radius: 100px
          color: $color-text-l
          .icon-add
            margin-right: 5px
            font-size: $font-size-small-s
          .text
            font-size: $font-size-small
      .list-close
        text-align: center
        line-height: 50px
        background: $color-background
        font-size: $font-size-medium-x
        color: $color-text-l
</style>