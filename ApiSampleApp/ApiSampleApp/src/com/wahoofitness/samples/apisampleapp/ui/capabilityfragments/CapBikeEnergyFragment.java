package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikeEnergy;
import com.wahoofitness.connector.capabilities.BikeEnergy.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikeEnergyFragment extends CapabilityFragment {

	private final BikeEnergy.Listener mBikeEnergyListener = new BikeEnergy.Listener() {

		@Override
		public void onBikeEnergyData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}
	};
	private BikeEnergy.Data mLastCallbackData;
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

		BikeEnergy bikeEnergy = getBikeEnergyCap();
		if (bikeEnergy != null) {
			bikeEnergy.removeListener(mBikeEnergyListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikeEnergyCap().addListener(mBikeEnergyListener);
		refreshView();
	}

	private BikeEnergy getBikeEnergyCap() {
		return (BikeEnergy) getCapability(CapabilityType.BikeEnergy);
	}

	@Override
	protected void refreshView() {
		BikeEnergy cap = getBikeEnergyCap();
		if (cap != null) {
			BikeEnergy.Data data = cap.getBikeEnergyData();

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
