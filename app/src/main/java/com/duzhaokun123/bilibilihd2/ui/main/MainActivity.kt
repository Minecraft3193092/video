package com.duzhaokun123.bilibilihd2.ui.main

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.duzhaokun123.bilibilihd2.R
import com.duzhaokun123.bilibilihd2.bases.BaseActivity
import com.duzhaokun123.bilibilihd2.databinding.ActivityMainBinding
import com.duzhaokun123.bilibilihd2.navigation.setupWithNavController
import com.duzhaokun123.bilibilihd2.ui.TestActivity
import com.duzhaokun123.bilibilihd2.ui.settings.SettingsActivity
import com.duzhaokun123.bilibilihd2.utils.*
import com.duzhaokun123.bilibilihd2.utils.ImageViewUtil.setBiliLevel
import io.github.duzhaokun123.bilibili.api.BilibiliService

class MainActivity : BaseActivity<ActivityMainBinding>(
    R.layout.activity_main,
    Config.NO_TOOL_BAR,
    Config.LAYOUT_MATCH_HORI
) {


    private lateinit var navController: NavController
    private lateinit var headerView: View

    override fun findViews() {
        headerView = baseBinding.nv?.getHeaderView(0) ?: baseBinding.nrv?.headerView!!
    }

    override fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fcv) as NavHostFragment
        navController = navHostFragment.navController
        baseBinding.nv?.setupWithNavController(navController)
        baseBinding.nrv?.setupWithNavController(navController)
        val appBarConfiguration =
            AppBarConfiguration(navController.graph, baseBinding.dl)
        baseBinding.tb.setupWithNavController(navController, appBarConfiguration)
        (baseBinding.nv?.menu ?: baseBinding.nrv?.menu)!!.apply {
            findItem(R.id.item_settings)
                .setOnMenuItemClickListener {
                    startActivity<SettingsActivity>()
                    baseBinding.dl?.close()
                    true
                }
            findItem(R.id.item_test)
                .setOnMenuItemClickListener {
                    startActivity<TestActivity>()
                    baseBinding.dl?.close()
                    true
                }
        }
        baseBinding.dl?.addDrawerListener(object : DrawerLayout.SimpleDrawerListener() {
            override fun onDrawerOpened(drawerView: View) {
                reloadMyInfo()
            }
        })
        headerView.setOnClickListener {
            if (bilibiliClient.isLogin)
                BrowserUtil.openInApp(this, "bilibili://space/${bilibiliClient.loginResponse!!.userId}")
        }
    }

    override fun initData() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = navController.currentDestination?.label
    }

    override fun onResume() {
        super.onResume()
        reloadMyInfo()
    }

    override fun onBackPressed() {
        if (baseBinding.dl != null && baseBinding.dl!!.isOpen)
            baseBinding.dl!!.close()
        else
            super.onBackPressed()
    }

    override fun initActionBar() = baseBinding.tb

    override fun onApplyWindowInsetsCompat(insets: WindowInsetsCompat) {
        super.onApplyWindowInsetsCompat(insets)
        insets.maxSystemBarsDisplayCutout.let {
            baseBinding.abl.updatePadding(left = it.left, right = it.right, top = it.top)
            baseBinding.fcv.updatePadding(left = it.left, right = it.right)
            (baseBinding.nv ?: baseBinding.nrv)?.updatePadding(left = it.left, top = it.top)
        }
    }

    @SuppressLint("SetTextI18n")
    fun reloadMyInfo() {
        if (bilibiliClient.isLogin)
            runIOCatchingResultRunMain(this, { BilibiliService.userApi.nav().await()}) { nav ->
                headerView.findViewById<TextView>(R.id.tv_name)?.apply {
                    text = nav.data.uname
                    setTextColor(getColorCompat(if (nav.data.vipStatus == 0) R.color.textColor else R.color.biliPink))
                }
                headerView.findViewById<TextView>(R.id.tv_bBi)?.text =
                    "B币: ${nav.data.wallet.bcoinBalance}"
                runIOCatchingResultRunMain(this, { BilibiliService.accountApi.getCoin().await() }) {
                    headerView.findViewById<TextView>(R.id.tv_coins)?.text =
                        "硬币: ${it.data.money}"
                }

                headerView.findViewById<ImageView>(R.id.iv_level)?.setBiliLevel(nav.data.levelInfo.currentLevel)
                glideSafeLoadInto(nav.data.face, headerView.findViewById(R.id.civ_face))
            }
        else {
            headerView.findViewById<TextView>(R.id.tv_name)?.text = "未登录"
            headerView.findViewById<TextView>(R.id.tv_bBi)?.text = "B币: --"
            headerView.findViewById<TextView>(R.id.tv_coins)?.text = "硬币: --"
            headerView.findViewById<ImageView>(R.id.iv_level)?.setBiliLevel(0)
            headerView.findViewById<ImageView>(R.id.civ_face).setImageDrawable(null)
        }
        if (baseBinding.nrv != null)
            headerView.findViewById<View>(R.id.civ_face).visibility =
                if (bilibiliClient.isLogin) View.VISIBLE else View.GONE
    }
}