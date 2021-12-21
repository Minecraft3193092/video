package com.duzhaokun123.bilibilihd2.model

import com.duzhaokun123.bilibilihd2.utils.DateFormat
import io.github.duzhaokun123.bilibili.api.api.model.History


data class HistoryCardModel(
    val title: String?,
    val desc: String?,
    val coverUrl: String?,
    val uri: String?,
    val badge: String?,
    /**
     * 百分进度
     */
    val progress: Int,
    val hasProgress: Boolean,
) {
    companion object {
        fun parse(history: History): List<HistoryCardModel> {
            val badgeMap = mutableMapOf<String, String>()
            history.data.tab.forEach { tab ->
                badgeMap[tab.type] = tab.name
            }
            val models = mutableListOf<HistoryCardModel>()
            history.data.list.forEach { item ->
                val title = item.title
                val desc = "${item.authorName} ${DateFormat.format1.format(item.viewAt * 1000L)}"
                val coverUrl = item.cover.takeUnless { it.isEmpty() } ?: item.covers?.get(0)
                val uri = when(item.history.business) {
                    "archive", "pgc" -> "bilibili://video/${item.history.oid}"
                    "live" -> "bilibili://live/${item.history.oid}"
                    "article" -> "bilibili://article/${item.history.oid}"
                    else -> null
                }
                val badge = badgeMap[item.history.business] ?: item.history.business
                val hasProgress = item.progress != 0 && item.duration != 0
                val progress =
                    if (hasProgress) ((item.progress.toFloat() / item.duration) * 100).toInt() else 0
                models.add(
                    HistoryCardModel(title, desc, coverUrl, uri, badge, progress, hasProgress)
                )
            }
            return models
        }
    }
}