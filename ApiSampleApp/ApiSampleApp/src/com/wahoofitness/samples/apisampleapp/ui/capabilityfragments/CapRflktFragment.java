package com.wahoofitness.samples.apisampleapp.ui.capabilityfragments;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wahoofitness.common.display.DisplayButtonPosition;
import com.wahoofitness.common.display.DisplayConfiguration;
import com.wahoofitness.common.display.DisplayElement;
import com.wahoofitness.common.display.DisplayElementString;
import com.wahoofitness.common.display.DisplayPage;
import com.wahoofitness.common.log.Logger;
import com.wahoofitness.connector.capabilities.Capability.CapabilityType;
import com.wahoofitness.connector.capabilities.Rflkt;
import com.wahoofitness.connector.capabilities.Rflkt.ButtonPressType;
import com.wahoofitness.connector.capabilities.Rflkt.LoadConfigResult;
import com.wahoofitness.connector.conn.connections.SensorConnection;
import com.wahoofitness.connector.conn.connections.params.ProductType;
import com.wahoofitness.connector.packets.dcp.response.DCPR_DateDisplayOptionsPacket.DisplayDateFormat;
import com.wahoofitness.connector.packets.dcp.response.DCPR_DateDisplayOptionsPacket.DisplayDayOfWeek;
import com.wahoofitness.connector.packets.dcp.response.DCPR_DateDisplayOptionsPacket.DisplayTimeFormat;
import com.wahoofitness.connector.packets.dcp.response.DCPR_DateDisplayOptionsPacket.DisplayWatchFaceStyle;
import com.wahoofitness.connector.util.threading.Poller;
import com.wahoofitness.samples.apisampleapp.R;
import com.wahoofitness.samples.apisampleapp.service.HardwareConnectorService;
import com.wahoofitness.samples.apisampleapp.ui.util.UserRequest;

public class CapRflktFragment extends CapabilityFragment {

	private final Rflkt.Listener mRflktListener = new Rflkt.Listener() {

		@Override
		public void onAutoPageScrollRecieved() {
			registerCallbackResult("onAutoPageScrollRecieved", "");
			refreshView();
		}

		@Override
		public void onBacklightPercentReceived(int arg0) {
			registerCallbackResult("onBacklightPercentReceived", arg0);
			refreshView();
		}

		@Override
		public void onButtonPressed(DisplayButtonPosition arg0, ButtonPressType arg1) {
			registerCallbackResult("onButtonPressed", arg0, arg1);

			Rflkt rflkt = getRflktCap();
			DisplayConfiguration cfg = rflkt.getDisplayConfiguration();
			if (cfg == null)
				return;
			List<DisplayPage> visiblePages = cfg.getVisiblePages();

			if (visiblePages.size() > 1) {
				DisplayPage page = rflkt.getPage();
				int visibleIndex = visiblePages.indexOf(page);
				if (visibleIndex >= 0 && visibleIndex < visiblePages.size() - 1) {
					rflkt.sendSetPageIndex(visiblePages.get(visibleIndex + 1).getPageIndex());
				} else if (visibleIndex == visiblePages.size() - 1) {
					rflkt.sendSetPageIndex(visiblePages.get(0).getPageIndex());
				} else {
					L.e("onButtonPressed current visible page not found");
				}
			} else {
				L.i("onButtonPressed not enough visible pages to go right");
			}

			refreshView();
		}

		@Override
		public void onColorInvertedReceived(boolean arg0) {
			registerCallbackResult("onColorInvertedReceived", arg0);
			refreshView();
		}

		@Override
		public void onConfigVersionsReceived(int[] arg0) {
			registerCallbackResult("onConfigVersionsReceived", arg0);
			refreshView();
		}

		@Override
		public void onDateReceived(Calendar arg0) {
			registerCallbackResult("onDateReceived", CapRflktFragment.toString(arg0));
			refreshView();
		}

		@Override
		public void onDisplayOptionsReceived(DisplayDateFormat arg0, DisplayTimeFormat arg1,
				DisplayDayOfWeek arg2, DisplayWatchFaceStyle arg3) {
			registerCallbackResult("onDisplayOptionsReceived", arg0, arg1, arg2, arg3);
			refreshView();
		}

		@Override
		public void onLoadComplete() {
			registerCallbackResult("onLoadComplete", "");
			refreshView();
		}

		@Override
		public void onLoadFailed(LoadConfigResult arg0) {
			registerCallbackResult("onLoadFailed", arg0);
			refreshView();
		}

		@Override
		public void onLoadProgressChanged(int arg0) {
			registerCallbackResult("onLoadProgressChanged", arg0);
			refreshView();
		}

		@Override
		public void onPageIndexReceived(int arg0) {
			registerCallbackResult("onPageIndexReceived", arg0);
			refreshView();
		}

		@Override
		public void onNotificationDisplayReceived(boolean b) {

		}
	};
	private TextView mTextView;
	private Button stressButton;

	private static final Logger L = new Logger("CapRflktFragment");

