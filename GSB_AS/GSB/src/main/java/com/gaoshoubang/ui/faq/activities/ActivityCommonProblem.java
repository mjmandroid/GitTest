package com.gaoshoubang.ui.faq.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gaoshoubang.R;
import com.gaoshoubang.base.activities.BaseActivity;
import com.gaoshoubang.widget.HorizontalListView;
import com.gaoshoubang.bean.Problem;
import com.gaoshoubang.ui.faq.presenter.CommonProblemPresenterImpl;
import com.gaoshoubang.ui.faq.adapter.ProblemNavAdapter;
import com.gaoshoubang.ui.faq.view.CommonProView;
import com.gaoshoubang.util.FullTitleBar;
import com.gaoshoubang.util.UmEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 常见问题
 */
public class ActivityCommonProblem extends BaseActivity<CommonProblemPresenterImpl> implements OnClickListener, CommonProView {
	private static final String TAG = "ActivityCommonProblem";
	private HorizontalListView horizontalList;
	private ExpandableListView problemList;
	private TextView problemTitle;
	private TextView phone;

	private List<Problem> problemNav;
	private List<Problem.Qs> problemQS;
	private ProblemNavAdapter problemNavAdapter;
	private QsAdapter qsAdapter;


	@Override
	protected CommonProblemPresenterImpl getPresenter() {
		return new CommonProblemPresenterImpl();
	}

	@Override
	protected int getLayoutId() {
		return R.layout.activity_common_problem;
	}


	protected void initView() {
		new FullTitleBar(this, "#ffffff");
		View headerView = View.inflate(this, R.layout.activity_common_problem_header, null);
		horizontalList = (HorizontalListView) headerView.findViewById(R.id.problem_horizontal_list);
		problemTitle = (TextView) headerView.findViewById(R.id.problem_title);

		problemList = (ExpandableListView) findViewById(R.id.problem_list);
		phone = (TextView) findViewById(R.id.problem_phone);

		problemList.addHeaderView(headerView);

		phone.setOnClickListener(this);

		problemNav = new ArrayList<Problem>();
		problemNavAdapter = new ProblemNavAdapter(this, problemNav);
		horizontalList.setAdapter(problemNavAdapter);

		problemQS = new ArrayList<Problem.Qs>();
		qsAdapter = new QsAdapter(this, problemQS);
		problemList.setAdapter(qsAdapter);
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void loadData() {
		mPresenter.requestHelpShow();
	}

	@Override
	protected void bindData() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.problem_phone:
				setUmEvent(UmEvent.Gsy_aboutAndHelp_phone);
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000685333"));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				break;
		}
	}

	OnItemClickListener onItemClickListener = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			problemNavAdapter.setIsSelect(position);
			problemNavAdapter.notifyDataSetChanged();
			problemTitle.setText(problemNav.get(position).getName());
			problemQS.clear();
			problemQS.addAll(problemNav.get(position).getQs());
			qsAdapter.notifyDataSetChanged();
		}
	};


	/**
	 * @param problems
	 */
	@Override
	public void afterGetHelp(List<Problem> problems) {
		problemNav.clear();
		problemNav.addAll(problems);
		problemNavAdapter.notifyDataSetChanged();
		horizontalList.setOnItemClickListener(onItemClickListener);

		problemTitle.setText(problemNav.get(0).getName());

		problemQS.addAll(problemNav.get(0).getQs());
		qsAdapter.notifyDataSetChanged();
	}

	class QsAdapter extends BaseExpandableListAdapter {
		private List<Problem.Qs> list;
		private Activity activity;
		private boolean isChanged = false;

		public QsAdapter(Activity activity, List<Problem.Qs> list) {
			super();
			this.list = list;
			this.activity = activity;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			for (int i = 0; i < list.size(); i++) {
				if (problemList.isGroupExpanded(i)) {
					problemList.collapseGroup(i);
				}
			}
			isChanged = true;
		}

		// 得到子item需要关联的数据
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			list = new ArrayList<Problem.Qs>();
			return (list.get(groupPosition).getCc());
		}

		// 得到子item的ID
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}

		// 设置子item的组件
		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.item_problem_content, null);
				new ContentViewHolder(convertView);
			}
			ContentViewHolder viewHolder = (ContentViewHolder) convertView.getTag();
			viewHolder.content.setText(list.get(groupPosition).getCc());
			return convertView;
		}

		// 设置父item组件
		@Override
		public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = View.inflate(activity, R.layout.item_problem_title, null);
				new TitleViewHolder(convertView);
			}
			final TitleViewHolder viewHolder = (TitleViewHolder) convertView.getTag();
			viewHolder.title.setText(list.get(groupPosition).getTitle());
			if (isChanged) {
				viewHolder.arrow.clearAnimation();
			}
			viewHolder.itemTitle.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!isExpanded) {
						isChanged = false;
						problemList.expandGroup(groupPosition);
						RotateAnimation rAnima = new RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						rAnima.setFillAfter(true);
						rAnima.setDuration(200);
						viewHolder.arrow.startAnimation(rAnima);
					}
					else {
						problemList.collapseGroup(groupPosition);
						RotateAnimation rAnima = new RotateAnimation(180f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
						rAnima.setFillAfter(true);
						rAnima.setDuration(200);
						viewHolder.arrow.startAnimation(rAnima);
					}
				}
			});
			return convertView;
		}

		// 获取当前父item下的子item的个数
		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		// 获取当前父item的数据
		@Override
		public Object getGroup(int groupPosition) {
			return list.get(groupPosition);
		}

		@Override
		public int getGroupCount() {
			return list.size();
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public boolean hasStableIds() {
			return true;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}

	}

	class TitleViewHolder {
		LinearLayout itemTitle;
		TextView title;
		ImageView arrow;

		public TitleViewHolder(View view) {
			itemTitle = (LinearLayout) view.findViewById(R.id.problem_item_title);
			title = (TextView) view.findViewById(R.id.problem_title);
			arrow = (ImageView) view.findViewById(R.id.problem_arrow);
			view.setTag(this);
		}
	}

	class ContentViewHolder {
		TextView content;

		public ContentViewHolder(View view) {
			content = (TextView) view.findViewById(R.id.problem_context);
			view.setTag(this);
		}
	}

}
