package com.hrishi.tempmail

import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrishi.tempmail.ApiInstance.api
import com.hrishi.tempmail.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.lang.Exception

const val TAG = "MainActivity"
var email = "uwjofp@wwjmp.com"
var mailLogin = "uwjofp"
var mailDomain = "wwjmp.com"

class MainActivity : AppCompatActivity(), inbox_rvAdapter.itemClick {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val api = ApiInstance.api
        binding.rvIndox.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launchWhenCreated {
            val IdList = api.generateMailId()
            val mailId = IdList.body()!![0]
            email = mailId
            val split = mailId.split("@")
            mailLogin = split[0]
            mailDomain = split[1]
            binding.tvMailId.text = mailId

            copyToClipboard(mailId, this@MainActivity)
        }

        binding.btnCopyToClipboard.setOnClickListener { copyToClipboard(email, this) }


        binding.btnRefresh.setOnClickListener {refreshInbox()}

        binding.swipeTorRefresh.setOnRefreshListener {
            refreshInbox()
            binding.swipeTorRefresh.isRefreshing = false
        }
    }


    //Inbox item click listner
    override fun onItemClick(id: Int) {
        lifecycleScope.launchWhenCreated {

            val mailDetails = api.getMailDetails(mailLogin, mailDomain, id.toString())

            val alertDialog = AlertDialog.Builder(this@MainActivity)
                .setTitle(mailDetails.body()!!.from)
                .setMessage(mailDetails.body()!!.textBody)
                .setPositiveButton("Ok") { _, _ -> }
                .create()

            alertDialog.show()
        }
    }

    //Copies data to Clipboard
    private fun copyToClipboard(data: String, context: Context) {
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Mail ID", data)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
    }

    private fun refreshInbox(){
        lifecycleScope.launchWhenCreated {

            try{
                val mailList = api.getallMail(mailLogin, mailDomain)
                val data = mailList.body()!!
                val adapter = inbox_rvAdapter(data, this@MainActivity)
                binding.rvIndox.adapter = adapter
                adapter.notifyDataSetChanged()
            }catch (e : HttpException){
                Toast.makeText(this@MainActivity, "Http Exception ${e.message()}", Toast.LENGTH_LONG).show()
            }catch (e : Exception){
                Toast.makeText(this@MainActivity, "Error ${e.message}", Toast.LENGTH_LONG).show()
            }

        }
    }
}