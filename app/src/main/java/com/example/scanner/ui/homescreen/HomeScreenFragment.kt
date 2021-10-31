package com.example.scanner.ui.homescreen

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.scanner.R
import com.example.scanner.databinding.FragmentScanHomeBinding

class HomeScreenFragment:Fragment(R.layout.fragment_scan_home) {
    private lateinit var binding:FragmentScanHomeBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentScanHomeBinding.bind(view)


    }
}