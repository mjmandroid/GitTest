package com.gaoshoubang.ui.common.view;

import com.gaoshoubang.bean.MessageBean;
import com.gaoshoubang.base.view.BaseView;

import java.util.List;

/**
 * Created by lzx on 2017/6/9.
 */

public interface MessageView extends BaseView {
	void afterGetMessages(List<MessageBean> data);
}
