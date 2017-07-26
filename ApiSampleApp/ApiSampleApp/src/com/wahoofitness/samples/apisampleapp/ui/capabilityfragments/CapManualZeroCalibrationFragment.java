package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.ManualZeroCalibration;
import com.wahoofitness.connector.capabilities.ManualZeroCalibration.ManualZeroCalibrationResult;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapManualZeroCalibrationFragment extends CapabilityFragment {

	private final ManualZeroCalibration.Listener mManualZeroCalibrationListener = new ManualZeroCalibration.Listener() {

		@Override
		public void onManualZeroCalibrationResult(boolean success, ManualZeroCalibrationResult manualZeroCalibrationResult) {
			registerCallbackResult("onGetCrankLengthResponse", success, manualZeroCalibrationResult);
			refreshView();
		}
	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendStartManualZeroConfiguration", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getManualZeroCalibrationCap().sendStartManualZeroCalibration();
			}
		}));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		ManualZeroCalibration manualZeroCalibration = getManualZeroCalibrationCap();
		if (manualZeroCalibration != null) {
			manualZeroCalibration.removeListener(mManualZeroCalibrationListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getManualZeroCalibrationCap().addListener(mManualZeroCalibrationListener);
		refreshView();
	}

	private ManualZeroCalibration getManualZeroCalibrationCap() {
		return (ManualZeroCalibration) getCapability(CapabilityType.ManualZeroCalibration);
	}

	@Override
	protected void refreshView() {
		ManualZeroCalibration cap = getManualZeroCalibrationCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(ManualZeroCalibration.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());
		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
