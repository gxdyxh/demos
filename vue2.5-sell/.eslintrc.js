module.exports = {
  root: true,
  env: {
    node: true
  },
  extends: [
    'plugin:vue/essential',
    '@vue/standard'
  ],
  parserOptions: {
    parser: 'babel-eslint'
  },
  rules: {
    "no-irregular-whitespace":0,
    'block-spacing':0,
    "no-multi-spaces": 0,
    "no-spaced-func":0,
    'space-before-function-paren': 0,
    "no-trailing-spaces":0,
    "comma-spacing": 0,
    "no-irregular-whitespace":"off",
    "generator-star-spacing": 0,
    "key-spacing": [0, { "beforeColon": false, "afterColon": false }],
    'indent':["off",2],
    'no-console': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'no-debugger': process.env.NODE_ENV === 'production' ? 'error' : 'off',
    'semi': 0,
    "no-mixed-spaces-and-tabs": [0],
    "no-irregular-whitespace":"off"//这禁止掉 空格报错检查
  }
}
