package com.duzhaokun123.bilibilihd2.ui.main.dynamicholders

import android.annotation.SuppressLint
import android.content.Context
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.databinding.DynamicCard1Binding
import com.duzhaokun123.bilibilihd2.model.DynamicCardModel
import com.duzhaokun123.bilibilihd2.ui.main.DynamicAdapter

class DynamicHolderType1(context: Context) :
    DynamicAdapter.BaseDynamicHolder<DynamicCard1Binding, DynamicCardModel.Type1>(
        context, R.layout.dynamic_card_1
    ) {
    @SuppressLint("SetTextI18n")
    override fun initView(
        contentBinding: DynamicCard1Binding,
        model: DynamicCardModel,
        typedCard: DynamicCardModel.Type1
    ) {
        contentBinding.flContent.removeAllViews()
        val holder = DynamicAdapter.supportedTypes.getOrDefault(
            typedCard.originType, DynamicHolderType0::class.java
        ).getConstructor(Context::class.java).newInstance(context) as DynamicAdapter.BaseDynamicHolder<*, Any>
        val d = typedCard.toDynamicCardModel()
        holder.setModel(d)
        holder.rootBinding.flContent.removeAllViews()
        val content = holder.contentBinding.root
        content.setOnClickListener { holder.onCardClick(d, d.card) }
        contentBinding.flContent.addView(content)
    }

    override fun initData(
        contentBinding: DynamicCard1Binding,
        model: DynamicCardModel,
        typedCard: DynamicCardModel.Type1
    ) {
        contentBinding.card = typedCard
    }
}