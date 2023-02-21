package com.bsr.bsrcoin

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
//import android.provider.ContactsContract
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import com.bsr.bsrcoin.Fragments.*
import com.bsr.bsrcoin.MysqlConst.Constants
import com.bsr.bsrcoin.databinding.ActivityHomeScreenBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.provider.ContactsContract
import androidx.annotation.RequiresApi



class MainActivity : AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var isReadPermissionGranted = false
    private var isLocationPermissionGranted = false
    private var isRecordPermissionGranted = false
    lateinit var DrawerLayout:DrawerLayout
    lateinit var Toolbar:Toolbar
    lateinit var Frame:FrameLayout
    lateinit var NavigationView:NavigationView
    lateinit var floatingActionButton : FloatingActionButton
    lateinit var binding: ActivityHomeScreenBinding

     lateinit var view: View

     private lateinit var readbtn: Button

    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)


        val readbtn = findViewById<Button>(R.id.readbtn)
        readbtn.setOnClickListener {
            val intent = Intent(this@MainActivity,display_contacts::class.java)
            startActivity(intent)
        }



        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        {
            permissions ->
            isReadPermissionGranted = permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: isReadPermissionGranted
         //   isLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: isLocationPermissionGranted
           // isRecordPermissionGranted = permissions[Manifest.permission.RECORD_AUDIO] ?: isRecordPermissionGranted
        }


        initialise()
        setUpActionBar()






        var depoboolread = "true"
        var insuboolread = "true"
        var shareboolread = "true"
        var loanboolread = "true"

        findViewById<FloatingActionButton>(R.id.query).setOnClickListener {
            val intent = Intent(this@MainActivity,ChatActivity::class.java)
            startActivity(intent)
        }

        val actionBarDrawerToggle=ActionBarDrawerToggle(this@MainActivity,DrawerLayout,R.string.open_drawer,R.string.close_drawer)
        DrawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        openMyaccount()

        if(SharedPrefmanager.getInstance(this).isAdmin) {
            NavigationView.menu.clear()
            NavigationView.inflateMenu(R.menu.navigation_admin)
        }else{
            NavigationView.menu.clear()
            NavigationView.inflateMenu(R.menu.navigation_menu)
        }

        val id  = SharedPrefmanager.getInstance(this).keyUsernameName + ChatActivity.salt + SharedPrefmanager.getInstance(this).keyId

        floatingActionButton.setOnClickListener{v ->
            if(SharedPrefmanager.getInstance(this).isAgent)
            {
                val intent = Intent(this, AdminChatUser::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }
            else
            {
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("id",id)
                startActivity(intent)
            }
        }

        NavigationView.setNavigationItemSelectedListener {
            val frame=R.id.Frame

            val queue = Volley.newRequestQueue(this)
            val url = Constants.url_PermissionRead
            val JsonObjectRequest= JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener{response ->
                    var deporesponse = response["deposit"] as String
                    var insuranceresponse = response["insurance"] as String
                    var loanresponse = response["loan"] as String
                    var shareresponse = response["share"] as String

                    depoboolread=deporesponse.toString()
                    insuboolread=insuranceresponse.toString()
                    loanboolread=loanresponse.toString()
                    shareboolread=shareresponse.toString()
                },
                Response.ErrorListener { error ->

                }
            )
            queue.add(JsonObjectRequest)
            when(it.itemId)
        {
            R.id.Perm ->{
                supportFragmentManager.beginTransaction().replace(frame,Permissions()).commit()
                supportActionBar?.title="Permissions"
            }
            R.id.my_account->{openMyaccount()}
//            R.id.home->{openHome()}
            R.id.transactions->{supportFragmentManager.beginTransaction().replace(frame,TransactionsFragment()).commit()
                supportActionBar?.title="Transactions"}
            R.id.loans->{
                if(SharedPrefmanager.getInstance(this.applicationContext).isAdmin)
                    supportFragmentManager.beginTransaction().replace(frame, ViewLoanFragment()).commit()
                else
                    if (loanboolread=="true") {
                        supportFragmentManager.beginTransaction().replace(frame, LoanMainFragment())
                            .commit()
                        supportActionBar?.title = "Loans"
                    }
                else{
                        Toast.makeText(this@MainActivity, "Access Denied", Toast.LENGTH_SHORT).show()
                    }
            }
            R.id.insurance->{
                if(SharedPrefmanager.getInstance(this.applicationContext).isAdmin)
                    supportFragmentManager.beginTransaction().replace(frame,ViewInsuranceFragment()).commit()
                else
                    if (insuboolread=="true") {
                        supportFragmentManager.beginTransaction()
                            .replace(frame, InsuranceMainFragment()).commit()

                        supportActionBar?.title = "Insurance"
                    }
                else{
                        Toast.makeText(this@MainActivity, "Access Denied", Toast.LENGTH_SHORT).show()
                    }
            }
            R.id.bond->{
                supportFragmentManager.beginTransaction().replace(frame, BondMainFragment()).commit()
                supportActionBar?.title="Bond"
            }
                R.id.deposit->{
                    if (depoboolread=="true"){
                        supportFragmentManager.beginTransaction().replace(frame, DepositMainFragment()).commit()
                        supportActionBar?.title="Deposit"
                    }
                else{
                        Toast.makeText(this@MainActivity, "Access Denied", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.Share->{
                    if (depoboolread=="true") {
                        supportFragmentManager.beginTransaction().replace(frame, ShareMainFragment()).commit()
                        supportActionBar?.title = "Share"
                    }
                    else{
                        Toast.makeText(this@MainActivity, "Access Denied", Toast.LENGTH_SHORT).show()
                    }
                }
                R.id.logout->{
                    val alertDialog=AlertDialog.Builder(this@MainActivity)
                    alertDialog.setTitle("Exit?")
                    alertDialog.setMessage("Are you sure you want to exit app?")
                    alertDialog.setPositiveButton("Yes"){text,listener->
                        if(SharedPrefmanager.getInstance(this@MainActivity.applicationContext).logout()) {
                            ActivityCompat.finishAffinity(this)
                        }

                    }
                    alertDialog.setNegativeButton("No"){text,listener->
                        openMyaccount()
                    }
                    alertDialog.setOnCancelListener{
                        openMyaccount()
                    }
                    alertDialog.show()
                }
        }
            DrawerLayout.closeDrawers()
            return@setNavigationItemSelectedListener true
        }

        reqPermit()
        read()
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==android.R.id.home)
        {
            DrawerLayout.openDrawer(GravityCompat.START)

        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpActionBar() {
        setSupportActionBar(Toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        if(supportFragmentManager.backStackEntryCount==0){
            super.onBackPressed()
        }
        else{
            supportFragmentManager.popBackStack()
        }

    }

    private fun openHome() {
        NavigationView.setCheckedItem(R.id.home)
        supportFragmentManager.beginTransaction().replace(R.id.Frame, MainFragment()).commit()
        supportActionBar?.title="Home"
    }

    private fun initialise() {
        val sharedinstance = SharedPrefmanager.getInstance(this@MainActivity)
        DrawerLayout=findViewById(R.id.DrawerLayout)
        Toolbar=findViewById(R.id.Toolbar)
        Frame=findViewById(R.id.Frame)
        NavigationView=findViewById(R.id.NavigationView)
        val header = NavigationView.getHeaderView(0)
        header.findViewById<TextView>(R.id.tvUserName).text = sharedinstance.keyUsernameName
        header.findViewById<TextView>(R.id.tvUserAccount).text = sharedinstance.keyAccount
        if(sharedinstance.dpUri(sharedinstance.keyId)!=""){
        header.findViewById<ImageView>(R.id.ivUser).setImageURI(Uri.parse(sharedinstance.dpUri(sharedinstance.keyId)))}
        header.findViewById<TextView>(R.id.tvemail).text = sharedinstance.keyEmail

        floatingActionButton = findViewById(R.id.query)
    }
     private fun openMyaccount(){
        NavigationView.setCheckedItem(R.id.my_account)
        supportFragmentManager.beginTransaction().replace(R.id.Frame, MyAccountFragment()).commit()
        supportActionBar?.title="My Account"

    }

//    private fun viewUserDetails(view: View){
//        supportActionBar?.title="User Details"
//        setContentView(R.layout.user_data_screen)
//    }


private fun reqPermit() {
    isReadPermissionGranted = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    isLocationPermissionGranted = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

isRecordPermissionGranted = ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED





//
//    isContactsPermissionGranted = ContextCompat.checkSelfPermission(
//        this,
//        Manifest.permission.WRITE_CONTACTS
//    ) == PackageManager.PERMISSION_GRANTED


    val permissionRequest: MutableList<String> = ArrayList()

    if (!isReadPermissionGranted) {
      //  permissionRequest.add(Manifest.permission.READ_EXTERNAL_STORAGE)
      permissionRequest.add(Manifest.permission.READ_CONTACTS)
    }
//    if (!isRecordPermissionGranted) {
//        permissionRequest.add(Manifest.permission.RECORD_AUDIO)
//      //  permissionRequest.add(Manifest.permission.READ_CONTACTS)
//    }
//    if (!isLocationPermissionGranted) {
//        permissionRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
//       permissionRequest.add(Manifest.permission.READ_CONTACTS)
//    }





    if (permissionRequest.isNotEmpty()){
        permissionLauncher.launch(permissionRequest.toTypedArray())
}
}
    @RequiresApi(Build.VERSION_CODES.O)
    fun read (){
        var cursor=contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null)
         startManagingCursor(cursor)
        var from = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.CommonDataKinds.Phone._ID)

        var to = intArrayOf(android.R.id.text1,android.R.id.text2)
//   R.id.view_contacts -> {}

        var simple = SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,from,to)

        val view_contacts= view.findViewById<Spinner>(R.id.view_contacts)
       view_contacts.adapter = simple

        // display_contacts.xml is used to display these contacts

    }

}
