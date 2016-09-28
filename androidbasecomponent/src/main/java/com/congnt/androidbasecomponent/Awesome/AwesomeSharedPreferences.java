package com.congnt.androidbasecomponent.Awesome;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * Created by NGUYEN TRUNG CONG on 09/13/2016
 */
public abstract class AwesomeSharedPreferences {
    protected final SharedPreferences pref;
    protected final SharedPreferences.Editor editor;
    protected Context context;

    public AwesomeSharedPreferences(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
        editor = pref.edit();
    }

    public abstract class CollectionSharedPreferences<T> extends SingleSharedPreferences<T> {

        public abstract void put(Object id, Object value);

        public abstract Object get(Object id);

        public abstract boolean has(Object id);

    }

    protected abstract class SingleSharedPreferences<T> {
        protected abstract String ID();

        public void save(T t) {
            Type type = new TypeToken<T>() {
            }.getType();
            editor.putString(ID(), new Gson().toJson(t, type));
            editor.commit();
        }

        public T load(T defaultT) {
            Type type = new TypeToken<T>() {
            }.getType();
            String str = pref.getString(ID(), "");
            return str.equals("") ? defaultT : (T) new Gson().fromJson(str, type);
        }
    }

}
