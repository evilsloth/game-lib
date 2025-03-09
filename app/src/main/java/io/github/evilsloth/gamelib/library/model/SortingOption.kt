package io.github.evilsloth.gamelib.library.model

import io.github.evilsloth.gamelib.R

enum class SortingOption(
    val title: Int,
    val comparator: Comparator<LibraryItem>
) {
    NAME_ASC(R.string.sort_name_asc, compareBy(String.CASE_INSENSITIVE_ORDER, { it.name })),
    NAME_DESC(R.string.sort_name_desc,compareByDescending(String.CASE_INSENSITIVE_ORDER, { it.name })),
    RATING_DESC(R.string.rating_desc, compareByDescending { it.rating }),
    RATING_ASC(R.string.rating_asc, compareBy(nullsLast(), { it.rating })),
    USER_RATING_DESC(R.string.user_rating_desc, compareByDescending { it.userRating }),
    USER_RATING_ASC(R.string.user_rating_asc, compareBy(nullsLast(), { it.userRating }));
}


