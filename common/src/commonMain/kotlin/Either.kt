package com.babel.cdm.components.common

sealed class Either<out L, out R> {
    //Failure
    data class Left<out L>(val value: L) : Either<L, Nothing>()

    //Success
    data class Right<out R>(val value: R) : Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)

    fun <T> fold(left: (L) -> T, right: (R) -> T): T
            =
        when (this) {
            is Either.Left  -> left(value)
            is Either.Right -> right(value)
        }

    fun <T> map(f: (R) -> T): Either<L, T> =
        flatMap { Either.Right(f(it)) }
}


fun <L, R, T> Either<L, R>.flatMap(f: (R) -> Either<L, T>):
        Either<L, T> =
    fold({ this as Either.Left }, f)

