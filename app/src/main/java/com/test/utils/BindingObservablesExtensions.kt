package com.test.utils

import androidx.databinding.Observable
import androidx.databinding.ObservableField
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import androidx.databinding.Observable as DataBindingObservable

/**
 * When we subscribe to an [Flowable] created by this function,
 * the [Flowable] will emit the current value of [ObservableField]
 * and also emit any future changes.
 */
fun <T> ObservableField<T>.toFlowable(): Flowable<T> {
    return Flowable.create({ subscriber ->
        // To emit the current value.
        get()?.let {
            subscriber.onNext(it)
        }
        object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(observable: DataBindingObservable, id: Int) {
                try {
                    get()?.let {
                        subscriber.onNext(it)
                    }
                } catch (e: Exception) {
                    subscriber.onError(e)
                }
            }
        }.let {
            subscriber.setCancellable { this.removeOnPropertyChangedCallback(it) }
            this.addOnPropertyChangedCallback(it)
        }
    }, BackpressureStrategy.LATEST)
}