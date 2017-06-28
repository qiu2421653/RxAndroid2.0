package com.qiu.rxandroid.rxandroid;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.InputStream;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author @Qiu
 * @version V1.0
 * @Description:测试RxAndroid-->调度器
 * @date 2017/6/28 14:46
 */
public class SubscribeActivity extends AppCompatActivity {

	private static final String TAG = SubscribeActivity.class.getSimpleName();

	private ImageView iView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_subscribe);
		iView = (ImageView) findViewById(R.id.imageView);
		initThirdRxAndroid();
	}

	/**
	 * 发送数据是在io线程
	 * 订阅者在主线程执行
	 */
	private void initFirstRxAndroid() {
		Flowable.create(new FlowableOnSubscribe<Integer>() {
			@Override
			public void subscribe(@NonNull FlowableEmitter<Integer> e) throws Exception {
				e.onNext(1);
				Thread.sleep(2000);
				e.onNext(2);
				Thread.sleep(3000);
				e.onNext(3);
				Thread.sleep(4000);
				e.onComplete();
			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.newThread())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Integer>() {
					@Override
					public void accept(@NonNull Integer integer) throws Exception {
						Log.e(TAG, "accept:" + integer);
					}
				});

	}

	/**
	 * 本地图片?
	 */
	private void initSecondRxAndroid() {
		final int drawableRes = R.drawable.header;
		Flowable.create(new FlowableOnSubscribe<Drawable>() {
			@Override
			public void subscribe(@NonNull FlowableEmitter<Drawable> e) throws Exception {
				Drawable drawable = getApplication().getResources().getDrawable(drawableRes);
				e.onNext(drawable);
				e.onComplete();

			}
		}, BackpressureStrategy.BUFFER)
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Drawable>() {
					@Override
					public void accept(@NonNull Drawable drawable) throws Exception {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							iView.setBackground(drawable);
						} else {
							iView.setBackgroundDrawable(drawable);
						}
						Log.e(TAG, "显示");
					}
				});

	}


	/**
	 * Asset下图片
	 */
	private void initThirdRxAndroid() {
		String fileUrl = "header.jpg";
		Flowable.just(fileUrl)
				.map(new Function<String, Drawable>() {
					@Override
					public Drawable apply(@NonNull String s) throws Exception {
						Thread.sleep(2000);
						//方式一读取Asset下图片
						InputStream in = getAssets().open(s);
						//方式二读取Asset下图片
//						InputStream in = getClassLoader().getResourceAsStream("assets/header.jpg");
						Drawable drawAble = BitmapDrawable.createFromStream(in, s);
						return drawAble;
					}
				})
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<Drawable>() {
					@Override
					public void accept(@NonNull Drawable drawable) throws Exception {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
							iView.setBackground(drawable);
						} else {
							iView.setBackgroundDrawable(drawable);
						}
						Log.e(TAG, "显示");
					}
				});

	}

	public void onMyClick(View view) {
		Toast.makeText(getApplicationContext(), "可以交互!", Toast.LENGTH_SHORT).show();
	}

}
