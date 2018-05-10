package com.gaoshoubang.util;

import android.content.Context;
import android.graphics.Bitmap.Config;

import com.gaoshoubang.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class ImageLoaderUtils {

	/**
	 * 初始化* @param context
	 */
	public void imageLoaderConfig(Context context) {
		File cacheDir = StorageUtils.getOwnCacheDirectory(context, "Aimageloader/Cache");
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.diskCache(new UnlimitedDiskCache(cacheDir))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.writeDebugLogs()
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 默认的Options
	 */
	public DisplayImageOptions defaultOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				// .displayer(new FadeInBitmapDisplayer(500)) //图片显示方式：淡入
				.build();
		return options;
	}

	public DisplayImageOptions noCacheOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(false)
				.cacheOnDisk(false)
				// .displayer(new FadeInBitmapDisplayer(500)) //图片显示方式：淡入
				.build();
		return options;
	}

	/**
	 * 默认的Options
	 */
	public DisplayImageOptions defaultOptions(int roundedBitmapDisplayer) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				// .displayer(new FadeInBitmapDisplayer(500)) //图片显示方式：淡入
				.displayer(new RoundedBitmapDisplayer(roundedBitmapDisplayer)) //圆角
				.build();
		return options;
	}


	/**
	 * 推荐页的Options
	 */
	public DisplayImageOptions recommendOptions(int roundedBitmapDisplayer) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.showImageOnLoading(R.drawable.recommend_index_default)
				.showImageOnFail(R.drawable.recommend_index_default)
				.showImageForEmptyUri(R.drawable.recommend_index_default)
				.displayer(new RoundedBitmapDisplayer(roundedBitmapDisplayer)) //圆角
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

	/**
	 * 推荐页item的Options
	 */
	public DisplayImageOptions recommendItemOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.showImageOnLoading(R.drawable.recommend_item_default)
				.showImageOnFail(R.drawable.recommend_item_default)
				.showImageForEmptyUri(R.drawable.recommend_item_default)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

	/**
	 * 广告页
	 *
	 * @return
	 */
	public DisplayImageOptions adOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new FadeInBitmapDisplayer(500)) //图片显示方式：淡入
				.bitmapConfig(Config.RGB_565)
				.build();
		return options;
	}

	/**
	 * 头像
	 *
	 * @param roundedBitmapDisplayer
	 *
	 * @return
	 */
	public DisplayImageOptions headerOption(int roundedBitmapDisplayer) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.head_round)
				.showImageOnFail(R.drawable.head_round)
				.showImageForEmptyUri(R.drawable.head_round)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(roundedBitmapDisplayer)) //圆角
				.build();
		return options;
	}

	/**
	 * 发现页
	 *
	 * @param roundedBitmapDisplayer
	 *
	 * @return
	 */
	public DisplayImageOptions findOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.displayer(new RoundedBitmapDisplayer(500)) //圆角
				.build();
		return options;
	}

	/**
	 * 首页弹出的画廊效果
	 */
	public DisplayImageOptions galleryOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.popup_default)
				.showImageOnFail(R.drawable.popup_default)
				.showImageForEmptyUri(R.drawable.popup_default)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

	/**
	 * 高手币兑换
	 */
	public DisplayImageOptions GsbConversionOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.gaoshoubi_bg)
				.showImageOnFail(R.drawable.gaoshoubi_bg)
				.showImageForEmptyUri(R.drawable.gaoshoubi_bg)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

	/**
	 * 常见问题
	 */
	public DisplayImageOptions problemOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.problem_item_default)
				.showImageOnFail(R.drawable.problem_item_default)
				.showImageForEmptyUri(R.drawable.problem_item_default)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
				.displayer(new RoundedBitmapDisplayer(500)) //圆角
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}

	/**
	 * 发现页的媒体报道
	 */
	public DisplayImageOptions findNewsOptions() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.imageScaleType(ImageScaleType.EXACTLY)
				.showImageOnLoading(R.drawable.recommend_item_default)
				.showImageOnFail(R.drawable.recommend_item_default)
				.showImageForEmptyUri(R.drawable.recommend_item_default)
				.displayer(new RoundedBitmapDisplayer(5)) //圆角
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.build();
		return options;
	}
}