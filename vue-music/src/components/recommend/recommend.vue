<template>
    <div class="recommend" ref="recommend">
     <cube-scroll ref="scroll" :data="hots" :options="scrollOptions">
       <cube-slide ref="slide"
       :data="banner"
       :options="options"
       @change="changePage"
       v-show="focus.length">
        <cube-slide-item v-for="(item, index) in banner" :key="index" @click.native="clickHandler(item, index)">
            <img @load="imgLoad" class="slide_img" :src="item.image">
        </cube-slide-item>
      </cube-slide>
      <div class="recommend-list">
          <h1 class="list-title">热门歌单推荐</h1>
          <ul>
            <li @click.stop="selectItem(item)" v-for="(item,index) in hots" :key="index" class="item">
              <div class="icon">
                <img width="60" height="60" v-lazy="item.cover">
              </div>
              <div class="text">
                <h2 class="name" v-html="item.username"></h2>
                <p class="desc" v-html="item.title"></p>
              </div>
            </li>
          </ul>
      </div>
        <div class="loading-container" v-show="!hots.length">
            <cube-loading></cube-loading>
            <span>加载中...</span>
        </div>
        </cube-scroll>
    </div>
</template>
<script>
import { getRecommend } from 'api/recommend'
import { ERR_OK } from 'api/config'
import { playlistMixin } from 'common/js/mixin'
import { mapMutations } from 'vuex'

import { getSongListById, getSongListByMid } from 'api/album'

export default {
    mixins: [playlistMixin],
    name: 'recommend',
    data() {
        return {
            focus: [],
            hots: [],
            options: {
                listenScroll: true,
                probeType: 3,
                stopPropagation: true
            },
            scrollOptions: {
                /* lock x-direction when scrolling horizontally and  vertically at the same time */
                directionLockThreshold: 0,
                click: false
            }
        }
    },
    methods: {
        selectItem(item) {
            this._getAlbumSongById(item.content_id)
        },
        _getAlbumSongByMid(ablumId) {
            const toast = this.$createToast({
                time: 0,
                txt: '加载中...'
            })
            toast.show()
            getSongListByMid(ablumId).then((res) => {
                toast.hide()
                // console.log(res)
                if (res.code === ERR_OK) {
                    const album = {}
                    album.title = res.albumSonglist.data.songList[0].songInfo.album.name
                    album.cover = `https://y.gtimg.cn/music/photo_new/T002R300x300M000${ablumId}.jpg?max_age=2592000`
                    album.songlist = res.albumSonglist.data.songList
                    album.getType = 'mid'
                    this.setAlbum({})
                    this.setAlbum(album)
                    this.$router.push({
                        path: `/album/${ablumId}`
                    })
                }
            })
        },
        _getAlbumSongById(ablumId) {
            const toast = this.$createToast({
                time: 0,
                txt: '加载中...'
            })
            toast.show()
            getSongListById(ablumId).then((res) => {
                toast.hide()
                // console.log(res)
                if (res.code === ERR_OK) {
                    const album = {}
                    album.title = res.cdlist[0].dissname
                    album.cover = res.cdlist[0].logo
                    album.songlist = res.cdlist[0].songlist
                    this.setAlbum({})
                    this.setAlbum(album)
                    this.$router.push({
                        path: `/album/${ablumId}`
                    })
                }
            })
        },
        handlePlaylist(playList) {
            const bottom = playList.length > 0 ? 60 : 0
            if (this.$refs.scroll) {
                // this.$refs.scroll.$el.style.bottom = `${bottom}px`
                this.$refs.scroll.$el.style.height = `calc(100% - ${bottom}px)`
                this.$refs.scroll.refresh()
            }
        },
         changePage(current) {
            // console.log('当前轮播图序号为:' + current)
        },
        clickHandler(item, index) {
          // console.log(item.url.indexOf('http'))
            if (item.url.indexOf('http') < 0) {
                const reg = new RegExp('^[0-9]*$')
                if (reg.test(item.url)) {
                    this._getAlbumSongById(item.url);
                } else {
                    this._getAlbumSongByMid(item.url);
                }
            } else {
                window.location.href = item.url;
            }
        },
        _getRecommend() {
            getRecommend().then((res) => {
                console.log(res)
                if (res.code === ERR_OK) {
                    this.focus = res.focus.data.content
                    this.hots = res.recomPlaylist.data.v_hot
                    // this.$nextTick(() => {
                    //     this.$refs.scroll.refresh()
                    // })
                }
            })
        },
        imgLoad() {
            if (!this.bannerImgLoad) {
                this.$refs.scroll.refresh()
                this.bannerImgLoad = true
            }
        },
        fetch() {
            if (!this.fetched) {
                this.fetched = true
                this._getRecommend()
            }
        },
        ...mapMutations({
            setAlbum: 'SET_ALBUM'
        })
    },
    computed: {
        banner() {
            const result = []
            for (let i = 0; i < this.focus.length; i++) {
                result.push({
                    url: this.focus[i].jump_info.url,
                    image: this.focus[i].pic_info.url
                });
            }
            return result
        }
    }
}
</script>

<style lang="stylus">
.recommend
    height: 100%;
    .recommend-list
        .list-title
            height: 65px
            line-height: 65px
            text-align: center
            font-size: $font-size-medium
            color: $color-theme
        .item
            display: flex
            box-sizing: border-box
            align-items: center
            padding: 0 20px 20px 20px
            .icon
                flex: 0 0 60px
                width: 60px
                padding-right: 20px
            .text
                display: flex
                flex-direction: column
                justify-content: center
                flex: 1
                line-height: 20px
                overflow: hidden
                font-size: $font-size-medium
            .name
                margin-bottom: 10px
                color: $color-text
            .desc
                color: $color-text-d
    .cube-slide
        .cube-slide-item
            .slide_img
                width: 100%
        .cube-slide-dots
            bottom: 10px;
            span
                width: 8px;
                height: 8px;
                margin: 0 2px;
                border-radius: 4px;
                opacity: 0.7;
                &.active
                    width: 16px;
</style>
