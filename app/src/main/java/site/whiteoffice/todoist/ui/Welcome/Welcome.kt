package site.whiteoffice.todoist.ui.Welcome

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.welcome.view.*
import kotlinx.coroutines.launch
import site.whiteoffice.todoist.R
import site.whiteoffice.todoist.ui.MainActivity

//import site.whiteoffice.todoist.ui.WelcomeDirections

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Welcome.newInstance] factory method to
 * create an instance of this fragment.
 */
class Welcome : Fragment() {
    // TODO: Rename and change types of parameters
    //private var param1: String? = null
    //private var param2: String? = null
    val data by viewModels<WelcomeViewModel>()

    private var todoistAction:Int = TodoistAction.getCode.raw
    private var todoistCode:String? = null

    //private val args: WelcomeArgs by navArgs()
    //private val args by navArgs<WelcomeArgs>()

    enum class TodoistAction (val raw: Int) {
        getCode (0),
        getToken (1),
        start(2)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //param1 = it.getString(ARG_PARAM1)
            //param2 = it.getString(ARG_PARAM2)
            todoistAction = it.getInt(MainActivity.todoistActionKey)
            //todoistAction = TodoistAction.getToken.raw

            todoistCode = it.getString(MainActivity.todoistActionCodeKey)
        }

        println("arguments : $arguments")
        //println("args : $args")

        //data = ViewModelProvider(this).get(WelcomeViewModel::class.jav
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val rootView = inflater.inflate(R.layout.welcome, container, false)


        return rootView

        //return inflater.inflate(R.layout.fragment_welcome, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        println("todoistAction : $todoistAction")
        println("todoistCode : $todoistCode")

        //        data.getLoginStatus().observe(viewLifecycleOwner, Observer { newState ->
        data.loginStatus.observe(viewLifecycleOwner, Observer { newState ->
            println("newState : $newState")
            /*when (newState) {
                WelcomeViewModel.LoginStatus.InComplete -> println("start spinner")
                WelcomeViewModel.LoginStatus.ProceedYes -> {
                    //val action = WelcomeDirections.actionWelcomeToProjectList()
                    val action =
                        WelcomeDirections.actionWelcomeToImageDump()
                    view.findNavController().navigate(action)
                }
            }*/
            when (newState) {
                WelcomeViewModel.LoginStatus.Default -> {
                    //hide login button
                    //no message
                    //spinner stop
                    view.loginButton.visibility = View.INVISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE
                }
                WelcomeViewModel.LoginStatus.NeedCode -> {
                    //display login button
                    //no message
                    //spinner start
                    view.loginButton.visibility = View.VISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE

                }
                WelcomeViewModel.LoginStatus.NeedToken -> {
                    //hide login button
                    //display 'loading...'
                    //spinner start
                    view.loginButton.visibility = View.INVISIBLE
                    view.welcomeMessage.text = "LOADING ..."
                    view.pBar.visibility = View.VISIBLE
                }
                WelcomeViewModel.LoginStatus.HaveToken -> {
                    //hide login button
                    //no message
                    //spinner stop
                    view.loginButton.visibility = View.INVISIBLE
                    view.welcomeMessage.text = ""
                    view.pBar.visibility = View.INVISIBLE

                    val action =
                        WelcomeDirections.actionWelcomeToImageDump()
                    view.findNavController().navigate(action)
                }
            }
        })


        view.loginButton.setOnClickListener {
            view.pBar.visibility = View.VISIBLE
            val url = "https://todoist.com/oauth/authorize?client_id=e87862c9e3c64471878820dda9fa5aaa&scope=data:read_write,data:delete,task:add&state=${MainActivity.secretString}"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        when (todoistAction) {
            TodoistAction.getCode.raw -> {
                println("do nothing")

                data.setLoginStatusToNeedCode()
                //will be user initiated process

                //val url = "https://todoist.com/oauth/authorize?client_id=e87862c9e3c64471878820dda9fa5aaa&scope=data:read_write,data:delete,task:add&state=${MainActivity.secretString}"
                //val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                //startActivity(intent)
            }

            TodoistAction.getToken.raw -> {
                println("get token action")
                val code = todoistCode
                if (code != null) {
                    data.getToken(code)
                }
            }

            TodoistAction.start.raw -> {
                println("GO!")
                //data.setLoginStatusToProceed()
                data.setLoginStatusToHaveToken()
            }
        }




        lifecycleScope.launch {
            //data.deleteAll()
            //data.loadProjects()
        }



        /*if (todoistAction == TodoistAction.getToken.raw) {
            val code = todoistCode
            if (code != null) {
                data.getToken(code)
            }
            //data.testMethodToSetLoginStatus()

        } else if (todoistAction == TodoistAction.start.raw) {

        } else {
            val url = "https://todoist.com/oauth/authorize?client_id=e87862c9e3c64471878820dda9fa5aaa&scope=data:read_write,data:delete,task:add&state=${MainActivity.secretString}"
            //val url = "https://todoist.com/oauth/authorize?client_id=asdf&scope=data:read,data:delete&state=${MainActivity.secretString}"
            //val url = "https://todoist.com/oauth/authorize?client_id=e87862c9e3c64471878820dda9fa5aaa&scope=data:read,data:delete&state=anotherSecret"

            //val url = "https://google.com/"

            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }*/


    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Welcome.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Welcome().apply {
                arguments = Bundle().apply {
                    //putString(ARG_PARAM1, param1)
                    //putString(ARG_PARAM2, param2)
                }
            }
    }
}