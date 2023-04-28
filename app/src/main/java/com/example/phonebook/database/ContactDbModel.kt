package com.example.phonebook.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactDbModel(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "phone_number") val phoneNumber: String,
    @ColumnInfo(name = "tag") val tag: String,
    @ColumnInfo(name = "color_id") val colorId: Long,
    @ColumnInfo(name = "in_trash") val isInTrash: Boolean
) {
    companion object {
        val DEFAULT_CONTACT = listOf(
            ContactDbModel(1, "Mom", "0811233211","Family",  1, false),
            ContactDbModel(2, "Dad", "0885554646","Family",  2,false),
            ContactDbModel(3, "Khem", "027665656","Home", 3, false),
            ContactDbModel(4, "Prayuth", "0881234567","Mobile",4, false),
            ContactDbModel(5, "Thomas", "0665538765","Mobile",  5, false),
            ContactDbModel(6, "Thammasat Hospital", "029269999","Emergency",  12, false),
            ContactDbModel(6, "Babe", "0912344321","Mobile",  7, false),
        )
    }
}