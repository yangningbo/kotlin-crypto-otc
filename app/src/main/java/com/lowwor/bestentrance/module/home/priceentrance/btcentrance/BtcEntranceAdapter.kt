package com.lowwor.bestentrance.module.home.priceentrance.btcentrance

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lowwor.bestentrance.R
import com.lowwor.bestentrance.data.model.price.EntrancePrice

/**
 * Created by lowwor on 2018/1/31.
 */
class BtcEntranceAdapter : BaseQuickAdapter<EntrancePrice, BaseViewHolder>(R.layout.item_entrance_price) {

    override fun convert(helper: BaseViewHolder, item: EntrancePrice) {

        helper.setText(R.id.text_market_name, item.name)
        helper.setText(R.id.text_step, item.step)
        helper.setText(R.id.text_price, item.price.toString())
    }


}