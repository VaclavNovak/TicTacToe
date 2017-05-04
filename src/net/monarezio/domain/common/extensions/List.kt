package net.monarezio.domain.common.extensions

/**
 * Created by monarezio on 04/05/2017.
 */

fun<E> List<E>.set(index: Int, value: E): List<E> {
    return mapIndexed { i, e ->
        if(i == index) value
        else e
    }
}