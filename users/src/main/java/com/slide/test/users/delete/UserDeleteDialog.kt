package com.slide.test.users.delete

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.slide.test.core_ui.component.ConfirmationDialog
import com.slide.test.core_ui.component.InfoDialog
import com.slide.test.core_ui.component.LoadingOverlay
import com.slide.test.users.R

/**
 * Created by Stefan Halus on 20 May 2022
 */
@Composable
fun UserDeleteDialogRoute(
    modifier: Modifier = Modifier,
    onDismiss: (Boolean) -> Unit,
    viewModel: UserDeleteViewModel = hiltViewModel(),
) {
    val viewState by viewModel.observableState.subscribeAsState(initial = viewModel.initialState)

    UserDeleteDialog(
        viewState = viewState,
        onDismiss = onDismiss,
        onApprove = { viewModel.dispatch(Action.UserDeleteConfirmation) }
    )
}

@Composable
fun UserDeleteDialog(
    modifier: Modifier = Modifier,
    viewState: State,
    onDismiss: (Boolean) -> Unit,
    onApprove: () -> Unit,
) {

    when {
        viewState.isLoading -> LoadingOverlay()
        viewState.userDeleteSuccess == false -> ErrorDialog(viewState.errorMessage) { onDismiss(false) }
        viewState.userDeleteSuccess == true -> SuccessDialog(viewState.userName) { onDismiss(true) }
        else -> ConfirmationDialog(
            title = stringResource(R.string.delete_user_dialog_title),
            text = stringResource(R.string.user_delete_dialog_prompt, viewState.userName),
            confirmActionText = stringResource(R.string.user_delete_dialog_button_confirm),
            dismissActionText = stringResource(R.string.user_delete_dialog_button_dismiss),
            onCancel = { onDismiss(false) },
            onApprove = onApprove
        )
    }
}

@Composable
fun SuccessDialog(userName: String = "", onDismiss: () -> Unit) {
    InfoDialog(
        title = stringResource(R.string.dialog_info_success_title),
        text = stringResource(R.string.user_delete_dialog_success_message, userName),
        buttonText = stringResource(R.string.dialog_button_ok),
        onApprove = onDismiss,
        onDismiss = onDismiss
    )
}

@Composable
fun ErrorDialog(
    errorMessage: String?,
    onDismiss: () -> Unit,
) {
    InfoDialog(
        title = "Error",
        text = errorMessage ?: "",
        buttonText = "Ok",
        onApprove = onDismiss,
        onDismiss = onDismiss
    )
}
