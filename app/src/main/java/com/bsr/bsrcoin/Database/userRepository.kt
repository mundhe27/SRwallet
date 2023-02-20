package com.bsr.bsrcoin.Database

import android.content.Context
import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class userRepository {
    companion object {

        var userdatabase: UserDatabase? = null

        var userentitylist: UserEntity? = null
        var userentitylistt : LiveData<List<UserEntity>> ?=null

        fun initializeDB(context: Context) : UserDatabase {
            return UserDatabase.getDbInstance(context)
        }

        fun insertData(context: Context, userEntity: UserEntity) {

            userdatabase = initializeDB(context)

            CoroutineScope(IO).launch {
                val loginDetails = userEntity
                userdatabase!!.getUserDao().InsertData(loginDetails)
            }

        }

        fun deleteData(context: Context, userEntity: UserEntity) {

            userdatabase = initializeDB(context)

            CoroutineScope(IO).launch {
                val loginDetails = userEntity
                userdatabase!!.getUserDao().deleteUser(loginDetails)
            }

        }

        fun getLoginDetails(context: Context, username: String,password: String , agent: Boolean) : UserEntity? {

            userdatabase = initializeDB(context)

            userentitylist=userdatabase!!.getUserDao().getUser(username,password,agent)

            return userentitylist
        }

        fun getallDetails(context: Context) : LiveData<List<UserEntity>> {

            userdatabase = initializeDB(context)

            userentitylistt = userdatabase!!.getUserDao().getAllUsers()

            return userentitylistt as LiveData<List<UserEntity>>
        }

    }
}