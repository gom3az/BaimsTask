package com.baimstask.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ShapeDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.baimstask.R
import com.baimstask.ui.theme.BaimsTaskTheme

@Composable
fun ErrorState(onRetry: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            shadowElevation = 2.dp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(dimensionResource(R.dimen.padding_medium)),
            shape = MaterialTheme.shapes.large,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(vertical = dimensionResource(R.dimen.padding_small)),
                    text = stringResource(id = R.string.something_went_wrong)
                )
                Button(
                    onClick = onRetry,
                    shape = ShapeDefaults.ExtraSmall,
                    contentPadding = PaddingValues(
                        vertical = dimensionResource(R.dimen.padding_small),
                        horizontal = dimensionResource(R.dimen.padding_medium)
                    ),
                    modifier = Modifier.padding(start = dimensionResource(R.dimen.padding_small)),
                ) {
                    Text(
                        text = stringResource(id = R.string.retry),
                    )
                }
            }
        }
    }
}

@Preview()
@Composable
fun DashboardErrorPreview() {
    BaimsTaskTheme {
        ErrorState({})
    }
}
