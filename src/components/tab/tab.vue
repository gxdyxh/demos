<template>
    <div class="tab">
      <cube-tab-bar v-model="selectedLabel"
                    show-slider
                    ref="tabNav"
                    :data="tabs" class="border-bottom-1px">
      </cube-tab-bar>
      <div class="tab-slide-container">
        <cube-slide
          ref="slide"
          :loop="false"
          :initial-index="index"
          :auto-play="false"
          :show-dots="false"
          :options="slideOptions"
          @scroll="scroll"
          @change="changePage"
        >
          <!-- 商品 -->
          <cube-slide-item v-for="(tab,index) in tabs" :key="index">
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
            default () {
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
    mounted () {
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
            get () {
                return this.tabs[this.index].label
            },
            set (newVal) {
                this.index = this.tabs.findIndex((value) => {
                    return value.label === newVal
                })
            }
        }
    }
  }
</script>
<style lang="stylus" rel="stylesheet/stylus" scoped>
@import "~common/stylus/variable";
.tab
    display: flex
    flex-direction : column
    height:100%
    >>>.cube-tab
        padding:10px 0

    >>>.tab-slide-container
       flex : 1
       overflow: hidden;
</style>
