package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Kickr;
import com.wahoofitness.connector.capabilities.Kickr.BikeTrainerMode;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;
import com.wahoofitness.samples.apisampleapp.ui.util.UserRequest;

public class CapKickrFragment extends CapabilityFragment {

	private final Kickr.Listener mKickrListener = new Kickr.Listener() {


		@Override
		public void onGetBikeTrainerModeResponse(boolean b, BikeTrainerMode bikeTrainerMode) {

		}

		@Override
		public void onSetBikeTrainerModeResponse(boolean b, BikeTrainerMode bikeTrainerMode) {

		}

		@Override
		public void onSetSimModeGradeResponse(boolean success) {
			registerCallbackResult("onSetSimModeGradeResponse", success);
			refreshView();
		}

		@Override
		public void onSetSimModeRollingResistanceResponse(boolean success) {
			registerCallbackResult("onSetSimModeRollingResistanceResponse", success);
			refreshView();
		}

		@Override
		public void onSetSimModeWindResistanceResponse(boolean success) {
			registerCallbackResult("onSetSimModeWindResistanceResponse", success);
			refreshView();
		}

		@Override
		public void onSetSimModeWindSpeedResponse(boolean success) {
			registerCallbackResult("onSetSimModeWindSpeedResponse", success);
			refreshView();
		}

		@Override
		public void onSetWheelCircumferenceResponse(boolean success) {
			registerCallbackResult("onSetWheelCircumferenceResponse", success);
			refreshView();
		}

	};
	private TextView mTextView;

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "sendGetBikeTrainerMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getKickrCap().sendGetBikeTrainerMode();
			}
		}));


		ll.addView(createSimpleButton(context, "sendSetErgMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestInt(context, 0, "Enter ERG level (watts)", null, 65, "",
						new UserRequest.IntListener() {

							@Override
							public void onInt(int integer) {
								getKickrCap().sendSetErgMode(integer);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetResistanceMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Request resistance %", null, null, 0.0,
						1.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getKickrCap().sendSetResistanceMode((float) number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetSimMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestText(context, 0, "Enter sim mode paramters",
						"Use format weight,rollingResistanceCoefficient,windResistanceCoefficient",
						null, "55,0.0023,0.0034", new UserRequest.TextListener() {

							@Override
							public void onText(String text) {
								String[] els = text.split(",");
								if (els.length == 3) {
									try {
										Float weight = Float.valueOf(els[0]);
										Float rollingResistanceCoefficient = Float.valueOf(els[1]);
										Float windResistanceCoefficient = Float.valueOf(els[2]);
										getKickrCap().sendSetSimMode(weight,
												rollingResistanceCoefficient,
												windResistanceCoefficient);
									} catch (Exception e) {
										Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT)
												.show();
									}
								} else {
									Toast.makeText(context, "Invalid input", Toast.LENGTH_SHORT)
											.show();
								}
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetSimModeGrade", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Enter grade", null, null, -45.0, +45.0,
						new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getKickrCap().sendSetSimModeGrade((float) number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetSimModeRollingResistance",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserRequest.requestDouble(context, 0,
								"Enter Rolling Resistance Coefficient", null, 0.0054, "",
								new UserRequest.DoubleListener() {

									@Override
									public void onDouble(double number) {
										getKickrCap().sendSetSimModeRollingResistance(
												(float) number);
									}
								});
					}
				}));

		ll.addView(createSimpleButton(context, "sendSetSimModeWindResistance",
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						UserRequest.requestDouble(context, 0, "Enter wind resistance coefficient",
								null, 0.0023, "", new UserRequest.DoubleListener() {

									@Override
									public void onDouble(double number) {
										getKickrCap().sendSetSimModeWindResistance(
												(float) number);
									}
								});
					}
				}));

		ll.addView(createSimpleButton(context, "sendSetSimModeWindSpeed", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Enter wind speed", null, null, -30.0,
						+30.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getKickrCap().sendSetSimModeWindSpeed((float) number);
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetStandardMode", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, "Enter standard mode level", null, null,
						0.0, 9.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getKickrCap().sendSetStandardMode((int) Math.round(number));
							}
						});
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetWheelCircumference", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDouble(context, 0, "Enter wheel circumference in millimeters",
						null, 2000.34, "", new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getKickrCap().sendSetWheelCircumference((float) number);
							}
						});
			}
		}));

		refreshView();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Kickr Kickr = getKickrCap();
		if (Kickr != null) {
			Kickr.removeListener(mKickrListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
		getKickrCap().addListener(mKickrListener);
		refreshView();
	}

	private Kickr getKickrCap() {
		return (Kickr) getCapability(CapabilityType.Kickr);
	}

	@Override
	protected void refreshView() {
		Kickr cap = getKickrCap();
		if (cap != null) {

			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(Kickr.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());

		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}
}
