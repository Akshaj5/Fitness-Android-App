package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.common.datatypes.Distance;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.CrankLength;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;
import com.wahoofitness.samples.apisampleapp.ui.util.UserRequest;

public class CapCrankLengthFragment extends CapabilityFragment {

	private final CrankLength.Listener mCrankLengthListener = new CrankLength.Listener() {

		@Override
		public void onGetCrankLengthResponse(boolean success, Distance crankLength) {
			registerCallbackResult("onGetCrankLengthResponse", success, crankLength);
			refreshView();
		}

		@Override
		public void onSetCrankLengthResponse(boolean success, Distance crankLength) {
			registerCallbackResult("onGetCrankLengthResponse", success, crankLength);
			refreshView();
		}

		@Override
		public void onSetCrankLengthSupported(boolean supported) {
			registerCallbackResult("onSetCrankLengthSupported", supported);
			refreshView();
		}
	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetCrankLength", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getCrankLengthCap().sendGetCrankLength();
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetCrankLength", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDouble(context, 0, "crank length (mm)", null, 200.0, "", new UserRequest.DoubleListener() {

					@Override
					public void onDouble(double number) {
						getCrankLengthCap().sendSetCrankLength(Distance.fromMm(number));
					}
				});
			}
		}));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		CrankLength crankLength = getCrankLengthCap();
		if (crankLength != null) {
			crankLength.removeListener(mCrankLengthListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getCrankLengthCap().addListener(mCrankLengthListener);
		refreshView();
	}

	private CrankLength getCrankLengthCap() {
		return (CrankLength) getCapability(CapabilityType.CrankLength);
	}

	@Override
	protected void refreshView() {
		CrankLength cap = getCrankLengthCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(CrankLength.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());
		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
