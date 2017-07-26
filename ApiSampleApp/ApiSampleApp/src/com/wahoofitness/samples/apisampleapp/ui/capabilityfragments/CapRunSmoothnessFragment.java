package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.RunSmoothness;
import com.wahoofitness.connector.capabilities.RunSmoothness.Data;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapRunSmoothnessFragment extends CapabilityFragment {

	private final RunSmoothness.Listener mRunSmoothnessListener = new RunSmoothness.Listener() {

		@Override
		public void onRunSmoothnessData(Data data) {
			mLastCallbackData = data;
			refreshView();

			View v = getView();
			if (v != null)
				v.setBackgroundColor(data.getColor());
		}

	};
	private RunSmoothness.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "enableSmoothness", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRunSmoothnessCap().enableSmoothness();
			}
		}));

		ll.addView(createSimpleButton(context, "disableSmoothness", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRunSmoothnessCap().disableSmoothness();
			}
		}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		RunSmoothness cap = getRunSmoothnessCap();
		if (cap != null) {
			cap.removeListener(mRunSmoothnessListener);
		}

	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getRunSmoothnessCap().addListener(mRunSmoothnessListener);
		refreshView();
	}

	private RunSmoothness getRunSmoothnessCap() {
		return (RunSmoothness) getCapability(CapabilityType.RunSmoothness);
	}

	@Override
	protected void refreshView() {
		RunSmoothness cap = getRunSmoothnessCap();
		if (cap != null) {
			RunSmoothness.Data data = cap.getRunSmoothnessData();

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
