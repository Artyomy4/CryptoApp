package ru.teempton.cryptoapp.api

import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query
import ru.teempton.cryptoapp.pojo.CoinInfoListOfDate
import ru.teempton.cryptoapp.pojo.CoinPriceInfoRowDate

interface ApiService {

    @GET("top/totalvolfull")
    fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) api_key:String = "3aa615a5c9d8ea99fceab9df955f7075c16d1fe3bcf60b9954765aa2d41b2307",
        @Query(QUERY_PARAM_LIMIT) limit:Int = 10,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym:String = CURRENCY
    ):Single<CoinInfoListOfDate>

    @GET("pricemultifull")
    fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) api_key:String = "3aa615a5c9d8ea99fceab9df955f7075c16d1fe3bcf60b9954765aa2d41b2307",
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms:String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms:String = CURRENCY

    ):Single<CoinPriceInfoRowDate>

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
        private const val QUERY_PARAM_LIMIT = "limit"
        private const val QUERY_PARAM_TO_SYMBOL = "tsym"
        private const val QUERY_PARAM_TO_SYMBOLS = "tsyms"
        private const val QUERY_PARAM_FROM_SYMBOLS = "fsyms"
        private const val CURRENCY = "USD"
    }
}