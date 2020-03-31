const EVEN_SHOW = 'show'
const EVEN_HIDE = 'hide'
export default {
    data() {
        return {
            visible: false
        }
    },
    methods: {
      show() {
          this.visible = true
          this.$emit(EVEN_SHOW)
      },
      hide() {
        this.visible = false
        this.$emit(EVEN_HIDE)
      }
    }
}
