// const webpack = require('webpack')
const path = require('path')
const axios = require('axios')
const cheerio = require('cheerio')
function resolve (dir) {
  return path.join(__dirname, dir)
}

module.exports = {
  publicPath: '',

  chainWebpack (config) {
    config.resolve.alias
      .set('components', resolve('src/components'))
      .set('common', resolve('src/common'))
      .set('api', resolve('src/api'))
      .set('views', resolve('src/views'))
  },

  css: {
    loaderOptions: {
      stylus: {
        'resolve url': true,
        import: [
          './src/theme'
        ]
      }
    }
  },

  pluginOptions: {
    'cube-ui': {
      postCompile: true,
      theme: true
    }
  },
  devServer: {
    before(app) {
      app.get('/api/songsrc', function(req, res) {
        const params = Object.assign({}, { 'ADTAG': 'newyqq.song' }, req.query)
        axios.get('https://i.y.qq.com/v8/playsong.html', {
          headers: {
            'user-agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Mobile Safari/537.36'
          },
          params: params
        }).then((response) => {
          var $ = cheerio.load(response.data);
          var t = $('html').find('script');
          var obj = null
          t.each(function(i, elem) {
            if (obj !== null) {
              return false
            }
            var text = $(this).html();
            text.trim().split('\n').forEach(function(v, i) {
              if (v.match('songlist')) {
                obj = v
                return false
              }
            })
          });
          if (obj) {
            var objText = obj.substring(obj.indexOf('[') + 1, (obj.length - 2))
            res.json({ src: JSON.parse(objText).m4aUrl })
          }
        })
      })
      app.get('/api/album/song', function(req, res) {
        var url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'
        axios.get(url, {
          headers: {
            'referer': 'https://i.y.qq.com/n2/m/share/details/taoge.html',
            'origin': 'https://i.y.qq.com',
            'user-agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Mobile Safari/537.36'
          },
          params: req.query
        }).then((response) => {
          var ret = response.data
          if (typeof ret === 'string') {
            var reg = /^\w+\(({[^()]+})\)$/
            var matches = ret.match(reg)
            if (matches) {
              ret = JSON.parse(matches[1])
            }
          }
          res.json(ret)
        }).catch((e) => {
          console.log(e)
        })
      })
      app.get('/api/album/song', function(req, res) {
        var url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'
        axios.get(url, {
          headers: {
            'referer': 'https://i.y.qq.com/n2/m/share/details/taoge.html',
            'origin': 'https://i.y.qq.com',
            'user-agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.26 Mobile Safari/537.36'
          },
          params: req.query
        }).then((response) => {
          var ret = response.data
          console.log(ret)
          if (typeof ret === 'string') {
            var reg = /^\w+\(({[^()]+})\)$/
            var matches = ret.match(reg)
            if (matches) {
              ret = JSON.parse(matches[1])
            }
          }
          res.json(ret)
        }).catch((e) => {
          console.log(e)
        })
      })
      app.get('/api/new/song', function(req, res) {
        var url = 'https://c.y.qq.com/qzone/fcg-bin/fcg_ucc_getcdinfo_byids_cp.fcg'
        axios.get(url, {
          headers: {
            'Host': 'c.y.qq.com',
            'Referer': 'https://y.qq.com/n/yqq/playlist/7458130060.html'
          },
          params: req.query
        }).then((response) => {
          var ret = response.data
          console.log(ret)
          if (typeof ret === 'string') {
            var reg = /^\w+\(({[^()]+})\)$/
            var matches = ret.match(reg)
            if (matches) {
              ret = JSON.parse(matches[1])
            }
          }
          res.json(ret)
        }).catch((e) => {
          console.log(e)
        })
      })
      app.get('/api/rank/song', function(req, res) {
        var url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'
        axios.get(url, {
          headers: {
            'Host': 'u.y.qq.com',
            'Referer': 'https://y.qq.com/n/yqq/toplist/27.html',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0'
          },
          params: req.query
        }).then((response) => {
          var ret = response.data
          if (typeof ret === 'string') {
            var reg = /^\w+\(({[^()]+})\)$/
            var matches = ret.match(reg)
            if (matches) {
              ret = JSON.parse(matches[1])
            }
          }
          res.json(ret)
        }).catch((e) => {
          console.log(e)
        })
      })
      app.get('/api/search', function(req, res) {
        var url = 'https://c.y.qq.com/soso/fcgi-bin/search_for_qq_cp'
        axios.get(url, {
          headers: {
            'Host': 'u.y.qq.com',
            'Referer': 'https://y.qq.com/n/yqq/toplist/27.html',
            'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:75.0) Gecko/20100101 Firefox/75.0'
          },
          params: req.query
        }).then((response) => {
          var ret = response.data

          if (typeof ret === 'string') {
            var reg = /^\w+\(({[^()]+})\)$/
            var matches = ret.match(reg)
            if (matches) {
              ret = JSON.parse(matches[1])
            }
          }
          res.json(ret)
        }).catch((e) => {
          // console.log(e)
        })
      })
      app.get('/api/lyric', function(req, res) {
        var url = 'https://c.y.qq.com/lyric/fcgi-bin/fcg_query_lyric_new.fcg'
        axios.get(url, {
          headers: {
            referer: 'https://c.y.qq.com/',
            host: 'c.y.qq.com'
          },
          params: req.query
        }).then((response) => {
          var ret = response.data
          if (typeof ret === 'string') {
            var reg = /^\w+\(({[^()]+})\)$/
            var matches = ret.match(reg)
            if (matches) {
              ret = JSON.parse(matches[1])
            }
          }
          res.json(ret)
        }).catch((e) => {
          // console.log(e)
        })
      })
      // app.get('/api/songsrc', function(req, res) {
      //   var url = 'https://u.y.qq.com/cgi-bin/musicu.fcg'
      //   axios.get(url, {
      //     headers: {
      //       referer: 'https://y.qq.com/portal/player.html',
      //       Host: 'u.y.qq.com'
      //     },
      //     params: req.query
      //   }).then((response) => {
      //     var ret = response.data
      //     if (typeof ret === 'string') {
      //       var reg = /^\w+\(({[^()]+})\)$/
      //       var matches = ret.match(reg)
      //       if (matches) {
      //         ret = JSON.parse(matches[1])
      //       }
      //     }
      //     res.json(ret)
      //   }).catch((e) => {
      //     console.log(e)
      //   })
      // })
    }
  }
}
