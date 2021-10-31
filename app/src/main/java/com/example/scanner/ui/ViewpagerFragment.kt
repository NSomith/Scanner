package com.example.scanner.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.scanner.R
import com.example.scanner.databinding.FragmentViewPagerBinding
import com.example.scanner.ui.onboarding.OnBoardingScreen1
import com.example.scanner.ui.onboarding.OnBoardingScreen2
import com.example.scanner.ui.onboarding.OnBoardingScreen3

class ViewpagerFragment:Fragment(R.layout.fragment_view_pager) {
    lateinit var binding: FragmentViewPagerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentViewPagerBinding.bind(view)

        val fragmentlist = listOf<Fragment>(
            OnBoardingScreen1(),
            OnBoardingScreen2(),
            OnBoardingScreen3()
        )
        val adapter = ViewPagerAdapter(fragmentlist,childFragmentManager,viewLifecycleOwner.lifecycle)

        binding.apply {
            viewPager.adapter = adapter
        }
    }
}