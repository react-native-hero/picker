package com.github.reactnativehero.picker

import android.graphics.Color
import com.contrarywind.adapter.WheelAdapter
import com.contrarywind.view.WheelView
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableArray
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.bridge.WritableMap
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.events.RCTEventEmitter

class PickerView(private val reactContext: ThemedReactContext) : WheelView(reactContext) {

    companion object {
        private val dividerColor = Color.parseColor("#33000000")
    }

    var options: ReadableArray = Arguments.createArray()

        set(value) {
            field = value

            val count = value.size()
            val stringList = arrayListOf<String>()

            for (i in 0 until value.size()) {
                val map = value.getMap(i)!!
                var text = "undefined"
                if (map.hasKey("text")) {
                    text = map.getString("text") as String
                }
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

                override fun getMaxValue(): Int {
                    return 0
                }

                override fun getMinValue(): Int {
                    return 0
                }
            }

            if (tempIndex >= 0) {
                selectedIndex = tempIndex
                tempIndex = -1
            }

        }

    var tempIndex = -1

    var selectedIndex = -1

        set(value) {
            field = value

            val optionsSize = options.size()

            if (value < 0 || value >= optionsSize) {
                if (optionsSize == 0) {
                    tempIndex = value
                }
                return
            }

            currentItem = value

            val map = Arguments.createMap()
            map.putInt("index", value)

            // putMap 必须传入 WritableMap
            // 如果直接传 ReadableMap 会报错
            val option = Arguments.createMap()
            option.merge(options.getMap(value)!!)

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

    var rowHeight = 44

        set(value) {
            field = value

            setLineSpacingMultiplier(rowHeight / fontSize)
        }

    var visibleCount = 6

        set(value) {
            field = value

            setItemsVisibleCount(visibleCount)
        }

    init {
        setCyclic(false)
        setAlphaGradient(true)
        setDividerColor(dividerColor)
        setOnItemSelectedListener {
            selectedIndex = it
        }
    }

    private fun sendEvent(eventName: String, params: WritableMap) {
        reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(id, eventName, params)
    }

}
