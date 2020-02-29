package com.github.florent37.singledateandtimepicker;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;

import java.util.Locale;

/**
 * Helper class, provides {@link Locale} specific methods.
 */
public class LocaleHelper {

	/**
	 * Retrieves the string from resources by specific {@link Locale}.
	 *
	 * @param context    The context.
	 * @param locale     The requested locale.
	 * @param resourceId The string resource id.
	 *
	 * @return The string.
	 */
	public static String getString(@NonNull Context context, @NonNull Locale locale, @StringRes int resourceId) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			Configuration config = new Configuration(context.getResources().getConfiguration());
			config.setLocale(locale);
			return context.createConfigurationContext(config).getString(resourceId);
		} else {
			Resources resources = context.getResources();
			Configuration conf = resources.getConfiguration();
			Locale savedLocale = conf.locale;
			conf.locale = locale;
			resources.updateConfiguration(conf, null);

			// retrieve resources from desired locale
			String result = resources.getString(resourceId);

			// restore original locale
			conf.locale = savedLocale;
			resources.updateConfiguration(conf, null);
			return result;
		}
	}
}
