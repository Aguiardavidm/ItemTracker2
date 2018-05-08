package david.itemtracker

import android.arch.persistence.room.Room
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.activity_character.*


//https://www.simplifiedcoding.net/android-recyclerview-cardview-tutorial/
class characterActivity : AppCompatActivity() {

    var mDb: AppDatabase= Room.databaseBuilder(this, AppDatabase::class.java, "DB-CREATION").allowMainThreadQueries().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        buildRecyclerView()
    }

    private fun buildRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        var characters = mDb.TrackerDao().getAllEntries()

        val adapter = characterAdapter(characters)
        recyclerView.adapter = adapter
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_characters -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_create -> {
                displayAlert()

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //http://www.prandroid.com/2017/09/alert-dialog-box-in-android-using-kotlin.html
    fun displayAlert() {
        val alert = AlertDialog.Builder(this)
        var editTextName: EditText? = null

        with(alert) {
            val title = setTitle("Create Character")
            
            editTextName = EditText(context)
            editTextName!!.hint = "Name"
            editTextName!!.inputType = InputType.TYPE_CLASS_TEXT

            setPositiveButton("Create") { dialog, whichButton ->
                mDb?.TrackerDao()?.insert(character = TrackerEntity(editTextName!!.text.toString()))
                buildRecyclerView()
                dialog.dismiss()
            }

            setNegativeButton("Cancel") { dialog, whichButton ->
                dialog.dismiss()
            }
        }
        val dialog = alert.create()
        dialog.setView(editTextName)
        dialog.show()
    }

}

