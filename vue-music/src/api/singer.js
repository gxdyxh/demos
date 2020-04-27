import jsonp from 'common/js/jsonp'
import { baseUrl, commonParams, options } from './config'
import axios from 'axios';

// export function getSingerList() {
//   const url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'

//   const data = Object.assign({}, commonParams, {
//     platform: 'yqq',
//     uin: 0,
//     g_tk: '5381',
//     needNewCode: 0,
//     hostUin: 0,
//     rnd: Math.random(),
//     format: 'jsonp',
//     callback: 'singer',
//     data: '{"singerList":{"module":"Music.SingerListServer","method":"get_singer_list","param":{"area":-100,"sex":-100,"genre":-100,"index":-100,"sin":0,"cur_page":1}}}'
//   })
//   options.name = 'singer'
//   return jsonp(url, data, options)
// }

export function getSingerList() {
    const url = 'https://c.y.qq.com/v8/fcg-bin/v8.fcg'
    const data = Object.assign({}, commonParams, {
      platform: 'yqq',
      uin: 0,
      g_tk: '5381',
      needNewCode: 0,
      hostUin: 0,
      rnd: Math.random(),
      format: 'jsonp',
      callback: 'singer',
      channel: 'singer',
      page: 'list',
      key: 'all_all_all',
      pagesize: '100',
      pagenum: '1'
    })
    options.name = 'singer'
    return jsonp(url, data, options)
  }

export function getSongList(singerId, page) {
    const pageSize = 30
    const url = 'https://c.y.qq.com/v8/fcg-bin/fcg_v8_singer_track_cp.fcg'
    const data = Object.assign({}, commonParams, {
        platform: 'yqq',
        uin: 0,
        g_tk: '5381',
        needNewCode: 0,
        hostUin: 0,
        rnd: Math.random(),
        format: 'jsonp',
        callback: 'song',
        singermid: singerId,
        order: 'listen',
        songstatus: 1,
        num: pageSize,
        begin: page * pageSize
    })
    options.name = 'song'
    return jsonp(url, data, options)
}

export function getNewSong(albumId) {
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
        onlysong: 1,
        g_tk_new_20200303: 5381,
        loginUin: 0
    })
    return axios.get(baseUrl + url, { params: data }).then((res) => {
        console.log('getAlbumSongList', res)
        return Promise.resolve(res.data)
    })
}