package com.div.home.model;

import com.div.home.util.AppContext;
import com.div.home.util.Utils;

import org.parceler.Parcel;

/**
 * Created by Nirav Mandani on 20-12-2019.
 * Followal Solutions
 */
@Parcel
public class Appliance {

    String id;
    String displayName;
    int status;
    int icon;

    public Appliance() {
    }

    public Appliance(String displayName, int status, int icon) {
        this.id = Utils.getCurrentSSID(AppContext.getInstance().getContext());
        this.displayName = displayName;
        this.status = status;
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
