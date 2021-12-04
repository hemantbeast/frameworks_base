/*
 * Copyright (C) 2021 The Android Open Source Project
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

package android.media.tv.interactive;

import android.graphics.Rect;
import android.net.Uri;
import android.media.tv.BroadcastInfoResponse;
import android.view.Surface;
import android.media.tv.BroadcastInfoResponse;

/**
 * Sub-interface of ITvIAppService.aidl which is created per session and has its own context.
 * @hide
 */
oneway interface ITvIAppSession {
    void startIApp();
    void release();
    void notifyTuned(in Uri channelUri);
    void setSurface(in Surface surface);
    void dispatchSurfaceChanged(int format, int width, int height);
    void notifyBroadcastInfoResponse(in BroadcastInfoResponse response);

    void createMediaView(in IBinder windowToken, in Rect frame);
    void relayoutMediaView(in Rect frame);
    void removeMediaView();
}