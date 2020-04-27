import { mapGetters, mapMutations, mapActions } from 'vuex'
import { playMode } from 'common/js/config'
import { shuffle } from 'common/js/util'
export const playlistMixin = {
  computed: {
    ...mapGetters([
      'playList'
    ])
  },
  mounted () {
    this.handlePlaylist(this.playList)
  },
  activated () {
    this.handlePlaylist(this.playList)
  },
  watch: {
    playList (newVal) {
      this.handlePlaylist(newVal)
    }
  },
  methods: {
    handlePlaylist () {
      throw new Error('component must implement handlePlaylist method')
    }
  }
}

export const playerMixin = {
  methods: {
    getFavoriteIcon(song) {
        return this.isFavorite(song) ? 'icon-favorite' : 'icon-not-favorite'
    },
    toggleFavorite(song) {
        if (this.isFavorite(song)) {
            this.delFavoriteList(song)
        } else {
            this.saveFavoriteList(song)
        }
    },
    isFavorite(song) {
        const index = this.favoriteList.findIndex((item) => {
            return item.id === song.id
        })
        return index > -1
    },
    changeModel() {
      const model = (this.model + 1) % 3
      console.log(model)
      this.setPlayMode(model)
      let list = null
      if (model === playMode.random) {
        list = shuffle(this.sequenceList)
      } else {
        list = this.sequenceList
      }
      this.restCurrentIndex(list)
      this.setPlaylist(list)
    },
    restCurrentIndex(list) {
      const index = list.findIndex((item) => {
        return item.id === this.currentSong.id
      })
      this.setCurrentIndex(index)
    },
    togglePlaying() {
      if (!this.songReady) { return }
      // console.log('playing', !this.playing)
      this.setPlayingState(!this.playing)
      if (this.currentLyric) {
        this.currentLyric.togglePlay()
      }
    },
    ...mapMutations({
      setFullScreen: 'SET_FULLSCREEN',
      setPlayingState: 'SET_PLAYING_STATE',
      setCurrentIndex: 'SET_CURRENT_INDEX',
      setPlayMode: 'SET_PLAY_MODE',
      setPlaylist: 'SET_PLAYLIST'
    }),
    ...mapActions([
        'saveFavoriteList',
        'delFavoriteList'
    ])
  },
  computed: {
    iconModel() {
      return this.model === playMode.sequence ? 'icon-sequence' : this.model === playMode.loop ? 'icon-loop' : 'icon-random'
    },
    ...mapGetters([
      'fullScreen',
      'playList',
      'currentSong',
      'playing',
      'currentIndex',
      'model',
      'sequenceList',
      'favoriteList'
    ])
  }
}

export const searchMixin = {
    data() {
        return {
            query: ''
        }
    },
    methods: {
        showTip() {
            this.topTipComp = this.topTipComp || this.$createTopTip({
                $props: {
                    msgText: '1首歌曲已添加到播放队列'
                }
            })
            this.topTipComp.show();
        },
        onSelect(item) {
            // console.log('onSelect', item)
            this.saveSearchHisroty(item)
        },
        onQueryChange(query) {
            this.query = query
        },
        addQuery(query) {
            this.$refs.searchBox.setQuery(query)
        },
        ...mapActions([
            'saveSearchHisroty',
            'delSearchHisroty',
            'clearSearchHisroty'
        ])
    },
    computed: {
        ...mapGetters([
            'searchHistoryList'
        ])
    }
}
