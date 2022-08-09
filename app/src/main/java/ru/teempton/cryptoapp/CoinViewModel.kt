package ru.teempton.cryptoapp

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.teempton.cryptoapp.api.ApiFactory
import ru.teempton.cryptoapp.db.AppDB
import ru.teempton.cryptoapp.pojo.CoinPriceInfo
import ru.teempton.cryptoapp.pojo.CoinPriceInfoRowDate
import java.util.concurrent.TimeUnit

class CoinViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDB.getInstance(application)
    private val compositeDisposable = CompositeDisposable()
    val priceList = db.coinPriceInfoDao().getPriceList()

    init {
        loadData()
    }

    fun getDetailInfo(fSym:String):LiveData<CoinPriceInfo>{
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    private fun loadData() {
        val disposable =
            ApiFactory.apiService.getTopCoinsInfo(limit = 100)//стртовали загрузку самых попудярныъх валют
                .map {
                    it.data?.map { it.coinInfo?.name }?.joinToString(",")
                }//преобразовали List в строку
                .flatMap { ApiFactory.apiService.getFullPriceList(fSyms = it.toString()) }//flatMap возьмет строку(it) и передает во внутрь
                .map { getPriceListFromRawDate(it) }
                .delaySubscription(10,TimeUnit.SECONDS)//период через какой повторять загрузеку
                .repeat()//повторяет загрузку
                .retry()//повторяет загрузку при ошибках
                .subscribeOn(Schedulers.io())
                .subscribe({
                    db.coinPriceInfoDao().insertPriceList(it)
                    //Log.d("MyLog", "Success: $it")
                }, {
                    Log.d("MyLog", "Failure: ${it.message.toString()}")
                })
        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawDate(coinPriceInfoRowDate: CoinPriceInfoRowDate): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRowDate.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}