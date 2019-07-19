package com.example.rxjavatest

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.rxjavatest.API.API
import com.example.rxjavatest.API.RetrofitManager
import com.example.rxjavatest.API.RetrofitManager.api
import com.example.rxjavatest.Model.Albums
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "RxJava"
    }

    lateinit var api: API

    //Avoid Retrofit return data cause app crash when App is background.
    var disposableList = ArrayList<Disposable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        api = RetrofitManager.api
        callAPI2()

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
        api.getAllAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<Albums>> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                    disposableList.add(d)
                }

                override fun onNext(t: List<Albums>) {
                    text_view.text = t.toString()
                }

                override fun onError(e: Throwable) {
                }
            })
    }

    fun callAPI2() {
        api.getAllAlbums()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(object:Consumer<List<Albums>>{
                override fun accept(t: List<Albums>?) {
                    text_view.text=t.toString()
                }
            })
            .observeOn(Schedulers.io())
            .flatMap(object:Function<List<Albums>,ObservableSource<Albums>>{
                override fun apply(t: List<Albums>): ObservableSource<Albums> {
                    return Observable.fromIterable(t).delay(10, TimeUnit.MICROSECONDS)
                }
            })
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Albums> {
                override fun onComplete() {
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: Albums) {
                    text_view.text=t.toString()
                    Log.d(TAG,t.toString())
                    Log.d(TAG,Thread.currentThread().name)
                    Thread.sleep(100)
                }

                override fun onError(e: Throwable) {
                }
            })


    }

    override fun onDestroy() {
        super.onDestroy()
        disposableList.clear()
    }
}
