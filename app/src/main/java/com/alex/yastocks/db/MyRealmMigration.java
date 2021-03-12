package com.alex.yastocks.db;

import androidx.annotation.Nullable;

import io.realm.DynamicRealm;
import io.realm.FieldAttribute;
import io.realm.RealmMigration;
import io.realm.RealmSchema;

public class MyRealmMigration implements RealmMigration {
    @Override
    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
        // DynamicRealm exposes an editable schema
        RealmSchema schema = realm.getSchema();

        // Migrate to version 1: Add a new class StockSummary.
        if (oldVersion == 0) {
            schema.create("StockSummary")
                    .addField("ticker", String.class, FieldAttribute.PRIMARY_KEY)
                    .addField("longBusinessSummary", String.class)
                    .addField("sector", String.class)
                    .addField("country", String.class);
            oldVersion++;
        }

        // Migrate to version 2: Add a new class StockChart.
        if (oldVersion == 1) {
            schema.create("ChartPoint")
                    .addField("ticker", String.class, FieldAttribute.PRIMARY_KEY)//F
                    .addField("interval", String.class)                         //15m
                    .addField("seconds", Long.class)                            //"612362600"
                    .addField("open", Float.class)                             //10.86
                    .addField("high", Float.class)                             //11.06
                    .addField("low", Float.class)                              //10.84
                    .addField("close", Float.class);                           //11.02
            oldVersion++;
        }
    }

    @Override
    public int hashCode() {
        return 37;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return true;
    }
}
