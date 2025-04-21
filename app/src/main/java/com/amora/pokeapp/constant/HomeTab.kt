package com.amora.pokeapp.constant

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.ui.graphics.vector.ImageVector
import com.amora.pokeapp.R

enum class HomeTab(
	@StringRes val title: Int,
	val icon: ImageVector
) {
	HOME(R.string.menu_home, Icons.Filled.Home),
	ABOUT(R.string.menu_about, Icons.Filled.EmojiPeople);

	companion object {
		fun getTabFromSource(@StringRes resource: Int): HomeTab {
			return when (resource) {
				R.string.menu_home -> HOME
				R.string.menu_about -> ABOUT
				else -> HOME
			}
		}
	}
}