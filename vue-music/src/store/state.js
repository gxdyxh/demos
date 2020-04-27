import { playMode } from 'common/js/config';
import { loadSearch, loadPlay, loadFavorite } from 'common/js/cache';

const state = {
    album: {},
    singer: {},
    playing: false,
    fullScreen: false,
    playList: [],
    sequenceList: [],
    model: playMode.sequence,
    currentIndex: -1,
    searchHistoryList: loadSearch(),
    playHistory: loadPlay(),
    favoriteList: loadFavorite()
}
export default state