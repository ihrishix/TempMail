package com.hrishi.tempmail

import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.hrishi.tempmail.ApiInstance.api
import com.hrishi.tempmail.databinding.ActivityMainBinding

const val TAG = "MainActivity"
var email = "demo@domain.com"
var mailLogin = "demo"
var mailDomain = "domain"
class MainActivity : AppCompatActivity(), inbox_rvAdapter.itemClick {

    lateinit var binding : ActivityMainBinding

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

        binding.btnRefresh.setOnClickListener {
            lifecycleScope.launchWhenCreated {
                val mailList = api.getallMail(mailLogin, mailDomain)
                val data = mailList.body()!!
                val adapter =  inbox_rvAdapter(data, this@MainActivity)
                binding.rvIndox.adapter = adapter
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onItemClick(id: Int) {
        lifecycleScope.launchWhenCreated {

            val mailDetails = api.getMailDetails(mailLogin, mailDomain, id.toString())
            Log.d(TAG, "onItemClick: ${mailDetails.body()!!.size}")
//
//            val alertDialog = AlertDialog.Builder(this@MainActivity)
//                .setTitle(mailDetails.body()!![0].from)
//                .setMessage(mailDetails.body()!![0].textBody)
//                .setPositiveButton("Ok"){_, _ -> }
//                .create()
//
//            alertDialog.show()
        }
    }

    fun copyToClipboard(data : String, context : Context){
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Mail ID", data)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT).show()
    }
}