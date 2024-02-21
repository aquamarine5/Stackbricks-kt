package org.aquarngd.stackbricks

class ExceptionalResponse(
    val isSuccess: Boolean,
    val exception: Exception?,
    val helpText: String?
) {
    companion object {
        fun success(): ExceptionalResponse {
            return ExceptionalResponse(true, null, null)
        }

        fun fail(exception: Exception, helpText: String?): ExceptionalResponse {
            return ExceptionalResponse(false, exception, helpText)
        }

        fun fail(exceptionalResponse: ExceptionalResponse): ExceptionalResponse {
            return ExceptionalResponse(
                false,
                exceptionalResponse.exception,
                exceptionalResponse.helpText
            )
        }
    }
}