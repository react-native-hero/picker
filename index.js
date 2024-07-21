import React, {
  PureComponent,
} from 'react'

import {
  Platform,
  requireNativeComponent,
} from 'react-native'

import PropTypes from 'prop-types'

const isAndroid = Platform.OS === 'android'

class PickerComponent extends PureComponent {

  static propTypes = {
    options: PropTypes.arrayOf(
      PropTypes.shape({
        text: PropTypes.string.isRequired,
      })
    ).isRequired,
    selectedIndex: PropTypes.number,
    height: PropTypes.number.isRequired,
    color: PropTypes.string,
    fontSize: PropTypes.number,
    rowHeight: PropTypes.number,
    onChange: PropTypes.func,
    style: PropTypes.any,
  }

  static defaultProps = {
    selectedIndex: 0,
    color: '#000',
    fontSize: 16,
    rowHeight: 44,
  }

  handleChange = event => {
    const { onChange } = this.props
    if (onChange) {
      onChange(event.nativeEvent)
    }
  }

  render() {
    const {
      height,
      rowHeight,
      style,
      ...props
    } = this.props

    const pickerStyle = {
      flex: 1,
      height,
    }
    // 安卓 picker 没有垂直居中
    // 这里支持简单处理，稍微接近 ios 的效果
    if (isAndroid) {
      const gap = Math.floor(height / 100) * 20
      pickerStyle.height -= 2 * gap
      pickerStyle.marginVertical = gap
    }
    return (
      <RNTPicker
        {...props}
        style={
          style
          ? [pickerStyle, style]
          : pickerStyle
        }
        rowHeight={rowHeight}
        visibleCount={Math.ceil(height / rowHeight)}
        onChange={this.handleChange}
      />
    )
  }

}

const RNTPicker = requireNativeComponent('RNTPicker', PickerComponent)

export const Picker = PickerComponent

