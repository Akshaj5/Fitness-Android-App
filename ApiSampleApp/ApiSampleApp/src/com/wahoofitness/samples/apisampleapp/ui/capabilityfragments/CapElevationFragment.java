package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Elevation;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapElevationFragment extends CapabilityFragment {

	private final Elevation.Listener mElevationListener = new Elevation.Listener() {

		@Override
		public void onElevationData(Elevation.Data data) {
			mLastCallbackData = data;
			refreshView();

		}

	};
	private Elevation.Data mLastCallbackData;
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

		Elevation cap = getElevationCap();
		if (cap != null) {
			cap.removeListener(mElevationListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getElevationCap().addListener(mElevationListener);
		refreshView();
	}

	private Elevation getElevationCap() {
		return (Elevation) getCapability(CapabilityType.Elevation);
	}

	@Override
	protected void refreshView() {
		Elevation cap = getElevationCap();
		if (cap != null) {
			Elevation.Data data = cap.getElevationData();

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
