<template>
    <div class="rank" ref="rank">
        <cube-scroll
                ref="scroll"
                :data="topList"
                :options="options"
                class="list"
                v-show="topList.length>0"
        >
            <ul>
                <li @click="selectItem(item)" class="item" v-for="(item,topi) in topList" :key="topi">
                    <div class="icon">
                        <img width="100" height="100" v-lazy="item.frontPicUrl"/>
                    </div>
                    <ul class="songlist">
                        <li class="song" v-for="(song,index) in item.song" :key="index">
                            <span>{{index + 1}}.</span>
                            <span>{{song.title}}-{{song.singerName}}</span>
                        </li>
                    </ul>
                </li>
            </ul>
        </cube-scroll>
        <div class="loading-container" v-show="!topList.length">
            <cube-loading></cube-loading>
            <span>加载中...</span>
        </div>
    </div>
</template>
<script>
import { getTopList, getSongList } from 'api/rank'
import { ERR_OK } from 'api/config'
import { playlistMixin } from 'common/js/mixin'
import { mapMutations } from 'vuex'
export default {
    name: 'ran',
    mixins: [playlistMixin],
    data() {
      return {
          topList: [],
          options: {
              click: false
          }
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
        fetch() {
            if (!this.fetched) {
                this.fetched = true
                this._getTopList()
            }
        },
        _getTopList() {
            getTopList().then((res) => {
                if (res.code === ERR_OK) {
                    res.topList.data.group.forEach((group) => {
                        group.toplist.forEach((top) => {
                            this.topList.push(top)
                        })
                    })
                    console.log(this.topList)
                }
            })
        },
        selectItem(item) {
            console.log(item)
            const toast = this.$createToast({
                time: 0,
                txt: '加载中...'
            })
            toast.show()
            getSongList(item.topId, item.period).then((res) => {
                toast.hide()
                console.log(res)
                if (res.code === ERR_OK) {
                    const album = {}
                    album.title = res.topinfo.ListName
                    album.cover = res.topinfo.pic_v12
                    album.songlist = res.songlist
                    this.setAlbum({})
                    this.setAlbum(album)
                    this.$router.push({
                        path: `/top/${item.topId}`
                    })
                }
            })
        },
        ...mapMutations({
            setAlbum: 'SET_ALBUM'
        })
    }
}
</script>
<style scoped lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  @import "~common/stylus/mixin"

  .rank
      position: relative
      width: 100%
      top: 0px
      bottom: 0
      height: 100%;
      .item
        display: flex
        margin: 20px 20px
        height: 100px
        border-radius: 4px;
        overflow: hidden;
        &:first-child
            margin-top: 0px
        .icon
          flex: 0 0 100px
          width: 100px
          height: 100px
        .songlist
          flex: 1
          display: flex
          flex-direction: column
          justify-content: center
          padding: 0 20px
          height: 100px
          overflow: hidden
          background: $color-highlight-background
          color: $color-text-d
          font-size: $font-size-small
          .song
            no-wrap()
            line-height: 26px
      .loading-container
        position: absolute
        width: 100%
        top: 50%
        transform: translateY(-50%)
</style>