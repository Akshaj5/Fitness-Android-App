package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.SpinDown;
import com.wahoofitness.connector.capabilities.SpinDown.ErrorCode;
import com.wahoofitness.connector.capabilities.SpinDown.SpinDownResult;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapSpinDownFragment extends CapabilityFragment {

	private final SpinDown.Listener mSpinDownListener = new SpinDown.Listener() {

		@Override
		public void onSpindownComplete(SpinDownResult spinDownResult) {
			registerCallbackResult("onSpindownComplete", spinDownResult);
			refreshView();
		}

		@Override
		public void onSpindownFailed(ErrorCode errorCode) {
			registerCallbackResult("onSpindownFailed", errorCode);
			refreshView();
		}

		@Override
		public void onSpindownStarted() {
			registerCallbackResult("onSpindownStarted");
			refreshView();
		}
	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetSpinDownMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getSpinDownCap().sendStartSpinDown();
			}
		}));


		refreshView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		SpinDown spinDown = getSpinDownCap();
		if (spinDown != null) {
			spinDown.removeListener(mSpinDownListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
		getSpinDownCap().addListener(mSpinDownListener);
		refreshView();
	}

	private SpinDown getSpinDownCap() {
		return (SpinDown) getCapability(CapabilityType.SpinDown);
	}

	@Override
	protected void refreshView() {
		SpinDown cap = getSpinDownCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(SpinDown.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());
		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
