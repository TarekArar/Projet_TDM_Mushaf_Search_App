package com.mazrou.boilerplate.util


class Constants {

    companion object {


        const val NETWORK_TIMEOUT = 50000L
        const val CACHE_TIMEOUT = 2000L
        const val TESTING_NETWORK_DELAY = 0L // fake network delay for testing
        const val TESTING_CACHE_DELAY = 0L // fake cache delay for testing
        const val IS_SUBSCRIBED = "is_subscribed_to_firebase"

        const val NETWORK_DISRUPTED_TIME_OUT = 3600 * 2
        const val SHARED_PREFERENCES_USER_NAME = "username"
        const val SHARED_PREFERENCES_PIN_CODE = "pinCode"
        private val TAG: String = "AppDebug"

        const val UNABLE_TO_RESOLVE_HOST = "Unable to resolve host"
        const val UNABLE_TODO_OPERATION_WO_INTERNET = "Can't do that operation without an internet connection"

        const val RESPONSE_PASSWORD_UPDATE_SUCCESS = "successfully changed password"
        const val RESPONSE_CHECK_PREVIOUS_AUTH_USER_DONE = "Done checking for previously authenticated user."
        const val RESPONSE_MUST_BECOME_CODINGWITHMITCH_MEMBER = "You must become a member on Codingwithmitch.com to access the API. Visit https://codingwithmitch.com/enroll/"
        const val RESPONSE_NO_PERMISSION_TO_EDIT = "You don't have permission to edit that."
        const val RESPONSE_HAS_PERMISSION_TO_EDIT = "You have permission to edit that."
        const val SUCCESS_BLOG_CREATED = "created"
        const val SUCCESS_BLOG_DELETED = "deleted"
        const val SUCCESS_BLOG_UPDATED = "updated"
        const val ERROR_SAVE_ACCOUNT_PROPERTIES = "Error saving account properties.\nTry restarting the app."
        const val ERROR_SAVE_AUTH_TOKEN = "Error saving authentication token.\nTry restarting the app."
        const val ERROR_SOMETHING_WRONG_WITH_IMAGE = "Something went wrong with the image."
        const val ERROR_MUST_SELECT_IMAGE = "You must select an image."
        const val UNKNOWN_ERROR = "UNKNOWN ERROR "
        const val GENERIC_AUTH_ERROR = "Error"
        const val INVALID_PAGE = "Invalid page."
        const val ERROR_CHECK_NETWORK_CONNECTION = "Check network connection."
        const val ERROR_UNKNOWN = "Unknown error"
        const val INVALID_CREDENTIALS = "Invalid credentials"
        const val SOMETHING_WRONG_WITH_IMAGE = "Something went wrong with the image."
        const val INVALID_STATE_EVENT = "Invalid state event"
        const val CANNOT_BE_UNDONE = "This can't be undone."
        const val NETWORK_ERROR = "Network Error"
        const val NETWORK_ERROR_TIMEOUT = "Network timeout"
        const val CACHE_ERROR_TIMEOUT = "Cache timeout"
        const val INTERNAL_SERVER_ERROR = "Service indisponible \n\nContactez le support"


        const val CHOSE_PRODUCT = "Vous n'avez pas choisir un produit"



        fun isNetworkError(msg: String): Boolean {
            when {
                msg.contains(UNABLE_TO_RESOLVE_HOST) -> return true
                else -> return false
            }
        }

        fun isPaginationDone(errorResponse: String?): Boolean {
            // if error response = '{"detail":"Invalid page."}' then pagination is finished
            return errorResponse?.contains(INVALID_PAGE) ?: false
        }
    }
}