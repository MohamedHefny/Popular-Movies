package com.example.mohamedhefny.popularmovie_new_tray.DataBase;


import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by Mohamed Hefny on 23/04/2016.
 */
    @SimpleSQLConfig(
            name = "MovieProvider",
            authority = "com.example.mohamedhefny.popularmovie_new_tray.provider.authority",
            database = "test.db",
            version = 1)
    public class MyProviderConfig implements ProviderConfig {
        @Override
        public UpgradeScript[] getUpdateScripts() {
            return new UpgradeScript[0];
        }
    }
