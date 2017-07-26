package com.wahoofitness.samples.apisampleapp.ui;

import java.util.ArrayList;
import java.util.Collection;

import android.app.Fragment;
import android.os.Bundle;

import com.wahoofitness.connector.HardwareConnectorEnums.HardwareConnectorState;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.HardwareConnectorTypes.NetworkType;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorServiceConnection;

/** Base class Fragment with automatic binding to the {@link HardwareConnectorService} */
public abstract class HardwareConnectorFragment extends Fragment implements
		HardwareConnectorService.Listener {

	private final HardwareConnectorServiceConnection mServiceConnection = new HardwareConnectorServiceConnection() {

		@Override
		protected void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

			// Register the listener
			service.addListener(HardwareConnectorFragment.this);

			// Notify the contrece class
			HardwareConnectorFragment.this.onHardwareConnectorServiceConnected(service);
		}
	};

	@Override
	public void onConnectorStateChanged(NetworkType networkType,
			HardwareConnectorState hardwareState) {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mServiceConnection.bind(getActivity());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// Deregister the listener
		HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
		if (service != null) {
			service.removeListener(this);
		}

		// Unbind
		mServiceConnection.unbind();
	}

	@Override
	public void onNewCapabilityDetected(SensorConnection sensorConnection,
			CapabilityType capabilityType) {

	}

	@Override
	public void onSensorConnectionStateChanged(SensorConnection sensorConnection,
			SensorConnectionState state) {

	}

	protected SensorConnection connectSensor(ConnectionParams params) {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.connectSensor(params);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	protected void disconnectSensor(ConnectionParams params) {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				service.disconnectSensor(params);
			}
		}

	}

	protected boolean enableDiscovery(boolean enable) {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.enableDiscovery(enable);

			} else {
				return false;
			}
		} else {
			return false;
		}

	}

	protected Collection<ConnectionParams> getDiscoveredConnectionParams() {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.getDiscoveredConnectionParams();
			} else {
				return new ArrayList<ConnectionParams>();
			}
		} else {
			return new ArrayList<ConnectionParams>();
		}
	}

	protected HardwareConnectorState getHardwareConnectorState(NetworkType networkType) {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.getHardwareConnectorState(networkType);
			} else {
				return HardwareConnectorState.HARDWARE_NOT_SUPPORTED;
			}
		} else {
			return HardwareConnectorState.HARDWARE_NOT_SUPPORTED;
		}
	}

	protected SensorConnection getSensorConnection(ConnectionParams params) {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.getSensorConnection(params);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	protected Collection<SensorConnection> getSensorConnections() {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.getSensorConnections();
			} else {
				return new ArrayList<SensorConnection>();
			}
		} else {
			return new ArrayList<SensorConnection>();
		}
	}

	protected boolean isDiscovering() {
		if (mServiceConnection != null) {
			HardwareConnectorService service = mServiceConnection.getHardwareConnectorService();
			if (service != null) {
				return service.isDiscovering();
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	protected abstract void onHardwareConnectorServiceConnected(HardwareConnectorService service);
}
