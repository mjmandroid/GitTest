package com.gaoshoubang.ui.information.view;

import com.gaoshoubang.bean.CityStreetBean;
import com.gaoshoubang.base.view.BaseView;

/**
 * Created by Administrator on 2017/5/4.
 */

public interface AdressView extends BaseView {
	void afterGetCityStreetBean(CityStreetBean bean);

	void afterSetArea(String state);
}
