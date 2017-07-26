package com.wahoofitness.samples.apisampleapp.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.HardwareConnector;
import com.wahoofitness.connector.HardwareConnectorEnums.HardwareConnectorState;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionError;
import com.wahoofitness.connector.HardwareConnectorEnums.SensorConnectionState;
import com.wahoofitness.connector.HardwareConnectorTypes.NetworkType;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.listeners.discovery.DiscoveryListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of a {@link Service} encapsulating and abstracting the Wahoo API's
 * {@link HardwareConnector}.
 */
public class HardwareConnectorService extends Service {

	public class HardwareConnectorServiceBinder extends Binder {

		public HardwareConnectorService getService() {
			return HardwareConnectorService.this;
		}
	}

	/** Listener class allowing clients to be notified when API events occur */
	public interface Listener {

		void onDeviceDiscovered(ConnectionParams params);

		void onDiscoveredDeviceLost(ConnectionParams params);

		void onDiscoveredDeviceRssiChanged(ConnectionParams params);

		void onNewCapabilityDetected(SensorConnection sensorConnection, CapabilityType capabilityType);

		void onSensorConnectionStateChanged(SensorConnection sensorConnection, SensorConnectionState state);

		void onConnectorStateChanged(NetworkType networkType, HardwareConnectorState hardwareState);
	}

	static {
		// Set the API logging to VERBOSE
		Logger.setLogLevel(Log.VERBOSE);
	}

	/** Allows activies and fragments to bind to this service */
	private final IBinder mBinder = new HardwareConnectorServiceBinder();

	/** Listens for discovery events from the API */
	private final DiscoveryListener mDiscoveryListener = new DiscoveryListener() {

		@Override
		public void onDeviceDiscovered(ConnectionParams params) {
			// Notify our listeners
			for (Listener listener : mListeners) {
				listener.onDeviceDiscovered(params);
			}
		}

		@Override
		public void onDiscoveredDeviceLost(ConnectionParams params) {
			// Notify our listeners
			for (Listener listener : mListeners) {
				listener.onDiscoveredDeviceLost(params);
			}
		}

		@Override
		public void onDiscoveredDeviceRssiChanged(ConnectionParams params, int rssi) {
			// Notify our listeners
			for (Listener listener : mListeners) {
				listener.onDiscoveredDeviceRssiChanged(params);
			}
		}
	};

	/** The API's main access point, the {@link HardwareConnector} */
	private HardwareConnector mHardwareConnector;


	/** Listens for events from the API's {@link HardwareConnector} */
	private final HardwareConnector.Listener mHardwareConnectorListener = new HardwareConnector.Listener() {

		@Override
		public void onHardwareConnectorStateChanged(NetworkType networkType, HardwareConnectorState hardwareState) {
			// Notify our listeners
			for (Listener listener : mListeners) {
				listener.onConnectorStateChanged(networkType, hardwareState);
			}
		}

		@Override
		public void onFirmwareUpdateRequired(SensorConnection sensorConnection, String currentVersionNumber, String recommendedVersion) {
		}
	};

	/** The registered set of {@link Listener}s we must notifiy when API events occur */
	private final Set<Listener> mListeners = new HashSet<Listener>();

	/**
	 * Listens for events from the API's {@link SensorConnection}. We use the same listener for all
	 * {@link SensorConnection}s
	 */
	private final SensorConnection.Listener mSensorConnectionListener = new SensorConnection.Listener() {

		@Override
		public void onNewCapabilityDetected(SensorConnection sensorConnection, CapabilityType capabilityType) {

			// Notify our listeners
			for (Listener listener : mListeners) {
				listener.onNewCapabilityDetected(sensorConnection, capabilityType);
			}
		}

		@Override
		public void onSensorConnectionError(SensorConnection sensorConnection, SensorConnectionError error) {

		}

		@Override
		public void onSensorConnectionStateChanged(SensorConnection sensorConnection, SensorConnectionState state) {

			// Notify our listeners
			for (Listener listener : mListeners) {
				listener.onSensorConnectionStateChanged(sensorConnection, state);
			}
		}
	};

	public HardwareConnectorService() {
	}

	/** Registers a {@link Listener} to be notified of API events */
	public void addListener(Listener listener) {
		mListeners.add(listener);
	}

	/**
	 * Requests a {@link SensorConnection} for the specified {@link ConnectionParams}. Returns null
	 * if called in inappropratie state.
	 * @see HardwareConnector#requestSensorConnection(ConnectionParams, SensorConnection.Listener)
	 */
	public SensorConnection connectSensor(ConnectionParams params) {
		if (mHardwareConnector != null) {
			return mHardwareConnector.requestSensorConnection(params, mSensorConnectionListener);
		} else {
			return null;
		}
	}

	/**
	 * Requests the disconnection of the {@link SensorConnection} with the specified
	 * {@link ConnectionParams}
	 * @param params the {@link ConnectionParams}
	 */
	public void disconnectSensor(ConnectionParams params) {
		if (mHardwareConnector != null) {
			SensorConnection sensorConnection = mHardwareConnector.getSensorConnection(params);
			if (sensorConnection != null) {
				sensorConnection.disconnect();
			}
		}
	}

	/**
	 * Starts/stops device discovery. Results can be obtained via a registered {@link Listener}
	 * @param enable true to enable discovery/false to disable
	 * @return true if ok, false if called in invalid state
	 */
	public boolean enableDiscovery(boolean enable) {
		if (mHardwareConnector != null) {
			if (enable) {
				mHardwareConnector.startDiscovery(mDiscoveryListener);
			} else {
				mHardwareConnector.stopDiscovery();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Gets the results of the current (or last performed discovery) as a collection of
	 * {@link ConnectionParams} objects
	 */
	public Collection<ConnectionParams> getDiscoveredConnectionParams() {
		if (mHardwareConnector != null) {
			return mHardwareConnector.getDiscoveredConnectionParams();
		} else {
			return new ArrayList<ConnectionParams>();
		}
	}

	/** Gets the {@link HardwareConnector} */
	public HardwareConnector getHardwareConnector() {
		return mHardwareConnector;
	}

	/**
	 * Gets the {@link SensorConnection} for the specified {@link SensorConnection} or null of not
	 * found
	 */
	public SensorConnection getSensorConnection(ConnectionParams params) {
		if (mHardwareConnector != null) {
			return mHardwareConnector.getSensorConnection(params);
		} else {
			return null;
		}
	}

	public Collection<SensorConnection> getSensorConnections() {
		if (mHardwareConnector != null) {
			return mHardwareConnector.getSensorConnections();
		} else {
			return new ArrayList<SensorConnection>();
		}
	}

	public boolean isDiscovering() {
		if (mHardwareConnector != null) {
			return mHardwareConnector.isDiscovering();
		} else {
			return false;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mHardwareConnector = new HardwareConnector(this, mHardwareConnectorListener);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		mHardwareConnector.stopDiscovery();
		mHardwareConnector.shutdown();
		mHardwareConnector = null;
	}

	public void removeListener(Listener listener) {
		mListeners.remove(listener);
	}

	public HardwareConnectorState getHardwareConnectorState(NetworkType networkType) {
		if (mHardwareConnector != null) {
			return mHardwareConnector.getHardwareConnectorState(networkType);
		} else {
			return HardwareConnectorState.HARDWARE_NOT_SUPPORTED;
		}
	}
}
