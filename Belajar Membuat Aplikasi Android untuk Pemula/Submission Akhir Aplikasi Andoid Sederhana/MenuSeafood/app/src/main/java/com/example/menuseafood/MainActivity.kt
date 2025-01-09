package com.example.menuseafood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var rvMenuSeafood: RecyclerView
    private val list = ArrayList<MenuSeafood>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rvMenuSeafood = findViewById(R.id.rv_Seafood) // Pastikan ID ini sesuai dengan layout XML
        rvMenuSeafood.setHasFixedSize(true)
        list.addAll(getListMenuSeafood())
        showRecyclerList()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu) // Meng-inflate menu dari file XML
        return super.onCreateOptionsMenu(menu)
    }

    private fun getListMenuSeafood(): ArrayList<MenuSeafood> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val Detail = resources.getStringArray(R.array.Detail)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listSeafood = ArrayList<MenuSeafood>()
        for (i in dataName.indices) {
            val seafood = MenuSeafood(dataName[i], dataDescription[i], Detail[i],dataPhoto.getResourceId(i, -1))
            listSeafood.add(seafood)
        }
        dataPhoto.recycle()
        return listSeafood
    }

    private fun showRecyclerList() {
        rvMenuSeafood.layoutManager = LinearLayoutManager(this)
        val listMenuSeafoodAdapter = ListMenuSeafoodAdapter(list)
        rvMenuSeafood.adapter = listMenuSeafoodAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_about -> {
                // Ketika menu "About" ditekan, buat Intent untuk membuka AboutActivity
                val intent = Intent(this, AboutActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
