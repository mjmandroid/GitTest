package com.gaoshoubang.ui.common.view;

import com.gaoshoubang.bean.response.NewsResponse;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by lzx on 2017/6/9.
 */

public interface NewsView extends BaseView {
	void afterGetNews(int p, NewsResponse listNewsResponse);
}
