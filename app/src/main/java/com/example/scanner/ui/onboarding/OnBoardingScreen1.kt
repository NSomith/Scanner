package com.example.scanner.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.scanner.R
import com.example.scanner.databinding.FragmentOnBoardingScreen1Binding

class OnBoardingScreen1:Fragment(R.layout.fragment_on_boarding_screen1) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentOnBoardingScreen1Binding.bind(view)
        val viewpager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)

        binding.apply {
            buttonNext.setOnClickListener {
                viewpager.currentItem = 1
            }
        }
    }
}