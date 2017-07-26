package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.FirmwareVersion;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapFirmwareVersionFragment extends CapabilityFragment {

	private final FirmwareVersion.Listener mFirmwareVersionListener = new FirmwareVersion.Listener() {

		@Override
		public void onFirmwareVersion(String firmwareVersion) {
			registerCallbackResult("onFirmwareVersion", firmwareVersion);
			refreshView();

		}

		@Override
		public void onFirmwareUpgradeRequired(String currentVersion, String recommendedVersion) {
			registerCallbackResult("onFirmwareUpgradeRequired", currentVersion, recommendedVersion);
			refreshView();

		}

	};
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

		FirmwareVersion cap = getFirmwareVersionCap();
		if (cap != null) {
			cap.removeListener(mFirmwareVersionListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getFirmwareVersionCap().addListener(mFirmwareVersionListener);
		refreshView();
	}

	private FirmwareVersion getFirmwareVersionCap() {
		return (FirmwareVersion) getCapability(CapabilityType.FirmwareVersion);
	}

	@Override
	protected void refreshView() {
		FirmwareVersion cap = getFirmwareVersionCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(FirmwareVersion.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}

	}
}
