package com.bsr.bsrcoin.ViewModels

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.bsr.bsrcoin.Database.UserEntity
import com.bsr.bsrcoin.Database.userRepository

class UserViewModel(application: Application) : AndroidViewModel(application) {


    lateinit var allUsers: LiveData<List<UserEntity>>
    var userEntity: UserEntity? = null



    fun insertUser(context: Context,userEntity: UserEntity) = {
        userRepository.insertData(context,userEntity)
    }

    fun deleteUser(context: Context,userEntity: UserEntity) =  {
        userRepository.deleteData(context,userEntity)
    }


    fun getallUser(context: Context) =  {
        allUsers=userRepository.getallDetails(context )
    }


    fun getUser(context: Context,mail: String, password: String, isagent: Boolean): UserEntity? {

        userEntity = userRepository.getLoginDetails(context,mail, password, isagent)

        return userEntity
    }
}