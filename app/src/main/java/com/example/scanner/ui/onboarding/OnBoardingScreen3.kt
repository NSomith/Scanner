package com.example.scanner.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.scanner.R
import com.example.scanner.databinding.FragmentOnBoardingScreen3Binding

class OnBoardingScreen3:Fragment(R.layout.fragment_on_boarding_screen3) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentOnBoardingScreen3Binding.bind(view)
        val viewPager = requireActivity().findViewById<ViewPager2>(R.id.view_pager)

        binding.apply {
            animationView.repeatCount = 2

            buttonFinish.setOnClickListener {
                findNavController().navigateUp()
            }

            buttonPrevious.setOnClickListener {
                viewPager.currentItem = 1
            }
        }

    }
}