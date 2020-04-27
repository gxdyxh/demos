<template>
    <div class="navtab" slot="content">
      <cube-tab-bar v-model="selectedLabel"
                    show-slider
                    :use-transition="disabled"
                    ref="tabNav"
                    :data="tabs">
      </cube-tab-bar>
      <div class="tab-slide-container">
        <cube-slide
          ref="slide"
          :loop="loop"
          :initial-index="index"
          :auto-play="autoPlay"
          :show-dots="showDots"
          :options="slideOptions"
          @scroll="scroll"
          @change="changePage"
        >
          <cube-slide-item v-for="(tab, index) in tabs" :key="index">
                <component :is="tab.component" :data="tab.data" ref="component"></component>
          </cube-slide-item>
        </cube-slide>
      </div>
    </div>
</template>

<script type="text/ecmascript-6">
  export default {
      name: 'tab',
      props: {
          tabs: {
              type: Array,
              default() {
                  return []
              }
          },
          initIndex: {
              type: Number,
              default: 0
          }
      },
    data () {
      return {
        index: this.initIndex,
        disabled: false,
        loop: false,
        autoPlay: false,
        showDots: false,
        slideOptions: {
          listenScroll: true,
          probeType: 3,
          /* lock y-direction when scrolling horizontally and  vertically at the same time */
          directionLockThreshold: 0
        },
        scrollOptions: {
          /* lock x-direction when scrolling horizontally and  vertically at the same time */
          directionLockThreshold: 0
        }
      }
    },
    mounted() {
       this.changePage(this.index)
    },
    methods: {
      changePage (current) {
        this.index = current
        const component = this.$refs.component[current]
        component.fetch && component.fetch()
      },
      scroll (pos) {
        const x = Math.abs(pos.x)
        const tabItemWidth = this.$refs.tabNav.$el.clientWidth
        const slideScrollerWidth = this.$refs.slide.slide.scrollerWidth
        const deltaX = x / slideScrollerWidth * tabItemWidth
        this.$refs.tabNav.setSliderTransform(deltaX)
      }
    },
    computed: {
      selectedLabel: {
        get() {
            return this.tabs[this.index].label
        },
        set(newVal) {
            this.index = this.tabs.findIndex((item) => {
            return item.label === newVal
            })
        }
      }
    }
  }
</script>
<style lang="stylus" rel="stylesheet/stylus">
    @import "~common/stylus/variable"
.navtab
    /*display: flex;*/
    /*flex-direction: column;*/
    /*flex: 1;*/
    .cube-tab-bar
        height: 44px
        line-height: 44px
        font-size: $font-size-medium
        .cube-tab
            padding: 0
    .tab-slide-container
        flex: 1;
        height: calc(100% - 44px)
</style>
