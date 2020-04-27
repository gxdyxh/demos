<template>
    <div class="search">
        <div class="search-box-wrapper">
            <search-box @query="onQueryChange" ref="searchBox"></search-box>
        </div>
        <div class="shortcut-wrapper" v-show="!query">
            <cube-scroll
                    ref="shortcut"
                    :data="shortcut"
                    class="shortcut222"
                    >
            <div class="shortcut">
                <div class="hot-key">
                    <h1 class="title">热门搜索</h1>
                    <ul>
                        <li @click="addQuery(item.k)" class="item" v-for="(item,index) in hotKey" :key="index">
                            <span >{{item.k}}</span>
                        </li>
                    </ul>
                </div>
                <div class="search-history" v-show="searchHistoryList.length">
                    <h1 class="title">
                        <span class="text">搜索历史</span>
                        <span class="clear" @click="delAll">
                            <i class="icon-clear"></i>
                        </span>
                    </h1>
                    <search-list @select="addQuery" @delete="delSearchHisroty" :searches="searchHistoryList"></search-list>
                </div>
            </div>
            </cube-scroll>
        </div>
        <div class="search-result" ref="searchWrapper" v-show="query">
            <suggest ref="suggest" @select="onSelect" :click="false" :query="query"></suggest>
        </div>
    </div>
</template>
<script>
import SearchBox from 'components/search-box/search-box'
import { playlistMixin, searchMixin } from 'common/js/mixin'
import { getHotKey } from 'api/search';
import { ERR_OK } from 'api/config';
import Suggest from 'components/suggest/suggest'
import SearchList from 'components/search-list/search-list'
export default {
    mixins: [playlistMixin, searchMixin],
    name: 'search',
    data() {
      return {
          hotKey: []
      }
    },
    created() {
        this._getHotKey()
    },
    methods: {
        handlePlaylist(playList) {
            const bottom = playList.length > 0 ? 60 : 0
            if (this.$refs.shortcut) {
                // this.$refs.scroll.$el.style.bottom = `${bottom}px`
                this.$refs.shortcut.$el.style.height = `calc(100% - ${bottom}px)`
                this.$refs.shortcut.refresh()
            }
            // this.$refs.searchWrapper.style.height = `calc(100% - ${bottom + 70}px)`
            this.$refs.suggest.setBottom(bottom)
            this.$refs.suggest.refresh()
        },
        delAll() {
            this.$createDialog({
                type: 'confirm',
                title: '',
                content: '是否清空所有搜索历史',
                confirmBtn: {
                    text: '清空',
                    active: true,
                    disabled: false,
                    href: 'javascript:;'
                },
                cancelBtn: {
                    text: '取消',
                    active: false,
                    disabled: false,
                    href: 'javascript:;'
                },
                onConfirm: () => {
                    this.clearSearchHisroty()
                }
            }).show()
        },
        _getHotKey() {
            getHotKey().then((res) => {
                console.log('getHotKey', res)
                if (res.code === ERR_OK) {
                    this.hotKey = res.data.hotkey.slice(0, 10)
                }
            })
        }
    },
    watch: {
      query(newVal) {
          if (!newVal) {
              setTimeout(() => {
                  if (this.$refs.shortcut) {
                      this.$refs.shortcut.refresh()
                  }
                  this.$refs.suggest.refresh()
              }, 20)
          }
      }
    },
    computed: {
        shortcut() {
          return this.hotKey.concat(this.searchHistoryList)
        }
    },
    components: {
        SearchBox,
        Suggest,
        SearchList
    }
}
</script>
<style lang="stylus" rel="stylesheet/stylus">
  @import "~common/stylus/variable"
  @import "~common/stylus/mixin"

  .search
    width: 100%
    height: 100%
    display: flex;
    flex-direction: column;
    .search-box-wrapper
      margin: 3px 20px 20px 20px
    .shortcut-wrapper
      height: calc(100% - 63px);
      /*width: 100%*/
      /*flex: 1;*/
      .shortcut
        height: 100%
        overflow: hidden
        .hot-key
          margin: 0 20px 20px 20px
          ul
             overflow: hidden;
          .title
            margin-bottom: 20px
            font-size: $font-size-medium
            color: $color-text-l
          .item
            display: inline-block
            padding: 5px 10px
            margin: 0 20px 10px 0
            border-radius: 6px
            background: $color-highlight-background
            font-size: $font-size-medium
            color: $color-text-d
            float: left
        .search-history
          position: relative
          margin: 0 20px
          .title
            display: flex
            align-items: center
            height: 40px
            font-size: $font-size-medium
            color: $color-text-l
            .text
              flex: 1
            .clear
              extend-click()
              .icon-clear
                font-size: $font-size-medium
                color: $color-text-d
    .search-result
         height: calc(100% - 63px);
      /*width: 100%*/
      /*display: flex;*/
      /*flex: 1;*/
</style>