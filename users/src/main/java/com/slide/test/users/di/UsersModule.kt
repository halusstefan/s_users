package com.slide.test.users.di

import com.slide.test.users.common.UserAvatarFactory
import com.slide.test.users.common.UserAvatarFactoryImplementation
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * Created by Stefan Halus on 12 Jun 2025
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class UsersModule {

    @Binds
    internal abstract fun bindsUserAvatarFactory(
        implementation: UserAvatarFactoryImplementation
    ): UserAvatarFactory
}