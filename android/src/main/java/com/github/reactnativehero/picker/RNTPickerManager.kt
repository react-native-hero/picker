package com.github.reactnativehero.picker

import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewProps
import com.facebook.react.uimanager.annotations.ReactProp

class RNTPickerManager(private val reactAppContext: ReactApplicationContext) : SimpleViewManager<PickerView>() {

    override fun getName(): String {
        return "RNTPicker"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): PickerView {
        return PickerView(reactContext)
    }

    @ReactProp(name = "options")
    fun setOptions(view: PickerView, options: ReadableArray) {
        view.options = options
    }

    @ReactProp(name = "selectedIndex")
    fun setSelectedIndex(view: PickerView, selectedIndex: Int) {
        view.selectedIndex = selectedIndex
    }

    @ReactProp(name = ViewProps.COLOR, customType = "Color")
    fun setColor(view: PickerView, color: Int) {
        view.color = color
    }

    @ReactProp(name = "fontSize")
    fun setFontSize(view: PickerView, fontSize: Float) {
        view.fontSize = fontSize
    }

    @ReactProp(name = "rowHeight")
    fun setRowHeight(view: PickerView, rowHeight: Int) {
        view.rowHeight = rowHeight
    }

    @ReactProp(name = "visibleCount")
    fun setVisibleCount(view: PickerView, visibleCount: Int) {
        view.visibleCount = visibleCount
    }

    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
        return MapBuilder.builder<String, Any>()
                .put("onChange", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onChange")))
                .build()
    }

}
