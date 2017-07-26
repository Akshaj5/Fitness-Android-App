package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikePedalPowerContribution;
import com.wahoofitness.connector.capabilities.BikePedalPowerContribution.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikePedalPowerContributionFragment extends CapabilityFragment {

	private final BikePedalPowerContribution.Listener mBikePedalPowerContributionListener = new BikePedalPowerContribution.Listener() {

		@Override
		public void onBikePedalPowerContributionData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}
	};
	private BikePedalPowerContribution.Data mLastCallbackData;
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

		BikePedalPowerContribution bikePedalPowerContribution = getBikePedalPowerContributionCap();
		if (bikePedalPowerContribution != null) {
			bikePedalPowerContribution.removeListener(mBikePedalPowerContributionListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikePedalPowerContributionCap().addListener(mBikePedalPowerContributionListener);
		refreshView();
	}

	private BikePedalPowerContribution getBikePedalPowerContributionCap() {
		return (BikePedalPowerContribution) getCapability(CapabilityType.BikePedalPowerContribution);
	}

	@Override
	protected void refreshView() {
		BikePedalPowerContribution cap = getBikePedalPowerContributionCap();
		if (cap != null) {
			BikePedalPowerContribution.Data data = cap.getBikePedalPowerContributionData();

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
