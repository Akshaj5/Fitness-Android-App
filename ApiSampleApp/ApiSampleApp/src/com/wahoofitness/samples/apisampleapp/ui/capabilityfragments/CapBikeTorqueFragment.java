package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikeTorque;
import com.wahoofitness.connector.capabilities.BikeTorque.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikeTorqueFragment extends CapabilityFragment {

	private final BikeTorque.Listener mBikeTorqueListener = new BikeTorque.Listener() {

		@Override
		public void onBikeTorqueData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}
	};
	private BikeTorque.Data mLastCallbackData;
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

		BikeTorque bikeTorque = getBikeTorqueCap();
		if (bikeTorque != null) {
			bikeTorque.removeListener(mBikeTorqueListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikeTorqueCap().addListener(mBikeTorqueListener);
		refreshView();
	}

	private BikeTorque getBikeTorqueCap() {
		return (BikeTorque) getCapability(CapabilityType.BikeTorque);
	}

	@Override
	protected void refreshView() {
		BikeTorque cap = getBikeTorqueCap();
		if (cap != null) {
			BikeTorque.Data data = cap.getBikeTorqueData();

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
