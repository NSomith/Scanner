package com.example.scanner.di

import com.example.scanner.ui.homescreen.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { HomeScreenViewModel(scanRepo = get(), filteredTextModelRepo = get(), pref = get()) }

}