package com.example.letsgo.models

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = Counter.counterTable)
class Counter(
    @ColumnInfo(name = "sets") private val sets: Int,
    @ColumnInfo(name = "reps") private val reps: Int,
    @ColumnInfo(name = "intensity") private val intensity: Int,
    @ColumnInfo(name = "unit") private val unit: String,
    @ColumnInfo(name = "tag") private val tag: String
) {

    // Use 0 as a default when creating the object or initializing the autogenerated key
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0

    val intensityInUnits = "${intensity} ${unit}"
    val volume = "${sets} × ${reps}"
    val hashtag = "#${tag}"

    @ColorInt
    private val setsPaintColor:Int = Color.rgb( 223, 34, 34)
    private val setsPaintColorTransparent = Color.argb(0, 223, 34, 34)
    private val minSetsRadius = 200f
    private val maxSetsRadius = 500f

    private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = setsPaintColor
        style = Paint.Style.FILL
    }

    lateinit var canvas: Canvas
    lateinit var bitmap: Bitmap

    private var setProgress = 0

    fun progress() = "$setProgress of ${sets}"
    fun isInProgress() = setProgress < sets

    fun getRandomRadius() = Random.nextInt(minSetsRadius.toInt(), maxSetsRadius.toInt()).toFloat()
    fun getRadialGradient(x: Float, y: Float, rad: Float) = RadialGradient(x, y, rad, setsPaintColor, setsPaintColorTransparent, Shader.TileMode.CLAMP)

    fun increment(){

        setProgress++

    }

    fun isBitmapInit(): Boolean {
        return ::bitmap.isInitialized
    }

    fun drawCircle(x: Float, y: Float, rad: Float){

        pointPaint.shader = getRadialGradient(x, y, rad)
        canvas.drawCircle(x, y, rad, pointPaint)

    }

    companion object{
        const val counterTable = "counters"
    }

}