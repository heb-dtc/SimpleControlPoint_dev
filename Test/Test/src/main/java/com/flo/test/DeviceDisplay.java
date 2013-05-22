package com.flo.test;

import org.teleal.cling.model.meta.Device;

/**
 * Created by florent.noel on 5/22/13.
 */
public class DeviceDisplay {
    private Device mDevice = null;

    public DeviceDisplay(Device device) {
        mDevice = device;
    }

    public Device getDevice() {
        return mDevice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceDisplay that = (DeviceDisplay) o;
        return mDevice.equals(that.mDevice);
    }

    @Override
    public int hashCode() {
        return mDevice.hashCode();
    }

    @Override
    public String toString() {
        String name = mDevice.getDetails() != null && mDevice.getDetails().getFriendlyName() != null ? mDevice.getDetails().getFriendlyName() : mDevice.getDisplayString();

        // Display a little star while the device is being loaded
        return mDevice.isFullyHydrated() ? mDevice.getDisplayString() : mDevice.getDisplayString() + " *";
    }
}
