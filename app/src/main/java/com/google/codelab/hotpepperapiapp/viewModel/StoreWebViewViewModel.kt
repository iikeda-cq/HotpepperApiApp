package com.google.codelab.hotpepperapiapp.viewModel

import com.google.codelab.hotpepperapiapp.Signal
import io.reactivex.subjects.PublishSubject

class StoreWebViewViewModel {
    val onClickFab: PublishSubject<Signal> = PublishSubject.create()

    fun onClickFab() {
        onClickFab.onNext(Signal)
    }
}
