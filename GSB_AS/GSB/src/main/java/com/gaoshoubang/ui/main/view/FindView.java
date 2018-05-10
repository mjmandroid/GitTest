package com.gaoshoubang.ui.main.view;

import com.gaoshoubang.bean.FindBean;
import com.gaoshoubang.bean.NewsBean;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by lzx on 2017/6/26.
 */

public interface FindView extends BaseView {
	void afterGetFindData(FindBean data);

	void showFooterRefresh();

	void afterGetMessageMedia(List<NewsBean> data);
}
