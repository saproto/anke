package com.proto.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Dennis on 20/03/2018.
 */

public class User implements Serializable {

    @SerializedName("name")
    private String name;

    @SerializedName("calling_name")
    private String callingName;

    @SerializedName("email")
    private String email;

    private String birthdate;

    private String phone;

    private String diet;

    private String website;

    @SerializedName("phone_visible")
    private int phoneVisible;

    @SerializedName("utwente_username")
    private String utwenteUsername;

    @SerializedName("address_visible")
    private int addressVisible;

    @SerializedName("receive_sms")
    private int receiveSMS;

    @SerializedName("keep_protube_history")
    private int protubeHistory;

    @SerializedName("pref_calendar_alarm")
    private String preferenceCalendarAlarm;

    @SerializedName("pref_calendar_relevant_only")
    private int preferenceCalendarRelevantOnly;

    @SerializedName("edu_username")
    private String eduUsername;

    @SerializedName("utwente_department")
    private String utwenteDepartment;

    @SerializedName("did_study_create")
    private int didCreate;

    @SerializedName("signed_nda")
    private int signedNDA;


    public String getName() {
        return name;
    }

    public String getCallingName() {
        return callingName;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getPhone() {
        return phone;
    }

    public String getDiet() {
        return diet;
    }

    public String getWebsite() {
        return website;
    }

    public int getPhoneVisible() {
        return phoneVisible;
    }

    public String getUtwenteUsername() {
        return utwenteUsername;
    }

    public int getAddressVisible() {
        return addressVisible;
    }

    public int getReceiveSMS() {
        return receiveSMS;
    }

    public int getProtubeHistory() {
        return protubeHistory;
    }

    public String getPreferenceCalendarAlarm() {
        return preferenceCalendarAlarm;
    }

    public int getPreferenceCalendarRelevantOnly() {
        return preferenceCalendarRelevantOnly;
    }

    public String getEduUsername() {
        return eduUsername;
    }

    public String getUtwenteDepartment() {
        return utwenteDepartment;
    }

    public int getDidCreate() {
        return didCreate;
    }

    public int getSignedNDA() {
        return signedNDA;
    }

    public String getEmail() {
        return email;
    }
}
