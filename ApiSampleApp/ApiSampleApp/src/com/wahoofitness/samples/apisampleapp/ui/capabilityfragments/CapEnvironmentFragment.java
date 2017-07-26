package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Environment;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapEnvironmentFragment extends CapabilityFragment {

	private final Environment.Listener mEnvironmentListener = new Environment.Listener() {

		@Override
		public void onEnvironmentData(Environment.Data data) {
			mLastCallbackData = data;
			refreshView();

		}

	};
	private Environment.Data mLastCallbackData;
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

		Environment cap = getEnvironmentCap();
		if (cap != null) {
			cap.removeListener(mEnvironmentListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getEnvironmentCap().addListener(mEnvironmentListener);
		refreshView();
	}

	private Environment getEnvironmentCap() {
		return (Environment) getCapability(CapabilityType.Environment);
	}

	@Override
	protected void refreshView() {
		Environment cap = getEnvironmentCap();
		if (cap != null) {
			Environment.Data data = cap.getEnvironmentData();

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
