import * as types from './mutation-types'
import { playMode } from 'common/js/config'
import { shuffle } from 'common/js/util'
import { saveSearch, deleteSearch, clearSearch, savePlay, saveFavorite, deleteFavorite } from 'common/js/cache'
export const selectPlay = function({ commit, state }, { list, index }) {
    const songId = list[index].id
    commit(types.SET_SEQUENCE_LIST, list)
    if (state.model === playMode.random) { // 随机播放
        list = shuffle(list)
        index = list.findIndex((item) => {
            return item.id === songId
        })
    }
    commit(types.SET_PLAYLIST, list)
    commit(types.SET_CURRENT_INDEX, index)
    commit(types.SET_FULLSCREEN, true)
    commit(types.SET_PLAYING_STATE, true)
}
export const randPlay = function({ commit, state }, { list }) {
    const index = parseInt(Math.random() * (parseInt(list.length)))
    commit(types.SET_PLAY_MODE, playMode.random)
    commit(types.SET_SEQUENCE_LIST, list)
    commit(types.SET_PLAYLIST, list)
    commit(types.SET_CURRENT_INDEX, index)
    commit(types.SET_FULLSCREEN, true)
    commit(types.SET_PLAYING_STATE, true)
}

export const insertSong = function({ commit, state }, song) {
    const playList = state.playList.slice()
    const sequenceList = state.sequenceList.slice()
    let currentIndex = state.currentIndex

    const currentSong = playList[currentIndex]
    const fpIndex = playList.findIndex((item) => {
        return item.id === song.id
    })
    currentIndex++
    playList.splice(currentIndex, 0, song)
    if (fpIndex > -1) {
        if (currentIndex > fpIndex) {
            playList.splice(fpIndex, 1)
        } else {
            playList.splice(fpIndex + 1, 1)
        }
    }

    const currentSIndex = sequenceList.findIndex((item) => {
        return item.id === currentSong.id
    }) + 1

    const fsIndex = sequenceList.findIndex((item) => {
        return item.id === song.id
    })
    sequenceList.splice(currentSIndex, 0, song)
    if (fsIndex > -1) {
        if (currentSIndex > fsIndex) {
            sequenceList.splice(fsIndex, 1)
        } else {
            sequenceList.splice(fsIndex + 1, 1)
        }
    }
    commit(types.SET_PLAYLIST, playList)
    commit(types.SET_SEQUENCE_LIST, sequenceList)
    commit(types.SET_CURRENT_INDEX, currentIndex)
    commit(types.SET_FULLSCREEN, true)
    commit(types.SET_PLAYING_STATE, true)
}

export const saveSearchHisroty = function({ commit, state }, query) {
    commit(types.SET_SEARCH_HISTORY, saveSearch(query))
}
export const delSearchHisroty = function({ commit, state }, query) {
    commit(types.SET_SEARCH_HISTORY, deleteSearch(query))
}
export const clearSearchHisroty = function({ commit, state }) {
    commit(types.SET_SEARCH_HISTORY, clearSearch())
}

export const delectSong = function({ commit, state }, song) {
    const playList = state.playList.slice()
    const sequenceList = state.sequenceList.slice()
    let currentIndex = state.currentIndex

    const fpIndex = playList.findIndex((item) => {
        return item.id === song.id
    })
    playList.splice(fpIndex, 1)
    const fsIndex = sequenceList.findIndex((item) => {
        return item.id === song.id
    })
    sequenceList.splice(fsIndex, 1)
    if (currentIndex > fpIndex || currentIndex === playList.length) {
        currentIndex--
    }
    commit(types.SET_PLAYLIST, playList)
    commit(types.SET_SEQUENCE_LIST, sequenceList)
    commit(types.SET_CURRENT_INDEX, currentIndex)
    if (!playList.length) {
        commit(types.SET_PLAYING_STATE, false)
    }
}
export const clearSong = function({ commit, state }) {
    commit(types.SET_PLAYLIST, [])
    commit(types.SET_SEQUENCE_LIST, [])
    commit(types.SET_CURRENT_INDEX, -1)
    commit(types.SET_FULLSCREEN, false)
    commit(types.SET_PLAYING_STATE, false)
}
export const savePlayHistory = function({ commit, state }, song) {
    commit(types.SET_PLAY_HISTORY, savePlay(song))
}
export const saveFavoriteList = function({ commit, state }, song) {
    commit(types.SET_FAVORITE_LIST, saveFavorite(song))
}
export const delFavoriteList = function({ commit, state }, song) {
    commit(types.SET_FAVORITE_LIST, deleteFavorite(song))
}