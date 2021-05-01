package com.duzhaokun123.bilibilihd2.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.updatePadding
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.databinding.LayoutIvOverlayBinding
import com.duzhaokun123.bilibilihd2.ui.BigImageViewActivity
import com.stfalcon.imageviewer.StfalconImageViewer
import java.io.File

object ImageViewUtil {
    fun ImageView.setBiliLevel(level: Int) {
        when (level) {
            0 -> this.setImageResource(R.drawable.ic_user_level_0)
            1 -> this.setImageResource(R.drawable.ic_user_level_1)
            2 -> this.setImageResource(R.drawable.ic_user_level_2)
            3 -> this.setImageResource(R.drawable.ic_user_level_3)
            4 -> this.setImageResource(R.drawable.ic_user_level_4)
            5 -> this.setImageResource(R.drawable.ic_user_level_5)
            6 -> this.setImageResource(R.drawable.ic_user_level_6)
        }
    }

    fun viewImage(context: Context, imageUrl: String?, imageView: ImageView? = null) =
       viewImage(context, listOf(imageUrl), listOf(imageView), 0)

    fun viewImage(context: Context, imageUrls: List<String?>, imageViews: List<ImageView?>, startPosition: Int) {
        var siv: StfalconImageViewer<*>? = null
        var position = startPosition
        val overlayBinding =
            DataBindingUtil.inflate<LayoutIvOverlayBinding>(
                LayoutInflater.from(context),
                R.layout.layout_iv_overlay,
                null,
                false
            )
        overlayBinding.tb.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.share -> {
                    runNewThread {
                        val shareUri: Uri
                        val srcFile = Glide.with(context).asFile().load(imageUrls[position]).submit().get()
                        val shareFile = File(
                            context.cacheDir,
                            "shareImg${File.separatorChar}share.jpeg"
                        ).apply { parentFile!!.mkdirs() } // FIXME: 20-11-2 你凭什么认为一定是 jpeg 格式
                        srcFile.copyTo(shareFile, overwrite = true)
                        shareUri = FileProvider.getUriForFile(
                            context,
                            "com.duzhaokun123.bilibilihd2.fileprovider",
                            shareFile
                        )
                        context.startActivity(Intent.createChooser(Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_STREAM, shareUri)
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            setDataAndType(shareUri, context.contentResolver.getType(shareUri))
                        }, context.getText(R.string.share_to)))
                    }
                    true
                }
                R.id.download -> {
                    imageUrls[position]?.let { it1 -> DownloadUtil.downloadPicture(context, it1) }
                    true
                }
                R.id.close -> {
                    siv?.close()
                    true
                }
                R.id.open -> {
                    BigImageViewActivity.enter(context, imageUrls[position]!!)
                    true
                }
                else -> false
            }
        }
        ViewCompat.setOnApplyWindowInsetsListener(overlayBinding.tb) { v, insets ->
            insets.maxSystemBarsDisplayCutout.let {
                v.updatePadding(left = it.left, right = it.right, top = it.top)
            }
            insets
        }
        StfalconImageViewer.Builder(context, imageUrls) { imageView, imageUrl ->
                Glide.with(context).load(imageUrl).into(imageView)
        }
            .withHiddenStatusBar(false)
            .withTransitionFrom(imageViews[position])
            .withOverlayView(overlayBinding.root)
            .withStartPosition(position)
            .withImageChangeListener {
                position = it
                siv!!.updateTransitionImage(imageViews[position])
            }
            .show().also { siv = it }
    }
}
