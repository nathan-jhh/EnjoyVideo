package com.techme.jetpack.pages.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.techme.jetpack.databinding.ActivityLayoutLoginBinding
import com.techme.jetpack.ext.invokeViewBinding
import com.techme.jetpack.http.ApiService
import com.tencent.connect.UserInfo
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var tencent: Tencent
    private val viewBinding: ActivityLayoutLoginBinding by invokeViewBinding()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.actionClose.setOnClickListener { finish() }
        viewBinding.actionLogin.setOnClickListener { login() }

        tencent = Tencent.createInstance("102047280", applicationContext)
    }

    private fun login() {
        tencent.login(this, "all", loginListener)
    }

    private val loginListener = object : LoginListener() {
        override fun onComplete(ret: Any) {
            val response = ret as JSONObject
            val openid = response.getString("openid")
            val accessToken = response.getString("access_token")
            val expiresIn = response.getLong("expires_in")
            tencent.openId = openid
            tencent.setAccessToken(accessToken, expiresIn.toString())

            getUserInfo()
        }
    }

    private fun getUserInfo() {
        val userInfo = UserInfo(applicationContext, tencent.qqToken)
        userInfo.getUserInfo(object : LoginListener() {
            override fun onComplete(any: Any) {
                super.onComplete(any)
                val response = any as JSONObject
                val nickname = response.optString("nickname")
                val avatar = response.optString("figureurl_2")
                save(nickname, avatar)
            }
        })
    }

    private fun save(nickname: String, avatar: String) {
        lifecycleScope.launch {
            val apiResult = ApiService.getService()
                .saveUser(nickname, avatar, tencent.openId, tencent.expiresIn)
            if (apiResult.success && apiResult.body != null) {
                UserManager.save(apiResult.body!!)
                finish()
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private open inner class LoginListener : IUiListener {
        override fun onComplete(p0: Any) {

        }

        override fun onError(err: UiError) {
            Toast.makeText(
                this@LoginActivity,
                "登录失败:reason${err.errorMessage}",
                Toast.LENGTH_SHORT
            ).show()
        }

        override fun onCancel() {
            Toast.makeText(this@LoginActivity, "登录失败", Toast.LENGTH_SHORT).show()
        }

        override fun onWarning(p0: Int) {

        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener)
        }
    }
}