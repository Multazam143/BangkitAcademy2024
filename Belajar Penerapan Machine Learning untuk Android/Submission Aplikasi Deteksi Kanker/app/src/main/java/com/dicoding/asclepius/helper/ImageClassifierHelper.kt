package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.media.Image
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import com.dicoding.asclepius.ml.CancerClassification
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.Error


class ImageClassifierHelper(
    var threshold: Float = 0.1f,
    var maxResult: Int = 3,
    val modelName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {
    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        // TODO: Menyiapkan Image Classifier untuk memproses gambar.
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResult)
        val baseOptionBuilder = BaseOptions.builder()
            .setNumThreads(4)
        optionsBuilder.setBaseOptions(baseOptionBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        }catch (e: IllegalStateException){
            classifierListener?.onError(context.getString(R.string.image_classifier_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri){
        // TODO: mengklasifikasikan imageUri dari gambar statis.
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val source = ImageDecoder.createSource(context.contentResolver, imageUri)
                ImageDecoder.decodeBitmap(source)
            } else {
                MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
            }.copy(Bitmap.Config.ARGB_8888, true)

            if (bitmap!= null){
                val imageProcessor = ImageProcessor.Builder ()
                    .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.BILINEAR))

                    .add(CastOp(DataType.UINT8))
                    .build()

                val tensorImage = imageProcessor.process(TensorImage.fromBitmap(bitmap))

                var inferenceTime = SystemClock.uptimeMillis()
                val results = imageClassifier?.classify(tensorImage)
                inferenceTime = SystemClock.uptimeMillis() - inferenceTime

                classifierListener?.onResults(results, inferenceTime)
            } else {
                classifierListener?.onError("Gagal memuat gambar")
            }
        } catch (e: Exception) {
            classifierListener?.onError("Error: ${e.message}")
            Log.e(TAG, "Error classifying image: ${e.localizedMessage}")
        }
    }




    interface  ClassifierListener {
        fun onError(error: String)
        fun onResults(
            results: List<Classifications>?,
            inferenceTime: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

}