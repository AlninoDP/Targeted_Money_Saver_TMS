package com.tms.targetedmoneysaver.helper

import android.content.Context
import android.util.Log
import com.google.android.gms.tflite.client.TfLiteInitializationOptions
import com.google.android.gms.tflite.gpu.support.TfLiteGpu
import com.google.android.gms.tflite.java.TfLite
import com.google.firebase.ml.modeldownloader.CustomModel
import com.google.firebase.ml.modeldownloader.CustomModelDownloadConditions
import com.google.firebase.ml.modeldownloader.DownloadType
import com.google.firebase.ml.modeldownloader.FirebaseModelDownloader
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tms.targetedmoneysaver.R
import org.tensorflow.lite.InterpreterApi
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder

class GoalCategoryPredictionHelper(
    private val modelName: String = "User-Category.tflite",
    val context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit,
    private val onDownloadSuccess: () -> Unit
) {
    private var interpreter: InterpreterApi? = null

    init {
        TfLiteGpu.isGpuDelegateAvailable(context).onSuccessTask { gpuAvailable ->
            val optionBuilder = TfLiteInitializationOptions.builder()
            if (gpuAvailable) {
                optionBuilder.setEnableGpuDelegateSupport(true)
            }
            TfLite.initialize(context, optionBuilder.build())
        }.addOnSuccessListener {
            downloadModel()
        }.addOnFailureListener {
            onError(context.getString(R.string.tflite_is_not_initialized_yet))
        }
    }




    @Synchronized
    private fun downloadModel() {
        val conditions = CustomModelDownloadConditions.Builder()
            .build()

        FirebaseModelDownloader.getInstance()
            .getModel("User-Category1", DownloadType.LOCAL_MODEL, conditions)
            .addOnSuccessListener { model: CustomModel ->
                try {
                    initializeInterpreter(model)
                    onDownloadSuccess()
                } catch (e: IOException) {
                    onError(e.message.toString())
                }
            }
            .addOnFailureListener { e: Exception ->
                onError(e.message.toString())
//                onError(context.getString(R.string.firebaseml_model_download_failed))
            }
    }


    private fun initializeInterpreter(model: Any) {
        interpreter?.close()
        try {

            val options = InterpreterApi.Options()
                .setRuntime(InterpreterApi.Options.TfLiteRuntime.FROM_SYSTEM_ONLY)
//                .addDelegateFactory(GpuDelegateFactory())


            if (model is ByteBuffer) {
                Log.e(TAG, "BEST")
                interpreter = InterpreterApi.create(model, options)
            } else if (model is CustomModel) {
                Log.e(TAG, "REST")
                model.file?.let {
                    interpreter = InterpreterApi.create(it, options)
                }
            }
        } catch (e: Exception) {
            onError(e.message.toString())
            Log.e(TAG, "ATASin ${e.message.toString()}")
        }

    }


    fun inspectModel() {
        if (interpreter == null) {
            Log.e(TAG, "Interpreter is not initialized.")
            return
        }

        try {
            // Inspect the first input tensor
            val inputTensor = interpreter?.getInputTensor(0)
            val inputType = inputTensor?.dataType() // Get the input tensor's data type
            val inputShape = inputTensor?.shape()  // Get the input tensor's shape

            Log.d(TAG, "Input tensor data type: $inputType")
            Log.d(TAG, "Input tensor shape: ${inputShape?.joinToString()}")

            // Inspect the first output tensor
            val outputTensor = interpreter?.getOutputTensor(0)
            val outputType = outputTensor?.dataType() // Get the output tensor's data type
            val outputShape = outputTensor?.shape()  // Get the output tensor's shape

            Log.d(TAG, "Output tensor data type: $outputType")
            Log.d(TAG, "Output tensor shape: ${outputShape?.joinToString()}")

        } catch (e: Exception) {
            Log.e(TAG, "Error while inspecting model tensors", e)
        }
    }


    fun predictCategory(inputString: String) {
        if (interpreter == null) {
            return
        }
        try {
            // Preprocessing input untuk mendapatkan vektor fitur
            val inputTensor = preprocessInput(inputString)

            // Array untuk output
            val output =  Array(1) {FloatArray (211) }

            // Menjalankan prediksi
            interpreter?.run(inputTensor, output)

            // Memproses output dan mendapatkan kategori berdasarkan index
            val predictedCategoryIndex = postprocessOutput(output[0])

            // Mendapatkan kategori dari index
            val predictedCategory = getCategoryFromIndex(predictedCategoryIndex)

            onResult("Kategori yang diprediksi: $predictedCategory")

        } catch (e: Exception) {
            onError("Prediksi gagal: ${e.message}")
            Log.e(TAG, "Error dalam predictCategory", e)
        }
    }

    private fun postprocessOutput(output: FloatArray): Int {
        val max = output.maxOrNull() ?: 0f
        return output.indexOfFirst { it == max }
    }

    private fun preprocessInput(inputString: String): ByteBuffer {
        // Tokenize the input string by spaces (simple tokenization)
        val tokens = inputString.split(" ")

        // Initialize a buffer to hold the preprocessed input data
        val inputBuffer = ByteBuffer.allocateDirect(5000 * 4) // Float is 4 bytes
        inputBuffer.order(ByteOrder.nativeOrder()) // Set the byte order to native

        // Convert tokens to indices or embeddings
        val tokenIndices = tokens.map { token -> token.hashCode() % 5000 } // Example: simple hash-based indexing

        // Fill the input buffer with the vectorized data, ensuring the length is 5000
        for (i in 0 until 5000) {
            val value = if (i < tokenIndices.size) {
                tokenIndices[i].toFloat()
            } else {
                val min = 5
                val max = 15
                val randomInRange = min + (Math.random() * (max - min))
                randomInRange.toFloat()
            }
            inputBuffer.putFloat(value)
        }

        // Reset position to 0 to prepare for use in the interpreter
        inputBuffer.rewind()

        return inputBuffer
    }



    private fun getCategoryFromIndex(index: Int): String {
        // Load the categories from the JSON file and get the category by index
        val categories = loadCategoriesFromJson()
        return categories.getOrElse(index) { "Unknown Category" }
    }

    private fun loadCategoriesFromJson(): List<String> {
        val inputStream = context.assets.open("label_encoder.json")
        val jsonString = inputStream.bufferedReader().use { it.readText() }

        val categoryType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(jsonString, categoryType)
    }



    companion object {
        private const val TAG = "PredictionHelper"
    }

}