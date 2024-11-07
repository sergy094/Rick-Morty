package com.sergiospinola.common.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sergiospinola.data.model.ErrorData
import com.sergiospinola.data.network.factory.ErrorFactory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface BaseViewModelInterface {
    val loading: StateFlow<Boolean>
    val error: StateFlow<ErrorData?>
    fun closeError()
}

open class BaseViewModel : ViewModel(), BaseViewModelInterface {

    override val loading = MutableStateFlow(false)
    override val error = MutableStateFlow<ErrorData?>(null)

    /**
     * Executes a 'suspend' function, managing loading and errors. Must be used inside a
     * 'viewModelScope.launch', just like it were if this method weren't used
     * @param showLoading If a loading dialog is displayed while executing the job.
     * @param showDefaultError If a error dialog is displayed when job ends with error.
     * @param onError Function that is executed after job ends unsuccessfully. Error message
     * @param job function with the action to be performed
     */
    protected suspend fun errorWrapper(
        showLoading: Boolean = true,
        showDefaultError: Boolean = true,
        onError: suspend (ErrorData) -> Unit = {},
        job: suspend () -> Unit,
    ) {
        if (showLoading) {
            loading.value = true
        }
        try {
            job()
        } catch (throwable: Throwable) {

            val error = ErrorFactory.buildError(throwable)

            if (showDefaultError) {
                this.error.value = error
            }
            onError(error)
        } finally {
            if (showLoading) {
                loading.value = false
            }
        }
    }

    override fun closeError() {
        viewModelScope.launch {
            error.value = null
        }
    }

    protected fun launchWithErrorWrapper(
        showLoading: Boolean = true,
        showDefaultError: Boolean = true,
        onError: suspend (ErrorData) -> Unit = {},
        job: suspend () -> Unit,
    ) =
        viewModelScope.launch {
            errorWrapper(showLoading, showDefaultError, onError, job)
        }
}