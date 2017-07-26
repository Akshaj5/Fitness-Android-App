package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Connection;
import com.wahoofitness.connector.capabilities.Connection.State;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapConnectionFragment extends CapabilityFragment {

	private final Connection.Listener mConnectionListener = new Connection.Listener() {

		@Override
		public void onStateChanged(State newState, State oldState) {
			registerCallbackResult("onStateChanged", newState, oldState);
			refreshView();
		}

		@Override
		public void onRssi(int rssi) {
			registerCallbackResult("onRssi", rssi);
			refreshView();
		}
	};
	// private Connection.Data mLastCallbackData;
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		Connection cap = getConnectionCap();
		if (cap != null) {
			cap.removeListener(mConnectionListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getConnectionCap().addListener(mConnectionListener);
		refreshView();
	}

	private Connection getConnectionCap() {
		return (Connection) getCapability(CapabilityType.Connection);
	}

	@Override
	protected void refreshView() {
		Connection cap = getConnectionCap();
		if (cap != null) {
			// Connection.Data data = cap.getConnectionData();
			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(Connection.class, cap));
			mTextView.append("\n\n");
			// mTextView.append("CALLBACK DATA\n");
			// mTextView.append(summarizeGetters(mLastCallbackData));
			// mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());
		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
