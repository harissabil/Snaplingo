package com.igd.snaplingo.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ObjectDetectionResponseItem(

	@field:SerializedName("score")
	val score: Double? = null,

	@field:SerializedName("box")
	val box: Box? = null,

	@field:SerializedName("label")
	val label: String? = null
)

data class Box(

	@field:SerializedName("ymin")
	val ymin: Int? = null,

	@field:SerializedName("xmin")
	val xmin: Int? = null,

	@field:SerializedName("ymax")
	val ymax: Int? = null,

	@field:SerializedName("xmax")
	val xmax: Int? = null
)
