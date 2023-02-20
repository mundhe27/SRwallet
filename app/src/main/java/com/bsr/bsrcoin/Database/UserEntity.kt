package com.bsr.bsrcoin.Database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Users")
data class UserEntity(

    @ColumnInfo(name = "Email") val email: String,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "Phone num") val phoneNum: String,
    @ColumnInfo(name = "Password") val password: String,
    @ColumnInfo(name = "Occupation") val occupation: String,
    @ColumnInfo(name = "Adhar/Pan Number") val adharNumber: String,
    @ColumnInfo(name = "Annual Income") val annualIncome: String,
    @ColumnInfo(name = "agent") val agent: Boolean,
    @ColumnInfo(name = "DOB") val dob: String,
    @ColumnInfo(name = "Address") val address: String,

    )
{

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int?=0

}
