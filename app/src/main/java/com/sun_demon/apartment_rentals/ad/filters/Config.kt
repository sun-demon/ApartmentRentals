package com.sun_demon.apartment_rentals.ad.filters

import kotlinx.serialization.Serializable

@Serializable
class Config(
    var districtFilter: DistrictFilter = DistrictFilter(),
    var ranges: Ranges = Ranges(),
    var sorting: Sorting = Sorting.DATE
)