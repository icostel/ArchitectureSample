package com.icostel.arhitecturesample.api

class Status private constructor(val type: Type) {
    enum class Type {
        NOT_STARTED, SUCCESS, ERROR, LOADING, CALL_ERROR, INPUTS_ERROR
    }

    companion object {

        fun notStarted(): Status {
            return Status(Type.NOT_STARTED)
        }

        fun success(): Status {
            return Status(Type.SUCCESS)
        }

        fun error(): Status {
            return Status(Type.CALL_ERROR)
        }

        fun loading(): Status {
            return Status(Type.LOADING)
        }
    }
}
