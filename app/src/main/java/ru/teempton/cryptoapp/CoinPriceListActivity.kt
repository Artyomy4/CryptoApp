package ru.teempton.cryptoapp

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider

class CoinPriceListActivity : AppCompatActivity() {

    private lateinit var viewModel: CoinViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_price_list)
        viewModel = ViewModelProvider(this).get(CoinViewModel::class.java)
//        viewModel.priceList.observe(this, Observer {
//            Log.d("MyLog","Success in activity: ${it}")
//        })
        viewModel.getDetailInfo("BTC").observe(this, Observer {
            Log.d("MyLog","Success in activity: ${it}")
        })
    }
}