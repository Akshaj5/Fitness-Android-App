package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunDistance;
import com.wahoofitness.connector.capabilities.RunDistance.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunDistanceFragment extends CapabilityFragment {

	private final RunDistance.Listener mRunDistanceListener = new RunDistance.Listener() {

		@Override
		public void onRunDistanceData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

		@Override
		public void onRunDistanceDataReset() {
			registerCallbackResult("onRunDistanceDataReset", "");

		}
	};
	private RunDistance.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "resetRunDistanceData", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRunDistanceCap().resetRunDistanceData();
			}
		}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		RunDistance runDistance = getRunDistanceCap();
		if (runDistance != null) {
			runDistance.removeListener(mRunDistanceListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunDistanceCap().addListener(mRunDistanceListener);
		refreshView();
	}

	private RunDistance getRunDistanceCap() {
		return (RunDistance) getCapability(CapabilityType.RunDistance);
	}

	@Override
	protected void refreshView() {
		RunDistance cap = getRunDistanceCap();
		if (cap != null) {
			RunDistance.Data data = cap.getRunDistanceData();

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
