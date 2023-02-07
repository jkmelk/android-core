package com.fastshift.cashier.di

import com.fastshift.cashier.screen.test_fragment.TestViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { TestViewModel(get()) }
}