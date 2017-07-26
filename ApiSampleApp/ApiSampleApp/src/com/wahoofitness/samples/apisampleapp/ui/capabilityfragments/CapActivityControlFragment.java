package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.connector.capabilities.ActivityControl;
import com.wahoofitness.connector.capabilities.ActivityControl.CalibrationData;
import com.wahoofitness.connector.capabilities.ActivityType;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.conn.characteristics.CalibrationDataFactory;
import com.wahoofitness.connector.conn.characteristics.CalibrationDataRun;
import com.wahoofitness.connector.util.QuadraticCurveFitter.CurveParams;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;

@SuppressLint("SetTextI18n")
public class CapActivityControlFragment extends CapabilityFragment {

	private final ActivityControl.Listener mActivityControlListener = new ActivityControl.Listener() {

		@Override
		public void onGetActivityType(boolean success, ActivityType activityType) {
			registerCallbackResult("onGetActivityType", success, activityType);
			refreshView();

		}

		@Override
		public void onSetActivityType(boolean success, ActivityType requestedActivityType,
				ActivityType currentActivityType) {
			registerCallbackResult("onSetActivityType", success, requestedActivityType,
					currentActivityType);
			refreshView();

		}

		@Override
		public void onGetActivityCalibration(boolean success, ActivityType activityType,
				CalibrationData calibrationData) {
			registerCallbackResult("onGetActivityCalibration", success, activityType,
					calibrationData);
			refreshView();

		}

		@Override
		public void onResetActivityCalibration(boolean success, ActivityType activityType) {
			registerCallbackResult("onResetActivityCalibration", success, activityType);
			refreshView();

		}

		@Override
		public void onSetActivityCalibration(boolean success, ActivityType activityType) {
			registerCallbackResult("onSetActivityCalibration", success, activityType);
			refreshView();

		}
	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetActivityType", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getActivityControlCap().sendGetActivityType();
			}
		}));

		for (final ActivityType activityType : ActivityType.values()) {
			ll.addView(createSimpleButton(context, "sendSetActivityType " + activityType,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							boolean ok = getActivityControlCap().sendSetActivityType(activityType);
							toast(ok);
						}
					}));
		}

		for (final ActivityType activityType : ActivityType.values()) {
			ll.addView(createSimpleButton(context, "sendGetActivityCalibration " + activityType,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							boolean ok = getActivityControlCap().sendGetActivityCalibration(
									activityType);
							toast(ok);
						}
					}));
		}

		for (final ActivityType activityType : ActivityType.values()) {
			ll.addView(createSimpleButton(context, "sendResetActivityCalibration " + activityType,
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							boolean ok = getActivityControlCap().sendResetActivityCalibration(
									activityType);
							toast(ok);
						}
					}));
		}

		ll.addView(createSimpleButton(context, "sendSetActivityCalibration 310.3, -196.1, 33.3",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						CalibrationData data = CalibrationDataRun.fromCurveParams(new CurveParams(
								310.3f, -196.1f, 33.3f, 0));
						boolean ok = getActivityControlCap().sendSetActivityCalibration(data);
						toast(ok);
					}
				}));

		ll.addView(createSimpleButton(context,
				"sendSetActivityCalibrationData 310.3f, -196.1f, 30.3f", new OnClickListener() {

					@Override
					public void onClick(View v) {
						CalibrationData data = CalibrationDataRun.fromCurveParams(new CurveParams(
								310.3f, -196.1f, 30.3f, 0));
						boolean ok = getActivityControlCap().sendSetActivityCalibration(data);
						toast(ok);
					}
				}));

		for (final CalibrationDataFactory.BuiltInXMotionType b : CalibrationDataFactory.BuiltInXMotionType
				.values()) {

			ll.addView(createSimpleButton(context, "sendSetActivityCalibration " + b.name(),
					new OnClickListener() {

						@Override
						public void onClick(View v) {
							boolean ok = getActivityControlCap().sendSetActivityCalibration(
									CalibrationDataFactory.createCalibrationData(b));
							toast(ok);
						}
					}));
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		ActivityControl ActivityControl = getActivityControlCap();
		if (ActivityControl != null) {
			ActivityControl.removeListener(mActivityControlListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {

		getActivityControlCap().addListener(mActivityControlListener);
		refreshView();
	}

	private ActivityControl getActivityControlCap() {
		return (ActivityControl) getCapability(CapabilityType.ActivityControl);
	}


	@Override
	protected void refreshView() {
		ActivityControl cap = getActivityControlCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(ActivityControl.class, cap));
			// mTextView.append("\n\n");
			// mTextView.append("CALLBACK DATA\n");
			// mTextView.append(summarizeGetters(mLastCallbackData));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}

	}
}
