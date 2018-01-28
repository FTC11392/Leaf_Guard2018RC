/*
Copyright (c) 2017 Robert Atkinson

All rights reserved.

Derived in part from information in various resources, including FTDI, the
Android Linux implementation, FreeBsc, UsbSerial, and others.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Robert Atkinson nor the names of his contributors may be used to
endorse or promote products derived from this software without specific prior
written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESSFOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package org.firstinspires.ftc.robotcore.internal.ftdi;

import android.hardware.usb.UsbDevice;

/**
 * A simple data structure pairing a USB vendor id and product id
 */
@SuppressWarnings("WeakerAccess")
public class VendorAndProductIds {
    private int vendorId;
    private int productId;

    public VendorAndProductIds(int vendor, int product) {
        this.vendorId = vendor;
        this.productId = product;
    }

    public VendorAndProductIds() {
        this.vendorId = 0;
        this.productId = 0;
    }

    public static VendorAndProductIds from(UsbDevice dev) {
        if (dev == null) {
            return new VendorAndProductIds();
        } else {
            return new VendorAndProductIds(dev.getVendorId(), dev.getProductId());
        }
    }

    public void setVendorId(int vid) {
        this.vendorId = vid;
    }

    public void setProductId(int pid) {
        this.productId = pid;
    }

    public int getVendorId() {
        return this.vendorId;
    }

    public int getProductId() {
        return this.productId;
    }

    public String toString() {
        return String.format("vid=0x%04x pid=0x%04x", vendorId, productId);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof VendorAndProductIds)) {
            return false;
        } else {
            VendorAndProductIds another = (VendorAndProductIds) o;
            return this.vendorId == another.vendorId && this.productId == another.productId;
        }
    }

    public int hashCode() {
        return this.vendorId ^ this.productId;
    }
}
