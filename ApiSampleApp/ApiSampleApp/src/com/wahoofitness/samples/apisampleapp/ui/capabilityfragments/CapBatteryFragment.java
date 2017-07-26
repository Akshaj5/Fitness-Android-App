package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Battery;
import com.wahoofitness.connector.capabilities.Battery.Data;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapBatteryFragment extends CapabilityFragment {

	private final Battery.Listener mBatteryListener = new Battery.Listener() {

		@Override
		public void onBatteryData(Data data) {
			mLastCallbackData = data;
			refreshView();

		}
	};
	private Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		ll.addView(mTextView);
		ll.addView(createSimpleButton(context, "sendReadBatteryData", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBatteryCap().sendReadBatteryData();
			}
		}));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Battery Battery = getBatteryCap();
		if (Battery != null) {
			Battery.removeListener(mBatteryListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getBatteryCap().addListener(mBatteryListener);
		refreshView();
	}

	private Battery getBatteryCap() {
		return (Battery) getCapability(CapabilityType.Battery);
	}

	@Override
	protected void refreshView() {
		Battery cap = getBatteryCap();
		if (cap != null) {

			Battery.Data data = cap.getBatteryData();

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
