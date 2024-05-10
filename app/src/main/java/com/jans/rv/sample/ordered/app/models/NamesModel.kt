package com.jans.rv.sample.ordered.app.models


import com.google.gson.annotations.SerializedName
import com.google.gson.annotations.Expose

class NamesModel : ArrayList<NamesModel.NamesModelItem>(){
    data class NamesModelItem(
        @SerializedName("about")
        @Expose
        var about: String = "",
        @SerializedName("address")
        @Expose
        var address: String = "",
        @SerializedName("age")
        @Expose
        var age: Int = 0,
        @SerializedName("balance")
        @Expose
        var balance: String = "",
        @SerializedName("company")
        @Expose
        var company: String = "",
        @SerializedName("email")
        @Expose
        var email: String = "",
        @SerializedName("eyeColor")
        @Expose
        var eyeColor: String = "",
        @SerializedName("favoriteFruit")
        @Expose
        var favoriteFruit: String = "",
        @SerializedName("firstname")
        @Expose
        var firstname: String = "",
        @SerializedName("friends")
        @Expose
        var friends: List<Friend> = listOf(),
        @SerializedName("gender")
        @Expose
        var gender: String = "",
        @SerializedName("greeting")
        @Expose
        var greeting: String = "",
        @SerializedName("guid")
        @Expose
        var guid: String = "",
        @SerializedName("_id")
        @Expose
        var id: String = "",
        @SerializedName("index")
        @Expose
        var index: Int = 0,
        @SerializedName("isActive")
        @Expose
        var isActive: Boolean = false,
        @SerializedName("lastname")
        @Expose
        var lastname: String = "",
        @SerializedName("latitude")
        @Expose
        var latitude: Double = 0.0,
        @SerializedName("longitude")
        @Expose
        var longitude: Double = 0.0,
        @SerializedName("phone")
        @Expose
        var phone: String = "",
        @SerializedName("picture")
        @Expose
        var picture: String = "",
        @SerializedName("registered")
        @Expose
        var registered: String = "",
        @SerializedName("tags")
        @Expose
        var tags: List<String> = listOf()
    ) {
        data class Friend(
            @SerializedName("id")
            @Expose
            var id: Int = 0,
            @SerializedName("name")
            @Expose
            var name: String = ""
        )
    }
}