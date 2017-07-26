package com.wahoofitness.samples.apisampleapp.ui.util;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/**
 * Utility class for requesting information from users using a {@link AlertDialog}
 */
@SuppressLint("SetTextI18n")
public abstract class UserRequest {

	public interface DoubleListener {

		void onDouble(double number);
	}

	public interface IntListener {

		void onInt(int integer);
	}

	public static class Option {

		private final int icon;
		private final Object tag;
		private final String text;

		public Option(int icon, String text, Object tag) {
			super();
			this.icon = icon;
			this.text = text;
			this.tag = tag;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Option other = (Option) obj;
			if (icon != other.icon)
				return false;
			if (tag == null) {
				if (other.tag != null)
					return false;
			} else if (!tag.equals(other.tag))
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + icon;
			result = prime * result + ((tag == null) ? 0 : tag.hashCode());
			result = prime * result + ((text == null) ? 0 : text.hashCode());
			return result;
		}
	}

	public interface TextListener {

		void onText(String text);
	}

	public static void requestDouble(Context context, int icon, String title, String description, Double defaultDouble, String hint, final
	DoubleListener listener) {

		AlertDialog.Builder builder = builder(context, icon, title);

		final EditText textView = new EditText(context);
		if (defaultDouble != null) {
			textView.setText(defaultDouble.toString());
		}
		textView.setHint(hint);
		textView.selectAll();
		textView.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
		textView.requestFocus();

		builder.setView(buildLayout(context, description, textView));

		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				try {
					String value = textView.getText().toString();
					Double d = Double.parseDouble(value);
					listener.onDouble(d);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	public static void requestInt(Context context, int icon, String title, String description, Integer defaultInt, String hint, final
	IntListener listener) {

		AlertDialog.Builder builder = builder(context, icon, title);

		final EditText textView = new EditText(context);
		if (defaultInt != null) {
			textView.setText(defaultInt.toString());
		}
		textView.setHint(hint);
		textView.selectAll();
		textView.setInputType(InputType.TYPE_CLASS_NUMBER);
		textView.requestFocus();

		builder.setView(buildLayout(context, description, textView));

		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				try {
					String value = textView.getText().toString();
					Integer d = Integer.parseInt(value);
					listener.onInt(d);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	public static void requestDoubleRange(Context context, int icon, String title, String description, Double defaultDouble, final double
			min, final double max, final DoubleListener listener) {

		final double delta = max - min;

		AlertDialog.Builder builder = builder(context, icon, title);

		final SeekBar seekBar = new SeekBar(context);
		seekBar.setPadding(10, 10, 10, 10);
		if (defaultDouble != null) {
			int percent = (int) (100.0 * (defaultDouble - min) / delta);
			seekBar.setProgress(percent);
		}

		LinearLayout hh = new LinearLayout(context);
		hh.setOrientation(LinearLayout.HORIZONTAL);
		hh.setPadding(10, 10, 10, 10);
		hh.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		TextView tvmin = new TextView(context);
		final TextView tvval = new TextView(context);
		TextView tvmax = new TextView(context);

		tvmin.setText("" + min);
		tvval.setText("" + (min + (seekBar.getProgress() * delta / 100.0)));
		tvmax.setText("" + max);

		hh.addView(tvmin);
		hh.addView(tvval);
		hh.addView(tvmax);

		tvmin.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
		tvval.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
		tvmax.setLayoutParams(new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				tvval.setText("" + (min + (seekBar.getProgress() * delta / 100.0)));
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});
		builder.setView(buildLayout(context, description, seekBar, hh));

		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				try {
					int percent = seekBar.getProgress();
					double value = min + (percent * delta / 100.0);
					listener.onDouble(value);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	public static void requestText(Context context, int icon, String title, String description, String defaultText, String hint, final
	TextListener listener) {

		AlertDialog.Builder builder = builder(context, icon, title);

		final EditText textView = new EditText(context);
		textView.setText(defaultText);
		textView.setHint(hint);
		textView.selectAll();
		textView.requestFocus();

		builder.setView(buildLayout(context, description, textView));

		builder.setNegativeButton("Cancel", null);
		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				listener.onText(textView.getText().toString());
			}
		});

		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
	}

	private static AlertDialog.Builder builder(Context context, int icon, String title) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if (icon > 0)
			builder.setIcon(icon);
		builder.setTitle(title);
		return builder;
	}

	private static LinearLayout buildLayout(Context context, String description, View... views) {
		LinearLayout linearLayout = new LinearLayout(context);
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		if (description != null) {
			TextView descView = new TextView(context);
			descView.setText(description);
			descView.setPadding(20, 20, 20, 20);
			descView.setTextAppearance(context, android.R.style.TextAppearance_Medium);
			linearLayout.addView(descView);
		}

		if (views != null) {
			for (View view : views) {
				linearLayout.addView(view);
				view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			}
		}

		return linearLayout;
	}
}
