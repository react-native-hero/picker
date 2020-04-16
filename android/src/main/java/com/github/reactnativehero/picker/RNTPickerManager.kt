package com.github.reactnativehero.picker

import android.graphics.Color
import com.contrarywind.adapter.WheelAdapter
import com.contrarywind.view.WheelView
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewProps
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import kotlin.collections.HashMap

class RNTPickerManager(private val reactAppContext: ReactApplicationContext) : SimpleViewManager<WheelView>() {

    companion object {
        private val dividerColor = Color.parseColor("#CCCCCC")
        private var fontSizes = HashMap<Int, Float>()
    }

    override fun getName(): String {
        return "RNTPicker"
    }

    override fun createViewInstance(reactContext: ThemedReactContext): WheelView {

        val view = WheelView(reactContext)

        view.setCyclic(false)
        view.setDividerColor(dividerColor)

        return view
    }

    @ReactProp(name = "options")
    fun setOptions(view: WheelView, options: ReadableArray) {

        val count = options.size()
        val mapList = arrayListOf<HashMap<String, String>>()
        val stringList = arrayListOf<String>()

        for (item in options.toArrayList()) {
            val map = item as HashMap<String, String>
            var text = "undefined"
            if (map.contains("text")) {
                text = map["text"].toString()
            }
            mapList.add(map)
            stringList.add(text)
        }

        view.adapter = object: WheelAdapter<String> {
            override fun indexOf(o: String?): Int {
                return stringList.indexOf(o)
            }

            override fun getItemsCount(): Int {
                return count
            }

            override fun getItem(index: Int): String {
                return stringList[index]
            }
        }

        view.setOnItemSelectedListener {
            val map = Arguments.createMap()
            val option = Arguments.createMap()
            for ((key, value) in mapList[it]) {
                option.putString(key, value)
            }
            map.putInt("index", it)
            map.putMap("option", option)
            sendEvent(view.id, "onChange", map)
        }

    }

    @ReactProp(name = "selectedIndex")
    fun setSelectedIndex(view: WheelView, selectedIndex: Int) {
        view.currentItem = selectedIndex
    }

    @ReactProp(name = ViewProps.COLOR, customType = "Color")
    fun setColor(view: WheelView, color: Int) {
        view.setTextColorOut(color)
        view.setTextColorCenter(color)
    }

    @ReactProp(name = "fontSize")
    fun setFontSize(view: WheelView, fontSize: Float) {
        fontSizes[view.id] = fontSize
        view.setTextSize(fontSize)
    }

    @ReactProp(name = "rowHeight")
    fun setRowHeight(view: WheelView, rowHeight: Int) {
        fontSizes[view.id]?.let {
            view.setLineSpacingMultiplier(rowHeight / it)
        }
    }

    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
        return MapBuilder.builder<String, Any>()
                .put("onChange", MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", "onChange")))
                .build()
    }

    override fun onDropViewInstance(view: WheelView) {
        super.onDropViewInstance(view)
        if (fontSizes.contains(view.id)) {
            fontSizes.remove(view.id)
        }
    }

    private fun sendEvent(id: Int, eventName: String, params: WritableMap) {
        reactAppContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, eventName, params)
    }

}
