package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikePower;
import com.wahoofitness.connector.capabilities.BikePower.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikePowerFragment extends CapabilityFragment {

	private final BikePower.Listener mBikePowerListener = new BikePower.Listener() {

		@Override
		public void onBikePowerData(Data data) {
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

		BikePower bikePower = getBikePowerCap();
		if (bikePower != null) {
			bikePower.removeListener(mBikePowerListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikePowerCap().addListener(mBikePowerListener);
		refreshView();
	}

	private BikePower getBikePowerCap() {
		return (BikePower) getCapability(CapabilityType.BikePower);
	}

	@Override
	protected void refreshView() {
		BikePower cap = getBikePowerCap();
		if (cap != null) {

			BikePower.Data data = cap.getBikePowerData();

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
