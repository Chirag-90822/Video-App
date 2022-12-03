package com.example.android

import android.os.Parcel
import android.os.Parcelable

class model(val title:String?, val path: String?, val duration:String?):Parcelable,CharSequence{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    private var wantToReadFlag:Boolean = false

    override fun describeContents(): Int {
      return 0
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
          dest?.writeString(title)
        dest?.writeString(path)
        dest?.writeString(duration)
    }

    companion object CREATOR : Parcelable.Creator<model> {
        override fun createFromParcel(parcel: Parcel): model {
            return model(parcel)
        }

        override fun newArray(size: Int): Array<model?> {
            return arrayOfNulls(size)
        }
    }

    fun isWantToReadFlag(): Boolean {
        return wantToReadFlag
    }


    fun setWantToReadFlag(wantToreadFlag: Boolean) {
        this.wantToReadFlag = wantToreadFlag
    }

    override val length: Int
        get() = title!!.length

    override fun get(index: Int): Char {
        var hio=title!!.get(index)
     return hio
    }

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return title!!.subSequence(startIndex,endIndex)
    }






}