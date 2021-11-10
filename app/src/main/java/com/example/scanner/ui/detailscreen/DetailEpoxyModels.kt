package com.example.scanner.ui.detailscreen

import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.scanner.R
import com.example.scanner.databinding.ModelChipBinding
import com.example.scanner.db.entity.FilterTextModel
import com.example.scanner.epoxy.ViewBindingKotlinModel
import com.google.android.material.card.MaterialCardView

@EpoxyModelClass
abstract class ChipEpoxyModel : ViewBindingKotlinModel<ModelChipBinding>(R.layout.model_chip) {

    @EpoxyAttribute
    lateinit var model: FilterTextModel

    @EpoxyAttribute
    lateinit var onModelClick: () -> Unit

    @EpoxyAttribute
    lateinit var initCard: (MaterialCardView) -> Unit

    override fun ModelChipBinding.bind() {
        initCard(chipCard)
        chipCard.setOnClickListener { onModelClick() }
        textViewChip.text = model.content
    }
}