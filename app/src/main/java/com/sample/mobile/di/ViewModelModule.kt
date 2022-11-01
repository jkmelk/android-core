package com.sample.mobile.di

import com.sample.mobile.screen.sample.SampleViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { SampleViewModel(get()) }
}