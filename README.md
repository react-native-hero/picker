# @react-native-hero/picker

## Getting started

Install the library using either Yarn:

```
yarn add @react-native-hero/picker
```

or npm:

```
npm install --save @react-native-hero/picker
```

## Link

- React Native v0.60+

For iOS, use `cocoapods` to link the package.

run the following command:

```
$ cd ios && pod install
```

For android, the package will be linked automatically on build.

- React Native <= 0.59

run the following command to link the package:

```
$ react-native link @react-native-hero/picker
```

## Example

```js
import {
  Picker
} from '@react-native-hero/picker'

<Picker
  options={[
    { text: 'displayed text', value: 'support string or number' },
    { text: '2', value: 'string' },
    { text: '3', value: 3 },
  ]}
  onChange={data => {
    data.index
    data.option
  }}
  style={{
    flex: 1,
    backgroundColor: '#eee'
  }}

  // optional
  selectedIndex={0}
  color="#000"
  fontSize={16}
  rowHeight={44}
/>
```
