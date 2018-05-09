package david.itemtracker

import android.arch.persistence.room.Room
import android.content.Intent
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
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_character.*

//This is the main activity and it controls all the items in the list
//https://www.simplifiedcoding.net/android-recyclerview-cardview-tutorial/
class characterActivity : AppCompatActivity() {

    var mDb: AppDatabase= Room.databaseBuilder(this, AppDatabase::class.java, "DB-CREATION").allowMainThreadQueries().build()

    //Your classic on create
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_character)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        buildRecyclerView()
    }

    //This sets up the recycler view
    //rv_parts.adapter = PartAdapter(testData, { partItem : PartData -> partItemClicked(partItem) })
    private fun buildRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        //getEntities is curretnly sorted by name so the recycler view is sorted the same
        var itemNames = mDb.TrackerDao().getEntities()
        val adapter = characterAdapter(itemNames, {item : TrackerEntity -> itemClicked(item)})

        recyclerView.adapter = adapter
    }

    //On click listener for recycler
    private fun itemClicked(partItem : TrackerEntity) {
        val intent = Intent(this,itemCreation::class.java)
        intent.putExtra("existing", true)
        intent.putExtra("name",partItem.itemName)
        intent.putExtra("type",partItem.itemType)
        intent.putExtra("description",partItem.itemDescription)
        startActivity(intent)
        Toast.makeText(this, "Clicked: ${partItem.itemName}", Toast.LENGTH_LONG).show()
    }

    //Controls the bottom navigation bar
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_characters -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_create -> {
                val intent = Intent(this,itemCreation::class.java)
                intent.putExtra("existing", false)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}

