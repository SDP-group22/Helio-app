package com.helio.app.model;

import android.util.Patterns;

public class IPAddress {
    public static final String DEFAULT = "192.168.0.1";
    private static final String ADDRESS_PREFIX = "http://";
    private static final String ADDRESS_SUFFIX = ":4310";

    public static boolean correctFormat(String ip) {
        return Patterns.IP_ADDRESS.matcher(ip).matches();
    }

    public static String getBaseAddressUrl(String ip) {
        return ADDRESS_PREFIX + ip + ADDRESS_SUFFIX;
    }
}
