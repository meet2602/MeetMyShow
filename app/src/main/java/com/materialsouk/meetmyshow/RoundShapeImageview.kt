package com.materialsouk.meetmyshow

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.ImageView

class RoundShapeImageview : ImageView {
    private var path: Path? = null
    private var radius = 18.0f
    private var rect: RectF? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init()
    }

    constructor(context: Context?, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    ) {
        init()
    }

    private fun init() {
        path = Path()
    }

    public override fun onDraw(canvas: Canvas) {
        rect = RectF(0.0f, 0.0f, width.toFloat(), height.toFloat())
        val path2 = path
        val rectF = rect
        val f = radius
        path2!!.addRoundRect(rectF!!, f, f, Path.Direction.CW)
        canvas.clipPath(path!!)
        super.onDraw(canvas)
    }
}
