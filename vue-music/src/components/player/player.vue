<template>
    <div class="player" v-show="playList.length>0">
        <transition name="normal"
                    @enter="enter"
                    @after-enter="afterEnter"
                    @leave="leave"
                    @after-leave="afterLeave"
        >
        <div class="normal-player" v-show="fullScreen">
            <div class="background">
                <img width="100%" height="100%" :src="currentSong.image">
            </div>
            <div class="top">
                <div class="back" @click="back">
                    <i class="icon-back"></i>
                </div>
                <h1 class="title" v-html="currentSong.name"></h1>
                <h2 class="subtitle" v-html="currentSong.singer"></h2>
             </div>
            <div class="middle">
                <cube-slide class="scroll-wrapper" ref="slide" :loop="false" :data="scrollData"  :auto-play="false" @change="changePage"  :options="options" @scroll="scroll">
                    <cube-slide-item >
                        <div class="middle-l">
                            <div class="cd-wrapper" ref="cdWrapper">
                                <div class="cd" :class="cdCls">
                                    <img class="image" :src="currentSong.image">
                                </div>
                            </div>
                            <div class="playing-lyric-wrapper">
                                <div class="playing-lyric">{{playingLyric}}</div>
                            </div>
                        </div>
                    </cube-slide-item>
                    <cube-slide-item >
                        <div class="middle-r">
                            <cube-scroll
                                    class="lyric-wrapper"
                                    ref="lyricScroll"
                                    :data="currentLyric &&  currentLyric.lines"
                                    :options="lyricOption"
                            >
                                <div v-if="currentLyric">
                                    <p ref="lyricLine" class="text" v-for="(line, index) in currentLyric.lines" :key="index" :class="{'current': currentLineNum === index}">{{line.txt}}</p>
                                </div>
                            </cube-scroll>
                        </div>
                    </cube-slide-item>
                </cube-slide>

            </div>
            <div class="bottom">
                <div class="progress-wrapper">
                    <span class="time time-l">{{format(currentTime)}}</span>
                    <div class="progress-bar-wrapper">
                        <progress-bar :percent="percent" @percentChange="percentChange"></progress-bar>
                    </div>
                    <span class="time time-r">{{format(currentSong.duration)}}</span>
                </div>
                <div class="operators">
                    <div class="icon i-c-2">
                        <i :class="iconModel" @click="changeModel"></i>
                    </div>
                    <div class="icon i-c-2" :class="disableCls">
                        <i class="icon-prev" @click="prev"></i>
                    </div>
                    <div class="icon i-center" :class="disableCls">
                        <i @click="togglePlaying" :class="playIcon"></i>
                    </div>
                    <div class="icon i-c-2" :class="disableCls">
                        <i class="icon-next" @click="next"></i>
                    </div>
                    <div class="icon i-c-2">
                        <i class="icon" @click="toggleFavorite(currentSong)" :class="getFavoriteIcon(currentSong)"></i>
                    </div>
                </div>
            </div>
        </div>
        </transition>
        <transition name="mini">
        <div class="mini-player" v-show="!fullScreen" @click="open">
            <div class="icon">
                <img :class="cdCls" width="40" height="40" :src="currentSong.image">
            </div>
            <div class="text">
                <h2 class="name" v-html="currentSong.name"></h2>
                <p class="desc"  v-html="currentSong.singer"></p>
            </div>
            <div class="control" :class="disableCls">
                <progress-circle :radius="32" :percent="percent">
                    <i @click.stop="togglePlaying" class="icon-mini" :class="miniIcon"></i>
                </progress-circle>
            </div>
            <div class="control" @click.stop="showPlayList">
                <i class="icon-playlist"></i>
            </div>
        </div>
        </transition>
        <playlist ref="playList"></playlist>
        <audio ref="audio" :src="songSrc" v-if="songSrc!=''" @canplay="ready" @error="error" @timeupdate="updataTime" @ended="onSongEnd"></audio>
    </div>
</template>

<script type="text/ecmascript-6">
import animations from 'create-keyframe-animation'
import { getSongFile } from 'api/song'
// import { ERR_OK } from 'api/config'

