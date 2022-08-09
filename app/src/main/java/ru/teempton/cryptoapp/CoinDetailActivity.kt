package ru.teempton.cryptoapp

import android.content.Context
import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.squareup.picasso.Picasso
import ru.teempton.cryptoapp.databinding.ActivityCoinDetailBinding
import ru.teempton.cryptoapp.databinding.ActivityCoinPriceListBinding
import ru.teempton.cryptoapp.pojo.CoinPriceInfo

class CoinDetailActivity : AppCompatActivity() {
    private lateinit var viewModel: CoinViewModel
    private lateinit var binding: ActivityCoinDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCoinDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }

        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)
        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
        viewModel.getDetailInfo(fromSymbol!!).observe(this, Observer {
            SetDataView(it)
        })
    }

    private fun SetDataView(coinPriceInfo: CoinPriceInfo) = with(binding) {
        tvPrice.text = coinPriceInfo.price
        tvMinPrice.text = coinPriceInfo.lowday
        tvMaxPrice.text = coinPriceInfo.highday
        tvLastDeal.text = coinPriceInfo.lastmarket
        tvUpdateDay.text = coinPriceInfo.getFormattedTime()
        tvFromSymbol.text = coinPriceInfo.fromsymbol
        tvToSymbol.text = coinPriceInfo.tosymbol
        tvValueDeal.text = coinPriceInfo.lastvolume
        tvVoliumByDay.text = coinPriceInfo.volumeday
        Picasso.get().load(coinPriceInfo.getFullImageUrl()).into(ivLogoCoin)
    }

    companion object {
        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }


}