package com.defey.labpuzzles.di

import com.defey.labpuzzles.repository.StorageRepository
import com.defey.labpuzzles.repository.StorageRepositoryImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val storageModule: Module
    get() = module {
        single<StorageRepository> { StorageRepositoryImpl(get()) }
    }