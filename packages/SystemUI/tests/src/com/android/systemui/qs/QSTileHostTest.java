/*
 * Copyright (C) 2018 The Android Open Source Project
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

package com.android.systemui.qs;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.testing.AndroidTestingRunner;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.test.filters.SmallTest;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.CollectionUtils;
import com.android.systemui.R;
import com.android.systemui.SysuiTestCase;
import com.android.systemui.classifier.FalsingManagerFake;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.qs.QSFactory;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.external.CustomTileStatePersister;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.external.TileServiceKey;
import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.CentralSurfaces;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.concurrency.FakeExecutor;
import com.android.systemui.util.settings.FakeSettings;
import com.android.systemui.util.settings.SecureSettings;
import com.android.systemui.util.time.FakeSystemClock;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;

import javax.inject.Provider;

@RunWith(AndroidTestingRunner.class)
@SmallTest
public class QSTileHostTest extends SysuiTestCase {

    private static String MOCK_STATE_STRING = "MockState";
    private static ComponentName CUSTOM_TILE =
            ComponentName.unflattenFromString("TEST_PKG/.TEST_CLS");
    private static final String CUSTOM_TILE_SPEC = CustomTile.toSpec(CUSTOM_TILE);
    private static final String SETTING = QSTileHost.TILES_SETTING;

    @Mock
    private StatusBarIconController mIconController;
    @Mock
    private QSFactory mDefaultFactory;
    @Mock
    private PluginManager mPluginManager;
    @Mock
    private TunerService mTunerService;
    @Mock
    private Provider<AutoTileManager> mAutoTiles;
    @Mock
    private DumpManager mDumpManager;
    @Mock
    private QSTile.State mMockState;
    @Mock
    private CentralSurfaces mCentralSurfaces;
    @Mock
    private QSLogger mQSLogger;
    @Mock
    private CustomTile mCustomTile;
    @Mock
    private UiEventLogger mUiEventLogger;
    @Mock
    private UserTracker mUserTracker;
    private SecureSettings mSecureSettings;
    @Mock
    private CustomTileStatePersister mCustomTileStatePersister;
    @Mock
    private TileServiceRequestController.Builder mTileServiceRequestControllerBuilder;
    @Mock
    private TileServiceRequestController mTileServiceRequestController;
    @Mock
    private TileLifecycleManager.Factory mTileLifecycleManagerFactory;
    @Mock
    private TileLifecycleManager mTileLifecycleManager;

    private FakeExecutor mMainExecutor;

    private QSTileHost mQSTileHost;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mMainExecutor = new FakeExecutor(new FakeSystemClock());

        when(mTileServiceRequestControllerBuilder.create(any()))
                .thenReturn(mTileServiceRequestController);
        when(mTileLifecycleManagerFactory.create(any(Intent.class), any(UserHandle.class)))
                .thenReturn(mTileLifecycleManager);

        mSecureSettings = new FakeSettings();
        saveSetting("");
        mQSTileHost = new TestQSTileHost(mContext, mIconController, mDefaultFactory, mMainExecutor,
                mPluginManager, mTunerService, mAutoTiles, mDumpManager, mCentralSurfaces,
                mQSLogger, mUiEventLogger, mUserTracker, mSecureSettings, mCustomTileStatePersister,
                mTileServiceRequestControllerBuilder, mTileLifecycleManagerFactory);

        mSecureSettings.registerContentObserverForUser(SETTING, new ContentObserver(null) {
            @Override
            public void onChange(boolean selfChange) {
                super.onChange(selfChange);
                mMainExecutor.execute(() -> mQSTileHost.onTuningChanged(SETTING, getSetting()));
                mMainExecutor.runAllReady();
            }
        }, mUserTracker.getUserId());
        setUpTileFactory();
    }

    private void saveSetting(String value) {
        mSecureSettings.putStringForUser(
                SETTING, value, "", false, mUserTracker.getUserId(), false);
    }

    private String getSetting() {
        return mSecureSettings.getStringForUser(SETTING, mUserTracker.getUserId());
    }

    private void setUpTileFactory() {
        when(mMockState.toString()).thenReturn(MOCK_STATE_STRING);
        // Only create this kind of tiles
        when(mDefaultFactory.createTile(anyString())).thenAnswer(
                invocation -> {
                    String spec = invocation.getArgument(0);
                    if ("spec1".equals(spec)) {
                        return new TestTile1(mQSTileHost);
                    } else if ("spec2".equals(spec)) {
                        return new TestTile2(mQSTileHost);
                    } else if ("spec3".equals(spec)) {
                        return new TestTile3(mQSTileHost);
                    } else if ("na".equals(spec)) {
                        return new NotAvailableTile(mQSTileHost);
                    } else if (CUSTOM_TILE_SPEC.equals(spec)) {
                        return mCustomTile;
                    } else if ("internet".equals(spec)
                            || "wifi".equals(spec)
                            || "cell".equals(spec)) {
                        return new TestTile1(mQSTileHost);
                    } else {
                        return null;
                    }
                });
        when(mCustomTile.isAvailable()).thenReturn(true);
    }

    @Test
    public void testLoadTileSpecs_emptySetting() {
        List<String> tiles = QSTileHost.loadTileSpecs(mContext, "");
        assertFalse(tiles.isEmpty());
    }

    @Test
    public void testLoadTileSpecs_nullSetting() {
        List<String> tiles = QSTileHost.loadTileSpecs(mContext, null);
        assertFalse(tiles.isEmpty());
    }

    @Test
    public void testInvalidSpecUsesDefault() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles, "spec1,spec2");
        saveSetting("not-valid");

        assertEquals(2, mQSTileHost.getTiles().size());
    }

    @Test
    public void testSpecWithInvalidDoesNotUseDefault() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles, "spec1,spec2");
        saveSetting("spec2,not-valid");

        assertEquals(1, mQSTileHost.getTiles().size());
        QSTile element = CollectionUtils.firstOrNull(mQSTileHost.getTiles());
        assertTrue(element instanceof TestTile2);
    }

    @Test
    public void testDump() {
        saveSetting("spec1,spec2");
        StringWriter w = new StringWriter();
        PrintWriter pw = new PrintWriter(w);
        mQSTileHost.dump(pw, new String[]{});
        String output = "QSTileHost:\n"
                + TestTile1.class.getSimpleName() + ":\n"
                + "    " + MOCK_STATE_STRING + "\n"
                + TestTile2.class.getSimpleName() + ":\n"
                + "    " + MOCK_STATE_STRING + "\n";
        assertEquals(output, w.getBuffer().toString());
    }

    @Test
    public void testDefault() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles_default, "spec1");
        saveSetting("default");
        assertEquals(1, mQSTileHost.getTiles().size());
        QSTile element = CollectionUtils.firstOrNull(mQSTileHost.getTiles());
        assertTrue(element instanceof TestTile1);
        verify(mQSLogger).logTileAdded("spec1");
    }

    @Test
    public void testNoRepeatedSpecs_addTile() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles, "spec1,spec2");
        saveSetting("spec1,spec2");

        mQSTileHost.addTile("spec1");

        assertEquals(2, mQSTileHost.mTileSpecs.size());
        assertEquals("spec1", mQSTileHost.mTileSpecs.get(0));
        assertEquals("spec2", mQSTileHost.mTileSpecs.get(1));
    }

    @Test
    public void testAddTileAtValidPosition() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles, "spec1,spec3");
        saveSetting("spec1,spec3");

        mQSTileHost.addTile("spec2", 1);
        mMainExecutor.runAllReady();

        assertEquals(3, mQSTileHost.mTileSpecs.size());
        assertEquals("spec1", mQSTileHost.mTileSpecs.get(0));
        assertEquals("spec2", mQSTileHost.mTileSpecs.get(1));
        assertEquals("spec3", mQSTileHost.mTileSpecs.get(2));
    }

    @Test
    public void testAddTileAtInvalidPositionAddsToEnd() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles, "spec1,spec3");
        saveSetting("spec1,spec3");

        mQSTileHost.addTile("spec2", 100);
        mMainExecutor.runAllReady();

        assertEquals(3, mQSTileHost.mTileSpecs.size());
        assertEquals("spec1", mQSTileHost.mTileSpecs.get(0));
        assertEquals("spec3", mQSTileHost.mTileSpecs.get(1));
        assertEquals("spec2", mQSTileHost.mTileSpecs.get(2));
    }

    @Test
    public void testAddTileAtEnd() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles, "spec1,spec3");
        saveSetting("spec1,spec3");

        mQSTileHost.addTile("spec2", QSTileHost.POSITION_AT_END);
        mMainExecutor.runAllReady();

        assertEquals(3, mQSTileHost.mTileSpecs.size());
        assertEquals("spec1", mQSTileHost.mTileSpecs.get(0));
        assertEquals("spec3", mQSTileHost.mTileSpecs.get(1));
        assertEquals("spec2", mQSTileHost.mTileSpecs.get(2));
    }

    @Test
    public void testNoRepeatedSpecs_customTile() {
        saveSetting(CUSTOM_TILE_SPEC);

        mQSTileHost.addTile(CUSTOM_TILE, /* end */ false);
        mMainExecutor.runAllReady();

        assertEquals(1, mQSTileHost.mTileSpecs.size());
        assertEquals(CUSTOM_TILE_SPEC, mQSTileHost.mTileSpecs.get(0));
    }

    @Test
    public void testAddedAtBeginningOnDefault_customTile() {
        saveSetting("spec1"); // seed

        mQSTileHost.addTile(CUSTOM_TILE);
        mMainExecutor.runAllReady();

        assertEquals(2, mQSTileHost.mTileSpecs.size());
        assertEquals(CUSTOM_TILE_SPEC, mQSTileHost.mTileSpecs.get(0));
    }

    @Test
    public void testAddedAtBeginning_customTile() {
        saveSetting("spec1"); // seed

        mQSTileHost.addTile(CUSTOM_TILE, /* end */ false);
        mMainExecutor.runAllReady();

        assertEquals(2, mQSTileHost.mTileSpecs.size());
        assertEquals(CUSTOM_TILE_SPEC, mQSTileHost.mTileSpecs.get(0));
    }

    @Test
    public void testAddedAtEnd_customTile() {
        saveSetting("spec1"); // seed

        mQSTileHost.addTile(CUSTOM_TILE, /* end */ true);
        mMainExecutor.runAllReady();

        assertEquals(2, mQSTileHost.mTileSpecs.size());
        assertEquals(CUSTOM_TILE_SPEC, mQSTileHost.mTileSpecs.get(1));
    }

    @Test
    public void testLoadTileSpec_repeated() {
        List<String> specs = QSTileHost.loadTileSpecs(mContext, "spec1,spec1,spec2");

        assertEquals(2, specs.size());
        assertEquals("spec1", specs.get(0));
        assertEquals("spec2", specs.get(1));
    }

    @Test
    public void testLoadTileSpec_repeatedInDefault() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles_default, "spec1,spec1");
        List<String> specs = QSTileHost.loadTileSpecs(mContext, "default");

        // Remove spurious tiles, like dbg:mem
        specs.removeIf(spec -> !"spec1".equals(spec));
        assertEquals(1, specs.size());
    }

    @Test
    public void testLoadTileSpec_repeatedDefaultAndSetting() {
        mContext.getOrCreateTestableResources()
                .addOverride(R.string.quick_settings_tiles_default, "spec1");
        List<String> specs = QSTileHost.loadTileSpecs(mContext, "default,spec1");

        // Remove spurious tiles, like dbg:mem
        specs.removeIf(spec -> !"spec1".equals(spec));
        assertEquals(1, specs.size());
    }

    @Test
    public void testNotAvailableTile_specNotNull() {
        saveSetting("na");
        verify(mQSLogger, never()).logTileDestroyed(isNull(), anyString());
    }

    @Test
    public void testCustomTileRemoved_stateDeleted() {
        mQSTileHost.changeTilesByUser(List.of(CUSTOM_TILE_SPEC), List.of());

        verify(mCustomTileStatePersister)
                .removeState(new TileServiceKey(CUSTOM_TILE, mQSTileHost.getUserId()));
    }

    @Test
    public void testRemoveTiles() {
        saveSetting("spec1,spec2,spec3");

        mQSTileHost.removeTiles(List.of("spec1", "spec2"));

        mMainExecutor.runAllReady();
        assertEquals(List.of("spec3"), mQSTileHost.mTileSpecs);
    }

    @Test
    public void testTilesRemovedInQuickSuccession() {
        saveSetting("spec1,spec2,spec3");
        mQSTileHost.removeTile("spec1");
        mQSTileHost.removeTile("spec3");

        mMainExecutor.runAllReady();
        assertEquals(List.of("spec2"), mQSTileHost.mTileSpecs);
        assertEquals("spec2", getSetting());
    }

    @Test
    public void testAddTileInMainThread() {
        saveSetting("spec1,spec2");

        mQSTileHost.addTile("spec3");
        assertEquals(List.of("spec1", "spec2"), mQSTileHost.mTileSpecs);

        mMainExecutor.runAllReady();
        assertEquals(List.of("spec1", "spec2", "spec3"), mQSTileHost.mTileSpecs);
    }

    @Test
    public void testRemoveTileInMainThread() {
        saveSetting("spec1,spec2");

        mQSTileHost.removeTile("spec1");
        assertEquals(List.of("spec1", "spec2"), mQSTileHost.mTileSpecs);

        mMainExecutor.runAllReady();
        assertEquals(List.of("spec2"), mQSTileHost.mTileSpecs);
    }

    @Test
    public void testRemoveTilesInMainThread() {
        saveSetting("spec1,spec2,spec3");

        mQSTileHost.removeTiles(List.of("spec3", "spec1"));
        assertEquals(List.of("spec1", "spec2", "spec3"), mQSTileHost.mTileSpecs);

        mMainExecutor.runAllReady();
        assertEquals(List.of("spec2"), mQSTileHost.mTileSpecs);
    }

    @Test
    public void testRemoveTileByUserInMainThread() {
        saveSetting("spec1," + CUSTOM_TILE_SPEC);

        mQSTileHost.removeTileByUser(CUSTOM_TILE);
        assertEquals(List.of("spec1", CUSTOM_TILE_SPEC), mQSTileHost.mTileSpecs);

        mMainExecutor.runAllReady();
        assertEquals(List.of("spec1"), mQSTileHost.mTileSpecs);
    }

    @Test
    public void testNonValidTileNotStoredInSettings() {
        saveSetting("spec1,not-valid");

        assertEquals(List.of("spec1"), mQSTileHost.mTileSpecs);
        assertEquals("spec1", getSetting());
    }

    @Test
    public void testNotAvailableTileNotStoredInSettings() {
        saveSetting("spec1,na");

        assertEquals(List.of("spec1"), mQSTileHost.mTileSpecs);
        assertEquals("spec1", getSetting());
    }

    private class TestQSTileHost extends QSTileHost {
        TestQSTileHost(Context context, StatusBarIconController iconController,
                QSFactory defaultFactory, Executor mainExecutor,
                PluginManager pluginManager, TunerService tunerService,
                Provider<AutoTileManager> autoTiles, DumpManager dumpManager,
                CentralSurfaces centralSurfaces, QSLogger qsLogger, UiEventLogger uiEventLogger,
                UserTracker userTracker, SecureSettings secureSettings,
                CustomTileStatePersister customTileStatePersister,
                TileServiceRequestController.Builder tileServiceRequestControllerBuilder,
                TileLifecycleManager.Factory tileLifecycleManagerFactory) {
            super(context, iconController, defaultFactory, mainExecutor, pluginManager,
                    tunerService, autoTiles, dumpManager, Optional.of(centralSurfaces), qsLogger,
                    uiEventLogger, userTracker, secureSettings, customTileStatePersister,
                    tileServiceRequestControllerBuilder, tileLifecycleManagerFactory);
        }

        @Override
        public void onPluginConnected(QSFactory plugin, Context pluginContext) {
        }

        @Override
        public void onPluginDisconnected(QSFactory plugin) {
        }
    }


    private class TestTile extends QSTileImpl<QSTile.State> {

        protected TestTile(QSHost host) {
            super(
                    host,
                    mock(Looper.class),
                    mock(Handler.class),
                    new FalsingManagerFake(),
                    mock(MetricsLogger.class),
                    mock(StatusBarStateController.class),
                    mock(ActivityStarter.class),
                    QSTileHostTest.this.mQSLogger
            );
        }

        @Override
        public State newTileState() {
            return mMockState;
        }

        @Override
        public State getState() {
            return mMockState;
        }

        @Override
        protected void handleClick(@Nullable View view) {}

        @Override
        protected void handleUpdateState(State state, Object arg) {}

        @Override
        public int getMetricsCategory() {
            return 0;
        }

        @Override
        public Intent getLongClickIntent() {
            return null;
        }

        @Override
        public CharSequence getTileLabel() {
            return null;
        }
    }

    private class TestTile1 extends TestTile {

        protected TestTile1(QSHost host) {
            super(host);
        }
    }

    private class TestTile2 extends TestTile {

        protected TestTile2(QSHost host) {
            super(host);
        }
    }

    private class TestTile3 extends TestTile {

        protected TestTile3(QSHost host) {
            super(host);
        }
    }

    private class NotAvailableTile extends TestTile {

        protected NotAvailableTile(QSHost host) {
            super(host);
        }

        @Override
        public boolean isAvailable() {
            return false;
        }
    }
}
