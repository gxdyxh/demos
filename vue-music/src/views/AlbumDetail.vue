<template>
    <transition name="slide">
        <music-list :songs="songs" :bg-image="bgImage" :title="title"></music-list>
    </transition>
</template>

<script>
    // import { getSongListById } from 'api/album'
    // import { ERR_OK } from 'api/config'
    import { createSong } from 'common/js/song'
    import { mapGetters } from 'vuex'

    import MusicList from 'components/music-list/music-list'

    export default {
        name: 'album-detail',
        data() {
            return {
                songs: []
            }
        },
        mounted() {
            this._getSongList()
        },
        activated() {
            this._getSongList()
        },
        // created() {
        //     this._getSongList()
        // },
        methods: {
            _getSongList() {
                if (!this.album) {
                    this.$router.push({
                        path: '/'
                    })
                }
                // console.log('album', this.album)
                // console.log('_getSongList', this.album.songlist)
                if (this.album.getType === 'mid') {
                    this.songs = this._normalizeMidSongList(this.album.songlist)
                    console.log('_getSongList', this.songs)
                } else {
                    this.songs = this._normalizeSongList(this.album.songlist)
                }
                // this.album.content_id.test()
                // getSongListById(this.album.content_id).then((res) => {
                //     if (res.code === ERR_OK) {
                //         // console.log(res.req_0.data.songlist)
                //         //  console.log(this._normalizeSongList(res.req_0.data.songlist))
                //         this.songs = this._normalizeSongList(this.album.songlist)
                //     }
                // })
            },
            _checkIsPay(list) {
                const ret = []
                list.forEach((item) => {
                    console.log(item)
                    if (item.pay.payplay === 0) {
                        ret.push(item)
                    }
                })
                return ret
            },
            _normalizeSongList(list) {
                const ret = []
                list.forEach((item) => {
                    const musicData = item
                    if (musicData.songid && musicData.albummid) {
                        ret.push(createSong(musicData))
                    }
                })
                return ret
            },
            _normalizeMidSongList(list) {
                const ret = []
                list.forEach((item) => {
                    const musicData = item.songInfo
                    console.log(musicData)
                    musicData.songid = musicData.id
                    musicData.songmid = musicData.mid
                    musicData.songname = musicData.name
                    musicData.albummid = musicData.album.mid
                    musicData.albumname = musicData.album.name
                    if (musicData.songid && musicData.albummid) {
                        ret.push(createSong(musicData))
                    }
                })
                return ret
            }
        },
        computed: {
            title() {
                return this.album.title
            },
            bgImage() {
                return this.album.cover
            },
            ...mapGetters([
                'album'
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
