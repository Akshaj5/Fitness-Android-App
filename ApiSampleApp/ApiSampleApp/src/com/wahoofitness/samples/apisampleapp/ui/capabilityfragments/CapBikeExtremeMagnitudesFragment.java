package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.BikeExtremeMagnitudes;
import com.wahoofitness.connector.capabilities.BikeExtremeMagnitudes.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBikeExtremeMagnitudesFragment extends CapabilityFragment {

	private final BikeExtremeMagnitudes.Listener mBikeExtremeMagnitudesListener = new BikeExtremeMagnitudes.Listener() {

		@Override
		public void onBikeExtremeMagnitudesData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}
	};
	private BikeExtremeMagnitudes.Data mLastCallbackData;
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

		BikeExtremeMagnitudes bikeExtremeMagnitudes = getBikeExtremeMagnitudesCap();
		if (bikeExtremeMagnitudes != null) {
			bikeExtremeMagnitudes.removeListener(mBikeExtremeMagnitudesListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBikeExtremeMagnitudesCap().addListener(mBikeExtremeMagnitudesListener);
		refreshView();
	}

	private BikeExtremeMagnitudes getBikeExtremeMagnitudesCap() {
		return (BikeExtremeMagnitudes) getCapability(CapabilityType.BikeExtremeMagnitudes);
	}

	@Override
	protected void refreshView() {
		BikeExtremeMagnitudes cap = getBikeExtremeMagnitudesCap();
		if (cap != null) {
			BikeExtremeMagnitudes.Data data = cap.getBikeExtremeMagnitudesData();

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
