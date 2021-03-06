package com.duzhaokun123.bilibilihd2.ui.settings

import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.bases.BaseFragment
import com.duzhaokun123.bilibilihd2.databinding.FragmentUsersBinding
import com.duzhaokun123.bilibilihd2.model.UserModel
import com.duzhaokun123.bilibilihd2.ui.login.LoginActivity
import com.duzhaokun123.bilibilihd2.utils.*
import com.duzhaokun123.generated.Settings
import com.github.salomonbrys.kotson.fromJson
import io.github.duzhaokun123.bilibili.api.BilibiliService

@Suppress("UNUSED")
class UsersFragment : BaseFragment<FragmentUsersBinding>(R.layout.fragment_users) {
    private val model by activityViewModels<SettingsActivity.Model>()

    private lateinit var exportLoginResponse: ActivityResultLauncher<String>
    private lateinit var importLoginResponse: ActivityResultLauncher<Array<String>>

    override fun initView() {
        baseBinding.btnAdd.setOnClickListener {
            PopupMenu(requireContext(), baseBinding.btnAdd).apply {
                menuInflater.inflate(R.menu.user_add_menu, menu)
                setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.item_login -> {
                            requireContext().startActivity<LoginActivity>()
                        }
                        R.id.item_import -> {
                            importLoginResponse.launch(arrayOf("*/*"))
                        }
                    }
                    true
                }
            }.show()
        }
        baseBinding.btnClear.setOnClickListener {
            selectUid(0)
        }
        baseBinding.btnRefresh.setOnClickListener {
            if (bilibiliClient.isLogin.not()) {
                TipUtil.showTip(context, "?????????")
            } else {
                runIOCatchingResultRunMain(context, { bilibiliClient.refresh() }) { re ->
                    if (re != null) {
                        UsersMap.put(re)
                        UsersMap.save()
                        selectUid(re.userId)
                    } else {
                        TipUtil.showTip(context, "????????????")
                    }
                }
            }
        }
        baseBinding.rv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        baseBinding.rv.adapter = UsersAdapter(requireBaseActivity(), UsersMap.users.toMutableList())
    }

    override fun initData() {
        model.selectedUid.observe(this) { uid ->
            if (uid != 0L)
                selectUid(uid)
        }
        model.loginResponseToExport.observe(this) {
            if (it != null) exportLoginResponse.launch("loginResponse.json")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exportLoginResponse =
            registerForActivityResult(ActivityResultContracts.CreateDocument()) { uri ->
                if (uri == null) return@registerForActivityResult
                requireContext().contentResolver.openOutputStream(uri)?.writer()?.use { out ->
                    try {
                        out.write(gson.toJson(model.loginResponseToExport.value))
                        TipUtil.showTip(context, "????????????")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        TipUtil.showTip(context, e.message)
                    } finally {
                        model.loginResponseToExport.value = null
                    }
                }
            }
        importLoginResponse =
            registerForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
                if (uri == null) return@registerForActivityResult
                requireContext().contentResolver.openInputStream(uri)?.reader()?.use { `in` ->
                    try {
                        UsersMap.put(gson.fromJson(`in`))
                        UsersMap.save()
                        baseBinding.rv.adapter =
                            UsersAdapter(requireBaseActivity(), UsersMap.users.toMutableList())
                        TipUtil.showTip(context, "????????????")
                    } catch (e: Exception) {
                        e.printStackTrace()
                        TipUtil.showTip(context, e.message)
                    }
                }
            }
    }

    override fun onResume() {
        super.onResume()
        model.subtitle.value = getText(R.string.users)
        reloadSelectedUser()
    }

    private fun selectUid(uid: Long) {
        model.selectedUid.value = 0
        Settings.selectedUid = uid
        application.reinitBilibiliClient(uid)
        reloadSelectedUser()
    }

    private fun reloadSelectedUser() {
        if (BilibiliService.loggedIn) {
            val cookies = BilibiliService.cookies!!
            baseBinding.model =
                UserModel(
                    "Loading...",
                    cookies.uid,
                    null,
                    "UID: ${cookies.uid}\n?????????: ${cookies.expires}"
                )
            runIOCatchingResultRunMain(
                context, { BilibiliService.userApi.nav().await() })
            { nav ->
                baseBinding.model = UserModel(
                    nav.data.uname,
                    cookies.uid,
                    nav.data.face,
                    "UID: ${cookies.uid}\n?????????: ${cookies.expires}"
                )
            }
        } else
            baseBinding.model = UserModel("?????????", 0, null, "")
    }
}