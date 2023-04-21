package com.example.myapplication.database

class Activity(
    var activity: String?,
    var name: String?,
    var age: Int,
    var userId: String?,
    var text: String?,
    var thumbnail: Int,
    var date: String?,
    var latitude: Double,
    var longitude: Double,
    var sex: String?
) {
    var distance = 0.0
    var daysTo: Long = 0

    fun setBirthday(userId: String?) {
        this.userId = userId
    }
}