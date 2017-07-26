package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunStepRate;
import com.wahoofitness.connector.capabilities.RunStepRate.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunStepRateFragment extends CapabilityFragment {

	private final RunStepRate.Listener mRunStepRateListener = new RunStepRate.Listener() {

		@Override
		public void onRunStepRateData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

		@Override
		public void onRunStepRateDataReset() {
			registerCallbackResult("onRunStepRateDataReset", "");
		}
	};
	private RunStepRate.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "resetRunStepRateData", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRunStepRateCap().resetRunStepRateData();
			}
		}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		RunStepRate runStepRate = getRunStepRateCap();
		if (runStepRate != null) {
			runStepRate.removeListener(mRunStepRateListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunStepRateCap().addListener(mRunStepRateListener);
		refreshView();
	}

	private RunStepRate getRunStepRateCap() {
		return (RunStepRate) getCapability(CapabilityType.RunStepRate);
	}

	@Override
	protected void refreshView() {
		RunStepRate cap = getRunStepRateCap();
		if (cap != null) {
			RunStepRate.Data data = cap.getRunStepRateData();

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
