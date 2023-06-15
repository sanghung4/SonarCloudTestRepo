package com.reece.pickingapp.di

import android.content.Context
import androidx.room.Room
import com.reece.pickingapp.data.PickingAppDatabase
import com.reece.pickingapp.data.PickingDatabaseDao
import com.reece.pickingapp.networking.ConnectivityService
import com.reece.pickingapp.networking.PickingApi
import com.reece.pickingapp.repository.UserPreferencesImp
import com.reece.pickingapp.utils.ActivityService
import com.reece.pickingapp.utils.StringConverter
import com.reece.pickingapp.view.ui.Utils
import com.reece.pickingapp.wrappers.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {
    @Singleton
    @Provides
    fun provideWebService(
        activityService: ActivityService,
        userPreferences: UserPreferencesImp,
        oktaWebAuthWrapper: OktaWebAuthWrapper
    ) = PickingApi(
        activityService,
        userPreferences,
        oktaWebAuthWrapper,
    )

    @Singleton
    @Provides
    fun provideConnectivityService(
        activityService: ActivityService
    ) = ConnectivityService(activityService)

    @Singleton
    @Provides
    fun provideUtils() = Utils()

    @Singleton
    @Provides
    fun provideActivityService() = ActivityService()

    @Singleton
    @Provides
    fun provideOIDCConfigWrapper() = OIDCConfigWrapper()

    @Singleton
    @Provides
    fun provideOktaWebAuthWrapper() = OktaWebAuthWrapper()

    @Singleton
    @Provides
    fun provideStringConverter() = StringConverter

    @Singleton
    @Provides
    fun providesProductsAdapterWrapper() = ProductsAdapterWrapper()

    @Singleton
    @Provides
    fun providesErrorListAdapterWrapper() = ErrorListAdapterWrapper()

    @Singleton
    @Provides
    fun providesOrdersAdapterWrapper() = OrdersAdapterWrapper()

    @Singleton
    @Provides
    fun providesMaterialAlertDialogBuilderWrapper() = MaterialAlertDialogBuilderWrapper()

    @Singleton
    @Provides
    fun providesAuthenticationPayloadWrapper() = AuthenticationPayloadWrapper()

    @Singleton
    @Provides
    fun providesPickFromOtherLocationsAdapterWrapper() = PickFromOtherLocationsAdapterWrapper()

    @Singleton
    @Provides
    fun providesReportAdapterWrapper() = ReportAdapterWrapper()

    @Singleton
    @Provides
    fun provideEclipseLoginDAO(pickingDatabase: PickingAppDatabase): PickingDatabaseDao
    = pickingDatabase.eclipseLoginDAO()

    @Singleton
    @Provides
    fun provideAppDataBase(@ApplicationContext context: Context): PickingAppDatabase
    = Room.databaseBuilder(
        context,
        PickingAppDatabase::class.java,
        "pickingapp_db"
    ).fallbackToDestructiveMigration().build()
}