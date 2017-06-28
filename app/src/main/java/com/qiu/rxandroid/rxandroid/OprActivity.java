package com.qiu.rxandroid.rxandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.reactivestreams.Publisher;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;


/**
 * @author @Qiu
 * @version V1.0
 * @Description:测试RxAndroid-->操作符
 * @date 2017/6/28 14:46
 */
public class OprActivity extends AppCompatActivity {
	public static final String TAG = OprActivity.class.getSimpleName();
	List<Integer> mList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initData();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opr);
		initSecondRxAndroid();
	}

	private void initData() {
		mList = new ArrayList<>();
		mList.add(1);
		mList.add(2);
		mList.add(3);
		mList.add(4);
	}

	/**
	 * FlatMap转换
	 * 发出一个List,依次输出，舍弃for循环
	 */
	private void initFirstRxAndroid() {
		Flowable.just(mList)
				//flatMap 返回的是一个 Flowable 对象,把从List发射出来的一个一个的元素发射出去
				.flatMap(new Function<List<Integer>, Publisher<Integer>>() {
					@Override
					public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
						return Flowable.fromIterable(integers);
					}
				})
				.subscribe(new Consumer<Integer>() {
					@Override
					public void accept(@NonNull Integer integer) throws Exception {
						Log.e(TAG, "flat-->accept:" + integer);
					}
				});
	}

	/**
	 * filter过滤
	 */
	private void initSecondRxAndroid() {
		Flowable.just(mList)
				.flatMap(new Function<List<Integer>, Publisher<Integer>>() {
					@Override
					public Publisher<Integer> apply(@NonNull List<Integer> integers) throws Exception {
						return Flowable.fromIterable(integers);
					}
				})
				//过滤小于3的数据，true->下发 false->拦截
				.filter(new Predicate<Integer>() {
					@Override
					public boolean test(@NonNull Integer integer) throws Exception {
						return integer < 3;
					}
				})
				.subscribe(new Consumer<Integer>() {
					@Override
					public void accept(@NonNull Integer integer) throws Exception {
						Log.e(TAG, "filter--->accept:" + integer);
					}
				});
//		方式二
//		Flowable.fromIterable(mList)
//				.filter(new Predicate<Integer>() {
//					@Override
//					public boolean test(@NonNull Integer integer) throws Exception {
//						return integer < 3;
//					}
//				}).subscribe(new Consumer<Integer>() {
//			@Override
//			public void accept(@NonNull Integer integer) throws Exception {
//				Log.e(TAG, "filter--->accept:" + integer);
//			}
//		});
	}

	/**
	 * take指定最多收到几个数据
	 */
	private void initThirdRxAndroid() {
		Flowable.fromIterable(mList)
				.take(2)
				.subscribe(new Consumer<Integer>() {
					@Override
					public void accept(Integer integer) throws Exception {
						Log.e(TAG, "take--->accept:" + integer);
					}
				});

//		方式二
//		Flowable.fromArray(1, 2, 3, 4)
//				.take(2)
//				.subscribe(new Consumer<Integer>() {
//					@Override
//					public void accept(Integer integer) throws Exception {
//						Log.e(TAG, "take--->accept:" + integer);
//					}
//				});
	}

	/**
	 * doOnNext
	 * 允许我们在每次输出一个元素之前做一些额外的事情(ps:不会改变数据本身的值，如果要改变使用map)
	 */
	private void initFourRxAndroid() {
		Flowable.just(1, 2, 3)
				.doOnNext(new Consumer<Integer>() {
					@Override
					public void accept(@NonNull Integer integer) throws Exception {
						Log.e(TAG, "doOnNext--->before:" + (++integer));
					}
				})
				.subscribe(new Consumer<Integer>() {
					@Override
					public void accept(@NonNull Integer integer) throws Exception {
						Log.e(TAG, "doOnNext--->accept:" + integer);
					}
				});
	}
}
