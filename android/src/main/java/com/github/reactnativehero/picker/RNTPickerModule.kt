package com.github.reactnativehero.picker

import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule
import okhttp3.*
import okhttp3.Callback
import okio.Buffer
import okio.BufferedSink
import okio.Okio
import java.io.File
import java.io.IOException
import java.util.*

class RNTPickerModule(private val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    companion object {
        private const val ERROR_CODE_DOWNLOAD_FAILURE = "1"
        private const val ERROR_CODE_UPLOAD_FAILURE = "2"
    }

    override fun getName(): String {
        return "RNTPicker"
    }

    override fun getConstants(): Map<String, Any>? {

        val constants: MutableMap<String, Any> = HashMap()

        constants["ERROR_CODE_DOWNLOAD_FAILURE"] = ERROR_CODE_DOWNLOAD_FAILURE
        constants["ERROR_CODE_UPLOAD_FAILURE"] = ERROR_CODE_UPLOAD_FAILURE

        return constants

    }

    @ReactMethod
    fun download(options: ReadableMap, promise: Promise) {

        val index = if (options.hasKey("index")) {
            options.getInt("index")
        }
        else {
            0
        }

        val url = options.getString("url") as String
        val path = options.getString("path") as String

        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                promise.reject(ERROR_CODE_DOWNLOAD_FAILURE, e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                response.body()?.let { body ->

                    val file = File(path)

                    val sink = Okio.buffer(Okio.sink(file))
                    val sinkBuffer = sink.buffer()

                    val source = body.source()

                    val totalSize = body.contentLength()
                    var loadSize = 0L
                    val bufferSize = 8 * 1024L

                    var readSize: Long
                    while (source.read(sinkBuffer, bufferSize).also { readSize = it } != -1L) {
                        sink.emit()
                        loadSize += readSize
                        if (index > 0) {
                            val map = Arguments.createMap()
                            map.putInt("index", index)
                            map.putDouble("progress", loadSize / totalSize.toDouble())
                            sendEvent("download_progress", map)
                        }
                    }

                    sink.flush()
                    sink.close()
                    source.close()

                    val map = Arguments.createMap()
                    map.putString("path", path)
                    map.putString("name", file.name)
                    map.putInt("size", file.length().toInt())
                    promise.resolve(map)

                }
            }
        })

    }

    @ReactMethod
    fun upload(options: ReadableMap, promise: Promise) {

        val index = if (options.hasKey("index")) {
            options.getInt("index")
        }
        else {
            0
        }

        val url = options.getString("url") as String

        val file = options.getMap("file") as ReadableMap

        val data = if (options.hasKey("data")) {
            options.getMap("data")
        }
        else {
            null
        }

        val headers = if (options.hasKey("headers")) {
            options.getMap("headers")
        }
        else {
            null
        }

        val path = file.getString("path") as String
        val name = file.getString("name") as String
        val mimeType = file.getString("mimeType") as String

        val localFile = File(path)
        val fileName = if (file.hasKey("fileName")) {
            file.getString("fileName")
        }
        else {
            localFile.name
        }

        val formBuilder = MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart(
                        name,
                        fileName,
                        createCustomRequestBody(
                            MediaType.parse(mimeType),
                            localFile
                        ) {
                            if (index > 0) {
                                val map = Arguments.createMap()
                                map.putInt("index", index)
                                map.putDouble("progress", it)
                                sendEvent("upload_progress", map)
                            }
                        }
                )

        data?.let {
            for ((key,value) in it.toHashMap()) {
                formBuilder.addFormDataPart(key, value.toString())
            }
        }

        val client = OkHttpClient()
        val requestBuilder = Request.Builder().url(url).post(formBuilder.build())

        headers?.let {
            for ((key, value) in it.toHashMap()) {
                requestBuilder.addHeader(key, value.toString())
            }
        }

        client.newCall(requestBuilder.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                promise.reject(ERROR_CODE_UPLOAD_FAILURE, e.localizedMessage)
            }

            override fun onResponse(call: Call, response: Response) {
                val map = Arguments.createMap()
                map.putInt("status_code", response.code())
                map.putString("body", response.body()?.string())
                promise.resolve(map)
            }
        })

    }

    private fun createCustomRequestBody(contentType: MediaType?, file: File, onProgress: (Double) -> Unit): RequestBody {
        return object : RequestBody() {
            override fun contentType(): MediaType? {
                return contentType
            }

            override fun contentLength(): Long {
                return file.length()
            }

            override fun writeTo(sink: BufferedSink) {

                val source = Okio.source(file)
                val buf = Buffer()

                val totalSize = contentLength()
                var loadSize = 0L
                val bufferSize = 2 * 1024L

                var readSize: Long
                while (source.read(buf, bufferSize).also { readSize = it } != -1L) {
                    sink.write(buf, readSize)
                    loadSize += readSize
                    onProgress(loadSize / totalSize.toDouble())
                }

            }
        }
    }

    private fun sendEvent(eventName: String, params: WritableMap) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }

}