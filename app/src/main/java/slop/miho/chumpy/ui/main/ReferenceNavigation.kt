package slop.miho.chumpy.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import slop.miho.chumpy.data.UiStyle

@Composable
fun ReferenceBottomBar(
    style: UiStyle,
    selectedTab: MainTab,
    settingsOpen: Boolean,
    selectedCategory: SettingsCategory,
    onTabSelected: (MainTab) -> Unit,
    onSettingsOpenChange: (Boolean) -> Unit,
    onCategorySelected: (SettingsCategory) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isA = style == UiStyle.KIRAN_A
    val bar = if (isA) Color(0xFF232C27) else Color(0xFF1B1B24)
    val active = if (isA) Color(0xFF86E3B0) else Color(0xFF4A3FD6)
    val activeText = if (isA) Color(0xFF00391F) else Color.White
    Surface(
        color = bar,
        shape = RoundedCornerShape(36.dp),
        modifier = modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (settingsOpen) {
                ReferenceNavItem("‹", true, active, activeText) { onSettingsOpenChange(false) }
                SettingsCategory.entries.forEach { category ->
                    ReferenceNavItem(
                        label = when (category) {
                            SettingsCategory.INTERFACE -> "Appearance"
                            SettingsCategory.CONNECTION -> "Routing"
                            SettingsCategory.ABOUT -> "About"
                        },
                        selected = selectedCategory == category,
                        active = active,
                        activeText = activeText,
                    ) { onCategorySelected(category) }
                }
            } else {
                ReferenceNavItem("Home", selectedTab == MainTab.HOME, active, activeText) { onTabSelected(MainTab.HOME) }
                ReferenceNavItem("Servers", selectedTab == MainTab.SERVERS, active, activeText) { onTabSelected(MainTab.SERVERS) }
                ReferenceNavItem("Settings", selectedTab == MainTab.SETTINGS, active, activeText) { onSettingsOpenChange(true) }
            }
        }
    }
}

@Composable
private fun RowScope.ReferenceNavItem(label: String, selected: Boolean, active: Color, activeText: Color, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = if (selected) active else Color.Transparent,
        contentColor = if (selected) activeText else MaterialTheme.colorScheme.onSurfaceVariant,
        shape = RoundedCornerShape(999.dp),
        modifier = Modifier.weight(1f),
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 12.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
