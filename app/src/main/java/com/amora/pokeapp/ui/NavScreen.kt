package com.amora.pokeapp.ui


sealed class NavScreen(val route: String) {
	// Root
	object AuthRoot : NavScreen("auth_root")
	object MainRoot : NavScreen("main_root")

	// Auth
	object Login : NavScreen("auth/login")
	object Registration : NavScreen("auth/registration")

	// Main
	object Home : NavScreen("main/home")
	object PokemonDetails : NavScreen("main/pokemonDetails") {
		const val routeWithArgument: String = "main/pokemonDetails/{pokeId}/{pokeName}"
		const val name: String = "pokeName"
		const val id: String = "pokeId"
	}

	companion object {
		fun createRoute(pokeId: Int, pokeName: String): String {
			return "main/pokemonDetails/$pokeId/$pokeName"
		}
	}
}