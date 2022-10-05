package com.base.applicaton.di

import com.base.applicaton.screen.test_fragment.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TestViewModel(get()) }
}