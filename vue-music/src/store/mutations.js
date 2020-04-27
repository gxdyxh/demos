import * as types from './mutation-types'
 // import Vue from 'vue'
const mutations = {
    [types.SET_SINGER](state, singer) {
       state.singer = singer
        // state.default = { ...state.default, singer }
        //  Vue.set(state.)
    },
    [types.SET_ALBUM](state, album) {
        state.album = album
        // state.default = { ...state.default, singer }
        //  Vue.set(state.)
    },
    [types.SET_PLAYING_STATE](state, flag) {
        state.playing = flag
    },
    [types.SET_FULLSCREEN](state, flag) {
        state.fullScreen = flag
    },
    [types.SET_PLAYLIST](state, list) {
        state.playList = list
    },
    [types.SET_SEQUENCE_LIST](state, list) {
        state.sequenceList = list
    },
    [types.SET_SEQUENCE_LIST](state, list) {
        state.sequenceList = list
    },
    [types.SET_PLAY_MODE](state, mode) {
        state.model = mode
    },
    [types.SET_CURRENT_INDEX](state, index) {
        state.currentIndex = index
    },
    [types.INS_PLAYLIST](state, song, index) {
        state.playList.splice(index, 0, song)
    },
    [types.INS_SEQUENCE_LIST](state, song, index) {
        state.sequenceList.splice(index, 0, song)
    },
    [types.SPLICE_PLAYLIST](state, index) {
        state.playList.splice(index, 1)
    },
    [types.SPLICE_SEQUENCE_LIST](state, index) {
        state.sequenceList.splice(index, 1)
    },
    [types.SET_SEARCH_HISTORY](state, list) {
        state.searchHistoryList = list
    },
    [types.SET_PLAY_HISTORY](state, list) {
        state.playHistory = list
    },
    [types.SET_FAVORITE_LIST](state, list) {
        state.favoriteList = list
    }
}
export default mutations