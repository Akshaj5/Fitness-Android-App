package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.DeviceInfo;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapDeviceInfoFragment extends CapabilityFragment {

	private final DeviceInfo.Listener mDeviceInfoListener = new DeviceInfo.Listener() {

		@Override
		public void onDeviceInfo(DeviceInfo.Type type, String value) {
			registerCallbackResult("onDeviceInfo-" + type, value);
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

		DeviceInfo deviceInfo = getDeviceInfoCap();
		if (deviceInfo != null) {
			deviceInfo.removeListener(mDeviceInfoListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getDeviceInfoCap().addListener(mDeviceInfoListener);
		refreshView();
	}

	private DeviceInfo getDeviceInfoCap() {
		return (DeviceInfo) getCapability(CapabilityType.DeviceInfo);
	}

	@Override
	protected void refreshView() {

		DeviceInfo cap = getDeviceInfoCap();

		if (cap != null) {
			StringBuilder sb = new StringBuilder();
			for (DeviceInfo.Type type : DeviceInfo.Type.values()) {
				String value = cap.getDeviceInfo(type);

				if (value != null) {
					sb.append(type).append(": ").append(value).append("\n");
				} else {
					sb.append(type).append(": ").append("n/a").append("\n");
				}
			}

			mTextView.setText(sb.toString().trim());
			mTextView.append("\n\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}

}
