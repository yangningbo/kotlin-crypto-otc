package com.lowwor.bestentrance.data.api

import com.lowwor.bestentrance.data.mapper.*
import com.lowwor.bestentrance.data.model.price.EntrancePrice
import com.lowwor.bestentrance.data.model.response.*
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables.zip
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by lowwor on 2018/1/23.
 */
@Singleton
class BestEntranceApi @Inject constructor(private val bestEntranceService: BestEntranceService) {


    fun getUsdtPrices(): Observable<List<EntrancePrice>> {
        return zip(getGankOtcUsdt(), getHuobiOtcUsdt(),
                getOtcbtcOtcUsdt(), { gankPriceRsp, huoBiPriceRsp, otcbtcPriceRsp ->


            listOf(GankMapper.convertToOneStepUsdtEntrancePrice(gankPriceRsp),
                    HuobiMapper.convertToOneStepUsdtEntrancePrice(huoBiPriceRsp),
                    OtcbtcMapper.convertToOneStepUsdtEntrancePrice(otcbtcPriceRsp)
            )
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    fun getBtcPrices(): Observable<List<EntrancePrice>> {
        return zip(getHuobiOtcUsdt().flatMap<Float> { huobiOtcUsdtRsp ->
            return@flatMap getHuobiTickerBtc().map {
                (huobiOtcUsdtRsp.data[0].price * it.tick.data[0].price).toFloat()
            }
        },
                getGankOtcUsdt().flatMap { gankOtcRsp ->
                    return@flatMap getGankTickerBtc().map { gankTickerBtcRsp ->
                        (gankTickerBtcRsp.last * gankOtcRsp.appraisedRates?.buyRate!!.toDouble()).toFloat()
                    }
                },
                getHuobiOtcBtc(), getOtcbtcOtcBtc(),
                getZbTickerBtc(), getLocalBitcoinOtcBtc(), { huobiTwoStepBtcPrice, gankTwoStepBtcPrice, huoBiPriceRsp, otcbtcPriceRsp, zbPriceRsp,
                                                             localBitcoinsPriceRsp ->

            listOf(HuobiMapper.convertToTwoStepBtcToEntrancePrice(huobiTwoStepBtcPrice),
                    HuobiMapper.convertToOneStepBtcEntrancePrice(huoBiPriceRsp),
                    GankMapper.convertToTwoStepBtcEntrancePrice(gankTwoStepBtcPrice),
                    OtcbtcMapper.convertToOneStepBtcEntrancePrice(otcbtcPriceRsp),
                    ZbMapper.convertToQcBtcEntrancePrice(zbPriceRsp),
                    LocalBitcoinMapper.convertToOneStepBtcEntrancePrice(localBitcoinsPriceRsp)
            )
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }


    private fun getHuobiOtcUsdt(): Observable<HuobiOtcRsp> =
            bestEntranceService.getHuobiOtcPrice(HUOBI_OTC_COIN_ID_USDT)

    private fun getHuobiTickerBtc(): Observable<HuobiTickerRsp> =
            bestEntranceService.getHuobiTickerPrice(HUOBI_TICKER_BTC_USDT)

    private fun getHuobiOtcBtc(): Observable<HuobiOtcRsp> =
            bestEntranceService.getHuobiOtcPrice(HUOBI_OTC_COIN_ID_BTC)

    private fun getOtcbtcOtcUsdt(): Observable<OtcbtcPriceRsp> =
            bestEntranceService.getOtcbtcOtcPrice(OTCBTC_OTC_CURRENCY_USDT)

    private fun getOtcbtcOtcBtc(): Observable<OtcbtcPriceRsp> =
            bestEntranceService.getOtcbtcOtcPrice(OTCBTC_OTC_CURRENCY_BTC)

    private fun getZbTickerBtc(): Observable<ZbTickerRsp> =
            bestEntranceService.getZbTickerPrice(ZB_TICKER_BTC_QC)

    private fun getGankOtcUsdt(): Observable<GankOtcRsp> =
            bestEntranceService.getGankOtcUsdtPrice(GANK_OTC_SYMBOL_USDT)

    private fun getGankTickerBtc(): Observable<GankTickerRsp> =
            bestEntranceService.getGankTickerPrice(GANK_TICKER_BTC_USDT)

    private fun getLocalBitcoinOtcBtc(): Observable<LocalBitcoinOtcRsp> =
            bestEntranceService.getLocalBitcoinOtcPrice()


}