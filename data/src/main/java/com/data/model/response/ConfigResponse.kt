package com.data.model.response

import com.google.gson.annotations.SerializedName

data class ConfigResponse(
        @SerializedName("dictionary_updated") var dictionaryUpdated: Boolean
)
