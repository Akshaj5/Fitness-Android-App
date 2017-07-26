package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunSteps;
import com.wahoofitness.connector.capabilities.RunSteps.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunStepsFragment extends CapabilityFragment {

	private final RunSteps.Listener mRunStepsListener = new RunSteps.Listener() {

		@Override
		public void onRunStepsData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

		@Override
		public void onRunStepsDataReset() {
			registerCallbackResult("onRunStepsDataReset", TimeInstant.now());
			refreshView();

		}

	};
	private RunSteps.Data mLastCallbackData;
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

		RunSteps runSteps = getRunStepsCap();
		if (runSteps != null) {
			runSteps.removeListener(mRunStepsListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunStepsCap().addListener(mRunStepsListener);
		refreshView();
	}

	private RunSteps getRunStepsCap() {
		return (RunSteps) getCapability(CapabilityType.RunSteps);
	}

	@Override
	protected void refreshView() {
		RunSteps cap = getRunStepsCap();
		if (cap != null) {
			RunSteps.Data data = cap.getRunStepsData();

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
