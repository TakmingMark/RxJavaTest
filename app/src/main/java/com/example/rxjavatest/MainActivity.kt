package com.example.rxjavatest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rxjavatest.API.RetrofitManager
import com.example.rxjavatest.Model.Albums
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "RxJava"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        callAPI()

//
//        Log.d(TAG,Thread.currentThread().name)
//        var observable = Observable.create(ObservableOnSubscribe<Int> {
//            it.onNext(1)
//            it.onNext(2)
//            Log.d(TAG,Thread.currentThread().name)
//            it.onComplete()
//        })
//
//        var observer = object : Observer<Int> {
//            override fun onComplete() {
//                Log.d(TAG, "onComplete()")
//            }
//
//            override fun onSubscribe(d: Disposable?) {
//                Log.d(TAG, "onSubscribe()")
//            }
//
//            override fun onNext(value: Int?) {
//                Log.d(TAG,Thread.currentThread().name)
//                Log.d(TAG, "$value")
//            }
//
//            override fun onError(e: Throwable?) {
//                Log.d(TAG, "onError()")
//            }
//        }
//
//        observable
//            .subscribeOn(Schedulers.newThread())
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnNext(Consumer<Int>{
//                Log.d(TAG,"After observeOn(mainThread), current thread is "+Thread.currentThread().name)
//                Log.d(TAG,"$it")
//            })
//            .observeOn(Schedulers.io())
//            .doOnNext(Consumer<Int> {
//                Log.d(TAG,"After observeOn(IO),current thread is "+Thread.currentThread().name)
//                Log.d(TAG,"$it")
//            })
//
//            .subscribe(observer)

    }

    fun callAPI() {
        var api = RetrofitManager.api

        api.getAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Albums> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Albums) {
                    text_view.text = t.title
                }

                override fun onError(e: Throwable) {
                }
            })

    }
}
