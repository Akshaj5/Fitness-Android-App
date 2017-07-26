package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.WheelRevs;
import com.wahoofitness.connector.capabilities.WheelRevs.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapWheelRevsFragment extends CapabilityFragment {

	private Data mLastCallbackData;
	private TextView mTextView;
	private final WheelRevs.Listener mWheelRevsListener = new WheelRevs.Listener() {

		@Override
		public void onWheelRevsData(Data data) {
			mLastCallbackData = data;
			refreshView();

		}
	};

	@Override
	public void initView(Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		WheelRevs wheelRevs = getWheelRevsCap();
		if (wheelRevs != null) {
			wheelRevs.removeListener(mWheelRevsListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getWheelRevsCap().addListener(mWheelRevsListener);
		refreshView();
	}

	private WheelRevs getWheelRevsCap() {
		return (WheelRevs) getCapability(CapabilityType.WheelRevs);
	}

	@Override
	protected void refreshView() {
		WheelRevs cap = getWheelRevsCap();
		if (cap != null) {

			WheelRevs.Data data = cap.getWheelRevsData();

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(data));
			mTextView.append("\n\n");
			mTextView.append("CALLBACK DATA\n");
			mTextView.append(summarizeGetters(mLastCallbackData));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}

	}
}
