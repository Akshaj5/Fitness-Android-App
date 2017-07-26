package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunMotion;
import com.wahoofitness.connector.capabilities.RunMotion.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunMotionFragment extends CapabilityFragment {

	private final RunMotion.Listener mRunMotionListener = new RunMotion.Listener() {

		@Override
		public void onRunMotionData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

		@Override
		public void onRunMotionDataReset() {
			registerCallbackResult("onRunMotionDataReset", TimeInstant.now());
			refreshView();

		}

	};
	private RunMotion.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "resetRunMotionData", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRunMotionCap().resetRunMotionData();
			}
		}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		RunMotion heartrate = getRunMotionCap();
		if (heartrate != null) {
			heartrate.removeListener(mRunMotionListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunMotionCap().addListener(mRunMotionListener);
		refreshView();
	}

	private RunMotion getRunMotionCap() {
		return (RunMotion) getCapability(CapabilityType.RunMotion);
	}

	@Override
	protected void refreshView() {
		RunMotion cap = getRunMotionCap();
		if (cap != null) {
			RunMotion.Data data = cap.getRunMotionData();

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
