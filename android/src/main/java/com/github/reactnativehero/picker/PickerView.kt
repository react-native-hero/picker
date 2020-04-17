package com.github.reactnativehero.picker

import android.graphics.Color
import com.contrarywind.adapter.WheelAdapter
import com.contrarywind.view.WheelView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class PickerView(private val reactContext: ThemedReactContext) : WheelView(reactContext) {

    companion object {
        private val dividerColor = Color.parseColor("#33000000")
    }

    private var mapList = arrayListOf<HashMap<String, String>>()

    var options: ReadableArray = Arguments.createArray()

        set(value) {
            field = value

            val count = options.size()

            mapList = arrayListOf()

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

            adapter = object: WheelAdapter<String> {
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

        }

    var selectedIndex = -1

        set(value) {
            field = value

            if (value < 0 || value >= options.size()) {
                return
            }

            currentItem = value

            val map = Arguments.createMap()
            val option = Arguments.createMap()
            for ((key, value) in mapList[value]) {
                option.putString(key, value)
            }
            map.putInt("index", value)
            map.putMap("option", option)
            sendEvent("onChange", map)
        }

    var color = Color.BLACK

        set(value) {
            field = value

            setTextColorOut(value)
            setTextColorCenter(value)
        }

    var fontSize = 16f

        set(value) {
            field = value

            setTextSize(value)
        }

    var rowHeight = 16

        set(value) {
            field = value

            setLineSpacingMultiplier(rowHeight / fontSize)
        }

    init {
        setCyclic(false)
        setDividerColor(dividerColor)
        setOnItemSelectedListener {
            selectedIndex = it
        }
    }

    private fun sendEvent(eventName: String, params: WritableMap) {
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, eventName, params)
    }

}