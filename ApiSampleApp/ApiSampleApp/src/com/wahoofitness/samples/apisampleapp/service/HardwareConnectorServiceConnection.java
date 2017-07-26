package com.wahoofitness.samples.apisampleapp.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService.HardwareConnectorServiceBinder;

/** Helper class to bind to {@link HardwareConnectorService} */
public abstract class HardwareConnectorServiceConnection {

	private Context mContext;
	private HardwareConnectorService mService = null;

	private final ServiceConnection mServiceConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName className, IBinder ibinder) {
			HardwareConnectorServiceBinder binder = (HardwareConnectorServiceBinder) ibinder;
			mService = binder.getService();
			onHardwareConnectorServiceConnected(mService);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService = null;
		}
	};

	/** Bind to the {@link HardwareConnectorService} */
	public void bind(Context context) {
		if (mContext == null) {
			mContext = context;

			Intent intent = new Intent(mContext, HardwareConnectorService.class);
			context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
		}
	}

	/** Gets the bound {@link HardwareConnectorService} or null if not bound */
	public HardwareConnectorService getHardwareConnectorService() {
		return mService;
	}

	/** Returns true if bound to {@link HardwareConnectorService}, else false */
	public boolean isBound() {
		return (mService != null) && (mService.getHardwareConnector() != null);
	}

	/** Unbind from {@link HardwareConnectorService} */
	public void unbind() {
		if (mContext != null) {
			mContext.unbindService(mServiceConnection);
			mContext = null;
		}
	}

	/** Called when the {@link HardwareConnectorService} is bound */
	protected abstract void onHardwareConnectorServiceConnected(HardwareConnectorService service);
}
