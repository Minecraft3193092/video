package com.duzhaokun123.bilibilihd2.utils

import com.duzhaokun123.generated.Settings
import com.github.salomonbrys.kotson.fromJson
import com.hiczp.bilibili.api.passport.model.LoginResponse

object UsersMap : HashMap<Long, LoginResponse>() {
    fun reload() {
        clear()
        for (v in Settings.users!!) {
            try {
                gson.fromJson<LoginResponse>(v)
            } catch (e: Exception) {
                TipUtil.showToast(e.message)
                continue
            }.let {
                put(it.userId, it)
            }
        }
    }

    fun put(loginResponse: LoginResponse) = put(loginResponse.userId, loginResponse)

    fun save() {
        val usersSet = mutableSetOf<String>()
        users.forEach { u ->
            usersSet.add(gson.toJson(u))
        }
        Settings.users = usersSet
    }

    val users
        get() = values.toSet()
}