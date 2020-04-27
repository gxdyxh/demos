// import jsonp from 'common/js/jsonp'
import { commonParams, baseUrl } from './config'
import axios from 'axios';

// export function getSongListById(albumId) {
//     const url = '/api/album/song'
//     const data = Object.assign({}, commonParams, {
//         platform: 'yqq',
//         g_tk: '5381',
//         needNewCode: 0,
//         hostUin: 0,
//         format: 'json',
//         data: `{"req_0":{"module":"srf_diss_info.DissInfoServer","method":"CgiGetDiss","param":{"disstid":${albumId},"onlysonglist":1,"song_begin":0,"song_num":150}}}`
//     })
//     return axios.get(url, { params: data }).then((res) => {
//         console.log('getAlbumSongList', res)
//         return Promise.resolve(res.data)
//     })
// }

export function getSongListById(albumId) {
    const url = '/api/new/song'
    const data = Object.assign({}, commonParams, {
        platform: 'yqq',
        g_tk: '5381',
        needNewCode: 0,
        hostUin: 0,
        format: 'json',
        disstid: albumId,
        type: 1,
        json: 1,
        utf8: 1,
        onlysong: 0,
        g_tk_new_20200303: 5381,
        loginUin: 0
    })
    return axios.get(baseUrl + url, { params: data }).then((res) => {
        console.log('getAlbumSongList', res)
        return Promise.resolve(res.data)
    })
}
export function getSongListByMid(albumId) {
    const url = '/api/album/song'
    const data = Object.assign({}, commonParams, {
        platform: 'yqq',
        g_tk: '5381',
        needNewCode: 0,
        hostUin: 0,
        format: 'json',
        data: `{"albumSonglist":{"method":"GetAlbumSongList","param":{"albumMid":"${albumId}","albumID":0,"begin":0,"num":150,"order":2},"module":"music.musichallAlbum.AlbumSongList"}}`
    })
    return axios.get(baseUrl + url, { params: data }).then((res) => {
        // console.log('getAlbumSongList', res)
        return Promise.resolve(res.data)
    })
}
