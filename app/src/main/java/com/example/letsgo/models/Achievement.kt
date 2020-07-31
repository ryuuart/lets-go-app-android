package com.example.letsgo.models

import android.graphics.*
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(tableName = Achievement.tableName)
class Achievement(
    @ColumnInfo(name = "sets") val sets: Int,
    @ColumnInfo(name = "reps") val reps: Int,
    @ColumnInfo(name = "intensity") val intensity: Int,
    @ColumnInfo(name = "unit") val unit: String,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "colorStart") val colorStart: Int,
    @ColumnInfo(name = "colorEnd") val colorEnd: Int    // previously called "color transparent"
) {

    // Use 0 as a default when creating the object or initializing the autogenerated key
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "id") var id: Int = 0

    fun intensityInUnits() = "${intensity} ${unit}"
    fun volume() = "${sets} × ${reps}"
    fun hashtag() = "#${tag}"

    @Ignore
    private val pointPaint: Paint =
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = colorStart; style = Paint.Style.FILL }
    @ColumnInfo(name = "canvas") lateinit var canvas: Canvas
    @ColumnInfo(name = "bitmap") lateinit var bitmap: Bitmap

    @ColumnInfo(name = "setProgress") private var setProgress = 0

    fun progress() = "$setProgress of ${sets}"
    fun isInProgress() = setProgress < sets

    fun getRandomRadius() = Random.nextInt(RADIUS_MIN, RADIUS_MAX).toFloat()
    fun getRadialGradient(x: Float, y: Float, rad: Float) = RadialGradient(x, y, rad, colorStart, colorEnd, Shader.TileMode.CLAMP)

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
        const val tableName = "achievements"
        const val RADIUS_MIN = 200
        const val RADIUS_MAX = 500
    }

}