package com.carousell.caronews.di.module;

import com.carousell.caronews.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {
    @ContributesAndroidInjector()
    abstract MainActivity bindMainActivity();
}
