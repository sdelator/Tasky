package com.example.tasky.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatedClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatedApi
