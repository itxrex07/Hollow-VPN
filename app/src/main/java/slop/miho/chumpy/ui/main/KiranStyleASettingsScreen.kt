package slop.miho.chumpy.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import slop.miho.chumpy.data.UiSettings

/** Style A entry point; the settings layout stays consistent while the selected style changes. */
@Composable
fun KiranStyleASettingsScreen(
    settings: UiSettings,
    onChange: ((UiSettings) -> UiSettings) -> Unit,
    modifier: Modifier = Modifier,
) {
    CleanAppearanceSettingsScreen(settings = settings, onChange = onChange, modifier = modifier)
}
