package david.itemtracker

import android.Manifest
import android.app.PendingIntent
import android.arch.persistence.room.Room
import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import kotlinx.android.synthetic.main.activity_item_creation.*
import android.app.Activity
import android.support.v4.app.ActivityCompat
import android.util.Log
import android.view.View
import android.widget.Toast


class itemCreation : AppCompatActivity() {

    val PICK_CONTACT = 7745
    var phoneNum: String = ""

    var mDb: AppDatabase= Room.databaseBuilder(this, AppDatabase::class.java, "DB-CREATION").allowMainThreadQueries().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item_creation)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        if(intent.getBooleanExtra("existing", false)){
            itemNameText.setText(intent.getStringExtra("name"))
            itemTypeText.setText(intent.getStringExtra("type"))
            itemDescriptionText.setText(intent.getStringExtra("description"))
            saveButton.visibility = View.INVISIBLE
        }
        else{
            itemNameText.hint = "Item Name"
            itemDescriptionText.hint = "Item Description"
            itemTypeText.hint = "Item Type"
        }

        saveButton.setOnClickListener{
            val intent = Intent(this, characterActivity::class.java)
            if(!intent.getBooleanExtra("existing", false)) {
                itemImage.buildDrawingCache()
                mDb.TrackerDao().insert(TrackerEntity(
                        itemName = itemNameText.text.toString(),
                        itemDescription = itemDescriptionText.text.toString(),
                        itemType = itemTypeText.text.toString()
                    )
                )
            }
            startActivity(intent)
        }

        sendButton.setOnClickListener {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 100)

            val contactIntent = Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI)
            startActivityForResult(contactIntent, PICK_CONTACT)
        }
    }

    //I took this from somewhere but for the life of me I cannot find the source
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (resultCode == Activity.RESULT_OK) {
            val contactData = data.data
            val c = contentResolver.query(contactData!!, null, null, null, null)
            if (c!!.moveToFirst()) {

                var phoneNumber = ""
                val contactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID))
                var hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))

                if (hasPhone.equals("1", ignoreCase = true))
                    hasPhone = "true"
                else
                    hasPhone = "false"

                if (java.lang.Boolean.parseBoolean(hasPhone)) {
                    val phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId, null, null)
                    while (phones!!.moveToNext()) {
                        phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                    }
                    phones.close()
                }
                phoneNum = cleanPhoneNum(phoneNumber)
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 100)
                sendText(createMessage(),phoneNum)
            }
            c.close()
        }
    }

    private fun createMessage():String{
        var returnString :String = "Name: " + itemNameText.text + ", Type: " + itemTypeText.text + ", Description " + itemDescriptionText.text
        Log.i("Message",returnString)
        return returnString
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_characters -> {
                var intent = Intent(this,characterActivity::class.java)
                startActivity(intent)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_create -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    //https://www.ssaurel.com/blog/how-to-send-and-receive-sms-in-android/
    private fun sendText(phoneNum: String, message: String){
        var pendingSent = PendingIntent.getBroadcast(applicationContext,0,Intent("SMS_SENT"),0)
        var pendingDelivered = PendingIntent.getBroadcast(applicationContext,0,Intent("SMS_DELIVERED"),0)
        var smsManager: SmsManager = SmsManager.getDefault()
        if (message.length > 160){
            var messageList = smsManager.divideMessage(message)
            smsManager.sendMultipartTextMessage(phoneNum,null,messageList,null,null)
        } else {
            smsManager.sendTextMessage(phoneNum,null,message,pendingSent,pendingDelivered)
        }
    }

    private fun cleanPhoneNum(phoneNum: String): String{
        var regex: Regex = Regex("[^0-9]")
        return regex.replace(phoneNum, "")
    }
}
