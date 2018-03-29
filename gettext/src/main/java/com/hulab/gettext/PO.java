package com.hulab.gettext;

import android.content.Context;
import android.util.Log;

import java.util.Locale;


public class PO {

    private final static String TAG = "GETTEXT";

    private static Messages sMessages = null;

    private static Messages sharedMessages(Context context) {

        if (sMessages == null) {
            String locale = Locale.getDefault().toString();
            setLocale(context, locale);
        }

        return sMessages;
    }


    public static void setLocale(Context context) {
        String locale = Locale.getDefault().toString();
        setLocale(context, locale);
    }

    /**
     * Set the current app's language manually. Otherwise, it will just take into account the
     * default locale.
     *
     * @param context The current context. This context must be non-null.
     * @param locale  The ISO code of the language, for example "en", "ru", "fr", "fr_CH".
     */
    public static void setLocale(Context context, String locale) {

        try {

            sMessages = Messages.load(context, locale);

        } catch (ClassNotFoundException e) {

            // try with the language iso code
            int idx = locale.indexOf('_');
            if (idx > -1) {
                setLocale(context, locale.substring(0, idx));
            } else {
                Log.w(TAG, "Locale not found ! Falling back on msg ids.");
                sMessages = new Messages();
            }

        } catch (Exception e) {
            Log.e(TAG, "Something went wrong while trying to set locale.");
            sMessages = new Messages();
        }
    }

    /**
     * Returns the translated string for the given key. The string is translated in the set language
     * if such a file is available.
     * <p>
     * Fallbacks are the following if only "en", "fr", and "pt-BR" are available:
     * <p>
     * "en_US" -> "en"
     * "fr_CH" -> "fr"
     * "pt_BR" -> "pt_BR"
     * "pt" -> {@param msgid}
     *
     * @param msgid   the key of the message
     * @return The translated message for the given key.
     */
    public static String gettext(String msgid) {
        assert sMessages != null : "should call setLocale(§)";
        return sMessages.gettext(msgid);
    }

    /**
     * Returns the translated string for the given key. The string is translated in the set language
     * if such a file is available.
     * <p>
     * Fallbacks are operating in the same way than {@link #gettext(String)}.
     * Plurals rules are referring to the ones given in the .po files. If no locale is available,
     * The last fallback is "en" rules: if {@param n} == 1, we use {@param msgid} as fallback. If
     * different than 1, we use {@param msgid_plural} as fallback.
     *
     * @param msgid        the key of the message
     * @param msgid_plural the plural key of the message
     * @param n            the quantity to consider when getting the plural form
     * @return The translated message for the given key in the given quantity.
     * Quantity is injected in the resulting string using
     * {@link java.lang.String#format(String, Object...) String.format()} method.
     */
    public static String ngettext(String msgid, String msgid_plural, int n) {
        assert sMessages != null : "should call setLocale(§)";
        return String.format(sMessages.ngettext(msgid, msgid_plural, n), n);
    }
}
