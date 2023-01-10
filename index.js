import React, {
  PureComponent,
} from 'react'

import {
  requireNativeComponent,
} from 'react-native'

import PropTypes from 'prop-types'

class PickerComponent extends PureComponent {

  static propTypes = {
    options: PropTypes.arrayOf(
      PropTypes.shape({
        text: PropTypes.string.isRequired,
      })
    ).isRequired,
    selectedIndex: PropTypes.number,
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
    let { onChange } = this.props
    if (onChange) {
      onChange(event.nativeEvent)
    }
  }

  render() {
    return (
      <RNTPicker
        {...this.props}
        onChange={this.handleChange}
      />
    )
  }

}

const RNTPicker = requireNativeComponent('RNTPicker', PickerComponent)

export const Picker = PickerComponent

