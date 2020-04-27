import jsonp from 'common/js/jsonp'
import { commonParams, options, ERR_OK, baseUrl } from 'api/config'
import axios from 'axios'

export function getSongFile2(mid) {
    const url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'
    const data = Object.assign({}, commonParams, {
      platform: 'yqq',
      rnd: Math.random(),
      format: 'jsonp',
      callback: 'songadd',
      data: `{"req":{"module":"CDN.SrfCdnDispatchServer","method":"GetCdnDispatch","param":{"guid":"0","calltype":0,"userip":""}},"req_0":{"module":"vkey.GetVkeyServer","method":"CgiGetVkey","param":{"guid":"0","songmid":["${mid}"],"uin":"0","platform":"yqq"}}}`
    })
    options.name = 'songadd'
    return jsonp(url, data, options).then((res) => {
        console.log(res)
        if (ERR_OK === res.code) {
            return Promise.resolve(`http://ws.stream.qqmusic.qq.com/${res.req_0.data.midurlinfo[0].filename}?guid=0&vkey=${res.data.items[0].vkey}&uin=0&fromtag=999`)
        } else {
            return Promise.reject(new Error('get song src false'))
        }
    })
 }

export function getSongFile(mid) {
    const url = '/api/songsrc'
    return axios.get(baseUrl + url, { params: { songmid: mid } }).then((res) => {
        console.log('getSongFile', res)
        if (res.data.src) {
            return Promise.resolve(res.data.src)
        } else {
            return Promise.reject(new Error('未获取歌曲资源'))
        }
    })
}

// export function getSongFile(mid) {
//     const url = 'https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg'
//     const data = Object.assign({}, {
//       platform: 'yqq',
//       guid: 0,
//       cid: '205361747',
//       format: 'jsonp',
//       callback: 'songadd',
//       songmid: mid,
//       filename: `C400${mid}.m4a`
//     })
//     options.name = 'songadd'
//     return jsonp(url, data, options).then((res) => {
//         console.log(res)
//         if (ERR_OK === res.code) {
//             return Promise.resolve(`http://ws.stream.qqmusic.qq.com/${res.data.items[0].filename}?guid=0&vkey=${res.data.items[0].vkey}&uin=0&fromtag=999`)
//         } else {
//             return Promise.reject(new Error('get song src false'))
//         }
//     })
//  }

export function getSongLrc(mid) {
    const url = '/api/lyric'
    const data = Object.assign({}, commonParams, {
        platform: 'yqq',
        songmid: mid,
        pcachetime: +new Date(),
        format: 'json'
    })
    return axios.get(baseUrl + url, { params: data }).then((res) => {
        return Promise.resolve(res.data)
    })
}