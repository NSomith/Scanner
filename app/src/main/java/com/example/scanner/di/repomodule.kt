package com.example.scanner.di

import com.example.scanner.repo.FilterTextRepo
import com.example.scanner.repo.ScanRepo
import org.koin.dsl.module

val repoModule = module {
    single { ScanRepo(db = get()) }
    single { FilterTextRepo(db = get()) }
}