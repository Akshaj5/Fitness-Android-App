package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wahoofitness.connector.capabilities.BikeTrainer;
import com.wahoofitness.connector.capabilities.BikeTrainer.AccelerometerInfo;
import com.wahoofitness.connector.capabilities.BikeTrainer.CalibrationInfo;
import com.wahoofitness.connector.capabilities.BikeTrainer.DeviceInfo;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;
import com.wahoofitness.samples.apisampleapp.ui.util.UserRequest;

public class CapBikeTrainerFragment extends CapabilityFragment {

	private final BikeTrainer.Listener mBikeTrainerListener = new BikeTrainer.Listener() {

		@Override
		public void onGetAccelerometerInfoResponse(boolean success, AccelerometerInfo accelerometerInfo) {
			registerCallbackResult("onGetAccelerometerInfoResponse", success, accelerometerInfo);
			refreshView();
		}

		@Override
		public void onGetCalibrationInfoResponse(boolean success, CalibrationInfo calibrationInfo) {
			registerCallbackResult("onGetCalibrationInfoResponse", success, calibrationInfo);
			refreshView();
		}

		@Override
		public void onGetDeviceCapabilitiesResponse(boolean success, int capabilities) {
			registerCallbackResult("onGetDeviceCapabilitiesResponse", success, capabilities);
			refreshView();
		}

		@Override
		public void onGetDeviceInfoResponse(boolean success, DeviceInfo deviceInfo) {
			registerCallbackResult("onGetDeviceInfoResponse", success, deviceInfo);
			refreshView();
		}

		@Override
		public void onGetTemperatureResponse(boolean success, int temperature) {
			registerCallbackResult("onGetTemperatureResponse", success, temperature);
			refreshView();
		}

		@Override
		public void onTestOpticalResponse(boolean success) {
			registerCallbackResult("onTestOpticalResponse", success);
			refreshView();
		}
	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetAccelerometerInfo", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetAccelerometerInfo();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetCalibrationInfo", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetCalibrationInfo();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetDeviceCapabilities", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetDeviceCapabilities();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetDeviceInfo", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetDeviceInfo();
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetTemperature", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getBikeTrainerCap().sendGetTemperature();
			}
		}));

		refreshView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		BikeTrainer bikeTrainer = getBikeTrainerCap();
		if (bikeTrainer != null) {
			bikeTrainer.removeListener(mBikeTrainerListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
		getBikeTrainerCap().addListener(mBikeTrainerListener);
		refreshView();
	}

	private BikeTrainer getBikeTrainerCap() {
		return (BikeTrainer) getCapability(CapabilityType.BikeTrainer);
	}

	@Override
	protected void refreshView() {
		BikeTrainer cap = getBikeTrainerCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(BikeTrainer.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());
		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
