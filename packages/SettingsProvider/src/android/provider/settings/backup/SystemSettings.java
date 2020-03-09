/*
 * Copyright (C) 2019 The Android Open Source Project
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

package android.provider.settings.backup;

import android.compat.annotation.UnsupportedAppUsage;
import android.provider.Settings;

/** Information about the system settings to back up */
public class SystemSettings {

    /**
     * Settings to backup.
     *
     * NOTE: Settings are backed up and restored in the order they appear
     *       in this array. If you have one setting depending on another,
     *       make sure that they are ordered appropriately.
     */
    @UnsupportedAppUsage
    public static final String[] SETTINGS_TO_BACKUP = {
        Settings.System.STAY_ON_WHILE_PLUGGED_IN,   // moved to global
        Settings.System.WIFI_USE_STATIC_IP,
        Settings.System.WIFI_STATIC_IP,
        Settings.System.WIFI_STATIC_GATEWAY,
        Settings.System.WIFI_STATIC_NETMASK,
        Settings.System.WIFI_STATIC_DNS1,
        Settings.System.WIFI_STATIC_DNS2,
        Settings.System.BLUETOOTH_DISCOVERABILITY,
        Settings.System.BLUETOOTH_DISCOVERABILITY_TIMEOUT,
        Settings.System.FONT_SCALE,
        Settings.System.DIM_SCREEN,
        Settings.System.SCREEN_OFF_TIMEOUT,
        Settings.System.SCREEN_BRIGHTNESS_MODE,
        Settings.System.ADAPTIVE_SLEEP,             // moved to secure
        Settings.System.APPLY_RAMPING_RINGER,
        Settings.System.VIBRATE_INPUT_DEVICES,
        Settings.System.MODE_RINGER_STREAMS_AFFECTED,
        Settings.System.TEXT_AUTO_REPLACE,
        Settings.System.TEXT_AUTO_CAPS,
        Settings.System.TEXT_AUTO_PUNCTUATE,
        Settings.System.TEXT_SHOW_PASSWORD,
        Settings.System.AUTO_TIME,                  // moved to global
        Settings.System.AUTO_TIME_ZONE,             // moved to global
        Settings.System.TIME_12_24,
        Settings.System.DTMF_TONE_WHEN_DIALING,
        Settings.System.DTMF_TONE_TYPE_WHEN_DIALING,
        Settings.System.HEARING_AID,
        Settings.System.TTY_MODE,
        Settings.System.MASTER_MONO,
        Settings.System.MASTER_BALANCE,
        Settings.System.SOUND_EFFECTS_ENABLED,
        Settings.System.HAPTIC_FEEDBACK_ENABLED,
        Settings.System.POWER_SOUNDS_ENABLED,       // moved to global
        Settings.System.DOCK_SOUNDS_ENABLED,        // moved to global
        Settings.System.LOCKSCREEN_SOUNDS_ENABLED,
        Settings.System.SHOW_WEB_SUGGESTIONS,
        Settings.System.SIP_CALL_OPTIONS,
        Settings.System.SIP_RECEIVE_CALLS,
        Settings.System.POINTER_SPEED,
        Settings.System.VIBRATE_WHEN_RINGING,
        Settings.System.RINGTONE,
        Settings.System.LOCK_TO_APP_ENABLED,
        Settings.System.NOTIFICATION_SOUND,
        Settings.System.ACCELEROMETER_ROTATION,
        Settings.System.SHOW_BATTERY_PERCENT,
        Settings.System.ALARM_VIBRATION_INTENSITY,
        Settings.System.MEDIA_VIBRATION_INTENSITY,
        Settings.System.NOTIFICATION_VIBRATION_INTENSITY,
        Settings.System.RING_VIBRATION_INTENSITY,
        Settings.System.HAPTIC_FEEDBACK_INTENSITY,
        Settings.System.HARDWARE_HAPTIC_FEEDBACK_INTENSITY,
        Settings.System.HAPTIC_FEEDBACK_ENABLED,
        Settings.System.DISPLAY_COLOR_MODE_VENDOR_HINT, // must precede DISPLAY_COLOR_MODE
        Settings.System.DISPLAY_COLOR_MODE,
        Settings.System.ALARM_ALERT,
        Settings.System.NOTIFICATION_LIGHT_PULSE,
        Settings.System.AE_THEME,
        Settings.System.ACCELEROMETER_ROTATION_ANGLES,
        Settings.System.LOCKSCREEN_ROTATION,
        Settings.System.NETWORK_TRAFFIC_STATE,
        Settings.System.NETWORK_TRAFFIC_AUTOHIDE_THRESHOLD,
        Settings.System.NETWORK_TRAFFIC_EXPANDED_STATUS_BAR_STATE,
        Settings.System.LOCKSCREEN_BATTERY_INFO,
        Settings.System.LOCKSCREEN_BATTERY_INFO_TEMP_UNIT,
        Settings.System.SCREEN_OFF_ANIMATION,
        Settings.System.VOLUME_KEY_CURSOR_CONTROL,
        Settings.System.BACK_GESTURE_HAPTIC,
        Settings.System.HEADS_UP_NOTIFICATIONS_THRESHOLD,
        Settings.System.TOAST_ANIMATION,
        Settings.System.THREE_FINGER_GESTURE,
        Settings.System.OMNIJAWS_WEATHER_ICON_PACK,
        Settings.System.OMNI_LOCKSCREEN_WEATHER_ENABLED,
        Settings.System.AICP_LOCKSCREEN_WEATHER_STYLE,
        Settings.System.LOCKSCREEN_WEATHER_SHOW_TEMP,
        Settings.System.LOCKSCREEN_WEATHER_SHOW_CITY,
        Settings.System.QS_FOOTER_TEXT_SHOW,
        Settings.System.QS_FOOTER_TEXT_STRING,
        Settings.System.CLICK_PARTIAL_SCREENSHOT,
        Settings.System.QQS_SHOW_BRIGHTNESS,
        Settings.System.SHOW_AUTO_BRIGHTNESS_BUTTON,
        Settings.System.BRIGHTNESS_SLIDER_POSITION,
        Settings.System.TORCH_LONG_PRESS_POWER_GESTURE,
        Settings.System.TORCH_LONG_PRESS_POWER_TIMEOUT,
        Settings.System.STATUS_BAR_QUICK_QS_PULLDOWN,
        Settings.System.POWERMENU_SOUNDPANEL,
        Settings.System.POWERMENU_SCREENSHOT,
        Settings.System.POWERMENU_SETTINGS,
        Settings.System.POWERMENU_LOCKDOWN,
        Settings.System.POWERMENU_AIRPLANE,
        Settings.System.POWERMENU_ADVANCED,
        Settings.System.POWERMENU_USERS,
        Settings.System.POWERMENU_LOGOUT,
        Settings.System.POWERMENU_EMERGENCY,
        Settings.System.POWERMENU_TORCH,
        Settings.System.STATUS_BAR_CLOCK_POSITION,
        Settings.System.STATUS_BAR_AM_PM,
        Settings.System.STATUS_BAR_CLOCK_AUTO_HIDE_LAUNCHER,
        Settings.System.STATUS_BAR_CLOCK_SECONDS,
        Settings.System.STATUS_BAR_CLOCK_DATE_DISPLAY,
        Settings.System.STATUS_BAR_CLOCK_DATE_STYLE,
        Settings.System.STATUS_BAR_CLOCK_DATE_POSITION,
        Settings.System.STATUS_BAR_CLOCK_DATE_FORMAT,
        Settings.System.STATUS_BAR_CLOCK_AUTO_HIDE,
        Settings.System.STATUS_BAR_CLOCK_AUTO_HIDE_HDURATION,
        Settings.System.STATUS_BAR_CLOCK_AUTO_HIDE_SDURATION,
        Settings.System.SHOW_QS_CLOCK,
        Settings.System.SHOW_QS_DATE,
        Settings.System.STATUS_BAR_CLOCK_SIZE,
        Settings.System.QS_HEADER_CLOCK_SIZE,
        Settings.System.USE_OLD_MOBILETYPE,
        Settings.System.ROAMING_INDICATOR_ICON,
        Settings.System.DATA_DISABLED_ICON,
        Settings.System.STATUSBAR_COLORED_ICONS,
        Settings.System.RINGTONE_VIBRATION_PATTERN,
        Settings.System.CUSTOM_RINGTONE_VIBRATION_PATTERN,
        Settings.System.INCREASING_RING,
        Settings.System.INCREASING_RING_START_VOLUME,
        Settings.System.INCREASING_RING_RAMP_UP_TIME,
        Settings.System.FLASHLIGHT_ON_CALL,
        Settings.System.FLASHLIGHT_ON_CALL_WAITING,
        Settings.System.FLASHLIGHT_ON_CALL_IGNORE_DND,
        Settings.System.FLASHLIGHT_ON_CALL_RATE,
        Settings.System.QS_TRANSPARENCY,
        Settings.System.ANIM_TILE_STYLE,
        Settings.System.ANIM_TILE_DURATION,
        Settings.System.ANIM_TILE_INTERPOLATOR,
        Settings.System.NOTIFICATION_MATERIAL_DISMISS,
        Settings.System.BACK_GESTURE_HEIGHT,
        Settings.System.TOAST_ICON,
        Settings.System.OMNI_LOCKSCREEN_MEDIA_BLUR,
        Settings.System.LOCKSCREEN_ALBUMART_FILTER,
        Settings.System.FINGERPRINT_SUCCESS_VIB,
        Settings.System.ADAPTIVE_PLAYBACK_ENABLED,
        Settings.System.ADAPTIVE_PLAYBACK_TIMEOUT,
        Settings.System.TRANSPARENT_POWER_MENU,
        Settings.System.TRANSPARENT_POWER_DIALOG_DIM,
        Settings.System.POWER_MENU_ANIMATIONS,
        Settings.System.VOLUME_DIALOG_TIMEOUT,
        Settings.System.STATUSBAR_CLOCK_CHIP,
        Settings.System.GLOBAL_ACTIONS_ONTHEGO,
        Settings.System.ON_THE_GO_ALPHA,
        Settings.System.ON_THE_GO_SERVICE_RESTART,
        Settings.System.ON_THE_GO_CAMERA,
        Settings.System.STATUS_BAR_LOGO,
        Settings.System.STATUS_BAR_LOGO_COLOR,
        Settings.System.STATUS_BAR_LOGO_POSITION,
        Settings.System.STATUS_BAR_LOGO_STYLE,
        Settings.System.STATUS_BAR_LOGO_COLOR_ACCENT,
        Settings.System.SCREENSHOT_SHUTTER_SOUND,
        Settings.System.LOCKSCREEN_PIN_SCRAMBLE_LAYOUT,
        Settings.System.LOCKSCREEN_QUICK_UNLOCK_CONTROL,
        Settings.System.BUTTON_BACKLIGHT_ONLY_WHEN_PRESSED,
        Settings.System.NAVIGATION_BAR_SHOW,
        Settings.System.NAVIGATION_BAR_MODE_OVERLAY,
        Settings.System.KEY_HOME_LONG_PRESS_ACTION,
        Settings.System.KEY_HOME_DOUBLE_TAP_ACTION,
        Settings.System.KEY_MENU_ACTION,
        Settings.System.KEY_MENU_LONG_PRESS_ACTION,
        Settings.System.KEY_ASSIST_ACTION,
        Settings.System.KEY_ASSIST_LONG_PRESS_ACTION,
        Settings.System.KEY_APP_SWITCH_ACTION,
        Settings.System.KEY_APP_SWITCH_LONG_PRESS_ACTION,
        Settings.System.HOME_WAKE_SCREEN,
        Settings.System.BACK_WAKE_SCREEN,
        Settings.System.MENU_WAKE_SCREEN,
        Settings.System.ASSIST_WAKE_SCREEN,
        Settings.System.APP_SWITCH_WAKE_SCREEN,
        Settings.System.CAMERA_WAKE_SCREEN,
        Settings.System.CAMERA_SLEEP_ON_RELEASE,
        Settings.System.CAMERA_LAUNCH,
        Settings.System.SWAP_VOLUME_BUTTONS,
        Settings.System.VOLUME_ROCKER_WAKE,
        Settings.System.VOLUME_BUTTON_MUSIC_CONTROL,
        Settings.System.VOLUME_KEYS_CONTROL_RING_TONE,
        Settings.System.VOLUME_ANSWER_CALL,
        Settings.System.STATUSBAR_BATTERY_BAR,
        Settings.System.STATUSBAR_BATTERY_BAR_COLOR,
        Settings.System.STATUSBAR_BATTERY_BAR_THICKNESS,
        Settings.System.STATUSBAR_BATTERY_BAR_STYLE,
        Settings.System.STATUSBAR_BATTERY_BAR_ANIMATE,
        Settings.System.STATUSBAR_BATTERY_BAR_LOCATION,
        Settings.System.DOUBLE_TAP_SLEEP_GESTURE,
        Settings.System.DOUBLE_TAP_SLEEP_LOCKSCREEN,
        Settings.System.QS_TILE_VERTICAL_LAYOUT,
        Settings.System.QS_LAYOUT_COLUMNS_LANDSCAPE,
        Settings.System.QS_LAYOUT_COLUMNS,
        Settings.System.QS_DATA_USAGE_PANEL,
        Settings.System.CHARGING_ANIMATION,
        Settings.System.ENABLE_RIPPLE_EFFECT,
        Settings.System.ENABLE_TASKBAR,
        Settings.System.STATUS_BAR_BATTERY_STYLE,
        Settings.System.STATUS_BAR_SHOW_BATTERY_PERCENT,
        Settings.System.STATUS_BAR_BATTERY_TEXT_CHARGING,
        Settings.System.USE_SLIM_RECENTS,
        Settings.System.RECENTS_MAX_APPS,
        Settings.System.RECENT_PANEL_GRAVITY,
        Settings.System.RECENT_PANEL_SCALE_FACTOR,
        Settings.System.RECENT_PANEL_FAVORITES,
        Settings.System.RECENT_PANEL_EXPANDED_MODE,
        Settings.System.RECENT_PANEL_BG_COLOR,
        Settings.System.RECENT_CARD_BG_COLOR,
        Settings.System.SLIM_RECENT_AICP_EMPTY_DRAWABLE,
        Settings.System.USE_RECENT_APP_SIDEBAR,
        Settings.System.RECENT_APP_SIDEBAR_CONTENT,
        Settings.System.RECENT_APP_SIDEBAR_DISABLE_LABELS,
        Settings.System.RECENT_APP_SIDEBAR_BG_COLOR,
        Settings.System.RECENT_APP_SIDEBAR_TEXT_COLOR,
        Settings.System.RECENT_APP_SIDEBAR_SCALE_FACTOR,
        Settings.System.RECENT_APP_SIDEBAR_OPEN_SIMULTANEOUSLY,
        Settings.System.SLIM_RECENTS_MEM_DISPLAY,
        Settings.System.SLIM_RECENTS_MEM_DISPLAY_LONG_CLICK_CLEAR,
        Settings.System.SLIM_RECENTS_ICON_PACK,
        Settings.System.SLIM_MEM_BAR_COLOR,
        Settings.System.SLIM_MEM_TEXT_COLOR,
        Settings.System.SLIM_RECENTS_CORNER_RADIUS,
        Settings.System.SLIM_RECENTS_BLACKLIST_VALUES,
        Settings.System.SLIM_RECENT_ENTER_EXIT_ANIMATION,
        Settings.System.NAV_BAR_COMPACT_LAYOUT,
    };
}