import ProgressBar from 'components/progress-bar/progress-bar'
import ProgressCircle from 'components/progress-circle/progress-circle'
import { mapActions } from 'vuex'
import { playerMixin } from 'common/js/mixin'
import { playMode } from 'common/js/config'
import Playlist from 'components/playlist/playlist'
import Lyric from 'lyric-parser'

export default {
    mixins: [playerMixin],
    name: 'player',
    data() {
        return {
            defdata: ['song', 'lyric'],
            songSrc: '',
            songReady: false,
            currentTime: 0,
            currentLyric: {},
            currentLineNum: 0,
            playingLyric: '',
            options: {
                listenScroll: true,
                probeType: 3
            },
            lyricOption: {}
        }
    },
    methods: {
        showPlayList() {
            this.$refs.playList.show()
        },
        changePage() {},
        scroll ({ x, y }) {
            // console.log(x, y)
        },
        percentChange(p) {
            const currentTime = p * this.currentSong.duration | 0
            this.$refs.audio.currentTime = currentTime
            if (this.currentLyric) {
                this.currentLyric.seek(currentTime * 1000)
            }
        },
        format(int) {
            int = int | 0
            const minute = ('0' + (int / 60 | 0)).slice(-2)
            const second = ('0' + (int % 60)).slice(-2)
            return `${minute}:${second}`
        },
        updataTime(e) {
            this.currentTime = e.target.currentTime
        },
        ready() {
            this.songReady = true
            this.savePlayHistory(this.currentSong)
        },
        error() {
            this.songReady = true
        },
        next() {
            if (!this.songReady) { return }
            if (this.playList.length === 1) {
                this.loop()
                return
            }
            let index = this.currentIndex + 1
            if (index === this.playList.length) {
                index = 1
            }
            this.setCurrentIndex(index)
        },
        prev() {
            if (!this.songReady) { return }
            if (this.playList.length === 1) {
                this.loop()
                return
            }
            let index = this.currentIndex - 1
            if (index < 0) {
                index = this.playList.length - 1
            }
            this.setCurrentIndex(index)
        },
        _getSongAddr(mid, cb) {
            getSongFile(mid).then((res) => {
                cb && cb(res)
                // if (res.code === ERR_OK) {
                //     console.log('getSongFile', res)
                //     const addr = 'http://ws.stream.qqmusic.qq.com/' + res.req_0.data.midurlinfo[0].flowurl
                //     cb && cb(addr)
                // }
            })
        },
        loop() {
            this.setPlayingState(false)
            this.$refs.audio.currentTime = 0
            this.currentLineNum = 0
            this.$refs.lyricScroll.scrollTo(0, 0, 1000)
            if (this.currentLyric) {
                this.currentLyric.seek(0)
            }
            setTimeout(() => {
                this.setPlayingState(true)
            }, 1)
        },
        onSongEnd() {
            if (this.model === playMode.loop) {
                this.loop()
            } else {
                this.next()
            }
        },
        _getPosAndScale() {
            const targetWidth = 40
            const paddingLeft = 40
            const paddingBottom = 30
            const paddingTop = 80
            const width = window.innerWidth * 0.8
            const scale = targetWidth / width
            const x = -(window.innerWidth / 2 - paddingLeft)
            const y = window.innerHeight - paddingTop - width / 2 - paddingBottom
            return {
                x, y, scale
            }
        },
        enter(el, done) {
            const { x, y, scale } = this._getPosAndScale()
            const animation = {
                0: {
                    transform: `translate3d(${x}px,${y}px,0) scale(${scale})`
                },
                60: {
                    transform: 'translate3d(0,0,0) scale(1.1)'
                },
                100: {
                    transform: 'translate3d(0,0,0) scale(1)'
                }
            }
            animations.registerAnimation({
                name: 'move',
                animation,
                presets: {
                    duration: 400,
                    easing: 'linear'
                }
            })
            animations.runAnimation(this.$refs.cdWrapper, 'move', done)
        },
        afterEnter() {
            animations.unregisterAnimation('move')
            this.$refs.cdWrapper.style.animation = ''
        },
        leave(el, done) {
            this.$refs.cdWrapper.style.transition = 'all 0.4s'
            const { x, y, scale } = this._getPosAndScale()
            this.$refs.cdWrapper.style['transform'] = `translate3d(${x}px,${y}px,0) scale(${scale})`
            this.$refs.cdWrapper.addEventListener('transitionend', done)
        },
        afterLeave() {
            this.$refs.cdWrapper.style.transition = ''
            this.$refs.cdWrapper.style['transform'] = ''
        },
        back() {
            this.setFullScreen(false)
        },
        open() {
            this.setFullScreen(true)
        },
        lyricHandle({ lineNum, txt }) {
            // console.log(lineNum)
            this.currentLineNum = lineNum
            if (lineNum > 5) {
                // const lineEl = this.$refs.lyricLine[lineNum - 5]
                this.$refs.lyricScroll.scrollTo(0, -(lineNum - 5) * 32, 1000)
            } else {
                this.$refs.lyricScroll.scrollTo(0, 0, 1000)
            }
            this.playingLyric = txt
        },
        getLyric() {
            console.log(this.currentSong)
            this.currentSong.getLyric().then((lyric) => {
                this.currentLyric = new Lyric(lyric, this.lyricHandle)
                console.log(this.currentLyric)
                if (this.playing) {
                    this.currentLyric.play()
                }
            })
        },
        ...mapActions(['savePlayHistory'])
    },
    computed: {
        scrollData() {
          const ret = []
          ret.push(this.currentSong)
          ret.push(this.currentLyric)
          return ret
        },
        percent() {
          return this.currentTime / this.currentSong.duration
        },
        miniIcon() {
            return this.playing ? 'icon-pause-mini' : 'icon-play-mini'
        },
        disableCls() {
            return this.songReady ? '' : 'disable'
        },
        cdCls() {
            return this.playing ? 'play' : 'pause'
        },
        playIcon() {
            return this.playing ? 'icon-pause' : 'icon-play'
        }
    },
    watch: {
        currentSong(newSong, oldSong) {
            if (!newSong.id) {
                return
            }
            if (newSong.id === oldSong.id) {
                return
            }
            this.songReady = false
            if (this.currentLyric && 'stop' in this.currentLyric) {
                this.currentLyric.stop()
                this.currentTime = 0
                this.playingLyric = ''
                this.currentLineNum = 0
            }
            clearTimeout(this.timer)
            this.timer = setTimeout(() => {
                this._getSongAddr(this.currentSong.mid, (addr) => {
                    console.log(addr)
                    this.songSrc = addr
                    this.$nextTick(() => {
                        this.playing === true && this.$refs.audio.play()
                        this.getLyric()
                    })
                })
            }, 200)
        },
        playing(newPlay) {
            if (this.songSrc !== '') {
                this.$nextTick(() => {
                    const audio = this.$refs.audio
                    newPlay ? audio.play() : audio.pause()
                })
            }
        }
    },
    components: {
        ProgressBar,
        ProgressCircle,
        Playlist
    }
}
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  @import "~common/stylus/mixin"

  .player
    .cube-slide-group
        width: 100%
    .normal-player
      position: fixed
      left: 0
      right: 0
      top: 0
      bottom: 0
      z-index: 150
      background: $color-background
      .background
        position: absolute
        left: 0
        top: 0
        width: 100%
        height: 100%
        z-index: -1
        opacity: 0.6
        filter: blur(20px)
      .top
        position: relative
        margin-bottom: 25px
        .back
          position absolute
          top: 0
          left: 6px
          z-index: 50
          .icon-back
            display: block
            padding: 9px
            font-size: $font-size-large-x
            color: $color-theme
            transform: rotate(-90deg)
        .title
          width: 70%
          margin: 0 auto
          line-height: 40px
          text-align: center
          no-wrap()
          font-size: $font-size-large
          color: $color-text
        .subtitle
          line-height: 20px
          text-align: center
          font-size: $font-size-medium
          color: $color-text
      .middle
        position: fixed
        width: 100%
        top: 80px
        bottom: 170px
        white-space: nowrap
        font-size: 0
        .middle-l
          display: inline-block
          vertical-align: top
          position: relative
          width: 100%
          height: 0
          padding-top: 80%
          .cd-wrapper
            position: absolute
            left: 10%
            top: 0
            width: 80%
            height: 100%
            .cd
              width: 100%
              height: 100%
              box-sizing: border-box
              border: 10px solid rgba(255, 255, 255, 0.1)
              border-radius: 50%
              &.play
                animation: rotate 20s linear infinite
              &.pause
                animation-play-state: paused
              .image
                position: absolute
                left: 0
                top: 0
                width: 100%
                height: 100%
                border-radius: 50%

          .playing-lyric-wrapper
            width: 80%
            margin: 30px auto 0 auto
            overflow: hidden
            text-align: center
            .playing-lyric
              height: 20px
              line-height: 20px
              font-size: $font-size-medium
              color: $color-text-l
        .middle-r
          display: inline-block
          vertical-align: top
          width: 100%
          height: 100%
          overflow: hidden
          .lyric-wrapper
            width: 80%
            margin: 0 auto
            overflow: hidden
            text-align: center
            .text
              line-height: 32px
              color: $color-text-l
              font-size: $font-size-medium
              &.current
                color: $color-text
      .bottom
        position: absolute
        bottom: 50px
        width: 100%
        .dot-wrapper
          text-align: center
          font-size: 0
          .dot
            display: inline-block
            vertical-align: middle
            margin: 0 4px
            width: 8px
            height: 8px
            border-radius: 50%
            background: $color-text-l
            &.active
              width: 20px
              border-radius: 5px
              background: $color-text-ll
        .progress-wrapper
          display: flex
          align-items: center
          width: 80%
          margin: 0px auto
          padding: 10px 0
          .time
            color: $color-text
            font-size: $font-size-small
            flex: 0 0 30px
            line-height: 30px
            width: 30px
            &.time-l
              text-align: left
            &.time-r
              text-align: right
          .progress-bar-wrapper
            padding: 0 10px
            flex: 1
        .operators
          display: flex
          align-items: center
          .icon
            flex: 1
            color: $color-theme
            &.disable
              color: $color-theme-d
            i
              font-size: 30px
          .i-left
            text-align: right
          .i-c-2
                text-align: center
          .i-center
            padding: 0 20px
            text-align: center
            i
              font-size: 40px
          .i-right
            text-align: left
          .icon-favorite
            color: $color-sub-theme
      &.normal-enter-active, &.normal-leave-active
        transition: all 0.4s
        .top, .bottom
          transition: all 0.4s cubic-bezier(0.86, 0.18, 0.82, 1.32)
      &.normal-enter, &.normal-leave-to
        opacity: 0
        .top
          transform: translate3d(0, -100px, 0)
        .bottom
          transform: translate3d(0, 100px, 0)
    .mini-player
      display: flex
      align-items: center
      position: fixed
      left: 0
      bottom: 0
      z-index: 180
      width: 100%
      height: 60px
      background: $color-highlight-background
      &.mini-enter-active, &.mini-leave-active
        transition: all 0.4s
      &.mini-enter, &.mini-leave-to
        opacity: 0
      .icon
        flex: 0 0 40px
        width: 40px
        padding: 0 10px 0 20px
        img
          border-radius: 50%
          &.play
            animation: rotate 10s linear infinite
          &.pause
            animation-play-state: paused
      .text
        display: flex
        flex-direction: column
        justify-content: center
        flex: 1
        line-height: 20px
        overflow: hidden
        .name
          margin-bottom: 2px
          no-wrap()
          font-size: $font-size-medium
          color: $color-text
        .desc
          no-wrap()
          font-size: $font-size-small
          color: $color-text-d
      .control
        flex: 0 0 30px
        width: 30px
        padding: 0 10px
        .icon-play-mini, .icon-pause-mini, .icon-playlist
          font-size: 30px
          color: $color-theme-d
        .icon-mini
          font-size: 32px
          position: absolute
          left: 0
          top: 0
    .scroll-wrapper
        overflow: unset
>>>.cube-slide-dots
        bottom: -20px !important;
  @keyframes rotate
    0%
      transform: rotate(0)
    100%
      transform: rotate(360deg)
</style>
