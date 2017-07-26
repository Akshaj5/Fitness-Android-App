package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.wahoofitness.common.datatypes.TimeInstant;
import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.capabilities.Capability;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Capability.Data;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ConnectionParams;
import com.wahoofitness.connector.util.Convert;
import com.wahoofitness.samples.apisampleapp.ui.HardwareConnectorFragment;

public abstract class CapabilityFragment extends HardwareConnectorFragment {

	private static final Logger L = new Logger("CapabilityFragment");
	private Map<String, String> mCallbackResults = new TreeMap<String, String>();
	private ConnectionParams mConnectionParams;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setHasOptionsMenu(true);
	}

	@Override
	public final View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Context context = inflater.getContext();
		ScrollView sv = createSimpleScrollView(context);
		LinearLayout ll = (LinearLayout) sv.getChildAt(0);
		initView(context, ll);
		return sv;
	}

	@Override
	public void onDeviceDiscovered(ConnectionParams params) {
		// N/A
	}

	@Override
	public void onDiscoveredDeviceRssiChanged(ConnectionParams params) {
		// N/A
	}

	@Override
	public void onDiscoveredDeviceLost(ConnectionParams params) {
		// N/A
	}

	public void setConnectionParams(ConnectionParams connectionParams) {
		if (connectionParams == null) {
			throw new IllegalArgumentException("ConnectionParams cannot be null");
		}
		this.mConnectionParams = connectionParams;
	}

	protected String getCallbackSummary() {
		StringBuilder sb = new StringBuilder();
		for (Entry<String, String> entry : mCallbackResults.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		return sb.toString().trim();
	}

	protected Capability getCapability(CapabilityType capabilityType) {
		SensorConnection sensorConnection = getSensorConnection();
		if (sensorConnection != null) {
			return sensorConnection.getCurrentCapability(capabilityType);
		} else {
			return null;
		}
	}

	protected SensorConnection getSensorConnection() {
		return getSensorConnection(mConnectionParams);
	}

	protected abstract void initView(Context context, LinearLayout ll);

	protected abstract void refreshView();

	protected void registerCallbackResult(String key, Object... values) {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append(TimeInstant.now().format("HH:mm:ss")).append("] ");
		for (Object value : values) {
			sb.append(value).append(" ");
		}
		mCallbackResults.put(key, sb.toString().trim());
	}

	protected void toast(Object result) {
		Toast.makeText(getActivity(), result.toString(), Toast.LENGTH_SHORT).show();
	}

	public static CapabilityFragment create(CapabilityType capabilityType, ConnectionParams params) {
		CapabilityFragment fragment = null;
		switch (capabilityType) {
		case Heartrate: {
			fragment = new CapHeartrateFragment();
			break;
		}
		case DeviceInfo: {
			fragment = new CapDeviceInfoFragment();
			break;
		}
		case Battery: {
			fragment = new CapBatteryFragment();
			break;
		}
		case CrankRevs: {
			fragment = new CapCrankRevsFragment();
			break;
		}
		case WheelRevs: {
			fragment = new CapWheelRevsFragment();
			break;
		}
		case BikePower: {
			fragment = new CapBikePowerFragment();
			break;
		}
		case BikeTrainer: {
			fragment = new CapBikeTrainerFragment();
			break;
		}
		case SpinDown: {
			fragment = new CapSpinDownFragment();
			break;
		}
		case Kickr: {
			fragment = new CapKickrFragment();
			break;
		}
		case FirmwareVersion: {
			fragment = new CapFirmwareVersionFragment();
			break;
		}
		case Rflkt: {
			fragment = new CapRflktFragment();
			break;
		}
		case ConfirmConnection: {
			fragment = new CapConfirmConnectionFragment();
			break;
		}
		case ManualZeroCalibration: {
			fragment = new CapManualZeroCalibrationFragment();
			break;
		}
		case CrankLength: {
			fragment = new CapCrankLengthFragment();
			break;
		}
		case BikeDeadSpotAngles: {
			fragment = new CapBikeDeadSpotAnglesFragment();
			break;
		}
		case BikeEnergy: {
			fragment = new CapBikeEnergyFragment();
			break;
		}
		case BikeExtremeMagnitudes: {
			fragment = new CapBikeExtremeMagnitudesFragment();
			break;
		}
		case BikePedalPowerBalance: {
			fragment = new CapBikePedalPowerBalanceFragment();
			break;
		}
		case BikePedalPowerContribution: {
			fragment = new CapBikePedalPowerContributionFragment();
			break;
		}
		case BikeTorque: {
			fragment = new CapBikeTorqueFragment();
			break;
		}
		case RunSpeed: {
			fragment = new CapRunSpeedFragment();
			break;
		}
		case RunDistance: {
			fragment = new CapRunDistanceFragment();
			break;
		}
		case RunStepRate: {
			fragment = new CapRunStepRateFragment();
			break;
		}
		case RunSteps: {
			fragment = new CapRunStepsFragment();
			break;
		}
		case RunStride: {
			fragment = new CapRunStrideFragment();
			break;
		}
		case RunMotion: {
			fragment = new CapRunMotionFragment();
			break;
		}
		case ActivityControl: {
			fragment = new CapActivityControlFragment();
			break;
		}
		case RunSmoothness: {
			fragment = new CapRunSmoothnessFragment();
			break;
		}
		case DeviceTap: {
			fragment = new CapDeviceTapFragment();
			break;
		}
		case Connection: {
			fragment = new CapConnectionFragment();
			break;
		}
		case Environment: {
			fragment = new CapEnvironmentFragment();
			break;
		}
		case Elevation: {
			fragment = new CapElevationFragment();
			break;
		}
		default: {
			break;
		}
		}

		if (fragment != null) {
			fragment.setConnectionParams(params);
			return fragment;
		} else {
			return null;
		}
	}

	protected static Button createSimpleButton(Context context, String text, OnClickListener ocl) {
		Button but = new Button(context);
		but.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		but.setTextAppearance(context, android.R.style.TextAppearance_Small);
		but.setText(text);
		but.setOnClickListener(ocl);
		return but;
	}

	protected static ScrollView createSimpleScrollView(Context context) {
		ScrollView sv = new ScrollView(context);
		sv.setPadding(20, 20, 20, 20);
		LinearLayout ll = new LinearLayout(context);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);
		return sv;
	}

	protected static TextView createSimpleTextView(Context context) {
		TextView tv = new TextView(context);
		tv.setTextAppearance(context, android.R.style.TextAppearance_Small);
		tv.setPadding(20, 20, 20, 20);
		return tv;
	}

	protected static final DecimalFormat sDecimalFormat = new DecimalFormat("#.00");

	protected static String summarizeGetters(Object object) {
		if (object != null) {
			return summarizeGetters(object.getClass(), object);
		} else {
			return "null";
		}
	}

	protected static String summarizeGetters(Class<?> clazz, Object object) {
		StringBuilder sb = new StringBuilder();
		if (object != null) {
			Method[] methods = clazz.getMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("get") || method.getName().startsWith("is")) {
					if (method.getTypeParameters().length > 0) {
						continue;
					}
					if (!method.getDeclaringClass().equals(clazz)) {
						continue;
					}
					try {
						Object result = method.invoke(object);
						if (result instanceof byte[]) {
							byte[] bytes = (byte[]) result;
							sb.append(method.getName()).append(": ").append(Convert.bytesToHexString(bytes)).append("\n");
						} else if (result instanceof int[]) {
							int[] bytes = (int[]) result;
							sb.append(method.getName()).append(": ").append(Arrays.toString(bytes)).append("\n");
						} else if (result instanceof Double) {
							sb.append(method.getName()).append(": ").append(sDecimalFormat.format(result)).append("\n");
						} else if (result instanceof Calendar) {
							Calendar cal = (Calendar) result;
							sb.append(method.getName()).append(": ").append(toString(cal)).append("\n");
						} else {
							sb.append(method.getName()).append(": ").append(result).append("\n");
						}
					} catch (Exception e) {
						L.e("summarizeGetters", object.getClass().getSimpleName(), method.getName(), e.getMessage());
					}
				}
			}

			if (object instanceof Capability.Data) {
				Capability.Data data = (Data) object;
				sb.append("getTime").append(": ").append(data.getTime()).append("\n");
			}
		} else {
			sb.append("null");
		}
		return sb.toString().trim();
	}

	protected static String toString(Calendar cal) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss", Locale.US);
		return sdf.format(cal.getTime()) + " " + cal.getTimeZone().getDisplayName();
	}
}
