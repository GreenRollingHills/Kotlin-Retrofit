package site.whiteoffice.todoist.ui.Welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.welcome.view.*
import site.whiteoffice.todoist.BuildConfig
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.MainActivity



class Welcome : Fragment() {

    val data by viewModels<WelcomeViewModel>()

    private var todoistCode:String? = null

    companion object {

        private val TAG = Welcome::class.java.simpleName
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        arguments?.let {
            if (savedInstanceState == null) {

                data.setLoginStatus(it.getInt(MainActivity.welcomeLoginStatusKey, WelcomeViewModel.LoginStatus.NeedCode.raw))

                val code = it.getString(MainActivity.todoistActionCodeKey)
                if (code != null) {
                    todoistCode = code

                } else {
                    data.setLoginStatus(WelcomeViewModel.LoginStatus.NeedCode.raw)
                }
            }
        }

        println("arguments : $arguments")


    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.welcome, container, false)

        return rootView

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("todoistCode : $todoistCode")

        view.loginButton.setOnClickListener {
            // TODO : Improve state storage per app requirements

            if (data.loginStatus.value == WelcomeViewModel.LoginStatus.NeedCode) {
                val state = BuildConfig.TODOIST_STATE_SECRET
                val url = "https://todoist.com/oauth/authorize?client_id=e87862c9e3c64471878820dda9fa5aaa&scope=data:read_write,data:delete,task:add&state=$state"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } else if (data.loginStatus.value == WelcomeViewModel.LoginStatus.NeedTokenButton) {
                todoistCode?.let { code ->
                    view.pBar.visibility = View.VISIBLE
                    data.getToken(code)
                }
            }


        }

        data.loginStatus.observe(viewLifecycleOwner, Observer { newState ->
            println("newState : $newState")

            when (newState) {
                WelcomeViewModel.LoginStatus.Default -> {
                    view.loginButton.visibility = View.INVISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE
                }
                WelcomeViewModel.LoginStatus.NeedCode -> {
                    view.loginButton.visibility = View.VISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE

                }
                WelcomeViewModel.LoginStatus.NeedTokenActive -> {
                    view.loginButton.visibility = View.INVISIBLE
                    view.welcomeMessage.text = "LOADING ..."
                    view.pBar.visibility = View.VISIBLE

                    val code = todoistCode
                    if (code != null) {
                        data.getToken(code)
                    }


                }

                WelcomeViewModel.LoginStatus.NeedTokenButton -> {
                    view.loginButton.visibility = View.VISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE
                }
                WelcomeViewModel.LoginStatus.HaveToken -> {
                    view.loginButton.visibility = View.INVISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE

                    val action =
                        WelcomeDirections.actionWelcomeToImageDump()
                    view.findNavController().navigate(action)
                }
            }
        })

        data.errorMessageState.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                view.pBar.visibility = View.INVISIBLE
                val toast = Toast.makeText(context, "There was an error", Toast.LENGTH_SHORT)
                toast.show()
                data.setErrorMessageState(false)
            }
        })




    }


}