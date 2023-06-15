package com.capstone.pedotan.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contract(
    val id: Int,
    val investor: String,
    val company: String,
    val loan: Int,
    val imageUrl: String
) : Parcelable

object ContractData {
    val contracts = listOf(
        Contract(
            1,
            "Pak Joko",
            "ABC Corporation",
            10_000_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            2,
            "Bu Rina",
            "XYZ Industries",
            12_500_000,
            "https://media.licdn.com/dms/image/C5603AQEpnVRQYaVTmA/profile-displayphoto-shrink_200_200/0/1628019640016?e=1692230400&v=beta&t=jDsBxuE7qXQhs9tN_HUQkEpew_yEiBm5CBi9L1AgdiQ"
        ),
        Contract(
            3,
            "Pak Budi",
            "DEF Company",
            11_200_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            4,
            "Pak Sutrisno",
            "GHI Group",
            15_000_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            5,
            "Bu Yanti",
            "JKL Enterprises",
            9_800_000,
            "https://media.licdn.com/dms/image/C5603AQEpnVRQYaVTmA/profile-displayphoto-shrink_200_200/0/1628019640016?e=1692230400&v=beta&t=jDsBxuE7qXQhs9tN_HUQkEpew_yEiBm5CBi9L1AgdiQ"
        ),
        Contract(
            6,
            "Pak Slamet",
            "MNO Corporation",
            14_500_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            7,
            "Pak Suparno",
            "PQR Industries",
            10_500_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            8,
            "Pak Suhardi",
            "STU Company",
            13_200_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            9,
            "Pak Bambang",
            "VWX Enterprises",
            12_000_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        ),
        Contract(
            10,
            "Pak Agus",
            "YZA Group",
            11_800_000,
            "https://media.licdn.com/dms/image/C5603AQGVjhHMUawVyA/profile-displayphoto-shrink_200_200/0/1647696620497?e=1691625600&v=beta&t=kEDr6RDxOHWk960a16U3p3QyW7rSye8o4uj41GadcSo"
        )
    )
}
