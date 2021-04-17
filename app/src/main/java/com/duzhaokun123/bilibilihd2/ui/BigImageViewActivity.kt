package com.duzhaokun123.bilibilihd2.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.bases.BaseActivity
import com.duzhaokun123.bilibilihd2.databinding.ActivityBivBinding
import com.duzhaokun123.bilibilihd2.utils.TipUtil
import com.duzhaokun123.bilibilihd2.utils.runMain
import com.duzhaokun123.bilibilihd2.utils.runNewThread
import kotlin.math.max
import kotlin.math.min

class BigImageViewActivity : BaseActivity<ActivityBivBinding>(
    R.layout.activity_biv,
    Config.TRANSPARENT_TOOL_BAR, Config.LAYOUT_NO_TOOL_BAR
) {
    companion object {
        private const val EXTRA_URL = "url"

        fun enter(context: Context, url: String) {
            context.startActivity(Intent(context, BigImageViewActivity::class.java).apply {
                putExtra(EXTRA_URL, url)
            })
        }
    }

    override fun initView() {
        title = null
    }

    override fun initData() {
        val url = startIntent.getStringExtra(EXTRA_URL) ?: return
        val imageView = baseBinding.pv
        Glide.with(this).load(url).addListener(object : RequestListener<Drawable> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable>?,
                isFirstResource: Boolean
            ): Boolean {
                TipUtil.showToast(e?.message)
                return true
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any?,
                target: Target<Drawable>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (resource is BitmapDrawable) {
                    runNewThread {
                        val bitmap =
                            Glide.with(this@BigImageViewActivity).asBitmap().load(url).submit()
                                .get()
                        val sX = imageView.measuredWidth / bitmap.width.toFloat()
                        val sY = imageView.measuredHeight / bitmap.height.toFloat()
                        val scale = min(sX, sY)
                        runMain {
                            imageView.apply {
                                scaleType = ImageView.ScaleType.CENTER
                                setImageBitmap(bitmap)
                                maximumScale = max(5F, scale)
                                mediumScale = (maximumScale + scale) / 2
                                minimumScale = scale
                                this@apply.scale = scale
                            }
                        }
                    }
                }
                return false
            }
        }).into(imageView)
    }
}