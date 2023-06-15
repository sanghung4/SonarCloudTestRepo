package com.reece.pickingapp.di

import com.reece.pickingapp.repository.PickingRepository
import com.reece.pickingapp.repository.PickingRepositoryImpl
import com.reece.pickingapp.repository.UserPreferences
import com.reece.pickingapp.repository.UserPreferencesImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {

    @Binds
    @ViewModelScoped
    abstract fun bindRepository(repo: PickingRepositoryImpl): PickingRepository

    @Binds
    @ViewModelScoped
    abstract fun bindUserPreferences(userPreferences: UserPreferencesImp): UserPreferences
}