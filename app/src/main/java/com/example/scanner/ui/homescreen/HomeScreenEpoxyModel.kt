package com.example.scanner.ui.homescreen

import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyModelClass
import com.example.scanner.R
import com.example.scanner.databinding.ModelPinnedHeaderBinding
import com.example.scanner.databinding.ModelScanHeaderBinding
import com.example.scanner.databinding.ModelScanTopBarBinding
import com.example.scanner.databinding.ScanListItemBinding
import com.example.scanner.db.entity.Scan
import com.example.scanner.epoxy.ViewBindingKotlinModel
import com.example.scanner.utils.dateAsString

@EpoxyModelClass
abstract class ScanTopBarEpoxyModel :
    ViewBindingKotlinModel<ModelScanTopBarBinding>(R.layout.model_scan_top_bar) {

        @EpoxyAttribute
        lateinit var onInfoClicked: () -> Unit

    override fun ModelScanTopBarBinding.bind() {
        imageViewInfo.setOnClickListener { onInfoClicked() }
    }

}

@EpoxyModelClass
abstract class ScanHeaderEpoxyModel :
    ViewBindingKotlinModel<ModelScanHeaderBinding>(R.layout.model_scan_header) {

    @EpoxyAttribute
    var numOfScans: String = ""

    override fun ModelScanHeaderBinding.bind() {
        textViewNumOfScans.text = numOfScans
    }
}

@EpoxyModelClass
abstract class ScanListItemEpoxyModel :
    ViewBindingKotlinModel<ScanListItemBinding>(R.layout.scan_list_item) {

    @EpoxyAttribute
    lateinit var scan: Scan

    @EpoxyAttribute
    lateinit var onScanClicked: (Scan) -> Unit

    override fun ScanListItemBinding.bind() {
        val title = if (scan.scanTitle.isEmpty()) scan.scanText.lines()[0]
        else scan.scanTitle

        textViewDate.text = dateAsString(scan.dateModified)
        textViewTitle.text = title
        textViewContent.text = scan.scanText
        card.setOnClickListener { onScanClicked(scan) }
        imageViewPinned.isVisible = scan.isPinned
    }
}

@EpoxyModelClass
abstract class ListHeaderEpoxyModel :
    ViewBindingKotlinModel<ModelPinnedHeaderBinding>(R.layout.model_pinned_header) {
    @EpoxyAttribute
    lateinit var headerTitle: String

    override fun ModelPinnedHeaderBinding.bind() {
        textViewListHeader.text = headerTitle
    }
}