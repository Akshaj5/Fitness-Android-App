package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikePedalPowerBalance;
import com.wahoofitness.connector.capabilities.BikePedalPowerBalance.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikePedalPowerBalanceFragment extends CapabilityFragment {

	private final BikePedalPowerBalance.Listener mBikePedalPowerBalanceListener = new BikePedalPowerBalance.Listener() {

		@Override
		public void onBikePedalPowerBalanceData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}
	};
	private BikePedalPowerBalance.Data mLastCallbackData;
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

		BikePedalPowerBalance bikePedalPowerBalance = getBikePedalPowerBalanceCap();
		if (bikePedalPowerBalance != null) {
			bikePedalPowerBalance.removeListener(mBikePedalPowerBalanceListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikePedalPowerBalanceCap().addListener(mBikePedalPowerBalanceListener);
		refreshView();
	}

	private BikePedalPowerBalance getBikePedalPowerBalanceCap() {
		return (BikePedalPowerBalance) getCapability(CapabilityType.BikePedalPowerBalance);
	}

	@Override
	protected void refreshView() {
		BikePedalPowerBalance cap = getBikePedalPowerBalanceCap();
		if (cap != null) {
			BikePedalPowerBalance.Data data = cap.getBikePedalPowerBalanceData();

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
