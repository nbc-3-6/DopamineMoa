package com.example.dopaminemoa

class Const {
    companion object { // 상수들은 어디서든지 접근 가능하고 변경되지 않아야 하므로, 정적 멤버로 선언
        // retrofit
        const val API_KEY = "AIzaSyDr76uajPQT6Yf9-bfZuADXsyx424bzwLc"

        // sharedPreference
        const val PREFERENCE_NAME = "pref"
        const val LIKED_ITEMS = "liked_items"
    }
}