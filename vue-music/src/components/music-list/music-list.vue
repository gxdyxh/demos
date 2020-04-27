<template>
    <div class="music-list">
        <div class="back" @click="back">
            <i class="icon-back"></i>
        </div>
        <h1 class="title" v-html="title"></h1>
        <div class="bg-image" :style="bgStyle" ref="bgImg">
            <div class="play-wrapper" ref="playBtn" v-show="songs.length">
                <div class="play">
                    <i class="icon-play"></i>
                    <span class="text" @click="randmodPlay">随机播放全部</span>
                </div>
            </div>
            <div class="filter" ref="filter"></div>
        </div>
        <div class="bg-layer" ref="layer"></div>
        <cube-scroll
                ref="scroll"
                :data="songs"
                :options="options"
                :scroll-events="['scroll']"
                @scroll="onScrollHandle"
                class="list"
                v-show="songs.length>0"
                >
            <ul class="list-wrapper">
                <li v-for="(song, index) in songs" :key="index"   class="list-item">
                    <song-list :song="song" :rank="rank" :index="index" @click="selectItem"></song-list>
                </li>
            </ul>
        </cube-scroll>
        <div class="loading-container" v-show="!songs.length">
            <cube-loading></cube-loading>
            <span>加载中...</span>
        </div>
    </div>
</template>
<script type="text/ecmascript-6">
import { mapActions } from 'vuex'
import SongList from 'components/song-list/song-list'

import { playlistMixin } from 'common/js/mixin'

const RESERVED_HEIGHT = 40
    export default {
        mixins: [playlistMixin],
        name: 'music-list',
        props: {
            bgImage: {
                type: String,
                default: ''
            },
            title: {
                type: String,
                default: ''
            },
            songs: {
                type: Array,
                default() {
                    return []
                }
            },
            rank: {
                type: Boolean,
                default: false
            }
        },
        data() {
            return {
                options: {
                    probeType: 3,
                    click: true
                },
                scrollY: 0
            }
        },
        methods: {
            handlePlaylist(playList) {
                const bottom = playList.length > 0 ? 60 : 0
                console.log(bottom)
                this.$refs.scroll.$el.style.bottom = `${bottom}px`
                this.$refs.scroll.$el.style.height = `calc(100% - ${this.imageHeight + bottom}px)`
                this.$refs.scroll.refresh()
            },
            back() {
              this.$router.back()
            },
            onScrollHandle(pos) {
                this.scrollY = pos.y
            },
            randmodPlay() {
                this.randPlay({
                    list: this.songs
                })
            },
            selectItem(song, index) {
                this.selectPlay({
                    list: this.songs,
                    index
                })
            },
            ...mapActions([
                'selectPlay',
                'randPlay'
            ])
        },
        watch: {
            scrollY(newVal) {
                let zIndex = 0
                let scale = 1
                let blur = 0
                const percent = Math.abs(newVal / this.imageHeight)
                const translateY = Math.max(this.mihnTranslateY, newVal)
                this.$refs.layer.style.transform = `translate3d(0,${translateY}px,0)`;
                this.$refs.layer.style.webkitTransform = `translate3d(0,${translateY}px,0)`;
                if (newVal > 0) {
                    scale = 1 + percent
                } else {
                    blur = Math.min(20 * percent)
                }

                this.$refs.filter.style.backdropFilter = `blur(${blur}px)`
                this.$refs.filter.style.webkitBackdropFilter = `blur(${blur}px)`

                if (newVal < this.mihnTranslateY) {
                    zIndex = 10
                    this.$refs.bgImg.style.paddingTop = 0
                    this.$refs.bgImg.style.height = `${RESERVED_HEIGHT}px`
                    this.$refs.playBtn.style.display = 'none'
                } else {
                    this.$refs.bgImg.style.paddingTop = '70%'
                    this.$refs.bgImg.style.height = 0
                    this.$refs.playBtn.style.display = ''
                }
                this.$refs.bgImg.style.zIndex = zIndex
                this.$refs.bgImg.style.transform = `scale(${scale})`;
                this.$refs.bgImg.style.webkitTransform = `scale(${scale})`;
            }
        },
        mounted() {
            this.imageHeight = this.$refs.bgImg.clientHeight
            this.mihnTranslateY = -this.imageHeight + RESERVED_HEIGHT
            this.$refs.scroll.$el.style.height = `calc(100% - ${this.imageHeight}px)`
        },
        computed: {
            bgStyle() {
                return `background-image:url(${this.bgImage})`
            }
        },
        components: {
            SongList
        }
    }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
    @import "~common/stylus/variable"
    @import "~common/stylus/mixin"
    >>>.cube-scroll-wrapper
            overflow: unset
            position: fixed;
            bottom: 0;
.music-list
    position: fixed
    z-index: 100
    top: 0
    left: 0
    bottom: 0
    right: 0
    background: $color-background
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
    .title
      position: absolute
      top: 0
      left: 10%
      z-index: 40
      width: 80%
      no-wrap()
      text-align: center
      line-height: 40px
      font-size: $font-size-large
      color: $color-text
    .bg-image
      position: relative
      width: 100%
      height: 0
      padding-top: 70%
      transform-origin: top
      background-size: cover
      .play-wrapper
        position: absolute
        bottom: 20px
        z-index: 50
        width: 100%
        .play
          box-sizing: border-box
          width: 135px
          padding: 7px 0
          margin: 0 auto
          text-align: center
          border: 1px solid $color-theme
          color: $color-theme
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
      .filter
        position: absolute
        top: 0
        left: 0
        width: 100%
        height: 100%
        background: rgba(7, 17, 27, 0.4)
    .bg-layer
      position: fixed
      height: 100%
      background: $color-background
      width: 100%
    .list-wrapper
      padding: 20px 30px
</style>