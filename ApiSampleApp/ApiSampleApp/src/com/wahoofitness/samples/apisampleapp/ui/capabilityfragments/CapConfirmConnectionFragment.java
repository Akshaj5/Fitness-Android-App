package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.ConfirmConnection;
import com.wahoofitness.connector.capabilities.ConfirmConnection.Error;
import com.wahoofitness.connector.capabilities.ConfirmConnection.Role;
import com.wahoofitness.connector.capabilities.ConfirmConnection.State;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

public class CapConfirmConnectionFragment extends CapabilityFragment {

	private final ConfirmConnection.Listener mConfirmConnectionListener = new ConfirmConnection.Listener() {

		@Override
		public void onConfirmationProcedureStateChange(State state, Error error) {
			registerCallbackResult("onConfirmationProcedureStateChange", state, error);
			refreshView();

		}

		@Override
		public void onUserAccept() {
			registerCallbackResult("onUserAccept", "");
			refreshView();

		}

	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "requestConfirmation", new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean result = getConfirmConnectionCap().requestConfirmation(Role.MASTER,
						getLocalBluetoothName(), getPersistentAppUuid(false), "HSC");
				toast("requestConfirmation: " + result);
			}
		}));

		ll.addView(createSimpleButton(context, "requestConfirmation (new UUID)",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						boolean result = getConfirmConnectionCap().requestConfirmation(Role.MASTER,
								getLocalBluetoothName(), getPersistentAppUuid(true), "HSC");
						toast("requestConfirmation: " + result);
					}
				}));

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ConfirmConnection confirmConnection = getConfirmConnectionCap();
		if (confirmConnection != null) {
			confirmConnection.removeListener(mConfirmConnectionListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
		getConfirmConnectionCap().addListener(mConfirmConnectionListener);
		refreshView();
	}

	private ConfirmConnection getConfirmConnectionCap() {
		return (ConfirmConnection) getCapability(CapabilityType.ConfirmConnection);
	}

	private UUID getPersistentAppUuid(boolean forceNew) {
		UUID uuid = null;
		SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
				"getPersistentAppUuid", 0);
		if (!forceNew) {
			String uuidStr = sharedPreferences.getString("appUuid", null);
			if (uuidStr != null) {
				try {
					uuid = UUID.fromString(uuidStr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (uuid == null) {
			uuid = UUID.randomUUID();
			Editor editor = sharedPreferences.edit();
			editor.putString("appUuid", uuid.toString());
			editor.commit();
		}

		return uuid;
	}

	@Override
	protected void refreshView() {
		ConfirmConnection cap = getConfirmConnectionCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(ConfirmConnection.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}

	/**
	 * Gets the local Bluetooth device name or the MODEL name if this is not available
	 */
	private static String getLocalBluetoothName() {
		String name = null;
		try {
			BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			name = bluetoothAdapter.getName().trim();
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (name == null || name.isEmpty()) {
			name = android.os.Build.MODEL;
		}
		return name;
	}
}
