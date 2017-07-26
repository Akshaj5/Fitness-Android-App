package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunSpeed;
import com.wahoofitness.connector.capabilities.RunSpeed.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunSpeedFragment extends CapabilityFragment {

	private final RunSpeed.Listener mRunSpeedListener = new RunSpeed.Listener() {

		@Override
		public void onRunSpeedData(Data data) {
			mLastCallbackData = data;
			refreshView();

		}

		@Override
		public void onRunSpeedDataReset() {
			registerCallbackResult("onRunSpeedDataReset", "");

		}
	};
	private RunSpeed.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {

		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "resetRunSpeedData", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRunSpeedCap().resetRunSpeedData();
			}
		}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		RunSpeed runSpeed = getRunSpeedCap();
		if (runSpeed != null) {
			runSpeed.removeListener(mRunSpeedListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunSpeedCap().addListener(mRunSpeedListener);
		refreshView();
	}

	private RunSpeed getRunSpeedCap() {
		return (RunSpeed) getCapability(CapabilityType.RunSpeed);
	}

	@Override
	protected void refreshView() {
		RunSpeed cap = getRunSpeedCap();
		if (cap != null) {
			RunSpeed.Data data = cap.getRunSpeedData();

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
