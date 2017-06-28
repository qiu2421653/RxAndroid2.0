package com.qiu.rxandroid.rxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:测试RxAndroid-->简单操作
 * @date 2017/6/28 10:40
 */
public class BasicActivity extends AppCompatActivity {

	private static final String TAG = BasicActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_basic);
		initMapUpRxAndroid();
	}


	/**
	 * 第一个RxAndroid程序,无背压
	 */
	private void initFirstRxAndroid() {
		//被观察者
		Observable<String> myObservable = Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
				e.onNext("haha");
				e.onNext("qiaqia");
				e.onNext("miamia");
				e.onComplete();
			}
		});

		//观察者
		Observer<String> myObserver = new Observer<String>() {
			@Override
			public void onSubscribe(@NonNull Disposable d) {

			}

			@Override
			public void onNext(@NonNull String s) {
				Log.e(TAG, "onNext:" + s);
			}

			@Override
			public void onError(@NonNull Throwable e) {

			}

			@Override
			public void onComplete() {

			}
		};
		myObservable.subscribe(myObserver);
	}

	/**
	 * 第二个RxAndroid有背压版
	 */
	private void initSecondRxAndroid() {
		Flowable.range(1, 10).subscribe(new Subscriber<Integer>() {
			Subscription sub;

			@Override
			public void onSubscribe(Subscription s) {
				Log.e("TAG", "onsubscribe start");
				//当订阅后，会首先调用这个方法，其实就相当于onStart()，
				sub = s;
				sub.request(1);
				Log.e("TAG", "onsubscribe end");
			}

			@Override
			public void onNext(Integer integer) {
				Log.e("TAG", "onNext--->" + integer);
				sub.request(1);
			}

			@Override
			public void onError(Throwable t) {
				t.printStackTrace();
			}

			@Override
			public void onComplete() {
				Log.e("TAG", "onComplete");
			}
		});
	}

	/**
	 * 第三个RxAndroid有背压版
	 */
	private void initThirdRxAndroid() {
		Flowable.just("Hello World")
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(@NonNull String s) throws Exception {
						Log.e(TAG, "accept:" + s);
					}
				});
	}

	/**
	 * 第四个RxAndroid有map转换符版
	 */
	private void initMapRxAndroid() {
		Flowable.just("Hello")
				.map(new Function<String, String>() {
					@Override
					public String apply(@NonNull String s) throws Exception {
						return s + " World";
					}
				})
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(@NonNull String s) throws Exception {
						Log.e(TAG, "map-->accept:" + s);
					}
				});
	}

	/**
	 * 第五个RxAndroid有map进阶转换符版
	 * 这里用了两个map，一个是把字符串转成hashcode，另一个是把hashcode 转成字符串
	 */
	private void initMapUpRxAndroid() {
		Flowable.just("Hello")
				.map(new Function<String, Integer>() {
					@Override
					public Integer apply(@NonNull String s) throws Exception {
						return s.hashCode();
					}
				})
				.map(new Function<Integer, String>() {
					@Override
					public String apply(@NonNull Integer integer) throws Exception {
						return integer.toString();
					}
				})
				.subscribe(new Consumer<String>() {
					@Override
					public void accept(@NonNull String s) throws Exception {
						Log.e(TAG, "map-->accept:" + s);
					}
				});
	}
}
