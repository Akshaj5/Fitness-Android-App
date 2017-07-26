package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.CrankRevs;
import com.wahoofitness.connector.capabilities.CrankRevs.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapCrankRevsFragment extends CapabilityFragment {

	private final CrankRevs.Listener mCrankRevsListener = new CrankRevs.Listener() {

		@Override
		public void onCrankRevsData(Data data) {
			mLastCallbackData = data;
			refreshView();

		}
	};
	private Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		CrankRevs crankRevs = getCrankRevsCap();
		if (crankRevs != null) {
			crankRevs.removeListener(mCrankRevsListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getCrankRevsCap().addListener(mCrankRevsListener);
		refreshView();
	}

	private CrankRevs getCrankRevsCap() {
		return (CrankRevs) getCapability(CapabilityType.CrankRevs);
	}

	@Override
	protected void refreshView() {
		CrankRevs cap = getCrankRevsCap();
		if (cap != null) {

			CrankRevs.Data data = cap.getCrankRevsData();

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
