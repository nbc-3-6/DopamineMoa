package com.example.dopaminemoa.presentation.util

import androidx.room.TypeConverter
import java.net.URL

object UtilityUrlConverter {

    /**
     * string을 URL로 변환하는 함수입니다.
     */
    @TypeConverter
    fun fromString(value: String?): URL? {
        return value?.let { URL(it) }
    }
}