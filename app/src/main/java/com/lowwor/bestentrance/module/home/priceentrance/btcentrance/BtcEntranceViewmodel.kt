package com.lowwor.bestentrance.module.home.priceentrance.btcentrance

import android.arch.lifecycle.MutableLiveData
import com.lowwor.bestentrance.base.BaseViewModel
import com.lowwor.bestentrance.data.api.BestEntranceApi
import com.lowwor.bestentrance.data.model.price.EntrancePrice
import com.lowwor.bestentrance.util.SingleLiveEvent
import io.reactivex.rxkotlin.plusAssign
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by lowwor on 2018/1/31.
 */
class BtcEntranceViewmodel @Inject constructor(
        private val api: BestEntranceApi
) : BaseViewModel() {
    val entrancePrices = MutableLiveData<List<EntrancePrice>>()
    val refreshStatus = SingleLiveEvent<Unit>()

    init {
        refreshStatus.call()
        updatePrices()
    }


    fun updatePrices() {
        disposables += api.getBtcPrices().subscribe({
            Timber.d(it.toString())
            entrancePrices.value = it
        }, { it.printStackTrace() })
    }
}