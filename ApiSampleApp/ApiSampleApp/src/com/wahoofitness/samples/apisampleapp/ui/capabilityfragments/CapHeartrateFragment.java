package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Heartrate;
import com.wahoofitness.connector.capabilities.Heartrate.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapHeartrateFragment extends CapabilityFragment {

	private final Heartrate.Listener mHeartrateListener = new Heartrate.Listener() {

		@Override
		public void onHeartrateData(Data data) {
			mLastCallbackData = data;
			refreshView();

		}

		@Override
		public void onHeartrateDataReset() {
			registerCallbackResult("onHeartrateDataReset", TimeInstant.now());
			refreshView();
		}
	};
	private Heartrate.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "resetHeartrateData", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHeartrateCap().resetHeartrateData();
			}
		}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Heartrate cap = getHeartrateCap();
		if (cap != null) {
			cap.removeListener(mHeartrateListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getHeartrateCap().addListener(mHeartrateListener);
		refreshView();
	}

	private Heartrate getHeartrateCap() {
		return (Heartrate) getCapability(CapabilityType.Heartrate);
	}

	@Override
	protected void refreshView() {
		Heartrate cap = getHeartrateCap();
		if (cap != null) {
			Heartrate.Data data = cap.getHeartrateData();

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
