package com.example.scanner.ui.homescreen

import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.EpoxyTouchHelper
import com.example.scanner.R
import com.example.scanner.databinding.FragmentScanHomeBinding
import com.example.scanner.service.FilterTextService
import com.example.scanner.utils.*
import com.google.android.material.transition.MaterialSharedAxis
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeScreenFragment : Fragment(R.layout.fragment_scan_home) {
    private val binding: FragmentScanHomeBinding by viewBinding()
    private val viewModel: HomeScreenViewModel by viewModel()

    private val selectImageRequest = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.showLoadingDialog()
            scanText(uri) { scannedText, filteredTextList ->
                viewModel.createScan(scannedText, filteredTextList)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateWindowsInset(binding.root)
        requireActivity().window.navigationBarColor = getColor(android.R.color.transparent)

        postponeEnterTransition()
        view.doOnPreDraw {
            startPostponedEnterTransition()
        }

        val loadingDailoag = createLoadingDialog()

        collectStateFlow(viewModel.viewState) { state ->
            binding.apply {
                linearLayoutEmpty.isVisible = state.isEmpty

                recyclerViewScans.withModels {
                    scanHeader {
                        id("scan_header")
                        numOfScans(getString(R.string.num_of_scans, state.itemCount))
                    }
                    if (state.isPinned.isNotEmpty()) {
                        listHeader {
                            id("pinned_header")
                            headerTitle(getString(R.string.header_pinned))
                        }
                        state.isPinned.forEach {
                            scanListItem {
                                id(it.scanId)
                                scan(it)
                                onScanClicked {
                                    exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                                    reenterTransition =
                                        MaterialSharedAxis(MaterialSharedAxis.X, false)
                                    val args = bundleOf("scan_id" to it.scanId.toInt())
//                                  nav
                                }
                            }
                        }
                    }
                    if (state.otherScan.isNotEmpty()) {
                        listHeader {
                            id("other_headers")
                            headerTitle(getString(R.string.headers_other))
                        }
                        state.otherScan.forEach {
                            scanListItem {
                                id(it.scanId)
                                scan(it)
                                onScanClicked {
                                    exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                                    reenterTransition =
                                        MaterialSharedAxis(MaterialSharedAxis.X, false)
                                    val args = bundleOf("scan_id" to it.scanId.toInt())
                                }
                            }
                        }
                    }
                }
            }
        }

        binding.apply {
            recyclerViewScans.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (dy > 0) {
                        buttonCreateScan.hide()
                    } else {
                        buttonCreateScan.show()
                    }
                }
            })
            val delete =
                ContextCompat.getDrawable(requireContext(), R.drawable.ic_round_delete_white_24)
            EpoxyTouchHelper.initSwiping(recyclerViewScans)
                .left()
                .withTarget(ScanListItemEpoxyModel::class.java)
                .andCallbacks(object : EpoxyTouchHelper.SwipeCallbacks<ScanListItemEpoxyModel>() {
                    override fun onSwipeProgressChanged(
                        model: ScanListItemEpoxyModel?,
                        itemView: View?,
                        swipeProgress: Float,
                        canvas: Canvas?
                    ) {
                        itemView?.let { view ->
                            view.alpha = swipeProgress + 1
                            val itemHeight = view.bottom - view.top
                            delete?.setTint(getColor(R.color.error_red))

                            val iconTop = view.top + (itemHeight - delete!!.intrinsicHeight) / 2
                            val iconMargin = (itemHeight - delete.intrinsicHeight) / 2
                            val iconLeft = view.right - iconMargin - delete.intrinsicWidth
                            val iconRight = view.right - iconMargin
                            val iconBottom = iconTop + delete.intrinsicHeight

                            delete.setBounds(iconLeft, iconTop, iconRight, iconBottom)
                            delete.draw(canvas!!)
                        }
                    }

                    override fun onSwipeCompleted(
                        model: ScanListItemEpoxyModel?,
                        itemView: View?,
                        position: Int,
                        direction: Int
                    ) {
                        model?.let {
                            viewModel.deleteScan(it.scan)
                        }
                    }
                })
        }

        collectFlow(viewModel.events) { homeEvents ->
            when (homeEvents) {
                is HomeScreenEvents.ShowCurrentScanSaved -> {
                    loadingDailoag.dismiss()
                    val arg = bundleOf("scan_id" to homeEvents.id, "is_created" to 1)
//                    findNavController().navigate(
//                        R.id.action_homeScanFragment_to_detailScanFragment,
//                        arg
//                    )
                }
                is HomeScreenEvents.ShowLoadingDialog -> {
                    loadingDailoag.show()
                }
                is HomeScreenEvents.ShowScanEmpty -> {
                    loadingDailoag.dismiss()
                    showSnackbarShort(
                        message = getString(R.string.no_text_found),
                        anchor = binding.buttonCreateScan
                    )
                }
                is HomeScreenEvents.ShowUndoDeleteScan -> {
                    showSnackbarLongWithAction(
                        message = getString(R.string.scan_deleted),
                        anchor = binding.buttonCreateScan,
                        actionText = getString(R.string.undo)
                    ) {
                        viewModel.insertScan(homeEvents.scan)
                    }
                }
                is HomeScreenEvents.ShowOnboarding -> {
//                    findNavController().navigate(R.id.action_homeScanFragment_to_viewPagerFragment)
                }
            }
        }

        binding.apply {
            recyclerViewScans.apply {
                layoutAnimation =
                    AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_anim)

                buttonCreateScan.setOnClickListener {
                    exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
                    reenterTransition = MaterialSharedAxis(MaterialSharedAxis.X, false)
                    selectImageRequest.launch("image/*")
                }
                /*
                Sets the animation to loop only 3 times and then stop as to not be too annoying.
                 */
                animationView.repeatCount = 2
            }
        }
    }
    private fun scanText(uri: Uri, action: (String, List<Pair<String, String>>) -> Unit) {
        val completeText = StringBuilder()
        val filterService: FilterTextService by inject()
        val list = mutableListOf<Pair<String, String>>()
        try {
            val image = InputImage.fromFilePath(requireContext(), uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnCompleteListener { task ->
                    val scannedText = task.result
                    for (block in scannedText.textBlocks) {
                        for (line in block.lines) {
                            for (element in line.elements) {
                                Log.d("DEBUGn", "scanText: element - ${element.text}")
                                list.addAll(filterService.filterTextForEmails(element.text))
                                list.addAll(filterService.filterTextForPhoneNumbers(element.text))
                                list.addAll(filterService.filterTextForLinks(element.text))
                                completeText.append(element.text + " ")
                            }
                        }
                    }
                    val display = completeText.toString()
                    action(display, list)
                }
                .addOnFailureListener { e -> throw e }
        } catch (e: Exception) {
            Log.e("DEBUGn", "scanText: ", e)
        }
    }
}