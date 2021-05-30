package com.google.codelab.hotpepperapiapp.viewModel

import androidx.lifecycle.ViewModel
import com.google.codelab.hotpepperapiapp.Signal
import io.reactivex.subjects.PublishSubject

class StoreWebViewViewModel : ViewModel() {
    val onClickFab: PublishSubject<Signal> = PublishSubject.create()

    fun onClickFab() {
        onClickFab.onNext(Signal)
    }
}
