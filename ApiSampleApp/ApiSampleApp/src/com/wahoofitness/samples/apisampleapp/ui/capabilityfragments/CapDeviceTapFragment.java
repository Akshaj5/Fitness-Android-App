package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.DeviceTap;
import com.wahoofitness.connector.capabilities.DeviceTap.Data;
import com.wahoofitness.connector.capabilities.DeviceTap.TapType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapDeviceTapFragment extends CapabilityFragment {

	private final DeviceTap.Listener mDeviceTapListener = new DeviceTap.Listener() {

		@Override
		public void onDeviceTapData(Data data) {
			mLastCallbackData = data;
			refreshView();
		}

		@Override
		public void onDeviceTapDataReset(TapType tapType) {
			registerCallbackResult("onDeviceTapDataReset " + tapType, TimeInstant.now());
			refreshView();
		}
	};
	private DeviceTap.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		for (final TapType tapType : TapType.values()) {
			ll.addView(createSimpleButton(context, "resetDeviceTapData " + tapType,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							getDeviceTapCap().resetDeviceTapData(tapType);
						}
					}));
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		DeviceTap cap = getDeviceTapCap();
		if (cap != null) {
			cap.removeListener(mDeviceTapListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getDeviceTapCap().addListener(mDeviceTapListener);
		refreshView();
	}

	private DeviceTap getDeviceTapCap() {
		return (DeviceTap) getCapability(CapabilityType.DeviceTap);
	}

	@Override
	protected void refreshView() {
		DeviceTap cap = getDeviceTapCap();
		if (cap != null) {

			for (TapType tapType : TapType.values()) {

				DeviceTap.Data data = cap.getDeviceTapData(tapType);

				mTextView.setText("");
				mTextView.append("***" + tapType + "***\n\n");
				mTextView.append("GETTER DATA\n");
				mTextView.append(summarizeGetters(data));
				mTextView.append("\n\n");
				mTextView.append("CALLBACK DATA\n");
				mTextView.append(summarizeGetters(mLastCallbackData));
				mTextView.append("\n\n");
			}

			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}

	}
}
