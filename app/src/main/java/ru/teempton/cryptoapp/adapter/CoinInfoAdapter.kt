package ru.teempton.cryptoapp.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ru.teempton.cryptoapp.R
import ru.teempton.cryptoapp.databinding.ItemCoinInfoBinding
import ru.teempton.cryptoapp.pojo.CoinPriceInfo

class CoinInfoAdapter(private val context: Context) :
    RecyclerView.Adapter<CoinInfoAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {//каждый раз когда будет присваиваться значение coinInfoList вызовется сеттер и рецайлвью обновит значение
            field = value
            notifyDataSetChanged()
        }

    var onCoinClickListener:OnCoinClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList.get(position)
        with(holder) {
            with(coin) {
                val symbolsTemplate = context.resources.getString(R.string.symbol_template)
                val lastUpdateTemplate = context.resources.getString(R.string.last_update_template)
                tvSymbol.text = String.format(symbolsTemplate,fromsymbol,tosymbol)
                tvPrice.text = price
                tvLastUpdate.text = String.format(lastUpdateTemplate,getFormattedTime())
                Picasso.get().load(getFullImageUrl()).into(ivLogoCoin)
            }
        }
        holder.itemView.setOnClickListener {
            onCoinClickListener?.onCoinClick(coin)
        }
    }

    override fun getItemCount(): Int {
        return coinInfoList.size
    }

    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemCoinInfoBinding.bind(itemView)
        val ivLogoCoin = binding.ivLogoCoin
        val tvSymbol = binding.tvSymbol
        val tvPrice = binding.tvPrice
        val tvLastUpdate = binding.tvLastUpdate

    }

    interface OnCoinClickListener{
        fun onCoinClick(coinPriceInfo: CoinPriceInfo)
    }
}