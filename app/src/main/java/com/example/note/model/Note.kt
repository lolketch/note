package com.example.note.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Note")
class Note() : Parcelable {
    @PrimaryKey(autoGenerate = true)
    var uid = 0

    @ColumnInfo(name = "text")
    var text: String? = null

    @ColumnInfo(name = "timestamp")
    var timestamp: Long = 0

    @ColumnInfo(name = "date_reminder")
    var date_reminder: Long = 0

    @ColumnInfo(name = "done")
    var done = false

    @ColumnInfo(name = "name_person")
    var name_person: String? = null

    @ColumnInfo(name = "email_person")
    var email_person: String? = null

    @ColumnInfo(name = "image_person")
    var image_person: String? = null

    constructor(parcel: Parcel) : this() {
        uid = parcel.readInt()
        text = parcel.readString()
        timestamp = parcel.readLong()
        done = parcel.readByte() != 0.toByte()
        name_person = parcel.readString()
        email_person = parcel.readString()
        image_person = parcel.readString()
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(uid)
        parcel.writeString(text)
        parcel.writeLong(timestamp)
        parcel.writeByte(if (done) 1 else 0)
        parcel.writeString(name_person)
        parcel.writeString(email_person)
        parcel.writeString(image_person)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Note

        if (uid != other.uid) return false
        if (text != other.text) return false
        if (timestamp != other.timestamp) return false
        if (done != other.done) return false
        if (name_person != other.name_person) return false
        if (email_person != other.email_person) return false
        if (image_person != other.image_person) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uid
        result = 31 * result + (text?.hashCode() ?: 0)
        result = 31 * result + timestamp.hashCode()
        result = 31 * result + done.hashCode()
        result = 31 * result + (name_person?.hashCode() ?: 0)
        result = 31 * result + (email_person?.hashCode() ?: 0)
        result = 31 * result + (image_person?.hashCode() ?: 0)
        return result
    }

    companion object CREATOR : Parcelable.Creator<Note> {
        override fun createFromParcel(parcel: Parcel): Note {
            return Note(parcel)
        }

        override fun newArray(size: Int): Array<Note?> {
            return arrayOfNulls(size)
        }
    }
}