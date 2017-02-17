/*
 * Copyright 2014 Viettel Telecom. All rights reserved.
 * VIETTEL PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.viettel.image.zoom;

/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */



import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.os.ParcelableCompatCreatorCallbacks;

/**
 * Parcelable Compat
 * ParcelableCompat.java
 * @version: 1.0 
 * @since:  08:33:05 20 Jan 2014
 */
public class ParcelableCompat {
    public static <T> Parcelable.Creator<T> newCreator(
            ParcelableCompatCreatorCallbacks<T> callbacks) {
        //v4 if (android.os.Build.VERSION.SDK_INT >= 13) {
//        if (android.support.v2.os.Build.VERSION.SDK_INT >= 13) {
//            ParcelableCompatCreatorHoneycombMR2Stub.instantiate(callbacks);
//        }
        return new CompatCreator<T>(callbacks);
    }

    static class CompatCreator<T> implements Parcelable.Creator<T> {
        final ParcelableCompatCreatorCallbacks<T> mCallbacks;

        public CompatCreator(ParcelableCompatCreatorCallbacks<T> callbacks) {
            mCallbacks = callbacks;
        }

        @Override
        public T createFromParcel(Parcel source) {
            return mCallbacks.createFromParcel(source, null);
        }

        @Override
        public T[] newArray(int size) {
            return mCallbacks.newArray(size);
        }
    }
}

