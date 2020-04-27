<template>
    <transition name="slide">
        <music-list :songs="songs" :rank="true" :bg-image="bgImage" :title="title"></music-list>
    </transition>
</template>

<script>
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
        activated() {
            this._getSongList()
        },
        created() {
            this._getSongList()
        },
        methods: {
            _getSongList() {
                console.log(this.album.songlist)
                this.songs = this._normalizeSongList(this.album.songlist)
            },
            _normalizeSongList(list) {
                const ret = []
                list.forEach((item) => {
                    const musicData = item.data
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
