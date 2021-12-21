package com.duzhaokun123.bilibilihd2.ui.main

import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import androidx.lifecycle.MutableLiveData
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.bases.BaseSimpleCardGridSRRVFragment
import com.duzhaokun123.bilibilihd2.databinding.ItemHistoryCardBinding
import com.duzhaokun123.bilibilihd2.model.HistoryCardModel
import com.duzhaokun123.bilibilihd2.utils.*
import com.duzhaokun123.generated.Settings

import io.github.duzhaokun123.bilibili.api.BilibiliService
import io.github.duzhaokun123.bilibili.api.api.model.History

class HistoryFragment :
    BaseSimpleCardGridSRRVFragment<ItemHistoryCardBinding, HistoryCardModel, HistoryFragment.HistoryModel>(
        R.layout.item_history_card,
        Settings.mainCardWidthDp.let { run { if (it == 0) 500 else it }.dpToPx() },
        HistoryModel::class
    ) {
    class HistoryModel : BaseSimpleCardGridSRRVFragment.BaseModel<HistoryCardModel>() {
        val viewAt = MutableLiveData(0L)
    }

    override suspend fun onRefreshIO(): List<HistoryCardModel>? {
        return runCatching {
            HistoryCardModel.parse(
                BilibiliService.apiApi.getHistory().await()
                    .also {
                        if (it.data.cursor != null) {
                            baseModel.viewAt.postValue(it.data.cursor.viewAt)
                        }else
                            setNoMoreData(true)
                    })
        }.also { if (it.isFailure) TipUtil.showTip(context, it.exceptionOrNull()!!.message) }
            .getOrNull()
    }

    override suspend fun onLoadMorIO(): List<HistoryCardModel>? {
        return runCatching {
            HistoryCardModel.parse(
                BilibiliService.apiApi.getHistory(
                    viewAt = baseModel.viewAt.value!!
                ).await().also {
                    if (it.data.cursor != null) {
                        baseModel.viewAt.postValue(it.data.cursor.viewAt)
                    } else
                        setNoMoreData(true)
                })
        }.onFailure {
            TipUtil.showTip(context, it.message)
        }.getOrNull()
    }

    override fun initItemView(
        itemBinding: ItemHistoryCardBinding, itemModel: HistoryCardModel, position: Int
    ) {
        itemBinding.cv.setOnClickListener {
            itemModel.uri?.let { BrowserUtil.openInApp(context, it) }
        }
        itemBinding.cv.setOnLongClickListener {
            itemBinding.ibTp.callOnClick()
            true
        }
        itemBinding.ibTp.setOnClickListener {
            PopupMenu(requireContext(), itemBinding.ibTp).apply {
                menu.add("检查封面").setOnMenuItemClickListener {
                    ImageViewUtil.viewImage(requireContext(), itemModel.coverUrl, itemBinding.iv)
                    true
                }
            }.show()
        }
    }

    override fun initItemData(
        itemBinding: ItemHistoryCardBinding, itemModel: HistoryCardModel, position: Int
    ) {
        itemBinding.model = itemModel
    }

    override fun onApplyWindowInsetsCompat(insets: WindowInsetsCompat) {
        super.onApplyWindowInsetsCompat(insets)
        insets.systemBars.let {
            baseBinding.cf.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = it.bottom
            }
            baseBinding.srl.updatePadding(bottom = it.bottom)
        }
    }
}