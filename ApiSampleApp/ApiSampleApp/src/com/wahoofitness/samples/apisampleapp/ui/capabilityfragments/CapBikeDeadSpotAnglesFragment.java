package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikeDeadSpotAngles;
import com.wahoofitness.connector.capabilities.BikeDeadSpotAngles.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikeDeadSpotAnglesFragment extends CapabilityFragment {

	private final BikeDeadSpotAngles.Listener mBikeDeadSpotAnglesListener = new BikeDeadSpotAngles.Listener() {

		@Override
		public void onBikeDeadSpotAnglesData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}
	};
	private BikeDeadSpotAngles.Data mLastCallbackData;
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

		BikeDeadSpotAngles bikeDeadSpotAngles = getBikeDeadSpotAnglesCap();
		if (bikeDeadSpotAngles != null) {
			bikeDeadSpotAngles.removeListener(mBikeDeadSpotAnglesListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikeDeadSpotAnglesCap().addListener(mBikeDeadSpotAnglesListener);
		refreshView();
	}

	private BikeDeadSpotAngles getBikeDeadSpotAnglesCap() {
		return (BikeDeadSpotAngles) getCapability(CapabilityType.BikeDeadSpotAngles);
	}

	@Override
	protected void refreshView() {
		BikeDeadSpotAngles cap = getBikeDeadSpotAnglesCap();
		if (cap != null) {
			BikeDeadSpotAngles.Data data = cap.getBikeDeadSpotAnglesData();

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
