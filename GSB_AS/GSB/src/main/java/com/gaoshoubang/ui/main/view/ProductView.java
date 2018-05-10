package com.gaoshoubang.ui.main.view;

import com.gaoshoubang.bean.ProductBean;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by lzx on 2017/6/26.
 */

public interface ProductView extends BaseView {
	void afterGetProduct(List<ProductBean> data);
}
