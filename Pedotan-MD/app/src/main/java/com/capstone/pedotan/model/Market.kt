package com.capstone.pedotan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Market(
    val id: Int,
    val komoditas: String,
    val harga: Int,
    val petani: String,
    val lokasi: String
) : Parcelable

object MarketData {
    val markets = listOf(
        Market(
            1,
            "Beras", 9_000,
            "Pak Budi",
            "Kabupaten Karanganyar"
        ),
        Market(
            2,
            "Kopi",
            100_000,
            "Bu Rina",
            "Lokasi Selesai"
        ),
        Market(
            3,
            "Jagung",
            120_000,
            "Pak Joko",
            "Kota Surakarta"
        ),
        Market(
            4,
            "Padi",
            150_000,
            "Pak Sutrisno",
            "Kabupaten Sragen"
        ),
        Market(
            5,
            "Jeruk",
            80_000,
            "Bu Yanti",
            "Kabupaten Klaten"
        ),
        Market(
            6,
            "Tebu",
            90_000,
            "Pak Slamet",
            "Kabupaten Wonogiri"
        ),
        Market(
            7,
            "Tempe",
            70_000,
            "Pak Suparno",
            "Kabupaten Sukoharjo"
        ),
        Market(
            8,
            "Kacang",
            110_000,
            "Pak Suhardi",
            "Kabupaten Boyolali"
        ),
        Market(
            9,
            "Teh",
            95_000,
            "Pak Bambang",
            "Kabupaten Karanganyar"
        ),
        Market(
            10,
            "Sawit",
            130_000,
            "Pak Agus",
            "Kabupaten Magetan"
        )
    )
}
