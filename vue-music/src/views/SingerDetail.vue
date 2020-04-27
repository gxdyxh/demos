<template>
  <transition name="slide">
    <music-list :songs="songs" :bg-image="bgImage" :title="title"></music-list>
  </transition>
</template>

<script>
  import { getSongList } from 'api/singer'
  import { ERR_OK } from 'api/config'
  import { createSong } from 'common/js/song'
  import { mapGetters } from 'vuex'

  import MusicList from 'components/music-list/music-list'

  export default {
    name: 'singer-detail',
    data() {
      return {
        songs: []
      }
    },
    activated() {
      this._getSongList(1)
    },
    created() {
      this._getSongList(1)
    },
    methods: {
      _getSongList(page) {
        getSongList(this.singer.id, page).then((res) => {
          if (res.code === ERR_OK) {
            console.log(res.data.list)
            this.songs = this.songs.concat(this._normalizeSongList(res.data.list))
          }
        })
      },
      _normalizeSongList(list) {
        const ret = []
        list.forEach((item) => {
          const musicData = item.musicData
          if (musicData.songid && musicData.albummid) {
            ret.push(createSong(musicData))
          }
        })
        return ret
      }
    },
    computed: {
      title() {
        return this.singer.name
      },
      bgImage() {
        return this.singer.avatar
      },
      ...mapGetters([
        'singer'
      ])
    },
    components: {
      MusicList
    }
  }
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
  .slide-enter-active,.slide-leave-active
    transition: all 0.3s
  .slide-enter,.slide-leave-to
    transform: translate3d(100%,0,0)
</style>
