package com.example.merquriassessment.model

import com.google.gson.Gson
import org.json.JSONArray

class DataParser {
    fun json2Contacts(jsonString: String?): ArrayList<Contacts> {
        val gson = Gson()
        val data = JSONArray(jsonString)
        val contactsList = ArrayList<Contacts>()
        try {
            for (i in 0 until data.length()) {
                val contacts: Contacts =
                    gson.fromJson(data.optJSONObject(i).toString(), Contacts::class.java)
                contactsList.add(contacts)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return contactsList
    }
}
