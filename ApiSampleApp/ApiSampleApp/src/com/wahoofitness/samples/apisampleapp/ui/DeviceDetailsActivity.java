package com.wahoofitness.samples.apisampleapp.ui;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.Toast;

import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnectorEnums.HardwareConnectorState;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.HardwareConnectorTypes.NetworkType;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.samples.apisampleapp.R;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorServiceConnection;
import com.wahoofitness.samples.apisampleapp.ui.capabilityfragments.CapabilityFragment;

/**
 * {@link Activity} class showing information for a specific connected device. The {@link Activity}
 * manages display several {@link Fragment}s, one for each of the {@link SensorConnection}'s
 * {@link Capability}s. Select the capability from the menu
 */
public class DeviceDetailsActivity extends Activity implements HardwareConnectorService.Listener {

	private static final Logger L = new Logger("DeviceDetailsActivity");

	private ConnectionParams mConnectionParams;

	private final HardwareConnectorServiceConnection mServiceConnection = new HardwareConnectorServiceConnection() {

		@Override
		protected void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
			service.addListener(DeviceDetailsActivity.this);

		}
	};

	@Override
	public void onConnectorStateChanged(NetworkType networkType,
			HardwareConnectorState hardwareState) {

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		SensorConnection sensorConnection = getSensorConnection();

		if (sensorConnection != null) {

			for (CapabilityType capabilityType : sensorConnection.getCurrentCapabilities()) {

				final CapabilityFragment fragment = CapabilityFragment.create(capabilityType,
						mConnectionParams);

				if (fragment != null) {

					MenuItem item = menu.add(capabilityType.name());

					item.setOnMenuItemClickListener(new OnMenuItemClickListener() {

						@Override
						public boolean onMenuItemClick(MenuItem item) {

							getFragmentManager().beginTransaction()
									.replace(R.id.container, fragment).commit();

							return true;
						}
					});

				}
			}

		} else {
			L.w("notifyDataSetChanged SensorConnection null");
		}

		return true;
	}

	@Override
	public void onDeviceDiscovered(ConnectionParams params) {

	}

	@Override
	public void onDiscoveredDeviceLost(ConnectionParams params) {

	}

	@Override
	public void onDiscoveredDeviceRssiChanged(ConnectionParams params) {

	}

	@Override
	public void onNewCapabilityDetected(SensorConnection sensorConnection,
			CapabilityType capabilityType) {

		invalidateOptionsMenu();

	}

	@Override
	public void onSensorConnectionStateChanged(SensorConnection sensorConnection,
			SensorConnectionState state) {
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.device_details_activity);

		// Deserialise the ConnecitonParams
		String connectionParamsStr = getIntent().getStringExtra("params");
		mConnectionParams = ConnectionParams.deserialize(connectionParamsStr);
		this.setTitle(mConnectionParams.getName());

		// Bind to the HardwareConnectorService
		mServiceConnection.bind(this);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		mServiceConnection.unbind();
	}

	@Override
	protected void onResume() {
		super.onResume();

		Toast.makeText(this, "Select capability from menu", Toast.LENGTH_SHORT).show();

	}

	private SensorConnection getSensorConnection() {
		if (mConnectionParams == null) {
			throw new AssertionError("mConnectionParams is null");
		}

		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.getSensorConnection(mConnectionParams);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/** Launches the {@link DeviceDetailsActivity} for the specifed {@link SensorConnection} */
	public static void launchActivity(Activity activity, SensorConnection sensorConnection) {
		Intent intent = new Intent(activity, DeviceDetailsActivity.class);

		// We serialize the SensorConnection's ConnectionParams, and pass the string into the
		// Activity
		intent.putExtra("params", sensorConnection.getConnectionParams().serialize());
		activity.startActivity(intent);
	}

}
