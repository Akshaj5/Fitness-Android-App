package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunStride;
import com.wahoofitness.connector.capabilities.RunStride.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunStrideFragment extends CapabilityFragment {

	private final RunStride.Listener mRunStrideListener = new RunStride.Listener() {

		@Override
		public void onRunStrideData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

	};
	private RunStride.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		RunStride runStride = getRunStrideCap();
		if (runStride != null) {
			runStride.removeListener(mRunStrideListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunStrideCap().addListener(mRunStrideListener);
		refreshView();
	}

	private RunStride getRunStrideCap() {
		return (RunStride) getCapability(CapabilityType.RunStride);
	}

	@Override
	protected void refreshView() {
		RunStride cap = getRunStrideCap();
		if (cap != null) {
			RunStride.Data data = cap.getRunStrideData();

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
