package com.example.data.repository

import com.example.data.local.FavoriteRecipeEntity
import com.example.data.local.FavoritesDao
import com.example.data.local.ShoppingDao
import com.example.data.local.ShoppingItemEntity
import com.example.data.model.Recipe
import com.example.data.model.RecipeCategory
import com.example.data.model.RecipeDifficulty
import com.example.data.model.RecipeIngredient
import com.example.data.model.RecipeStep
import com.example.data.model.VideoChapter
import com.example.data.model.VideoTutorial
import kotlinx.coroutines.flow.Flow

class MoroccanDataRepository(
    private val shoppingDao: ShoppingDao,
    private val favoritesDao: FavoritesDao
) {
    val allShoppingItems: Flow<List<ShoppingItemEntity>> = shoppingDao.getAllShoppingItems()
    val allFavorites: Flow<List<FavoriteRecipeEntity>> = favoritesDao.getAllFavorites()

    suspend fun addShoppingItem(item: ShoppingItemEntity) = shoppingDao.insertItem(item)

    suspend fun addRecipeIngredientsToShopping(recipe: Recipe, servingsMultiplier: Double) {
        val items = recipe.ingredients.map { ing ->
            ShoppingItemEntity(
                name = ing.name,
                amount = ing.formattedAmount(servingsMultiplier),
                category = ing.category,
                isChecked = false,
                recipeTitle = recipe.title
            )
        }
        shoppingDao.insertAll(items)
    }

    suspend fun toggleShoppingItem(id: Long, isChecked: Boolean) {
        shoppingDao.updateCheckedStatus(id, isChecked)
    }

    suspend fun deleteShoppingItem(item: ShoppingItemEntity) {
        shoppingDao.deleteItem(item)
    }

    suspend fun deleteCheckedItems() {
        shoppingDao.deleteCheckedItems()
    }

    suspend fun clearShoppingList() {
        shoppingDao.clearAll()
    }

    suspend fun toggleFavorite(recipeId: String, isCurrentlyFavorite: Boolean) {
        if (isCurrentlyFavorite) {
            favoritesDao.removeFavorite(recipeId)
        } else {
            favoritesDao.addFavorite(FavoriteRecipeEntity(recipeId))
        }
    }

    fun isFavorite(recipeId: String): Flow<Boolean> = favoritesDao.isFavorite(recipeId)

    // Curated authentic recipes
    fun getRecipes(): List<Recipe> = listOf(
        Recipe(
            id = "tajine-agneau-pruneaux",
            title = "Tajine d'Agneau aux Pruneaux et Amandes",
            arabicTitle = "طاجين اللحم بالبرقوق والمشمش",
            category = RecipeCategory.TAJINES,
            description = "Le grand classique des fêtes marocaines. Viande d'agneau fondante mijotée au safran, gingembre et oignons confits, couronnée de pruneaux caramélisés à la cannelle et d'amandes dorées.",
            culturalOrigin = "Fès et Marrakech - Plat royal servi lors des grands mariages et de l'Aïd.",
            prepTimeMinutes = 25,
            cookTimeMinutes = 90,
            difficulty = RecipeDifficulty.MEDIUM,
            defaultServings = 4,
            calories = 680,
            keySpices = listOf("Safran pur", "Gingembre", "Cannelle de Ceylan", "Ras el Hanout", "Smen"),
            ingredients = listOf(
                RecipeIngredient("Épaule ou collier d'agneau en morceaux", 1000.0, "g", "Boucherie & Volailles"),
                RecipeIngredient("Pruneaux moelleux", 300.0, "g", "Fruits & Légumes"),
                RecipeIngredient("Amandes émondées et dorées", 100.0, "g", "Épicerie fine"),
                RecipeIngredient("Oignons émincés finement", 2.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Gousses d'ail râpées", 3.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Graines de sésame grillées", 2.0, "c. à soupe", "Épices & Aromates"),
                RecipeIngredient("Bâton de cannelle", 1.0, "pièce(s)", "Épices & Aromates"),
                RecipeIngredient("Cannelle moulue", 1.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Gingembre moulu", 1.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Curcuma moulu", 1.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Pistils de safran infusés", 0.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Miel d'oranger ou de fleurs", 2.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Huile d'olive vierge extra", 4.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Beurre rance (Smen)", 1.0, "c. à café", "Épicerie fine"),
                RecipeIngredient("Sel et poivre blanc", 1.0, "pincée", "Épices & Aromates")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Macération de la viande (Chermoula)",
                    instruction = "Mélangez dans un plat creux le gingembre, le curcuma, le safran infusé, l'ail râpé, le sel, le poivre et un filet d'huile. Enrobez bien les morceaux d'agneau et laissez reposer 30 min.",
                    timerDurationMinutes = 30,
                    chefTip = "Ne mettez pas trop d'huile au départ : l'eau parfumée permet aux épices de pénétrer la chair de l'agneau en profondeur."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Saisie dans le tajine",
                    instruction = "Faites chauffer l'huile d'olive et le smen dans votre tajine ou cocotte en terre cuite. Déposez les oignons émincés puis la viande et le bâton de cannelle. Faites dorer à feu moyen pendant 10 minutes.",
                    timerDurationMinutes = 10,
                    chefTip = "Utilisez un diffuseur sous le tajine en terre cuite sur feu gaz ou plaque induction pour éviter les chocs thermiques."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Mijotage doux de la viande",
                    instruction = "Ajoutez un verre d'eau chaude sans verser directement sur la viande. Couvrez le tajine et laissez mijoter à feu très doux pendant 1h15 jusqu'à ce que la viande soit tendre comme du beurre.",
                    timerDurationMinutes = 75,
                    chefTip = "La sauce doit réduire jusqu'à devenir sirupeuse et onctueuse (la fameuse daghmira)."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Caramélisation des pruneaux",
                    instruction = "Pendant ce temps, pochez les pruneaux dans une petite casserole avec une louche de sauce du tajine, la cannelle en poudre, le miel et une cuillère d'eau de fleur d'oranger pendant 12 minutes.",
                    timerDurationMinutes = 12,
                    chefTip = "Les pruneaux doivent gonfler et briller sous le sirop de miel et cannelle."
                ),
                RecipeStep(
                    stepNumber = 5,
                    title = "Dressage traditionnel",
                    instruction = "Disposez les morceaux de viande au centre du tajine, nappez de sauce réduite, disposez les pruneaux lustrés, parsemez de sésame doré et d'amandes croustillantes. Servez fumant avec du bon pain maison Khobz.",
                    timerDurationMinutes = null,
                    chefTip = "Accompagnez de verres de thé à la menthe brûlant."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-tajine-pruneaux",
                title = "Tutoriel Vidéo : Le Véritable Tajine d'Agneau aux Pruneaux",
                chefName = "Chef Fatima Mâallma",
                durationText = "14:20 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+tajine+agneau+pruneaux+marocain+traditionnel",
                overview = "Dans cette masterclass complète, découvrez étape par étape le dosage précis du safran pur, la technique de caramélisation des pruneaux sans les éclater, et le secret d'une sauce Daghmira bien nappante.",
                chapters = listOf(
                    VideoChapter("00:00", "Introduction & Choix de la viande", "Choisir les morceaux d'agneau à mijoter (souris, collier, épaule)."),
                    VideoChapter("02:15", "La Chermoula au safran", "Mélange des épices avec l'eau safranée."),
                    VideoChapter("05:30", "Mijotage dans le plat en terre", "Cuisson à l'étouffée et réduction du jus."),
                    VideoChapter("09:10", "Caramélisation parfumée", "Préparation du sirop de cannelle et miel pour les pruneaux."),
                    VideoChapter("12:40", "Dressage & Amandes dorées", "Finition et présentation royale.")
                ),
                secretsOfChef = listOf(
                    "Ne couvrez jamais les pruneaux d'eau froide, utilisez le bouillon du tajine pour une saveur décuplée.",
                    "La véritable sauce marocaine se fait sans fécule : seule la réduction lente des oignons donne son onctuosité.",
                    "Faites dorer les amandes à froid dans l'huile pour une cuisson uniforme jusqu'au cœur."
                )
            )
        ),

        Recipe(
            id = "couscous-sept-legumes",
            title = "Couscous Royal aux Sept Légumes",
            arabicTitle = "الكسكس المغربي بالسبع خضار",
            category = RecipeCategory.COUSCOUS,
            description = "Le plat sacré du vendredi au Maroc. Une semoule fine roulée à la main et cuite 3 fois à la vapeur au-dessus d'un bouillon généreux de bœuf, agneau, courges, carottes, navets et pois chiches.",
            culturalOrigin = "Tradition nationale ancestrale - Symbole de partage et de convivialité familiale.",
            prepTimeMinutes = 40,
            cookTimeMinutes = 110,
            difficulty = RecipeDifficulty.ADVANCED,
            defaultServings = 6,
            calories = 720,
            keySpices = listOf("Gingembre frais", "Curcuma", "Poivre noir", "Smen", "Pistils de safran"),
            ingredients = listOf(
                RecipeIngredient("Semoule de blé dur moyenne", 800.0, "g", "Boulangerie"),
                RecipeIngredient("Viande de bœuf ou collier d'agneau", 800.0, "g", "Boucherie & Volailles"),
                RecipeIngredient("Pois chiches trempés la veille", 150.0, "g", "Épicerie fine"),
                RecipeIngredient("Carottes épluchées et coupées en deux", 4.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Navets longs et ronds", 3.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Courgettes rondes ou longues", 3.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Morceau de potiron rouge", 300.0, "g", "Fruits & Légumes"),
                RecipeIngredient("Quart de chou blanc", 1.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Tomates mûres concassées", 3.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Botte de coriandre et persil frais", 1.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Piment fort entier (facultatif)", 1.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Huile d'olive et de tournesol", 5.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Beurre rance (Smen)", 1.5, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Gingembre en poudre", 2.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Curcuma moulu", 1.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Safran pur en filaments", 1.0, "pincée", "Épices & Aromates")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Démarrage du bouillon (Marmite de couscoussier)",
                    instruction = "Faites dorer la viande avec les oignons, l'huile, le gingembre, le curcuma, le poivre et le sel. Ajoutez les pois chiches, les tomates et le bouquet d'herbes ficelé. Mouillez avec 2,5 litres d'eau et portez à ébullition.",
                    timerDurationMinutes = 20,
                    chefTip = "Ficelez bien le persil et la coriandre pour pouvoir les retirer facilement avant le dressage."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Premier passage de la semoule à la vapeur",
                    instruction = "Dans une grande Gsaâ (plat en terre), enduisez la semoule de 3 c. à soupe d'huile et un verre d'eau en aérant entre les paumes. Déposez dans le haut du couscoussier. Laissez passer la vapeur pendant 20 minutes.",
                    timerDurationMinutes = 20,
                    chefTip = "Régulez les grains pour éviter les grumeaux en les frottant avec des mouvements circulaires doux."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Deuxième passage & Ajout des légumes durs",
                    instruction = "Versez la semoule dans la Gsaâ, arrosez d'eau salée tiède et aérez à la fourchette. Ajoutez dans la marmite les légumes plus fermes (chou, carottes, navets). Remettez la semoule à cuire à la vapeur 20 minutes.",
                    timerDurationMinutes = 20,
                    chefTip = "Ajoutez toujours les légumes par ordre de temps de cuisson pour qu'aucun ne se défasse."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Troisième passage & Cuisson du potiron",
                    instruction = "Ajoutez dans la marmite les courgettes, le potiron et le piment entier. Travaillez la semoule une dernière fois avec le Smen parfumé. Remettez à la vapeur 15 minutes.",
                    timerDurationMinutes = 15,
                    chefTip = "Le smen fondu donne au couscous marocain son arôme inimitable et traditionnel."
                ),
                RecipeStep(
                    stepNumber = 5,
                    title = "Dressage dans la grande Gsaâ",
                    instruction = "Dressez la semoule en dôme, creusez un puits pour y déposer les viandes. Disposez les 7 légumes harmonieusement en rayons de soleil. Arrosez généreusement d'une louche de bouillon chaud.",
                    timerDurationMinutes = null,
                    chefTip = "Servez le reste du bouillon brûlant dans un bol à part pour que chacun sauce à sa guise."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-couscous-7-legumes",
                title = "Tutoriel Vidéo : L'Art du Roulage du Couscous Traditionnel",
                chefName = "Lalla Aïcha & Chef Rachid",
                durationText = "18:45 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+couscous+marocain+sept+legumes+traditionnel",
                overview = "Apprenez la technique ancestrale des trois cuissons vapeur de la graine de couscous dans le kesskess, l'étagement des légumes et le travail du beurre Smen.",
                chapters = listOf(
                    VideoChapter("00:00", "Choix du couscoussier & ingrédients", "Matériel traditionnel et préparation des 7 légumes."),
                    VideoChapter("03:10", "Préparation de la semoule (1er arrosage)", "L'huilage initial et aération des grains."),
                    VideoChapter("07:20", "Le bouillon parfumé au Smen", "Cuisson des viandes et pois chiches."),
                    VideoChapter("11:45", "Échelonnage des légumes", "Carottes, navets, puis potiron et courgettes."),
                    VideoChapter("15:30", "Le dressage majestueux dans la Gsaâ", "Arrosage équilibré et présentation en rayons.")
                ),
                secretsOfChef = listOf(
                    "Utilisez un linge propre humide ou du papier aluminium pour sceller la jonction entre le bas et le haut du couscoussier si la vapeur s'échappe.",
                    "Le potiron cuit très vite : ajoutez-le toujours 15 minutes maximum avant la fin.",
                    "Pour une semoule ultra légère, arrosez petit à petit et laissez les grains absorber l'eau 5 minutes avant de remettre sur le feu."
                )
            )
        ),

        Recipe(
            id = "pastilla-poulet-amandes",
            title = "Pastilla Traditionnelle au Poulet et Amandes",
            arabicTitle = "بسطيلة الدجاج واللوز الفاسية",
            category = RecipeCategory.PASTILLAS,
            description = "Le chef-d'œuvre sucré-salé de la gastronomie fassie. Feuilles de brick croustillantes dorées au four, garnies d'un effiloché de poulet aux oignons caramélisés, d'une crème d'œufs aux herbes et d'un lit d'amandes concassées à la cannelle et fleur d'oranger.",
            culturalOrigin = "Fès - Réservée aux grandes cérémonies et réceptions d'honneur.",
            prepTimeMinutes = 50,
            cookTimeMinutes = 60,
            difficulty = RecipeDifficulty.ADVANCED,
            defaultServings = 6,
            calories = 780,
            keySpices = listOf("Cannelle de Ceylan", "Fleur d'oranger", "Safran pur", "Gingembre", "Muscade"),
            ingredients = listOf(
                RecipeIngredient("Feuilles de brick ou feuilles de Warka", 12.0, "pièce(s)", "Boulangerie"),
                RecipeIngredient("Poulet fermier coupé en morceaux", 1200.0, "g", "Boucherie & Volailles"),
                RecipeIngredient("Oignons émincés finement", 1000.0, "g", "Fruits & Légumes"),
                RecipeIngredient("Amandes émondées, frites et concassées", 300.0, "g", "Épicerie fine"),
                RecipeIngredient("Œufs frais battus", 6.0, "pièce(s)", "Épicerie fine"),
                RecipeIngredient("Beurre fondu pour badigeonner", 120.0, "g", "Épicerie fine"),
                RecipeIngredient("Sucre glace", 100.0, "g", "Épicerie fine"),
                RecipeIngredient("Eau de fleur d'oranger de qualité", 3.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Cannelle moulue", 2.0, "c. à soupe", "Épices & Aromates"),
                RecipeIngredient("Pistils de safran pur", 1.0, "pincée", "Épices & Aromates"),
                RecipeIngredient("Gingembre moulu", 1.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Gomme arabique pilée (Meska horra)", 0.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Coriandre et persil frais hachés", 1.0, "botte(s)", "Fruits & Légumes")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Cuisson et effilochage du poulet",
                    instruction = "Faites cuire le poulet avec les oignons, l'huile, le safran, le gingembre, la cannelle, les herbes, le sel et le poivre dans une marmite sans ajouter trop d'eau. Une fois cuit, retirez le poulet, désossez-le et effilochez la chair.",
                    timerDurationMinutes = 35,
                    chefTip = "Les oignons vont rendre beaucoup d'eau : laissez cuire à couvert au début, puis découvrez."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Réduction de la sauce et brouillage des œufs",
                    instruction = "Faites réduire le jus d'oignons restant jusqu'à évaporation complète de l'eau. Versez les 6 œufs battus en filet en remuant vigoureusement sur feu doux jusqu'à obtenir une farce crémeuse et sèche. Égouttez soigneusement.",
                    timerDurationMinutes = 15,
                    chefTip = "Une farce aux œufs bien égouttée garantit que la pâte restera ultra-croustillante à la cuisson."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Préparation du croquant d'amandes",
                    instruction = "Concassez les amandes frites, mélangez-les avec la moitié du sucre glace, 1 cuillère de cannelle, la fleur d'oranger et la gomme arabique.",
                    timerDurationMinutes = 5,
                    chefTip = "Ne réduisez pas les amandes en poudre fine : gardez des petits éclats pour la mâche."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Montage de la pastilla",
                    instruction = "Beurrez un moule rond. Disposez les feuilles de warka en rosace en les badigeonnant de beurre fondu. Étalez une couche d'amandes, puis le poulet effiloché, puis la préparation aux œufs. Rabattez les feuilles et recouvrez de 2 feuilles beurrées rentrées vers le dessous.",
                    timerDurationMinutes = 15,
                    chefTip = "Badigeonnez généreusement de beurre clarifié chaque feuille pour un feuilletage parfait."
                ),
                RecipeStep(
                    stepNumber = 5,
                    title = "Cuisson et décoration traditionnelle",
                    instruction = "Enfournez à 180°C pendant 25 à 30 minutes jusqu'à ce que la pastilla soit dorée et croustillante. Saupoudrez de sucre glace et tracez des losanges avec la cannelle et quelques amandes entières.",
                    timerDurationMinutes = 30,
                    chefTip = "Laissez tiédir 5 minutes avant de découper pour des parts nettes."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-pastilla-poulet",
                title = "Tutoriel Vidéo : Le Pliage Parfait de la Pastilla Fassie",
                chefName = "Chef Moha & Lalla Souad",
                durationText = "16:10 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+pastilla+poulet+amandes+marocaine+authentique",
                overview = "Maîtrisez la technique de séchage de la sauce aux oignons, le montage en rosace sans déchirer les feuilles de warka, et la dorure croustillante au four.",
                chapters = listOf(
                    VideoChapter("00:00", "Les secrets de la farce", "Cuisson aromatique du poulet et des oignons."),
                    VideoChapter("04:30", "La réduction de la Daghmira", "Élimination totale de l'humidité et ajout des œufs."),
                    VideoChapter("08:15", "Le lit d'amandes frites parfumées", "Émondage, friture douce et fleur d'oranger."),
                    VideoChapter("11:20", "Le pliage en rosace", "Positionnement des feuilles et emballage hermétique."),
                    VideoChapter("14:50", "Décoration au pochoir cannelle & sucre", "Le quadrillage géométrique traditionnel.")
                ),
                secretsOfChef = listOf(
                    "Passez la farce aux œufs dans une passoire pendant 20 minutes pour retirer tout excès de liquide.",
                    "Mélangez le beurre fondu avec un trait d'huile végétale pour un feuilletage encore plus croustillant.",
                    "Pour un résultat traiteur, préparez la veille et cuisez le jour même."
                )
            )
        ),

        Recipe(
            id = "tajine-poulet-citron-olives",
            title = "Tajine de Poulet aux Citrons Confits et Olives",
            arabicTitle = "طاجين الدجاج بالليمون المصير والزيتون",
            category = RecipeCategory.TAJINES,
            description = "Le parfum envoûtant de la cuisine marocaine de tous les jours et des grandes occasions. Morceaux de poulet dorés mijotés à la chermoula parfumée au gingembre, pulpe de citron beldi et olives violettes.",
            culturalOrigin = "Cuisine citadine marocaine - Le plat de bienvenue chaleureux par excellence.",
            prepTimeMinutes = 20,
            cookTimeMinutes = 55,
            difficulty = RecipeDifficulty.EASY,
            defaultServings = 4,
            calories = 540,
            keySpices = listOf("Gingembre frais", "Curcuma", "Citron confit Beldi", "Coriandre fraîche", "Ail"),
            ingredients = listOf(
                RecipeIngredient("Cuisses ou hauts de cuisse de poulet", 1000.0, "g", "Boucherie & Volailles"),
                RecipeIngredient("Citrons confits au sel (Beldi)", 2.0, "pièce(s)", "Épicerie fine"),
                RecipeIngredient("Olives violettes ou rouges marocaines", 150.0, "g", "Épicerie fine"),
                RecipeIngredient("Oignons râpés ou émincés très fin", 2.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Gousses d'ail pilées", 4.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Coriandre et persil plat frais hachés", 3.0, "c. à soupe", "Fruits & Légumes"),
                RecipeIngredient("Gingembre frais râpé ou en poudre", 1.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Curcuma moulu", 1.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Pistils de safran infusés", 1.0, "pincée", "Épices & Aromates"),
                RecipeIngredient("Huile d'olive vierge", 4.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Poivre blanc moulu", 0.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Sel (avec modération car le citron est salé)", 0.5, "c. à café", "Épices & Aromates")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Préparation de la marinade (Chermoula)",
                    instruction = "Hachez finement la pulpe d'un citron confit (gardez l'écorce pour la finition). Mélangez-la avec l'ail pilé, les herbes, le gingembre, le curcuma, le safran et un peu d'eau. Frottez la volaille avec cette pâte et laissez mariner 20 min.",
                    timerDurationMinutes = 20,
                    chefTip = "Faites de petites incisions dans la chair du poulet pour faire pénétrer les sucs de la marinade."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Saisie des oignons et de la volaille",
                    instruction = "Dans le plat à tajine, faites suer les oignons dans l'huile d'olive. Déposez les morceaux de poulet marinés et laissez colorer doucement sur toutes les faces pendant 10 minutes.",
                    timerDurationMinutes = 10,
                    chefTip = "Ne laissez pas brûler l'ail, conservez un feu doux et régulier."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Cuisson mijotée à couvert",
                    instruction = "Ajoutez un demi-verre d'eau dans le fond du tajine. Couvrez et laissez mijoter à feu doux pendant 35 minutes en arrosant la viande de temps en temps avec le jus de cuisson.",
                    timerDurationMinutes = 35,
                    chefTip = "Si vous aimez une peau très dorée, vous pouvez passer le poulet 5 minutes sous le gril du four avant de le remettre dans le tajine."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Ajout des olives et écorces confites",
                    instruction = "Rincez les olives à l'eau bouillante pour les dessaler. Déposez-les dans la sauce avec les lanières d'écorce de citron confit. Laissez frémir 10 minutes à découvert pour napper la sauce.",
                    timerDurationMinutes = 10,
                    chefTip = "La sauce doit former de petites perles d'huile onctueuses autour des oignons réduits."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-tajine-poulet-citron",
                title = "Tutoriel Vidéo : Le Secret de la sauce M'qalli aux citrons confits",
                chefName = "Chef Simo",
                durationText = "12:15 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+tajine+poulet+citron+confit+olives+marocain",
                overview = "Comment doser le sel avec les citrons confits, obtenir une chair de poulet juteuse et une sauce jaune d'or étincelante sans excès de gras.",
                chapters = listOf(
                    VideoChapter("00:00", "Préparation du citron Beldi", "Séparation de la pulpe et de l'écorce."),
                    VideoChapter("02:40", "La marinade Chermoula", "Émulsion des épices et herbes fraîches."),
                    VideoChapter("05:50", "La cuisson douce du tajine", "Mijotage et surveillance de la réduction."),
                    VideoChapter("09:30", "Le pochage des olives", "Dessalage et intégration finale.")
                ),
                secretsOfChef = listOf(
                    "Utilisez de préférence des citrons beldi traditionnels à peau fine et parfum musqué.",
                    "Faites blanchir les olives violettes 2 minutes dans l'eau bouillante pour éliminer l'amertume et le trop-plein de sel."
                )
            )
        ),

        Recipe(
            id = "harira-marocaine-fes",
            title = "Harira Traditionnelle de Fès",
            arabicTitle = "الحريرة المغربية الأصيلة",
            category = RecipeCategory.SOUPES_ENTREES,
            description = "La soupe reine du Maroc, incontournable au coucher du soleil pendant le Ramadan. Un velouté savoureux de tomates fraîches, pois chiches fondants, lentilles, céleri branche, viande tendre et le liant velouté Tadwira.",
            culturalOrigin = "Fès et Rabat - Plat de rupture du jeûne servi avec dattes majhoul et chebakia au miel.",
            prepTimeMinutes = 25,
            cookTimeMinutes = 65,
            difficulty = RecipeDifficulty.MEDIUM,
            defaultServings = 6,
            calories = 380,
            keySpices = listOf("Gingembre", "Cannelle", "Smen", "Céleri branche", "Coriandre"),
            ingredients = listOf(
                RecipeIngredient("Morceaux de viande de bœuf ou d'agneau en dés", 300.0, "g", "Boucherie & Volailles"),
                RecipeIngredient("Pois chiches trempés et émondés", 150.0, "g", "Épicerie fine"),
                RecipeIngredient("Lentilles brunes rincées", 80.0, "g", "Épicerie fine"),
                RecipeIngredient("Tomates mûres mixées et filtrées", 1000.0, "g", "Fruits & Légumes"),
                RecipeIngredient("Concentré de tomates", 2.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Branches de céleri avec les feuilles hachées", 1.0, "botte(s)", "Fruits & Légumes"),
                RecipeIngredient("Coriandre et persil frais hachés", 1.0, "botte(s)", "Fruits & Légumes"),
                RecipeIngredient("Oignon moyen râpé", 1.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Petits vermicelles cheveux d'ange", 60.0, "g", "Boulangerie"),
                RecipeIngredient("Farine pour la Tadwira (liant)", 4.0, "c. à soupe", "Boulangerie"),
                RecipeIngredient("Jus de citron frais", 2.0, "c. à soupe", "Fruits & Légumes"),
                RecipeIngredient("Beurre rance (Smen)", 1.0, "c. à café", "Épicerie fine"),
                RecipeIngredient("Gingembre en poudre", 1.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Cannelle en poudre", 0.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Curcuma moulu", 1.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Huile d'olive vierge", 2.0, "c. à soupe", "Épicerie fine")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Cuisson de la base de viande et légumineuses",
                    instruction = "Dans une grande marmite, faites revenir les dés de viande, l'oignon râpé, les pois chiches, les lentilles, la moitié du céleri et des herbes avec l'huile, le gingembre, le curcuma, la cannelle, le sel et le poivre. Couvrez de 2 litres d'eau et laissez cuire 40 minutes.",
                    timerDurationMinutes = 40,
                    chefTip = "Retirez la peau des pois chiches en les frottant entre vos mains pour une harira plus digeste."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Ajout de la tomate et herbes fraîches",
                    instruction = "Ajoutez le coulis de tomates fraîches, le concentré de tomate délayé et le reste du céleri et de la coriandre. Laissez bouillir à feu moyen pendant 15 minutes.",
                    timerDurationMinutes = 15,
                    chefTip = "Ajouter la seconde moitié des herbes en milieu de cuisson conserve une fraîcheur aromatique intense."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Préparation de la Tadwira (Le Liant magique)",
                    instruction = "Dans un bol, délayez la farine dans un grand verre d'eau froide jusqu'à disparition complète des grumeaux (ou mixez-la). Passez au tamis fin.",
                    timerDurationMinutes = 5,
                    chefTip = "La farine doit être parfaitement fluide pour éviter tout grumeau dans la soupe."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Liaison et vermicelles",
                    instruction = "Versez la Tadwira en mince filet dans la marmite en remuant constamment à la cuillère en bois. Laissez cuire 10 minutes jusqu'à ce que la mousse blanche disparaisse. Ajoutez alors les cheveux d'ange et le smen.",
                    timerDurationMinutes = 10,
                    chefTip = "La disparition de l'écume blanche en surface indique que la farine est parfaitement cuite."
                ),
                RecipeStep(
                    stepNumber = 5,
                    title = "Finition au citron frais",
                    instruction = "Coupez le feu, ajoutez une cuillère de coriandre fraîche ciselée et un filet de jus de citron. Servez bien chaud avec des dattes et quartiers de citron.",
                    timerDurationMinutes = null,
                    chefTip = "La Harira est encore meilleure réchauffée le lendemain."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-harira-marocaine",
                title = "Tutoriel Vidéo : Réussir la Tadwira veloutée de la Harira",
                chefName = "Chef Choumicha & Fatima",
                durationText = "13:40 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+harira+marocaine+authentique+choumicha",
                overview = "La technique complète pour réussir la liaison sans grumeaux, le choix des herbes fraîches (céleri krafes) et l'équilibre subtil d'acidité des tomates.",
                chapters = listOf(
                    VideoChapter("00:00", "L'importance du céleri et des herbes", "Préparation du Krafes et émondage des pois chiches."),
                    VideoChapter("03:20", "Cuisson des légumineuses", "Cuisson conjointe des lentilles et de la viande."),
                    VideoChapter("06:40", "La sauce tomate veloutée", "Épaississement et couleur rouge rubis."),
                    VideoChapter("09:15", "La Tadwira sans grumeaux", "Versage en filet et test de la cuillère."),
                    VideoChapter("12:00", "Service traditionnel", "Accompagnement de dattes fraîches et chebakia.")
                ),
                secretsOfChef = listOf(
                    "Le secret réside dans le céleri branche (Krafes) : ne le remplacez jamais par du persil seul.",
                    "Ajoutez le smen tout à la fin de cuisson pour conserver son parfum délicat sans rancir."
                )
            )
        ),

        Recipe(
            id = "zaalouk-aubergines",
            title = "Zaalouk d'Aubergines Grillées & Épices",
            arabicTitle = "زعلوك الباذنجان المشوي",
            category = RecipeCategory.SOUPES_ENTREES,
            description = "Le caviar d'aubergines marocain par excellence. Aubergines préalablement brûlées à la flamme pour une note fumée envoûtante, écrasées à la fourchette avec des tomates gorgées de soleil, ail confit, cumin et huile d'olive.",
            culturalOrigin = "Entrée chaude ou froide servie sur toutes les tables de fête du Royaume.",
            prepTimeMinutes = 15,
            cookTimeMinutes = 25,
            difficulty = RecipeDifficulty.EASY,
            defaultServings = 4,
            calories = 190,
            keySpices = listOf("Cumin torréfié", "Paprika doux fumé", "Ail", "Coriandre fraîche", "Piment d'Espelette"),
            ingredients = listOf(
                RecipeIngredient("Belles aubergines fermes", 3.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Grosses tomates mûres émondées et concassées", 3.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Gousses d'ail écrasées", 4.0, "pièce(s)", "Fruits & Légumes"),
                RecipeIngredient("Coriandre et persil plat frais ciselés", 4.0, "c. à soupe", "Fruits & Légumes"),
                RecipeIngredient("Huile d'olive vierge extra", 5.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Paprika doux (Piment doux)", 1.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Cumin en poudre", 1.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Jus de demi-citron", 1.0, "c. à soupe", "Fruits & Légumes"),
                RecipeIngredient("Sel et poivre noir", 1.0, "pincée", "Épices & Aromates")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Cuisson fumée des aubergines",
                    instruction = "Piquez les aubergines avec une fourchette et faites-les griller directement sur la flamme du gaz ou sous le gril du four à 220°C pendant 20 minutes en les retournant régulièrement jusqu'à ce que la peau soit noircie et la chair tendre.",
                    timerDurationMinutes = 20,
                    chefTip = "Le passage direct sur la flamme confère au Zaalouk ce goût fumé authentique des restaurants de Marrakech."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Épluchage et concassage",
                    instruction = "Enfermez les aubergines chaudes 5 min dans un sac ou un bol couvert pour faciliter l'épluchage. Retirez la peau noircie et égouttez la chair dans une passoire pour retirer l'excès d'eau amère.",
                    timerDurationMinutes = 5,
                    chefTip = "Ne rincez pas les aubergines à l'eau : vous perdriez tout l'arôme boisé fumé."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Cuisson de la concassée de tomates",
                    instruction = "Dans une poêle, faites chauffer l'huile d'olive avec l'ail et les tomates. Laissez compoter 10 minutes jusqu'à ce que les tomates rendent leur eau.",
                    timerDurationMinutes = 10,
                    chefTip = "Écrasez les tomates à la cuillère en bois au fur et à mesure."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Mélange et réduction à la fourchette",
                    instruction = "Ajoutez la chair d'aubergine, le paprika, le cumin, le sel et le poivre. Écrasez le tout à la fourchette sur feu moyen pendant 10 minutes jusqu'à ce qu'il n'y ait plus de liquide. Terminez avec la coriandre fraîche et un filet d'huile d'olive crue.",
                    timerDurationMinutes = 10,
                    chefTip = "Le Zaalouk parfait doit être confit et bien lié, sans jus résiduel."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-zaalouk-fume",
                title = "Tutoriel Vidéo : Le Zaalouk fumé à la flamme comme à Marrakech",
                chefName = "Chef Laila",
                durationText = "08:50 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+zaalouk+marocain+aubergine+fume+traditionnel",
                overview = "Technique de brûlage de la peau d'aubergine, égouttage sans perte de goût, et réduction fondante aux épices marocaines.",
                chapters = listOf(
                    VideoChapter("00:00", "Le secret du goût fumé", "Grillage sur feu vif."),
                    VideoChapter("02:30", "Égouttage de la pulpe", "Retirer l'amertume sans rincer."),
                    VideoChapter("04:45", "La compotée d'ail et tomate", "Création de la base onctueuse."),
                    VideoChapter("07:10", "Écrasement et assaisonnement", "Le bon dosage cumin/paprika.")
                ),
                secretsOfChef = listOf(
                    "Ajoutez le cumin en toute fin de cuisson pour éviter qu'il ne devienne amer sous l'effet d'une chaleur prolongée.",
                    "Arrosez d'un filet de citron frais juste avant de déguster avec du pain marocain chaud."
                )
            )
        ),

        Recipe(
            id = "briouates-amandes-miel",
            title = "Briouates aux Amandes et Miel Pur",
            arabicTitle = "بريوات باللوز والعسل الحر",
            category = RecipeCategory.PASTILLAS,
            description = "Les joyaux de la pâtisserie marocaine. Triangles croustillants de pâte dorée garnis d'une pâte d'amandes raffinée parfumée à la fleur d'oranger, à la cannelle et à la gomme arabique, plongés dans un bain de miel tiède.",
            culturalOrigin = "Tétouan et Fès - Pâtisserie d'élite servie avec le thé vert à la menthe.",
            prepTimeMinutes = 45,
            cookTimeMinutes = 20,
            difficulty = RecipeDifficulty.MEDIUM,
            defaultServings = 6,
            calories = 340,
            keySpices = listOf("Eau de fleur d'oranger", "Cannelle de Ceylan", "Gomme arabique (Meska)", "Miel pur d'oranger"),
            ingredients = listOf(
                RecipeIngredient("Poudre d'amandes émondées", 500.0, "g", "Épicerie fine"),
                RecipeIngredient("Sucre en poudre", 200.0, "g", "Épicerie fine"),
                RecipeIngredient("Feuilles de pastilla (warka) ou brick", 15.0, "pièce(s)", "Boulangerie"),
                RecipeIngredient("Beurre doux ramolli", 60.0, "g", "Épicerie fine"),
                RecipeIngredient("Eau de fleur d'oranger", 4.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Cannelle en poudre", 1.0, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Gomme arabique broyée avec un peu de sucre", 0.5, "c. à café", "Épices & Aromates"),
                RecipeIngredient("Miel de fleurs ou d'oranger de qualité", 600.0, "g", "Épicerie fine"),
                RecipeIngredient("Huile neutre pour la friture ou beurre clarifié", 500.0, "ml", "Épicerie fine"),
                RecipeIngredient("Graines de sésame dorées ou amandes effilées", 3.0, "c. à soupe", "Épices & Aromates")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Préparation de la pâte d'amande (Oqda)",
                    instruction = "Mélangez la poudre d'amandes avec le sucre, la cannelle, la gomme arabique, le beurre ramolli et l'eau de fleur d'oranger. Pétrissez énergiquement jusqu'à obtenir une pâte souple et malléable. Façonnez des petites boules de la taille d'une bille (15g).",
                    timerDurationMinutes = 15,
                    chefTip = "Ne mettez pas trop de fleur d'oranger d'un coup pour que la pâte ne devienne pas collante."
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Découpe des bandes et pliage en triangle",
                    instruction = "Coupez les feuilles de warka en longues bandes de 5 cm de largeur. Déposez une boulette d'amande au bout, puis repliez en triangle de gauche à droite en serrant bien les bords pour emprisonner la farce. Collez la pointe avec un peu de blanc d'œuf ou pâte de farine.",
                    timerDurationMinutes = 20,
                    chefTip = "Bien fermer les coins du triangle empêche la pâte d'amande de s'échapper dans l'huile chaude."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Friture douce et dorure",
                    instruction = "Faites frire les briouates dans une huile moyennement chaude jusqu'à ce qu'elles prennent une belle couleur blonde dorée.",
                    timerDurationMinutes = 8,
                    chefTip = "Ne faites pas trop dorer les briouates dans l'huile : elles foncent naturellement dans le bain de miel."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Bain de miel et finition",
                    instruction = "Plongez immédiatement les briouates brûlantes dans le miel tiède parfumé à la fleur d'oranger. Laissez-les s'imbiber pendant au moins 15 minutes. Égouttez dans une passoire et parsemez de sésame grillé.",
                    timerDurationMinutes = 15,
                    chefTip = "Plus elles reposent dans le miel, plus leur cœur restera moelleux et fondant pendant des semaines."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-briouates-amandes",
                title = "Tutoriel Vidéo : Le Pliage Parfait des Triangles de Briouates",
                chefName = "Mâallma Nezha",
                durationText = "10:35 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+briouates+aux+amandes+marocaines+pliage+parfait",
                overview = "Apprenez le geste rapide et précis pour plier des triangles réguliers sans trous, la colle alimentaire maison et la cuisson au miel.",
                chapters = listOf(
                    VideoChapter("00:00", "La pâte d'amande (Oqda)", "Textures et arômes à la fleur d'oranger."),
                    VideoChapter("02:50", "La découpe des bandes de warka", "Dimensions optimales pour bouchées élégantes."),
                    VideoChapter("05:10", "Démonstration ralentie du pliage", "Angle après angle, scellage au blanc d'œuf."),
                    VideoChapter("07:45", "Le bain de miel chaud", "Imprégnation en profondeur et égouttage.")
                ),
                secretsOfChef = listOf(
                    "Utilisez du vrai miel chauffé doucement avec 2 cuillères de fleur d'oranger plutôt qu'un sirop de sucre pour une conservation longue durée.",
                    "Laissez sécher les briouates pliées 1 heure à l'air libre avant de les frire : la pâte sera encore plus croustillante."
                )
            )
        ),

        Recipe(
            id = "the-menthe-marocain",
            title = "Le Thé à la Menthe Rituel & Mousse Royale",
            arabicTitle = "أتاي مغربي بالنعناع والرزة",
            category = RecipeCategory.DESSERTS_THES,
            description = "Bien plus qu'une boisson, un art de vivre et le symbole suprême de l'hospitalité marocaine. Thé vert gunpowder rincé, bouquet de menthe fraîche nanâa, sucre selon le goût et versé en cascade pour créer la précieuse mousse (Rezza).",
            culturalOrigin = "Rituel national - Préparé traditionnellement par le chef de famille pour honorer les invités.",
            prepTimeMinutes = 5,
            cookTimeMinutes = 10,
            difficulty = RecipeDifficulty.EASY,
            defaultServings = 4,
            calories = 60,
            keySpices = listOf("Thé vert gunpowder", "Menthe fraîche Naanâa", "Fleur d'oranger ou Absinthe (Chiba)"),
            ingredients = listOf(
                RecipeIngredient("Thé vert de Chine en grains Gunpowder", 2.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Botte de menthe fraîche lavée (Naanâa)", 1.0, "botte(s)", "Fruits & Légumes"),
                RecipeIngredient("Eau de source filtrée", 1000.0, "ml", "Épicerie fine"),
                RecipeIngredient("Morceaux de pain de sucre ou sucre", 4.0, "c. à soupe", "Épicerie fine"),
                RecipeIngredient("Branche de Chiba (absinthe en hiver, facultatif)", 1.0, "pièce(s)", "Fruits & Légumes")
            ),
            steps = listOf(
                RecipeStep(
                    stepNumber = 1,
                    title = "Rinçage du thé vert (L'âme du thé)",
                    instruction = "Mettez les grains de thé dans la théière marocaine (Berrad). Versez un verre d'eau bouillante, laissez infuser 1 minute sans remuer, puis conservez ce premier verre doré ('l'esprit du thé'). Versez un second verre d'eau bouillante, remuez pour laver les grains et jetez ce deuxième verre qui contient l'amertume et la poussière.",
                    timerDurationMinutes = 2,
                    chefTip = "Ne jetez jamais le premier verre : c'est l'essence aromatique la plus pure du thé !"
                ),
                RecipeStep(
                    stepNumber = 2,
                    title = "Réunion et frémissement",
                    instruction = "Reversez le premier verre dans la théière avec le reste de l'eau bouillante. Posez le Berrad sur feu très doux pendant 2 minutes jusqu'à voir les premiers petits frémissements monter sans ébullition violente.",
                    timerDurationMinutes = 2,
                    chefTip = "Une ébullition trop forte brûle les feuilles et libère les tanins amers."
                ),
                RecipeStep(
                    stepNumber = 3,
                    title = "Ajout de la menthe et du sucre",
                    instruction = "Hors du feu, enfoncez la menthe fraîche au fond de la théière pour qu'elle soit immergée sous le liquide (sinon elle noircit). Ajoutez le sucre.",
                    timerDurationMinutes = 1,
                    chefTip = "Enfoncez la menthe avec une cuillère sans la froisser."
                ),
                RecipeStep(
                    stepNumber = 4,
                    title = "Le mélange en cascade (Teqlib)",
                    instruction = "Versez un verre de thé en tenant la théière très haut, puis reversez le verre dans la théière. Répétez l'opération 3 fois jusqu'à ce que le sucre soit parfaitement dissous et qu'une mousse onctueuse apparaisse.",
                    timerDurationMinutes = 2,
                    chefTip = "Ce geste aère le thé, dissout le sucre et développe tout le bouquet aromatique de la menthe."
                ),
                RecipeStep(
                    stepNumber = 5,
                    title = "Service en hauteur dans les verres décorés",
                    instruction = "Servez en levant la théière très haut au-dessus des verres pour former la fameuse mousse royale (Rezza). Dégustez brûlant avec des cornes de gazelle ou des briouates.",
                    timerDurationMinutes = null,
                    chefTip = "La mousse à la surface retient les effluves de menthe et protège le thé des poussières."
                )
            ),
            videoTutorial = VideoTutorial(
                id = "vid-the-marocain",
                title = "Tutoriel Vidéo : Le Rituel d'Atay et le service en cascade",
                chefName = "Mâallem Hicham",
                durationText = "07:15 min",
                youtubeUrl = "https://www.youtube.com/results?search_query=recette+the+a+la+menthe+marocain+traditionnel+rituel",
                overview = "Le secret du lavage des feuilles de gunpowder, le premier verre conservé, et le geste élégant du service en hauteur pour créer la mousse Rezza.",
                chapters = listOf(
                    VideoChapter("00:00", "Le Berrad et le thé Gunpowder", "Le choix des ustensiles en maillechort."),
                    VideoChapter("01:45", "Le lavage des grains et le premier verre", "Conserver l'arôme sans l'amertume."),
                    VideoChapter("03:30", "L'immersion de la menthe fraîche", "Éviter que les feuilles ne s'oxydent."),
                    VideoChapter("05:15", "Le service en hauteur et la couronne de mousse", "La signature visuelle du thé marocain.")
                ),
                secretsOfChef = listOf(
                    "Utilisez de l'eau la moins calcaire possible pour révéler la couleur ambrée étincelante.",
                    "En hiver, ajoutez un petit brin de Chiba (absinthe) ou de Fliou (menthe pouliot) pour un effet réchauffant."
                )
            )
        )
    )
}
