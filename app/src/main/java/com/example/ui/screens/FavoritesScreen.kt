package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Recipe
import com.example.ui.components.RecipeCard
import com.example.ui.theme.MintEmerald
import com.example.ui.theme.SaffronAmber
import com.example.ui.theme.TerracottaDark
import com.example.ui.theme.TerracottaPrimary

data class MoroccanSpiceGuide(
    val name: String,
    val arabicName: String,
    val origin: String,
    val description: String,
    val usageInKitchen: String
)

@Composable
fun FavoritesScreen(
    recipes: List<Recipe>,
    favoriteIds: Set<String>,
    onRecipeClick: (Recipe) -> Unit,
    onFavoriteToggle: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteRecipes = recipes.filter { favoriteIds.contains(it.id) }

    val spicesEncyclopedia = listOf(
        MoroccanSpiceGuide(
            name = "Ras el Hanout",
            arabicName = "رأس الحانout",
            origin = "Fès & Marrakech",
            description = "Littéralement 'la tête de la boutique'. Mélange noble composé de 20 à 35 épices rares : boutons de rose séchés, cardamome, noix de muscade, galanga, clous de girofle, macis et cannelle.",
            usageInKitchen = "Idéal pour les tajines d'agneau, la pastilla, la Mrouzia sucrée-salée et le couscous de fête."
        ),
        MoroccanSpiceGuide(
            name = "Safran Pur de Taliouine",
            arabicName = "زعفران تاليوين الحر",
            origin = "Anti-Atlas marocain (Taliouine)",
            description = "L'or rouge du Maroc. Récolté fleur par fleur à l'aube en automne, ce safran d'exception offre une couleur dorée intense et un arôme floral et terreux unique au monde.",
            usageInKitchen = "Infusez toujours les filaments dans 2 cuillères d'eau tiède avant de les verser dans vos tajines et pastillas."
        ),
        MoroccanSpiceGuide(
            name = "Le Smen Artisanal",
            arabicName = "السمن البلدي الحار",
            origin = "Cuisine pastorale & citadine",
            description = "Beurre cru clarifié au sel et infusé au thym sauvage (Zaâtar), puis affiné en jarres de terre cuite pendant des mois voire des années.",
            usageInKitchen = "Une simple demi-cuillère apporte l'arôme signature incomparable à la Harira et à la semoule de couscous."
        ),
        MoroccanSpiceGuide(
            name = "Citron Beldi Confit au Sel",
            arabicName = "الحامض المصير البلدي",
            origin = "Marrakech & Sous-Massa",
            description = "Petits citrons ronds à peau fine et parfum musqué, incisés en croix et confits dans leur propre jus et du gros sel pendant plusieurs semaines.",
            usageInKitchen = "Hachez la pulpe pour la marinade chermoula et taillez l'écorce translucide en lanières sur le tajine de poulet."
        ),
        MoroccanSpiceGuide(
            name = "Eau de Fleur d'Oranger Distillée",
            arabicName = "ماء الزهر المقطر",
            origin = "Fès & Khemisset",
            description = "Hydrolat pur obtenu par distillation à l'alambic des fleurs fraîches du bigaradier (oranger amer) au printemps.",
            usageInKitchen = "Parfume subtilement les cornes de gazelle, les briouates au miel, la salade d'oranges à la cannelle et le thé royal."
        ),
        MoroccanSpiceGuide(
            name = "Gomme Arabique (Meska Horra)",
            arabicName = "المسكة الحرة",
            origin = "Résine naturelle d'acacia",
            description = "Larmes de résine translucide pilées avec un peu de sucre cristal dans un mortier.",
            usageInKitchen = "Le liant secret qui donne une texture onctueuse et élastique incomparable à la pâte d'amandes."
        )
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("favorites_screen"),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Hero Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(TerracottaDark, TerracottaPrimary)
                        )
                    )
                    .padding(20.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Favoris & Secrets",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Vos recettes fétiches et l'encyclopédie des épices",
                                color = Color(0xFFFFE0B2),
                                fontSize = 13.sp
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = SaffronAmber.copy(alpha = 0.3f),
                            modifier = Modifier.size(44.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Favorites Section Title
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFFF5252),
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = "Mes Recettes Favorites (${favoriteRecipes.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        if (favoriteRecipes.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Vous n'avez pas encore de favoris",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Appuyez sur le cœur d'une recette pour la retrouver ici facilement.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        } else {
            items(favoriteRecipes, key = { "fav_${it.id}" }) { recipe ->
                Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    RecipeCard(
                        recipe = recipe,
                        isFavorite = true,
                        onFavoriteClick = { onFavoriteToggle(recipe.id) },
                        onClick = { onRecipeClick(recipe) }
                    )
                }
            }
        }

        // Spices Encyclopedia Header
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = SaffronAmber,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = "Guide des Épices & Secrets de la Mâallma",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Comprendre les arômes piliers de la gastronomie chérifienne",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Spice cards
        items(spicesEncyclopedia) { spice ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .testTag("spice_card_${spice.name}"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = spice.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = TerracottaPrimary
                            )
                            Text(
                                text = spice.origin,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SaffronAmber.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = spice.arabicName,
                                color = SaffronAmber,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = spice.description,
                        style = MaterialTheme.typography.bodySmall,
                        lineHeight = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MintEmerald.copy(alpha = 0.1f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Restaurant,
                                contentDescription = null,
                                tint = MintEmerald,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = spice.usageInKitchen,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
