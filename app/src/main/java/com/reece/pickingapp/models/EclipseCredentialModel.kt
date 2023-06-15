package com.reece.pickingapp.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.Instant
import java.util.Date
import java.util.UUID

@Entity(tableName = "eclipse_credentials_tbl")
data class EclipseCredentialModel(
    @PrimaryKey
    var uid: UUID = UUID.randomUUID(),
    @ColumnInfo(name = "ec_username")
    var username: String,
    @ColumnInfo(name = "ec_email")
    var email: String,
    @ColumnInfo(name = "ec_is_remember")
    var eclipsePass: String,
    @ColumnInfo(name = "ec_eclipse_pass")
    var isRemember: Boolean,
    @ColumnInfo(name = "ec_entry_date")
    var entryDate: String = Date.from(Instant.now()).toString()
){
    fun isValid(): Boolean{
        return !this.username.isNullOrEmpty() && !this.email.isNullOrEmpty() && !this.eclipsePass.isNullOrEmpty()
    }

    @Override
    override fun equals(otherCredentialModel: Any?): Boolean {
        if (this === otherCredentialModel) return true
        if (otherCredentialModel is EclipseCredentialModel){
            if(!otherCredentialModel.isValid() || !this.isValid()){
                return false
            }

            if(this.uid == otherCredentialModel.uid){
                return true
            }

            if((this.email == otherCredentialModel.email)
                && (this.username == otherCredentialModel.username)){
                return true
            }

        }

        return false
    }
}