	@Override
	public void initView(final Context context, LinearLayout ll) {
		mTextView = createSimpleTextView(context);
		refreshView();
		ll.addView(mTextView);

		ll.addView(createSimpleButton(context, "loadConfig", new OnClickListener() {

			@Override
			public void onClick(View v) {

				SensorConnection sc = getSensorConnection();
				ProductType productType = sc.getProductType();

				int id;
				if (productType == ProductType.MAGELLAN_BOISE
						|| productType == ProductType.MAGELLAN_ECHO) {
					id = R.raw.display_cfg_echo_default;
				} else {
					id = R.raw.display_cfg_rflkt_default;
				}

				DisplayConfiguration displayConfiguration = DisplayConfiguration.fromRawResource(
						getActivity().getResources(), id);
				LoadConfigResult result = getRflktCap().loadConfig(displayConfiguration);
				toast("loadConfig: " + result);
			}
		}));

		ll.addView(createSimpleButton(context, "sendGetBacklightPercent", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendGetBacklightPercent();
			}
		}));
		ll.addView(createSimpleButton(context, "sendGetColorInverted", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendGetColorInverted();
			}
		}));
		ll.addView(createSimpleButton(context, "sendGetDate", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendGetDate();
			}
		}));
		ll.addView(createSimpleButton(context, "sendGetDisplayOptions", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendGetDisplayOptions();
			}
		}));
		ll.addView(createSimpleButton(context, "sendGetPageIndex", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendGetPageIndex();
			}
		}));
		ll.addView(createSimpleButton(context, "sendSetAutoPageScrollDelay", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestInt(context, 0, "Auto Page Scroll Delay (ms)", null, 3000, "",
						new UserRequest.IntListener() {

							@Override
							public void onInt(int integer) {
								getRflktCap().sendSetAutoPageScrollDelay(integer);
							}
						});
			}
		}));
		ll.addView(createSimpleButton(context, "sendSetBacklightPercent", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestDoubleRange(context, 0, " Backlight Percent %", null, null, 0.0,
						100.0, new UserRequest.DoubleListener() {

							@Override
							public void onDouble(double number) {
								getRflktCap().sendSetBacklightPercent((int) number);
							}
						});
			}
		}));
		ll.addView(createSimpleButton(context, "sendSetColorInverted true", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendSetColorInverted(true);
			}
		}));
		ll.addView(createSimpleButton(context, "sendSetColorInverted false", new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRflktCap().sendSetColorInverted(false);
			}
		}));

		ll.addView(createSimpleButton(context, "sendSetDisplayOptions", new OnClickListener() {

			@Override
			public void onClick(View v) {
				DisplayDateFormat dateFormat = DisplayDateFormat.DD_MM_YYYY;
				DisplayTimeFormat timeFormat = DisplayTimeFormat.TWELVE_HOUR;
				DisplayDayOfWeek startDayOfWeek = DisplayDayOfWeek.MONDAY;
				DisplayWatchFaceStyle watchfaceStyle = DisplayWatchFaceStyle.ANALOG;
				getRflktCap().sendSetDisplayOptions(dateFormat, timeFormat, startDayOfWeek,
						watchfaceStyle);
			}
		}));
		ll.addView(createSimpleButton(context, "sendSetPageIndex", new OnClickListener() {

			@Override
			public void onClick(View v) {
				UserRequest.requestInt(context, 0, "Page Index", null, 0, "",
						new UserRequest.IntListener() {

							@Override
							public void onInt(int integer) {
								getRflktCap().sendSetPageIndex(integer);
							}
						});
			}
		}));
		// ll.addView(createSimpleButton(context, "sendSetSleepOnDisconnect true",
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// getRflktCap().sendSetSleepOnDisconnect(true);
		// }
		// }));
		// ll.addView(createSimpleButton(context, "sendSetSleepOnDisconnect false",
		// new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// getRflktCap().sendSetSleepOnDisconnect(false);
		// }
		// }));

		stressButton = createSimpleButton(context, "start stress", new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (stressPoller.isPolling()) {
					stressPoller.stop();
					stressButton.setBackgroundResource(android.R.drawable.btn_default);
				} else {
					stressPoller.start();
					stressButton.setBackgroundColor(Color.GREEN);
				}
			}
		});
		ll.addView(stressButton);
	}

	private static String randomString(Random r, int len) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < len; i++) {
			int ascii = r.nextInt(10) + 48;
			String s = Character.toString((char) ascii);
			sb.append(s);
		}
		return sb.toString();
	}

	private final Random random = new Random();
	Poller stressPoller = new Poller(500) {

		@Override
		protected void onPoll() {

			Rflkt rflkt = getRflktCap();
			if (rflkt == null) {
				stressPoller.stop();
				return;
			}
			DisplayConfiguration cfg = rflkt.getDisplayConfiguration();
			if (cfg == null) {
				stressPoller.stop();
				return;
			}

			for (DisplayPage page : cfg.getPages()) {
				List<DisplayElement> pageEls = page.getAllElements();
				for (DisplayElement pageEl : pageEls) {
					if (pageEl instanceof DisplayElementString) {
						DisplayElementString pageStr = (DisplayElementString) pageEl;
						if (!pageStr.isConstant()) {
							String val = randomString(random, 3);
							rflkt.setValue(pageStr.getUpdateKey(), val);
						}
					}
				}
			}

		}
	};

	@Override
	public void onDestroy() {
		super.onDestroy();
		Rflkt rflkt = getRflktCap();
		if (rflkt != null) {
			rflkt.removeListener(mRflktListener);
		}
	}

	@Override
	public void onHardwareConnectorServiceConnected(HardwareConnectorService service) {
		getRflktCap().addListener(mRflktListener);
		refreshView();
	}

	private Rflkt getRflktCap() {
		return (Rflkt) getCapability(CapabilityType.Rflkt);
	}

	@Override
	protected void refreshView() {
		Rflkt cap = getRflktCap();
		if (cap != null) {
			mTextView.setText("");
			mTextView.append("GETTER DATA\n");
			mTextView.append(summarizeGetters(Rflkt.class, cap));
			mTextView.append("\n\n");
			mTextView.append("CALLBACKS\n");
			mTextView.append(getCallbackSummary());
		} else {
			mTextView.setText("Please wait... no cap...");
		}
	}

}
