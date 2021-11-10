package com.example.scanner.utils

import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.fragment.app.Fragment
import com.example.scanner.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

fun Fragment.updateWindowsInset(rootview: View){
    ViewCompat.setOnApplyWindowInsetsListener(rootview){view, inset->
        view.updatePadding(
            bottom= inset.getInsets(WindowInsetsCompat.Type.statusBars()).bottom,
            top = inset.getInsets(WindowInsetsCompat.Type.systemBars()).top
        )
        inset
    }
}
fun Fragment.createLoadingDialog(): AlertDialog {
    val inflater = requireActivity().layoutInflater
    return MaterialAlertDialogBuilder(requireContext())
        .setView(inflater.inflate(R.layout.dialog_loading, null))
        .create()
}
fun Fragment.showSnackbarShort(message: String, anchor: View? = null) =
    Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT)
        .setAnchorView(anchor)
        .show()

fun Fragment.showSnackbarLongWithAction(
    message: String,
    actionText: String,
    anchor: View? = null,
    onAction: () -> Unit
) = Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG)
    .setAnchorView(anchor)
    .setAction(actionText) { onAction() }
    .show()

fun Fragment.getColor(@ColorRes res: Int) = ContextCompat.getColor(requireContext(), res)
