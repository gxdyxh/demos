export const singer = state => state.singer
export const album = state => state.album
export const playing = state => state.playing
export const fullScreen = state => state.fullScreen
export const playList = state => state.playList
export const sequenceList = state => state.sequenceList
export const model = state => state.model
export const currentIndex = state => state.currentIndex
export const searchHistoryList = state => state.searchHistoryList
export const playHistory = state => state.playHistory
export const favoriteList = state => state.favoriteList
export const currentSong = (state) => {
   return state.playList[state.currentIndex] || {}
}