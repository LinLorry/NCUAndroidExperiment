package cn.edu.ncu.datatransmit

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDate

data class StudentParcelable(var name: String, var birthday: LocalDate) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        LocalDate.of(parcel.readInt(), parcel.readInt(), parcel.readInt())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(birthday.year)
        parcel.writeInt(birthday.monthValue)
        parcel.writeInt(birthday.dayOfMonth)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StudentParcelable> {
        override fun createFromParcel(parcel: Parcel): StudentParcelable {
            return StudentParcelable(parcel)
        }

        override fun newArray(size: Int): Array<StudentParcelable?> {
            return arrayOfNulls(size)
        }
    }
}